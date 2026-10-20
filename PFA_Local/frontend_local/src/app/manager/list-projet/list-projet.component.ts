import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { Observable, Subject, Subscription, debounceTime, switchMap, tap } from 'rxjs';
import { ProjectService } from '../../services/projet.service';

@Component({
  selector: 'app-list-projet',
  templateUrl: './list-projet.component.html',
  styleUrls: ['./list-projet.component.css']
})
export class ListProjetComponent implements OnInit, OnDestroy {
  projects: any[] = [];
  urlUpload = 'http://localhost:8080/uploads/files';

  // pagination & tri
  currentPage = 0;
  pageSize = 4;
  totalPages = 0;
  sortBy: string = 'dateDebut';
  sortDir: 'asc' | 'desc' = 'asc';

  // filtres
  searchTerm: string = '';
  selectedStatus: string = '';
  dateDebutFrom?: string;
  dateDebutTo?: string;
  dateFinFrom?: string;
  dateFinTo?: string;
  hasFiles?: boolean | null = null;

  showArchived: boolean = false;
  private matriculeChef: string | null = null;

  // rx
  private searchTrigger$ = new Subject<void>();
  private sub?: Subscription;

  // stats progression
  progressData: { [key: number]: { progression: number, nbTerminees: number, nbTotal: number } } = {};

  statuses = [
    { value: 'EN_ATTENTE', label: 'En attente' },
    { value: 'EN_COURS', label: 'En cours' },
    { value: 'TERMINE', label: 'Terminé' },
    { value: 'ANNULE', label: 'Annulé' }
  ];

  constructor(private projectService: ProjectService, private router: Router) {}

  ngOnInit(): void {
    this.matriculeChef = localStorage.getItem('matricule');
    if (!this.matriculeChef) {
      this.router.navigate(['/login']);
      return;
    }

    this.sub = this.searchTrigger$
      .pipe(
        debounceTime(350),
        switchMap(() => this.fetchPage())
      )
      .subscribe({
        next: () => {},
        error: err => console.error('Erreur fetch projets', err)
      });

    // premier chargement
    this.triggerSearch();
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  private fetchPage(): Observable<any> {
    return this.projectService.searchProjetsForChef({
      chefMatricule: this.matriculeChef!,
      page: this.currentPage,
      size: this.pageSize,
      q: this.searchTerm?.trim() || undefined,
      status: this.selectedStatus || undefined,
      dateDebutFrom: this.dateDebutFrom || undefined,
      dateDebutTo: this.dateDebutTo || undefined,
      dateFinFrom: this.dateFinFrom || undefined,
      dateFinTo: this.dateFinTo || undefined,
      hasFiles: this.hasFiles ?? undefined,
      archived: this.showArchived,
      sortBy: this.sortBy,
      sortDir: this.sortDir
    }).pipe(
      tap((res: any) => {
        // contenu & pagination
        this.projects = res?.content ?? [];
        this.totalPages = res?.totalPages ?? 0;

        // si le filtre réduit le nombre de pages, on corrige la page
        if (this.totalPages > 0 && this.currentPage >= this.totalPages) {
          this.currentPage = Math.max(this.totalPages - 1, 0);
          this.triggerSearch();
          return;
        }

        // reset stats
        this.progressData = {};

        // charge stats seulement s'il y a des projets
        if (this.projects.length > 0) {
          this.loadProgressForProjects();
        }
      })
    );
  }

  private triggerSearch() {
    this.searchTrigger$.next();
  }

  onStatusChange(value: string) {
    this.selectedStatus = value;
    this.currentPage = 0;
    this.triggerSearch();
  }

  onDateChange() {
    this.currentPage = 0;
    this.triggerSearch();
  }

  onHasFilesToggle(checked: boolean) {
    this.hasFiles = checked ? true : null;
    this.currentPage = 0;
    this.triggerSearch();
  }

  goToPage(page: number) {
    if (page < 0 || (this.totalPages > 0 && page >= this.totalPages)) return;
    this.currentPage = page;
    this.triggerSearch();
  }

  changeSort(by: string) {
    if (this.sortBy === by) {
      this.sortDir = this.sortDir === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortBy = by;
      this.sortDir = 'asc';
    }
    this.currentPage = 0;
    this.triggerSearch();
  }

  // archivés / actifs
  viewArchivedProjects(page: number = 0): void {
    this.showArchived = true;
    this.goToPage(page);
  }

  backToActiveProjects(): void {
    this.showArchived = false;
    this.goToPage(0);
  }

  // progression
  loadProgressForProjects(): void {
    this.projects.forEach(project => {
      this.projectService.getProjectStats(project.numeroProjet).subscribe({
        next: (stats: any) => {
          this.progressData[project.numeroProjet] = {
            progression: stats?.progression ?? 0,
            nbTerminees: stats?.nbTerminees ?? 0,
            nbTotal: stats?.nbTotal ?? 0
          };
        },
        error: err => console.error(`Erreur stats projet ${project.numeroProjet}`, err)
      });
    });
  }

  // archiver
  archiveProject(project: any): void {
    Swal.fire({
      title: 'Archiver le projet ?',
      text: `Voulez-vous archiver ce projet?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Oui, archiver',
      cancelButtonText: 'Annuler'
    }).then(result => {
      if (result.isConfirmed) {
        this.projectService.archiveProject(project.numeroProjet).subscribe({
          next: () => {
            Swal.fire('Succès', 'Le projet est archivé', 'success');
            this.triggerSearch();
          },
          error: () => Swal.fire('Erreur', 'Impossible d’archiver le projet', 'error')
        });
      }
    });
  }

  // badges
  getStatusBadgeClass(status: string): string {
    if (!status) return 'bg-secondary';
    const s = status.toLowerCase();
    if (s.includes('attente')) return 'chip-waiting';
    if (s.includes('cours')) return 'chip-progress';
    if (s.includes('terminé') || s.includes('termine')) return 'chip-done';
    if (s.includes('annulé') || s.includes('annule')) return 'chip-cancel';
    return 'bg-secondary';
  }

  getProgressBarClass(v: number): string {
    if (v < 30) return 'bg-danger';
    if (v < 70) return 'bg-warning';
    return 'bg-success';
  }

  showMoreMembers(membres: any[]): void {
      const extraMembers = membres.slice(3)
        .map(m => `
          <div style="display:flex; align-items:center; margin-bottom:10px;">
            <img src="${this.urlUpload}/${m.image}" 
                 style="width:35px; height:35px; border-radius:50%; object-fit:cover; margin-right:10px;" />
            <span style="font-size:14px; color:#333; font-weight:500;">${m.nom} ${m.prenom}</span>
          </div>
        `)
        .join('');
  
      Swal.fire({
        title: 'Membres supplémentaires',
        html: `<div style="text-align:left; max-height:250px; overflow-y:auto;">${extraMembers}</div>`,
        showCloseButton: true,
        showConfirmButton: false,
        width: '400px',
        background: '#ffffff',
        customClass: {
          title: 'swal-title-custom'
        }
      });
    }

  // --- Recherche texte avec opérateurs de date ---
  onSearchTermInput(value: string) {
    // reset bornes à chaque saisie
    this.dateDebutFrom = this.dateDebutTo = this.dateFinFrom = this.dateFinTo = undefined;

    let s = (value || '').trim();

    // date:YYYY-MM-DD..YYYY-MM-DD
    const mRange = s.match(/\bdate:(\d{4}-\d{2}-\d{2})\.\.(\d{4}-\d{2}-\d{2})\b/i);
    if (mRange) {
      this.dateDebutFrom = mRange[1]; this.dateDebutTo = mRange[2];
      this.dateFinFrom   = mRange[1]; this.dateFinTo   = mRange[2];
      s = s.replace(mRange[0], '').trim();
    }

    // debut>= / debut<= / fin>= / fin<= / debut: / fin:
    const ops = [
      /\bdebut>=(\d{4}-\d{2}-\d{2})\b/i, /\bdebut<=(\d{4}-\d{2}-\d{2})\b/i,
      /\bfin>=(\d{4}-\d{2}-\d{2})\b/i,   /\bfin<=(\d{4}-\d{2}-\d{2})\b/i,
      /\bdebut:(\d{4}-\d{2}-\d{2})\b/i,  /\bfin:(\d{4}-\d{2}-\d{2})\b/i
    ];
    let m: RegExpMatchArray | null;

    m = s.match(ops[0]); if (m) { this.dateDebutFrom = m[1]; s = s.replace(m[0], '').trim(); }
    m = s.match(ops[1]); if (m) { this.dateDebutTo   = m[1]; s = s.replace(m[0], '').trim(); }
    m = s.match(ops[2]); if (m) { this.dateFinFrom   = m[1]; s = s.replace(m[0], '').trim(); }
    m = s.match(ops[3]); if (m) { this.dateFinTo     = m[1]; s = s.replace(m[0], '').trim(); }
    m = s.match(ops[4]); if (m) { this.dateDebutFrom = this.dateDebutTo = m[1]; s = s.replace(m[0], '').trim(); }
    m = s.match(ops[5]); if (m) { this.dateFinFrom   = this.dateFinTo   = m[1]; s = s.replace(m[0], '').trim(); }

    // le reste part en recherche libre (LIKE %...%)
    this.searchTerm = s;
    this.currentPage = 0;
    this.triggerSearch();
  }
}
