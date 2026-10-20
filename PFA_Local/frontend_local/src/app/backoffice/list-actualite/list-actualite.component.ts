import { Component } from '@angular/core';
import { ActualiteService, Actualite } from '../../services/actualite.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-list-actualite',
  templateUrl: './list-actualite.component.html',
  styleUrls: ['./list-actualite.component.css']
})
export class ListActualiteComponent {
  private expanded = new Set<number | string>();
  actualites: Actualite[] = [];
  currentPage = 0;
  totalPages = 0;

  constructor(private service: ActualiteService, private router: Router) {}

  ngOnInit() { this.loadPage(0); }

  loadPage(page: number) {
    this.service.getPagedActualites(page).subscribe((response: any) => {
      this.actualites = response.content;
      this.totalPages = response.totalPages;
      this.currentPage = response.number;
    });
  }

  // 👉 plus besoin de refreshListActus qui appelait un endpoint non aligné
  //   (si tu veux recharger, préfère juste reload la page courante)
  reloadCurrentPage() {
    this.loadPage(this.currentPage);
  }
  refreshListActus() {
  // on recharge juste la page courante depuis l’API paginée
  this.loadPage(this.currentPage);
}


  nextPage() {
    if (this.currentPage < this.totalPages - 1) this.loadPage(this.currentPage + 1);
  }
  prevPage() {
    if (this.currentPage > 0) this.loadPage(this.currentPage - 1);
  }

  toggleExpand(id: number | string) {
    this.expanded.has(id) ? this.expanded.delete(id) : this.expanded.add(id);
  }
  isExpanded(id: number | string) { return this.expanded.has(id); }

  addActualite() { this.router.navigate(['/backoffice/addActualite']); }

  getPhotoUrl(photoName: string): string {
    return `http://localhost:8080/uploads/files/${photoName}`;
  }
  getImageName(fullPath: unknown): string {
    if (typeof fullPath !== 'string') return '';
    return fullPath.includes('http') ? (fullPath.split('/').pop() || fullPath) : fullPath;
  }

deleteActu(actualite: any) {
  Swal.fire({
    title: 'Êtes-vous sûr ?',
    text: "Cette action est irréversible !",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    confirmButtonText: 'Oui, supprimer',
    cancelButtonText: 'Annuler'
  }).then((result) => {
    if (!result.isConfirmed) return;

    const id = actualite.id;
    const previous = [...this.actualites];

    // 1) suppression optimiste (snappy UI)
    this.expanded.delete(id);
    this.actualites = this.actualites.filter(a => a.id !== id);

    // 2) appel API
    this.service.deleteActu(id).subscribe({
      next: () => {
        Swal.fire({
          title: 'Supprimé !',
          text: 'L’actualité a été supprimée avec succès',
          icon: 'success',
          showConfirmButton: false,
          timer: 1500
        });

        // 👉 Toujours recharger la page courante pour “backfiller” à 5 éléments
        // (l’API va renvoyer content avec jusqu’à 5 items, en tirant sur la page suivante)
        this.service.getPagedActualites(this.currentPage).subscribe((response: any) => {
          // cas 1 : la page courante existe encore (il reste du contenu)
          if (response.content?.length > 0) {
            this.actualites  = response.content;
            this.totalPages  = response.totalPages;
            this.currentPage = response.number;
            return;
          }
          // cas 2 : la page courante est devenue vide => reculer d’une page si possible
          if (this.currentPage > 0) {
            this.loadPage(this.currentPage - 1);
          } else {
            // très rare : on était en page 0 et elle est vide => on garde l’état rafraîchi
            this.actualites  = response.content ?? [];
            this.totalPages  = response.totalPages ?? 0;
            this.currentPage = response.number ?? 0;
          }
        });
      },
      error: (err) => {
        // rollback si l’API échoue
        this.actualites = previous;
        Swal.fire({
          title: 'Erreur',
          text: 'La suppression a échoué. Réessayez plus tard.',
          icon: 'error'
        });
        console.error('deleteActu failed', err);
      }
    });
  });
}


  updateActu(actualite: Actualite) {
    this.router.navigate(['/backoffice/updateActualite', actualite.id]);
  }

  // 👍 pour des *ngFor* plus stables et performants
  trackById(index: number, item: Actualite) { return item.id; }
}
