import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import {  NgForm } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { UserService } from '../../services/user.service';
import { environment } from '../../../environments/environment.development';

@Component({
  selector: 'app-consulter-mon-profile',
  templateUrl: './consulter-mon-profile.component.html',
  styleUrl: './consulter-mon-profile.component.css'
})
export class ConsulterMonProfilComponent implements OnInit {

  user: any = {};
  baseUrlImage = environment.baseUrl + 'uploads/files/';
  selectedImage: File | null = null;
  previewImage: string | null = null;

  @ViewChild('imageInput') imageInput!: ElementRef;

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile() {
    this.userService.getMyProfile().subscribe(data => {
      this.user = data;
    });
  }

  triggerImageUpload() {
    this.imageInput.nativeElement.click();
  }

  onFileSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.selectedImage = file;
      const reader = new FileReader();
      reader.onload = (e: any) => this.previewImage = e.target.result;
      reader.readAsDataURL(file);
    }
  }

  // ⬇️ Recevoir le NgForm pour tester l'état pristine
  onUpdateProfile(form: NgForm) {
    // 1) Rien n’a changé ? (aucun champ modifié + pas d’image choisie)
    if (form.pristine && !this.selectedImage) {
      Swal.fire({
        title: 'Aucune modification',
        text: "Vous n'avez rien changé. Voulez-vous quitter la page ?",
        icon: 'info',
        showCancelButton: true,
        confirmButtonText: 'Oui, quitter',
        cancelButtonText: 'Rester ici',
        reverseButtons: true
      }).then(result => {
        if (result.isConfirmed) {
          this.router.navigate(['/backoffice/tableauBoard']);
        }
      });
      return; // on stoppe ici
    }

    // 2) Il y a des changements → on envoie la mise à jour
    const formData = new FormData();
    formData.append('nom', (this.user.nom ?? '').trim());
    formData.append('prenom', (this.user.prenom ?? '').trim());
    formData.append('email', (this.user.email ?? '').trim());

    if (this.selectedImage) {
      formData.append('image', this.selectedImage);
    }

this.userService.updateMyProfile(formData).subscribe(
  () => {
    Swal.fire({
      title: 'Succès',
      text: 'Profil mis à jour avec succès',
      icon: 'success',
      showConfirmButton: false,
      timer: 1500
    });

    // ⬇️ On attend la fin du toast puis on recharge la page
    setTimeout(() => {
      window.location.reload();
    }, 1500);
  },
  (error) => {
    let backendMessage = error.error?.message || "Une erreur est survenue";

    if (error.status === 409) {
      backendMessage = "Cet email existe déjà ! Veuillez en choisir un autre.";
    }

    Swal.fire({
      title: 'Erreur',
      text: backendMessage,
      icon: 'error',
      confirmButtonText: 'OK'
    });
  }
);






    
     
  }
  

}