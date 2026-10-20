import { Component } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']  
})
export class LoginComponent {
  matricule: string = '';
  password: string = '';
  showPassword = false;


  constructor(private router: Router, private loginservice: AuthenticationService) {}

  checkLogin() {
    if (!this.matricule || !this.password) {
      Swal.fire({
        icon: 'warning',
        title: 'Champs manquants',
        text: 'Veuillez saisir votre matricule et votre mot de passe.',
        confirmButtonColor: '#004080'
      });
      return;
    }
  this.loginservice.authenticate(this.matricule, this.password).subscribe(
      (data) => {
        const role = (data.roles && data.roles.length > 0) ? data.roles[0] : 'MEMBER';

        Swal.fire({
          icon: 'success',
          title: 'Connexion réussie',
          html: `Vous êtes connecté en tant que <b>${role}</b>`,
          showConfirmButton: false,
          timer: 1500
        }).then(() => {
          if (data.mustChangePassword) {
            this.redirectToUpdatePassword(role);
          } else {
            this.redirectToDashboard(role);
          }
        });
      },
    (error) => {
      console.error('Erreur connexion:', error);

      // Cas serveur down ou indisponible
      if (error.status === 0) {
        Swal.fire({
          icon: 'error',
          title: 'Serveur indisponible',
          text: 'Impossible de se connecter au serveur. Veuillez réessayer plus tard.',
          confirmButtonColor: '#d33'
        });
      }
      // Cas authentification invalide ou panne serveur détectée par backend
      else if (error.status === 401) {
        Swal.fire({
          icon: 'error',
          title: 'Identifiants incorrects',
          text: 'Matricule ou mot de passe incorrect.',
          confirmButtonColor: '#d33'
        });
      }
      else if (error.status === 503) {
        Swal.fire({
          icon: 'error',
          title: 'Service indisponible',
          text: 'Le service d’authentification est temporairement hors service. Veuillez réessayer plus tard.',
          confirmButtonColor: '#d33'
        });
      }
      else {
        Swal.fire({
          icon: 'error',
          title: 'Erreur inconnue',
          text: 'Une erreur inattendue est survenue.',
          confirmButtonColor: '#d33'
        });
      }
    }
  );
}

  

  private redirectToUpdatePassword(role: string) {
    if (role === 'ADMIN') {
      this.router.navigate(['/backoffice/updatePassword']);
    } else if (role === 'MANAGER') {
      this.router.navigate(['/manager/updatePassword']);
    } else if (role === 'MEMBER') {
      this.router.navigate(['/memeber/updatePassword']);
    }
  }

  private redirectToDashboard(role: string) {
    if (role === 'ADMIN') {
      this.router.navigate(['/backoffice']);
    } else if (role === 'MANAGER') {
      this.router.navigate(['/manager']);
    } else if (role === 'MEMBER') {
      this.router.navigate(['/memeber']);
    }
  }
}




  
  
  

  


