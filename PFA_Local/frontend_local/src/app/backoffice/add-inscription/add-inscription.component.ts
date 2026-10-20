import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-inscription',
  templateUrl: './add-inscription.component.html',
  styleUrl: './add-inscription.component.css'
})
export class AddInscriptionComponent {
  user = {
    matricule: '',
    nom: '',
    prenom: '',
    email: '',
    motDePasse: '',
    role: '',
    image: null
  };

  selectedFile: File | null = null;
  showPassword = false;
  roles = ['MANAGER', 'MEMBER']; // Doit correspondre aux valeurs BACKEND

  constructor(private userService: UserService, private router: Router) {}

onFileChanged(event: any) {
  const file = event.target.files[0];
  this.user.image = file; // lié à ngModel → Angular valide le champ
  this.selectedFile = file; // pour traitement
}



  // Vérification email
  isEmailValid(email: string): boolean {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }

  // Vérification mot de passe fort
  isPasswordStrong(password: string): boolean {
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return regex.test(password);
  }

  // Soumission du formulaire
  onSubmit() {
  // Double vérification sécurité (même si bouton est déjà désactivé)
  if (!this.user.matricule || !this.user.nom || !this.user.prenom || 
      !this.user.email || !this.user.motDePasse || !this.user.role || !this.selectedFile) {
    Swal.fire('Erreur', 'Veuillez remplir tous les champs', 'error');
    return;
  }

  // Vérification email
  if (!this.isEmailValid(this.user.email)) {
    Swal.fire('Erreur', 'Veuillez entrer un email valide', 'error');
    return;
  }

  // Vérification mot de passe fort
  if (!this.isPasswordStrong(this.user.motDePasse)) {
    Swal.fire('Erreur', 'Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial', 'error');
    return;
  }

  // Vérification extension image
  if (this.selectedFile && !this.selectedFile.type.startsWith('image/')) {
    Swal.fire('Erreur', 'Veuillez sélectionner une image valide (jpg, png, etc.)', 'error');
    return;
  }

  // Vérification matricule existante
  this.userService.checkMatriculeExists(this.user.matricule).subscribe({
    next: (exists: boolean) => {
      if (exists) {
        Swal.fire('Erreur', 'Le matricule existe déjà!', 'error');
        return;
      }
      this.createUser();
    },
    error: () => {
      Swal.fire('Erreur', 'Impossible de vérifier le matricule', 'error');
    }
  });
}
  // Création de l'utilisateur
 private createUser() {
  if (!this.selectedFile) {
    Swal.fire({
      title: 'Erreur',
      text: 'Veuillez sélectionner une image',
      icon: 'error',
      confirmButtonText: 'OK'
    });
    return;
  }

  const formData = new FormData();
  formData.append('matricule', this.user.matricule);
  formData.append('nom', this.user.nom);
  formData.append('prenom', this.user.prenom);
  formData.append('email', this.user.email);
  formData.append('motDePasse', this.user.motDePasse);
  formData.append('role', this.user.role);
  formData.append('image', this.selectedFile);

  this.userService.addUserAsAdmin(formData).subscribe({
    next: () => {
      Swal.fire({
        title: 'Succès',
        text: 'Utilisateur créé avec succès',
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
      // 💡 Vérifie si le back-end a renvoyé un message explicite
      const message = err?.error || 'Impossible de créer l’utilisateur';
      Swal.fire({
        title: 'Erreur',
        text: message,
        icon: 'error',
        confirmButtonText: 'OK'
      });
    }
  });
}


}
