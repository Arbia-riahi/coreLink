import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CertificationService } from '../../services/certification.service';

@Component({
  selector: 'app-update-certification',
  templateUrl: './update-certification.component.html'
})
export class UpdateCertificationComponent implements OnInit {

  certification: any = {};      // Contient les infos de la certification
  selectedFile: File | null = null; 
  certificationId!: number;     
  imageError: boolean = false;  

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private certificationService: CertificationService
  ) {}

  ngOnInit(): void {
    this.certificationId = +this.route.snapshot.paramMap.get('id')!;
    this.loadCertification();
  }

  // Charger la certification par ID
  loadCertification() {
    this.certificationService.getCertificationById(this.certificationId)
      .subscribe((data: any) => {
        console.log("Certification chargée :", data);
        this.certification = data;
      }, error => {
        console.error("Erreur de chargement :", error);
      });
  }

  // Sélection d'un fichier
  onFileChange(event: any) {
    this.selectedFile = event.target.files[0];
  }

  // Construire l'URL complète du logo
 getLogoUrl(photoName: string): string {
  if (!photoName) return '';

  // Si c'est une URL complète et mal formée, on remplace "certifications" par "files"
  if (photoName.startsWith('http')) {
    return photoName.replace('/uploads/certifications/', '/uploads/files/');
  }

  // Sinon, c'est un nom de fichier simple
  return `http://localhost:8080/uploads/files/${photoName}`;
}


  // Si le logo ne peut pas être chargé
  handleImageError() {
    this.imageError = true;
  }

  // Mettre à jour la certification
  updateCertification() {
    const formData = new FormData();
    formData.append('nom', this.certification.nom);
    formData.append('description', this.certification.description);
    if (this.selectedFile) {
      formData.append('file', this.selectedFile);
    }

    this.certificationService.updateCertification(this.certificationId, formData)
      .subscribe(() => {
        console.log("Certification mise à jour !");
        this.router.navigate(['/backoffice/listCertification']);
      }, error => {
        console.error("Erreur lors de la mise à jour :", error);
      });
  }
}
