import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css'] // <- styleUrls (pluriel)
})
export class LogoutComponent implements OnInit {
  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authenticationService.logOut();
    // Remplace l’URL courante pour éviter un "back" vers une page protégée
    this.router.navigate(['/home'], { replaceUrl: true });
  }
}
