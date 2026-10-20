import { Component, OnInit } from '@angular/core';
import { CertificationService } from '../../services/certification.service';

@Component({
  selector: 'app-list-certification',
  templateUrl: './list-certification.component.html'
})
export class ListCertificationComponent implements OnInit {
  certifications: any[] = [];

  constructor(private certificationService: CertificationService) {}

  ngOnInit(): void {
    this.loadCertifications();
  }

  loadCertifications() {
    this.certificationService.getAllCertifications().subscribe((res: any) => {
      this.certifications = res;
    });
  }




getLogoUrl(photoName: string): string {
  return `http://localhost:8080/uploads/files/${photoName}`;
}

getImageName(fullPath: string): string {
  // Si l'image est déjà une URL complète
  if (fullPath.includes('http')) {
    return fullPath.split('/').pop() || fullPath;
  }
  return fullPath;
}


  deleteCertification(id: number) {
    if (confirm('Voulez-vous vraiment supprimer cette certification ?')) {
      this.certificationService.deleteCertification(id)
        .subscribe(() => this.loadCertifications());
    }
  }
}

