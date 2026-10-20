import { Component, OnInit } from '@angular/core';
import { ProjectService, Project } from '../../services/projet.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-list-projet',
  templateUrl: './list-projet.component.html',
  styleUrls: ['./list-projet.component.css']
})

export class ListProjetComponent implements OnInit {

  projects: any[] = [];
  searchTerm: string = '';
  urlUpload = 'http://localhost:8080/uploads/files';

  currentPage = 0;
  pageSize = 4;
  totalPages = 0;
  allProjects: any[] = [];
  showArchived: boolean = false;
  progressData: { [key: number]: { progression: number, nbTerminees: number, nbTotal: number } } = {};

  statuses = [
    { value: 'EN_ATTENTE', label: 'En attente' },
    { value: 'EN_COURS', label: 'En cours' },
    { value: 'TERMINE', label: 'Terminé' },
    { value: 'ANNULE', label: 'Annulé' }
  ];

  selectedStatus: string = '';
  
  matricule: string = '';
  role: string = '';


  constructor(private projectService: ProjectService, private router: Router) {}



/*
   ngOnInit(): void {
    this.matricule = localStorage.getItem('matricule') || '';
    if (this.matricule) {
      this.projectService.getProjectsByMember(this.matricule).subscribe({
        next: (data) => {
          this.loadProjects();
        },
        error: (err) => console.error('Erreur projets member', err)
      });
    }
  }*/


   ngOnInit(): void {
    this.matricule = localStorage.getItem('matricule') || '';
    this.role = localStorage.getItem('role') || '';

    // 👉 Si USER est un MEMBER → on charge uniquement SES projets
    if (this.role === 'MEMBER') {
      this.projectService.getProjectsByMember(this.matricule).subscribe({
        next: (projects) => {
          this.allProjects = projects;
          this.projects = [...projects];
          this.totalPages = 1;  // pas de pagination serveur
          this.loadProgressForProjects();
        },
        error: (err) => console.error('Erreur projets membre', err)
      });

      return; // ⛔ STOP → on ne charge pas les projets globaux
    }

    // 👉 Si USER est MANAGER / ADMIN → charge tous les projets
    this.loadProjects();
  }


    /** Voir plus de membres */
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
  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.loadProjects(this.currentPage + 1);
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.loadProjects(this.currentPage - 1);
    }
  }


  /** Chargement des projets avec pagination */
  loadProjects(page: number = 0): void {
    this.currentPage = page;

    this.projectService.getProjetsWithPagination(this.currentPage, this.pageSize)
      .subscribe({
        next: (response) => {
          this.allProjects = response.content;
          this.projects = [...this.allProjects];
          this.totalPages = response.totalPages;

          this.filterProjects();
          this.loadProgressForProjects();
        },
        error: (err) => console.error('Erreur lors du chargement des projets', err)
      });
  }


  /** Chargement des stats de progression */
  loadProgressForProjects(): void {
    this.projects.forEach(project => {
      this.projectService.getProjectStats(project.numeroProjet).subscribe({
        next: (stats) => {
          this.progressData[project.numeroProjet] = {
            progression: stats.progression,
            nbTerminees: stats.nbTerminees,
            nbTotal: stats.nbTotal
          };
        },
        error: (err) => console.error(`Erreur chargement stats pour projet ${project.numeroProjet}`, err)
      });
    });
  }



  /** Retour à la liste des projets actifs */
  backToActiveProjects(): void {
    this.loadProjects();
  }

  /** Filtrage + Tri final */
  filterProjects(): void {
    const term = this.searchTerm.toLowerCase();

    this.projects = this.allProjects.filter(p => {
      const projectStatus = (p.status || '').toLowerCase();

      const matchSearch = term ? (
        (p.titre && p.titre.toLowerCase().includes(term)) ||
        (p.chefNomComplet && p.chefNomComplet.toLowerCase().includes(term)) ||
        projectStatus.includes(term) ||
        (p.dateDebut && String(p.dateDebut).toLowerCase().includes(term)) ||
        (p.dateFin && String(p.dateFin).toLowerCase().includes(term))
      ) : true;

      const matchStatus = this.selectedStatus
  ? p.status === this.getStatusLabelFromEnum(this.selectedStatus)
  : true;


      return matchSearch && matchStatus;
    });

    // Tri par dateDebut puis par dateFin si identiques
    this.projects.sort((a, b) => this.compareProjects(a, b));
  }
   getStatusLabelFromEnum(enumValue: string): string {
  const statusMap: { [key: string]: string } = {
    EN_ATTENTE: 'En attente',
    EN_COURS: 'En cours',
    TERMINE: 'Terminé',
    ANNULE: 'Annulé'
  };
  return statusMap[enumValue] || '';
}

  /** Fonction de tri globale */
  private compareProjects(a: any, b: any): number {
    const startA = this.parseDate(a.dateDebut).getTime();
    const startB = this.parseDate(b.dateDebut).getTime();

    if (startA === startB) {
      const endA = this.parseDate(a.dateFin).getTime();
      const endB = this.parseDate(b.dateFin).getTime();
      return endA - endB;
    }
    return startA - startB;
  }

  /** Parse une date (string ou Date) */
  private parseDate(date: any): Date {
    if (!date) return new Date(0);
    if (typeof date === 'string') {
      // Format yyyy-MM-dd ou dd/MM/yyyy
      const parts = date.includes('-') ? date.split('-') : date.split('/');
      if (parts[0].length === 4) {
        return new Date(Number(parts[0]), Number(parts[1]) - 1, Number(parts[2]));
      } else {
        return new Date(Number(parts[2]), Number(parts[1]) - 1, Number(parts[0]));
      }
    }
    return new Date(date);
  }

 

  /** Badge statut */
  getStatusBadgeClass(status: string): string {
    if (!status) return 'bg-secondary';
    const normalized = status.toLowerCase();
    if (normalized.includes('attente')) return 'chip-waiting';
    if (normalized.includes('cours')) return 'chip-progress';
    if (normalized.includes('terminé') || normalized.includes('termine')) return 'chip-done';
    if (normalized.includes('annulé') || normalized.includes('annule')) return 'chip-cancel';
    return 'bg-secondary';
  }

  /** Couleur progression */
  getProgressBarClass(value: number): string {
    if (value < 30) return 'bg-danger';
    if (value < 70) return 'bg-warning';
    return 'bg-success';
  }

  
}
