import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-update-password',
  templateUrl: './update-password.component.html',
  styleUrls: ['./update-password.component.css']
})
export class UpdatePasswordComponent implements OnInit {
  oldPassword = '';
  newPassword = '';
  confirmPassword = '';
  message = '';
  isFirstLogin = false; // état AVANT changement

  // indicateur (optionnel) si l'ancien mot de passe a déjà été vérifié
  isOldVerified: boolean | null = null;

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit() {
    this.isFirstLogin = localStorage.getItem('mustChangePassword') === 'true';
  }

  /** Vérification de la force du mot de passe */
  isPasswordStrong(password: string): boolean {
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return regex.test(password);
  }

  /** Redirection basée sur l'état initial */
  private redirectByInitialState() {
    if (this.isFirstLogin) {
      this.router.navigate(['/manager/tableauBoard']);
    } else {
      localStorage.clear();
      this.router.navigate(['/home/login']);
    }
  }

  /** Vérifie l'ancien mot de passe et affiche une alerte de succès si correct */
  verifyOldPassword(oldPwd: string) {
    return this.userService.verifyOldPassword(oldPwd);
  }

  onChangePassword() {
    // --- Vérification des champs vides ---
    const oldPwd = this.oldPassword?.trim();
    const newPwd = this.newPassword?.trim();
    const confirmPwd = this.confirmPassword?.trim();

    const missing: string[] = [];
    if (!oldPwd)     missing.push('Ancien mot de passe');
    if (!newPwd)     missing.push('Nouveau mot de passe');
    if (!confirmPwd) missing.push('Confirmation du mot de passe');

    if (missing.length) {
      Swal.fire({
        icon: 'warning',
        title: 'Champs manquants',
        html: `Merci de fournir toutes les informations demandées`,
        confirmButtonText: 'OK'
      });
      return;
    }
    // -------------------------------------

    if (newPwd !== confirmPwd) {
      Swal.fire('Erreur', 'Les mots de passe ne correspondent pas !', 'error');
      return;
    }

    if (!this.isPasswordStrong(newPwd)) {
      Swal.fire(
        'Mot de passe faible',
        'Le mot de passe doit contenir au moins 8 caractères, avec une majuscule, une minuscule, un chiffre et un caractère spécial.',
        'warning'
      );
      return;
    }

    // 1) Vérifier l'ancien mot de passe
   this.userService.verifyOldPassword(oldPwd).subscribe({
  next: ({ valid }) => {
    if (!valid) {
      Swal.fire('Erreur', 'Ancien mot de passe incorrect !', 'error');
      return;
    }
    // OK -> toast succès puis changement du mot de passe
    Swal.fire({
      icon: 'success',
      title: 'Ancien mot de passe confirmé',
      toast: true, position: 'top-end', showConfirmButton: false, timer: 1200
    }).then(() => {
      this.userService.changePassword(oldPwd, newPwd).subscribe({
        next: () => {
          localStorage.setItem('mustChangePassword', 'false');
          Swal.fire({ title: 'Succès', text: 'Mot de passe modifié', icon: 'success', showConfirmButton: false, timer: 1500 })
            .then(() => this.redirectByInitialState());
        },
        error: (err) => Swal.fire('Erreur', err?.error || 'Une erreur est survenue', 'error')
      });
    });
  },
  error: (err) => {
    const msg = err?.error?.message || "Impossible de vérifier l'ancien mot de passe.";
    Swal.fire('Erreur', msg, 'error');
  }
});

  }

  onCancel() {
    this.redirectByInitialState();
  }
}
