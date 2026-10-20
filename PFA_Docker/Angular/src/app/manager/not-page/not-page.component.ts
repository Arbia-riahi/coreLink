import { Router } from '@angular/router';
import { Component } from '@angular/core';

@Component({
  selector: 'app-not-page',
  templateUrl: './not-page.component.html',
  styleUrl: './not-page.component.css'
})
export class NotPageComponent {
    constructor(private router: Router) {}
 goHome() { this.router.navigateByUrl('/manager/tableauBoard'); }

}





