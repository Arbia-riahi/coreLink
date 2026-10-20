import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import Swal from 'sweetalert2'

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['../auth-shared.css', './forgot-password.component.css']
})
export class ForgotPasswordComponent {
  email = '';
  message = '';

  constructor(private userService: UserService) {}

  onForgotPassword() {
    if (!this.email) {
      Swal.fire({ icon: 'warning', title: 'Email requis', text: 'Merci de saisir votre email.' });
      return;
    }

    this.userService.forgotPassword(this.email).subscribe({
      next: (res) => {
        const msg = res?.message || 'Un email de réinitialisation a été envoyé.';
        const email = res?.email ? `email : ${res.email}` : '';
       

        Swal.fire({
          icon: 'success',
          title: 'lien envoyé',
          html: `${msg}<br><small>${email}</small>`,
          confirmButtonColor: '#3f51b5'
        });
      },
      error: (err) => {
        const errorMessage =
          err?.error?.message || err?.error || 'Une erreur est survenue.';
        Swal.fire({ icon: 'error', title: 'Erreur', text: errorMessage, confirmButtonColor: '#d33' });
      }
    });
  }
}

