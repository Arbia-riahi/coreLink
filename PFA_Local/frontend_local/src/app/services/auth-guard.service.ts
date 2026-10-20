import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuthenticationService } from './authentication.service';
import Swal from 'sweetalert2';

type Role = 'ADMIN' | 'MANAGER' | 'MEMBER';

@Injectable({ providedIn: 'root' })
export class AuthGuardService implements CanActivate {
  private readonly DASHBOARD_BY_ROLE: Record<Role, string> = {
    ADMIN: '/backoffice/tableauBoard/',
    MANAGER: '/manager/tableauBoard',
    MEMBER: '/memeber/listProjet'
  };

  constructor(private authService: AuthenticationService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
    const expectedRole: Role | undefined = route.data['expectedRole'];

    // 1) Non connecté => /home
    if (!this.authService.isUserLoggedIn()) {
      return this.router.createUrlTree(['/home']);
    }

    // ⚠️ lire les rôles uniquement après la vérification de connexion
    const userRoles = this.authService.getRoles() as Role[];

    // 2) Rôle non autorisé => message + redirection vers le dashboard du user
    if (expectedRole && !userRoles.includes(expectedRole)) {
      const target = this.getDefaultDashboard(userRoles);
      Swal.fire({
        icon: 'error',
        title: 'Accès refusé',
        text: 'Vous n’avez pas les droits nécessaires pour accéder à cette page.',
        confirmButtonColor: '#d33'
      });
      return this.router.createUrlTree([target]);
    }

    return true;
    }

  private getDefaultDashboard(userRoles: Role[]): string {
    const firstRole = userRoles?.[0];
    return (firstRole && this.DASHBOARD_BY_ROLE[firstRole]) ? this.DASHBOARD_BY_ROLE[firstRole] : '/home';
  }
}
