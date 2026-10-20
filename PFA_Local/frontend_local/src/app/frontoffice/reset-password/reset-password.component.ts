import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['../auth-shared.css', './reset-password.component.css']
})

export class ResetPasswordComponent implements OnInit {
  token = '';
  password = '';
  confirm = '';
  show = false;

  // indicateurs visuels des critères (même règle que onSubmit)
  get hasMinLength() { return this.password.length >= 8; }
  get hasUpper() { return /[A-Z]/.test(this.password); }
  get hasLower() { return /[a-z]/.test(this.password); }
  get hasDigit() { return /\d/.test(this.password); }
  get hasSymbol() { return /[@$!%*?&]/.test(this.password); }

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';

    if (!this.token) {
      Swal.fire({ icon: 'error', title: 'Token manquant', text: 'Lien invalide.' })
        .then(() => this.router.navigate(['/forgotPassword']));
      return;
    }

    // Vérifier la validité/expiration côté backend
    this.userService.validateResetToken(this.token).subscribe({
      next: () => {},
      error: (err) => {
        const msg = err?.error?.message || 'Lien de réinitialisation invalide ou expiré.';
        Swal.fire({ icon: 'error', title: 'Lien invalide', text: msg })
          .then(() => this.router.navigate(['/forgotPassword']));
      }
    });
  }

  onSubmit() {
    if (this.password !== this.confirm) {
      Swal.fire({ icon: 'warning', title: 'Attention', text: 'Les mots de passe ne correspondent pas.' });
      return;
    }

    // Même règle de complexité que le backend:
    const strong = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/;
    if (!strong.test(this.password)) {
      Swal.fire({
        icon: 'info',
        title: 'Mot de passe faible',
        text: 'Utilisez au moins 8 caractères avec majuscule, minuscule, chiffre et symbole.'
      });
      return;
    }

    this.userService.resetPassword(this.token, this.password).subscribe({
      next: (res) => {
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: res?.message || 'Mot de passe réinitialisé.'
        }).then(() => this.router.navigate(['/home/login'])); // ⇦ redirection demandée
      },
      error: (err) => {
        const msg = err?.error?.message || 'Impossible de réinitialiser le mot de passe.';
        Swal.fire({ icon: 'error', title: 'Erreur', text: msg });
      }
    });
  }
}

