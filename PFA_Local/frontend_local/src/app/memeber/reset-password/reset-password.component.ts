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
  token!: string;
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

    console.log("Token dans URL :", this.token);

this.userService.validateResetToken(this.token).subscribe({
  next: (res) => console.log("Token valide :", res),
  error: (err) => {
    console.error("Erreur 400 validateResetToken :", err);
  }
});
  }

 


  onResetPassword() {
    if (this.newPassword !== this.confirmPassword) {
      this.message = 'Les mots de passe ne correspondent pas.';
      return;
    }

    this.userService.resetPassword(this.token, this.newPassword).subscribe({
      next: (res: any) => {
        this.message = res?.message || 'Mot de passe réinitialisé avec succès.';
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: this.message,
          confirmButtonColor: '#28a745'
        }).then(() => this.router.navigate(['/manager/consulterMonProfil']));
      },
      error: (err: any) => {
        this.message = this.extractErrorMessage(err);
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: this.message,
          confirmButtonColor: '#d33'
        });
      }
    });
  }

  private extractErrorMessage(err: any): string {
    if (!err) return 'Le lien de réinitialisation n’est plus valide.';
    if (typeof err === 'string') return err;
    if (err.error) {
      if (typeof err.error === 'string') return err.error;
      if (err.error.message) return err.error.message;
    }
    if (err.message) return err.message;
    return 'Une erreur inconnue est survenue.';
  }
}
