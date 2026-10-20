import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-not-page-f',
  templateUrl: './not-page-f.component.html',
  styleUrl: './not-page-f.component.css'
})
export class NotPageFComponent {
  constructor(private router: Router) {}
  goHome() { this.router.navigateByUrl('/memeber/listProjet'); }

}

