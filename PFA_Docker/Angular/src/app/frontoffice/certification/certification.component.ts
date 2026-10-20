import { Component, OnInit } from '@angular/core';
import { CertificationService } from '../../services/certification.service';

@Component({
  selector: 'app-certification',
  templateUrl: './certification.component.html',
  styleUrl: './certification.component.css'
})
export class CertificationComponent implements OnInit {



  certifications: any[] = [];
  certificationGroups: any[][] = []; // Tableau de groupes de 4 certifications

  constructor(private certificationService: CertificationService) {}

  ngOnInit(): void {
    this.loadCertifications();
  }

  loadCertifications() {
    this.certificationService.getAllCertifications()
      .subscribe((res: any) => {
        this.certifications = res;
        this.groupCertifications();
      });
  }

  // Regrouper les certifications par 4
  private groupCertifications() {
    this.certificationGroups = [];
    for (let i = 0; i < this.certifications.length; i += 4) {
      this.certificationGroups.push(this.certifications.slice(i, i + 4));
    }
  }

  // Construire l'URL complète du logo
  getLogoUrl(fileName: string): string {
    return `http://localhost:8080/uploads/files/${fileName}`;
  }
}
