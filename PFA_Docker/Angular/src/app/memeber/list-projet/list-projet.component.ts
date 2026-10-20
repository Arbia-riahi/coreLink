import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/projet.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-list-projet',
  templateUrl: './list-projet.component.html',
  styleUrls: ['./list-projet.component.css']
})
export class ListProjetComponent implements OnInit {
  projects: any[] = [];
  allProjects: any[] = [];
  progressData: { [key: number]: { progression: number, nbTerminees: number, nbTotal: number } } = {};

  searchTerm: string = '';
  selectedStatus: string = '';
  urlUpload = 'http://localhost:8081/uploads/files';

  currentPage = 0;
  pageSize = 4;
  totalPages = 0;

  matricule: string = '';

  statuses = [
    { value: 'EN_ATTENTE', label: 'En attente' },
    { value: 'EN_COURS', label: 'En cours' },
    { value: 'TERMINE', label: 'Terminé' },
    { value: 'ANNULE', label: 'Annulé' }
  ];

  constructor(private projectService: ProjectService, private router: Router) {}

  ngOnInit(): void {
    this.matricule = localStorage.getItem('matricule') || '';
    if (!this.matricule) {
      this.router.navigate(['/login']);
      return;
    }

    this.loadProjects();
  }

  /** Charger la liste des projets avec pagination */
  loadProjects(page: number = 0): void {
    this.currentPage = page;
    this.projectService.getProjetsWithPagination(this.currentPage, this.pageSize)
      .subscribe({
        next: (response) => {
          this.allProjects = response.content || [];
          this.totalPages = response.totalPages || 0;
          this.projects = [...this.allProjects];
          this.filterProjects();
          this.loadProgressForProjects();
        },
        error: (err) => console.error('Erreur lors du chargement des projets', err)
      });
  }

  /** Recherche + filtre */
  filterProjects(): void {
    const term = (this.searchTerm || '').toLowerCase().trim();

    this.projects = this.allProjects.filter(p => {
      const titre = (p.titre || '').toLowerCase();
      const chef = (p.chefNomComplet || '').toLowerCase();
      const status = (p.status || '').toLowerCase();
      const dateDebut = String(p.dateDebut || '').toLowerCase();
      const dateFin = String(p.dateFin || '').toLowerCase();

      const matchSearch = term
        ? titre.includes(term)
          || chef.includes(term)
          || status.includes(term)
          || dateDebut.includes(term)
          || dateFin.includes(term)
        : true;

      const matchStatus = this.selectedStatus
        ? p.status === this.getStatusLabelFromEnum(this.selectedStatus)
          || p.status === this.selectedStatus
        : true;

      return matchSearch && matchStatus;
    });

    // Tri par date
    this.projects.sort((a, b) => {
      const ad = new Date(a.dateDebut || 0).getTime();
      const bd = new Date(b.dateDebut || 0).getTime();
      if (ad === bd) {
        const af = new Date(a.dateFin || 0).getTime();
        const bf = new Date(b.dateFin || 0).getTime();
        return af - bf;
      }
      return ad - bd;
    });
  }

  /** Progression */
  loadProgressForProjects(): void {
    this.projects.forEach(project => {
      this.projectService.getProjectStats(project.numeroProjet).subscribe({
        next: (stats) => {
          this.progressData[project.numeroProjet] = {
            progression: stats?.progression ?? 0,
            nbTerminees: stats?.nbTerminees ?? 0,
            nbTotal: stats?.nbTotal ?? 0
          };
        },
        error: (err) => console.error(`Erreur chargement stats pour projet ${project.numeroProjet}`, err)
      });
    });
  }

  /** Pagination */
  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) this.loadProjects(this.currentPage + 1);
  }

  previousPage(): void {
    if (this.currentPage > 0) this.loadProjects(this.currentPage - 1);
  }

  /** Statut en texte lisible */
  getStatusLabelFromEnum(enumValue: string): string {
    const map: Record<string, string> = {
      EN_ATTENTE: 'En attente',
      EN_COURS: 'En cours',
      TERMINE: 'Terminé',
      ANNULE: 'Annulé'
    };
    return map[enumValue] || '';
  }

  /** Style des badges */
  getStatusBadgeClass(status: string): string {
    if (!status) return 'bg-secondary';
    const s = status.toLowerCase();
    if (s.includes('attente')) return 'chip-waiting';
    if (s.includes('cours')) return 'chip-progress';
    if (s.includes('terminé') || s.includes('termine')) return 'chip-done';
    if (s.includes('annulé') || s.includes('annule')) return 'chip-cancel';
    return 'bg-secondary';
  }

  /** Couleur des barres de progression */
  getProgressBarClass(value: number): string {
    if (value < 30) return 'bg-danger';
    if (value < 70) return 'bg-warning';
    return 'bg-success';
  }

  /** Voir plus de membres */
  showMoreMembers(membres: any[]): void {
  const extraMembers = membres.slice(3)
    .map(m => `
      <div style="display:flex; align-items:center; margin-bottom:10px;">
        <img src="${this.getMembrePhoto(m.image)}"
             onerror="this.src='../../assets/avatar.webp'"
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
      background: '#ffffff'
    });
  }


  getMembrePhoto(imageName: string): string {
  if (!imageName) {
    return '../../assets/avatar.webp';
  }
  return `${this.urlUpload}/${imageName}`;
}

setDefaultMembreImage(event: any) {
  event.target.src = '../../assets/avatar.webp';
}

}
