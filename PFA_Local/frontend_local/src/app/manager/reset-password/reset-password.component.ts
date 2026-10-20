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
  // HttpErrorResponse?.error peut être string | object | Blob | ProgressEvent...
  const e = err?.error;

  if (typeof e === 'string') return e;

  if (e && typeof e === 'object') {
    if (typeof e.message === 'string') return e.message;
    if (typeof e.error === 'string') return e.error;
    if (Array.isArray((e as any).errors)) return (e as any).errors.join('\n');
    try { return JSON.stringify(e); } catch { /* ignore */ }
  }

  if (typeof err?.message === 'string') return err.message;

  return 'Une erreur inconnue est survenue.';
}


  

}
