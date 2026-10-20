import { Component } from '@angular/core';
import { ActualiteService } from '../../services/actualite.service';
import { Router } from '@angular/router';
import Swal, { SweetAlertOptions } from 'sweetalert2';



@Component({
  selector: 'app-add-actualite',
  templateUrl: './add-actualite.component.html',
  styleUrls: ['./add-actualite.component.css']
})
export class AddActualiteComponent {
  filesToUpload: File[] = [];
  private lastFileInput?: HTMLInputElement;

  readonly allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/jpg', 'image/webp'];
  isSubmitting = false;

  constructor(private service: ActualiteService, private router: Router) {}

  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.lastFileInput = input;
    this.filesToUpload = Array.from(input.files || []);
  }

  private successAutoClose(title = 'Succès', text = 'Votre actualité a été créée avec succès.', ms = 2500) {
    const opts: SweetAlertOptions = {
      icon: 'success',
      title,
      text,
      timer: ms,
      timerProgressBar: true,
      showConfirmButton: false,
      allowOutsideClick: false,
      allowEscapeKey: false
    };
    return Swal.fire(opts);
  }

  /** Erreur: pas de timer, bouton OK */
  private errorModal(title = 'Erreur', text = 'Une erreur est survenue.') {
    const opts: SweetAlertOptions = {
      icon: 'error',
      title,
      text,
      showConfirmButton: true,
      confirmButtonText: 'OK',
      allowOutsideClick: false
    };
    return Swal.fire(opts);
  }


  private fileErrors(): string[] {
    const errors: string[] = [];
    if (!this.filesToUpload.length) {
      errors.push('Ajoutez au moins une photo.');
      return errors;
    }
    this.filesToUpload.forEach((file) => {
      if (!this.allowedTypes.includes(file.type)) {
        errors.push('Seuls les fichiers de type image (JPG, PNG, GIF, WEBP) sont autorisés');
      }
    });
    return errors;
  }

  isReadyToCreate(form: any): boolean {
    const titre = (form?.value?.titre || '').trim();
    const description = (form?.value?.description || '').trim();
    const hasImage = this.filesToUpload.length > 0;
    return titre.length > 0 && description.length > 0 && hasImage;
  }

  private validateAll(form: any): string[] {
    const titre = (form?.value?.titre || '').trim();
    const description = (form?.value?.description || '').trim();
    const errors: string[] = [];
    if (!titre) errors.push('Veuillez renseigner le titre.');
    if (!description) errors.push('Veuillez renseigner la description.');
    errors.push(...this.fileErrors());
    return errors;
  }

 async persistActualite(form: any): Promise<void> {
    if (this.isSubmitting) return;

    const errors = this.validateAll(form);
    if (errors.length) {
      const message = errors.map(e => `• ${e}`).join('\n');
      await this.errorModal('Erreur', message);
      return;
    }

    const titre: string = (form?.value?.titre || '').trim();
    const description: string = (form?.value?.description || '').trim();

    const formData = new FormData();
    formData.append('titre', titre);
    formData.append('description', description);
    this.filesToUpload.forEach(file => formData.append('imageFiles', file));

    this.isSubmitting = true;

    this.service.createActualite(formData).subscribe({
      next: async () => {
        this.isSubmitting = false;

        await this.successAutoClose('Succès', 'Votre actualité a été créée avec succès', 2500);
        this.router.navigate(['/backoffice/listActualite']);
        this.filesToUpload = [];
        if (this.lastFileInput) this.lastFileInput.value = '';
        form?.reset?.();
      },
      error: async (err) => {
        this.isSubmitting = false;
        const message = err?.error?.message || 'Une erreur est survenue. Merci de réessayer.';
        await this.errorModal('Échec de la création', message);
      }
    });
  }
}
