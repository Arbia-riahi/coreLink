import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { UserService } from '../../services/user.service';

interface User {
  matricule: string;
  nom: string;
  prenom: string;
  email: string;
  role: string;
  image?: string;
}

@Component({
  selector: 'app-update-inscription',
  templateUrl: './update-inscription.component.html',
  styleUrls: ['./update-inscription.component.css']
})
export class UpdateInscriptionComponent implements OnInit {
  user: User = {
    matricule: '',
    nom: '',
    prenom: '',
    email: '',
    role: 'MEMBER'
  };
  roles: string[] = ['MANAGER', 'MEMBER'];

  selectedFile: File | null = null;

  matricule: string = '';
  currentImageUrl: string = '';
  previewImageUrl: string | ArrayBuffer | null = null;

  originalUserData: User = {
    matricule: '',
    nom: '',
    prenom: '',
    email: '',
    role: 'MEMBER'
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.matricule = this.route.snapshot.params['matricule'] || '';

    this.userService.getUserByMatricule(this.matricule).subscribe({
      next: (data: any) => {
        this.user = {
          matricule: data.matricule || '',
          nom: data.nom || '',
          prenom: data.prenom || '',
          email: data.email || '',
          role: data.role || 'MEMBER',
          image: data.image
        };
        this.currentImageUrl = this.user.image || '';
        this.originalUserData = { ...this.user };
      },
      error: () => {
        Swal.fire('Erreur', 'Impossible de charger les données', 'error');
      }
    });
  }
onFileChanged(event: Event) {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  if (file && file.type.startsWith('image/')) {
    this.selectedFile = file;

    const reader = new FileReader();
    reader.onload = (e) => {
      this.previewImageUrl = e.target?.result || null;
    };
    reader.readAsDataURL(file);
  } else {
    this.selectedFile = null;
    Swal.fire('Erreur', 'Veuillez sélectionner une image valide (jpg, png, etc.).', 'error');
    if (target) target.value = ''; // Reset input file
  }
}


  onSubmit() {
    const hasChanged =
      this.user.nom !== this.originalUserData.nom ||
      this.user.prenom !== this.originalUserData.prenom ||
      this.user.email !== this.originalUserData.email ||
      this.user.role !== this.originalUserData.role ||
      this.selectedFile !== null;

    if (!hasChanged) {
      Swal.fire({
        title: 'Aucun changement détecté',
        text: 'Vous n’avez rien modifié. Êtes-vous sûr de vouloir quitter ?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Oui',
        cancelButtonText: 'Non'
      }).then((result) => {
        if (result.isConfirmed) {
          this.router.navigate(['/backoffice/listInscription']);
        }
      });
    } else {
      this.submitUpdate();
    }
  }

 submitUpdate() {
  const formData = new FormData();
  formData.append('nom', this.user.nom);
  formData.append('prenom', this.user.prenom);
  formData.append('email', this.user.email);
  formData.append('role', this.user.role);

  if (this.selectedFile) {
    formData.append('image', this.selectedFile);
  }

  this.userService.updateUser(this.matricule, formData).subscribe({
    next: () => {
      Swal.fire({
        title: 'Succès',
        text: 'Utilisateur mis à jour avec succès',
        icon: 'success',
        showConfirmButton: false,
        timer: 2000,
        timerProgressBar: true
      });

      setTimeout(() => {
        this.router.navigate(['/backoffice/listInscription']);
      }, 2000);
    },
    error: (err) => {
      const message = err?.error || 'Impossible de mettre à jour l’utilisateur';
      Swal.fire({
        title: 'Erreur',
        text: message,
        icon: 'error',
        confirmButtonText: 'OK'
      });
    }
  });
}



  revenirALaListe() {
    this.router.navigate(['/backoffice/listInscription']);
  }


  // Détermine la photo finale (image user ou avatar.webp)
getUserPhoto(imagePath?: string): string {
  if (!imagePath || imagePath.trim() === '') {
    return '../../assets/avatar.webp';
  }
  return imagePath; // L’URL est déjà complète depuis ton API
}

// Si l'image plante → met avatar
onImageError(event: any) {
  event.target.src = '../../assets/avatar.webp';
}

}
