import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  token = '';
  newPassword = '';
  confirmPassword = '';
  message = ''; 

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';

    this.userService.validateResetToken(this.token).subscribe({
      next: () => {}, // token OK
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Lien expiré',
          text: this.extractErrorMessage(err) || 'Le lien de réinitialisation n’est plus valide.',
          confirmButtonColor: '#d33'
        }).then(() => this.router.navigate(['/backoffice/forgotPassword']));
      }
    });
  }

  onResetPassword() {
    if (this.newPassword !== this.confirmPassword) {
      Swal.fire({
        icon: 'warning',
        title: 'Attention',
        text: 'Les mots de passe ne correspondent pas.',
      });
      return;
    }

    this.userService.resetPassword(this.token, this.newPassword).subscribe({
      next: (res) => {
        // res est { message: string }
        this.message = res?.message || 'Mot de passe réinitialisé avec succès.';
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: this.message,
          confirmButtonColor: '#28a745'
        }).then(() => this.router.navigate(['/backoffice/login'])); // ou ta route cible
      },
      error: (err) => {
        const msg = this.extractErrorMessage(err);
        this.message = msg; // seulement si tu gardes un <div>{{message}}</div> dans le template
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: msg,
          confirmButtonColor: '#d33'
        });
      }
    });
  }

  private extractErrorMessage(err: any): string {
    const e = err?.error;
    if (typeof e === 'string') return e;
    if (e && typeof e === 'object') {
      if (typeof e.message === 'string') return e.message;
      if (typeof e.error === 'string') return e.error;
      try { return JSON.stringify(e); } catch {}
    }
    if (typeof err?.message === 'string') return err.message;
    return 'Une erreur inconnue est survenue.';
  }
}
