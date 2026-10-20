import { Component, OnInit } from '@angular/core';
import { ActualiteService } from '../../services/actualite.service';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-update-actualite',
  templateUrl: './update-actualite.component.html',
  styleUrls: ['./update-actualite.component.css']
})
export class UpdateActualiteComponent implements OnInit {
  actualite: any = { titre: '', description: '', photos: [] };
  filesToUpload: File[] = [];

  // 👉 le snapshot de référence (stringifié pour comparaison simple)
  private initialSnapshot: string | null = null;
  // 👉 flag simple pour savoir si les données sont chargées
  loading = true;

  constructor(
    private service: ActualiteService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.loading = false;
      return;
    }

    this.service.getActualiteById(id).subscribe({
      next: (response: any) => {
        this.actualite = {
          titre: response?.titre ?? '',
          description: response?.description ?? '',
          photos: Array.isArray(response?.photos) ? response.photos : []
        };
        // on fige l’état initial de façon déterministe
        this.initialSnapshot = JSON.stringify({
          titre: (this.actualite.titre || '').trim(),
          description: (this.actualite.description || '').trim(),
          photos: this.actualite.photos
        });
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement de l’actualité :', err);
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: 'Chargement impossible',
          text: 'Un problème est survenu lors du chargement de l’actualité.'
        });
      }
    });
  }

  getPhotoUrl(photoName: string): string {
    return `http://localhost:8080/uploads/files/${photoName}`;
  }

  onFileChange(event: any) {
    const files = event.target.files;
    this.filesToUpload = [];
    for (let i = 0; i < files.length; i++) {
      if (files[i].type.startsWith('image/')) {
        this.filesToUpload.push(files[i]);
      } else {
        Swal.fire({
          icon: 'warning',
          title: 'Fichier non autorisé',
          text: 'Seules les images sont autorisées.'
        });
      }
    }
  }

  persistActualite(form: any) {
  // 0) protéger le clic avant fin de chargement (optionnel si tu gardes le flag loading)
  if (this.loading) {
    Swal.fire({
      icon: 'info',
      title: 'Patientez un instant',
      text: 'Les données sont encore en cours de chargement.'
    });
    return;
  }

  // 1) snapshot courant
  const currentSnapshot = JSON.stringify({
    titre: (this.actualite.titre || '').trim(),
    description: (this.actualite.description || '').trim(),
    photos: Array.isArray(this.actualite.photos) ? this.actualite.photos : []
  });

  // 2) Aucun changement → afficher le SweetAlert de confirmation (2 boutons)
  if (currentSnapshot === this.initialSnapshot && this.filesToUpload.length === 0) {
    Swal.fire({
      icon: 'warning',
      title: 'Aucun changement détecté',
      text: 'Vous n’avez rien modifié. Êtes-vous sûr de vouloir quitter ?',
      showCancelButton: true,
      confirmButtonText: 'Oui, quitter la page',
      cancelButtonText: 'Non, rester',
      reverseButtons: true,            // inverse l’ordre pour éviter les misclicks
      allowOutsideClick: false,        // empêcher la fermeture en cliquant à l’extérieur
      allowEscapeKey: true
    }).then((result) => {
      if (result.isConfirmed) {
        this.router.navigate(['/backoffice/listActualite']);
      }
      // si "Non, rester" ou fermeture, on ne fait rien (on reste sur la page)
    });
    return; // NE PAS continuer vers l’update
  }

  // 3) Il y a des changements → on envoie l’update réel
  const formData = new FormData();
  formData.append('titre', (this.actualite.titre || '').trim());
  formData.append('description', (this.actualite.description || '').trim());

  if (this.filesToUpload.length > 0) {
    this.filesToUpload.forEach(file => formData.append('imageFiles', file));
  }

  const id = this.route.snapshot.paramMap.get('id');
  if (!id) return;

  this.service.updateActualite(id, formData).subscribe({
    next: () => {
      Swal.fire({
        icon: 'success',
        title: 'Actualité mise à jour',
        text: 'Les modifications ont été enregistrées avec succès',
        timer: 1500,
        showConfirmButton: false
      }).then(() => {
        this.router.navigate(['/backoffice/listActualite']);
      });
    },
    error: (err) => {
      console.error('Erreur update:', err);
      Swal.fire({
        icon: 'error',
        title: 'Échec de la mise à jour',
        text: 'Merci de réessayer ou de contacter le support.'
      });
    }
  });
}

}
