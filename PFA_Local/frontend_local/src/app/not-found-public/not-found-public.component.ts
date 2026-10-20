import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthenticationService } from '../services/authentication.service';

type Role = 'ADMIN' | 'MANAGER' | 'MEMBER';

@Component({
  standalone: true,
  selector: 'app-not-found-public',
  imports: [CommonModule],
  templateUrl: './not-found-public.component.html',
  styleUrls: ['./not-found-public.component.css'] // ⚠️ bien "styleUrls"
})
export class NotFoundPublicComponent {
  constructor(private router: Router, private auth: AuthenticationService) {}

  private readonly DASHBOARD_BY_ROLE: Record<Role, string> = {
    ADMIN: '/backoffice/tableauBoard',
    MANAGER: '/manager/tableauBoard',
    MEMBER: '/memeber/listProjet'
  };

  goHome() {
    if (this.auth.isUserLoggedIn()) {
      const role = (this.auth.getRoles() as Role[])[0];
      const target = (role && this.DASHBOARD_BY_ROLE[role]) ? this.DASHBOARD_BY_ROLE[role] : '/backoffice/tableauBoard';
      this.router.navigateByUrl(target);
    } else {
      this.router.navigateByUrl('/home');
    }
  }
}
