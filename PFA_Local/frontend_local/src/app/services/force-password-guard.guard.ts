import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class ForcePasswordGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(): boolean {
    const mustChangePassword = localStorage.getItem('mustChangePassword') === 'true';
    if (mustChangePassword) {
      Swal.fire({
        title: 'Changement de mot de passe requis',
        text: 'Vous devez changer votre mot de passe avant d\'utiliser la plateforme.',
        icon: 'warning',
        confirmButtonText: 'Changer maintenant'
      }).then(() => {
        // Redirection selon le rôle
        const roles = JSON.parse(localStorage.getItem('roles') || '[]');
        if (roles.includes('ADMIN')) {
          this.router.navigate(['/backoffice/updatePassword']);
        } else if (roles.includes('MANAGER')) {
          this.router.navigate(['/manager/updatePassword']);
        } else if (roles.includes('MEMBER')) {
          this.router.navigate(['/memeber/updatePassword']);
        }
      });
      return false;
    }
    return true;
  }
}









 

