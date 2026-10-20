// list-inscription.component.ts
import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-list-inscription',
  templateUrl: './list-inscription.component.html',
  styleUrls: ['./list-inscription.component.css']
})
export class ListInscriptionComponent implements OnInit {
  users: any[] = [];
  urlUpload = 'http://localhost:8080/uploads/files';
  defaultAvatar = 'assets/avatar-placeholder.png';

  currentPage = 0;
  pageSize = 4;
  totalPages = 0;
  totalElements = 0;

  searchTerm = '';
  private searchTimer?: any; // pour le debounce

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.loadUsers(); // charge page 0 sans filtre
  }

  normalizeImage(img?: string): string {
  if (!img) return this.defaultAvatar;
  return img.startsWith('http') ? img : `${this.urlUpload}/${img}`;
 }
  /** Charger avec pagination + filtre côté serveur */
  loadUsers(page: number = 0): void {
    this.currentPage = page;
    this.userService.getUsersWithPagination(this.currentPage, this.pageSize, this.searchTerm)
      .subscribe(data => {
        this.users = data.content ?? [];
        this.totalPages = data.totalPages ?? 0;
        this.totalElements = data.totalElements ?? 0;
      });
  }

  /** Déclenchée à chaque frappe (debounce 300 ms) */
  onSearchChange(value: string): void {
    this.searchTerm = value ?? '';
    clearTimeout(this.searchTimer);
    this.searchTimer = setTimeout(() => {
      // On repart toujours de la page 0 quand le filtre change
      this.loadUsers(0);
    }, 300);
  }

  onImgError(event: Event) {
  // Si l’image ne charge pas → on la supprime (ça évite les images cassées)
  (event.target as HTMLImageElement).style.display = 'none';
}


  trackByMatricule(index: number, item: any) {
    return item.matricule;
  }

  deleteUser(user: any) {
    Swal.fire({
      title: 'Êtes-vous sûr ?',
      text: `Supprimer l'utilisateur ${user.matricule} ?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.userService.deleteUser(user.matricule).subscribe({
          next: () => {
            Swal.fire({
              title: 'Succès',
              text: 'Utilisateur supprimé avec succès',
              icon: 'success',
              showConfirmButton: false,
              timer: 1000,
              timerProgressBar: true
            });
            this.loadUsers(this.currentPage);
          },
          error: (err) => {
            console.error(err);
            Swal.fire('Erreur', err?.error || 'Suppression impossible', 'error');
          }
        });
      }
    });
  }
}
