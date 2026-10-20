import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {

  email = '';
  message = '';

  constructor(private userService: UserService) {}

 onForgotPassword() {
    this.userService.forgotPassword(this.email).subscribe({
      next: (res: any) => {
        // Si le backend renvoie une string ou un objet
        const message = typeof res === 'string' ? res : 'Un email de réinitialisation a été envoyé.';
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: message,
          confirmButtonColor: '#3f51b5'
        });
      },
      error: (err: any) => {
        console.error('Erreur backend:', err);

        let errorMessage = 'Une erreur est survenue.';
        if (err.error) {
          if (typeof err.error === 'string') {
            errorMessage = err.error;
          } else if (err.error.message) {
            errorMessage = err.error.message;
          } else if (typeof err.error === 'object') {
            errorMessage = JSON.stringify(err.error);
          }
        }

        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: errorMessage,
          confirmButtonColor: '#d33'
        });
      }
    });
  }
}