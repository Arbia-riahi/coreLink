import { Component, OnInit } from '@angular/core';
import { CertificationService } from '../../services/certification.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-add-certification',
  templateUrl: './add-certification.component.html'
})
export class AddCertificationComponent implements OnInit {
  certification: any = {};
  selectedFile: File | null = null;
  isUpdate = false;
  certificationId!: number;

  constructor(
    private certificationService: CertificationService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isUpdate = true;
      this.certificationId = +id;
      this.certificationService.getCertificationById(this.certificationId)
        .subscribe(res => this.certification = res);
    }
  }

  onFileChange(event: any) {
    this.selectedFile = event.target.files[0];
  }

  persistCertification() {
    const formData = new FormData();
    formData.append('nom', this.certification.nom);
    formData.append('description', this.certification.description);
    if (this.selectedFile) {
      formData.append('file', this.selectedFile);
    }

    if (this.isUpdate) {
      this.certificationService.updateCertification(this.certificationId, formData)
        .subscribe(() => this.router.navigate(['/backoffice/listCertification']));
    } else {
      this.certificationService.createCertification(formData)
        .subscribe(() => this.router.navigate(['/backoffice/listCertification']));
    }
  }
}

