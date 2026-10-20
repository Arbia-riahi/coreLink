import { Component } from '@angular/core';

import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-page-not-found',
  templateUrl: './page-not-found.component.html',
  styleUrl: './page-not-found.component.css'
})
export class PageNotFoundComponent {
  constructor(private router: Router, private auth:AuthenticationService) {}
  

  goBackToDashboard() {
  const role = this.auth.  getPrimaryRole();
  const target =
    role === 'ADMIN'   ? '/backoffice' :
    role === 'MANAGER' ? '/manager'   :
    role === 'MEMBER'  ? '/memeber'   : '/home';

  this.router.navigateByUrl(target);
}


 

}



