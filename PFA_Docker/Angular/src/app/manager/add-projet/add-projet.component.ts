import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/projet.service';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-projet',
  templateUrl: './add-projet.component.html',
  styleUrls: ['./add-projet.component.css']
})
export class AddProjetComponent implements OnInit {

  project = {
    titre: '',
    description: '',
    dateDebut: '',
    dateFin: '',
    status: '',
    membres: [] as string[]
  };

  selectedFiles: File[] = [];
  // "today" au format YYYY-MM-DD à minuit (local) pour éviter les surprises de TZ
  today = (() => {
    const now = new Date();
    const mid = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    // toISOString est en UTC; on veut YYYY-MM-DD seulement
    const pad = (n: number) => (n < 10 ? '0' + n : '' + n);
    return `${mid.getFullYear()}-${pad(mid.getMonth() + 1)}-${pad(mid.getDate())}`;
  })();

  // Messages d'erreur dates
  dateError = '';       // fin vs début
  dateDebutError = '';  // début vs aujourd’hui
  fileError = '';

  statuses = [
    { value: 'EN_ATTENTE', label: 'En attente' },
    { value: 'EN_COURS',    label: 'En cours' },
    { value: 'TERMINE',     label: 'Terminé' },
    { value: 'ANNULE',      label: 'Annulé' }
  ];

  membersList: any[] = [];

  constructor(
    private projectService: ProjectService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit() {
    this.userService.getAllMembers().subscribe({
      next: (data: any[]) => {
        this.membersList = data || [];
      },
      error: () => Swal.fire('Erreur', 'Impossible de charger les membres.', 'error')
    });
  }

  // ========== VALIDATIONS ==========

  private toMidnight(d: Date): Date {
    return new Date(d.getFullYear(), d.getMonth(), d.getDate());
  }

  validateDates(): void {
    this.dateError = '';
    this.dateDebutError = '';

    const hasDebut = !!this.project.dateDebut;
    const hasFin   = !!this.project.dateFin;

    const todayMid = this.toMidnight(new Date());

    let debut: Date | null = null;
    let fin: Date | null = null;

    if (hasDebut) {
      const d = new Date(this.project.dateDebut);
      if (isNaN(d.getTime())) {
        this.dateDebutError = 'Date de début invalide.';
        return;
      }
      debut = this.toMidnight(d);

      // Règle: début >= aujourd’hui
      if (debut < todayMid) {
        this.dateDebutError = 'La date de début doit être aujourd’hui ou plus tard.';
      }
    }

    if (hasFin) {
      const f = new Date(this.project.dateFin);
      if (isNaN(f.getTime())) {
        this.dateError = 'Date de fin invalide.';
        return;
      }
      fin = this.toMidnight(f);
    }

    // Règle: fin >= début
    if (debut && fin && fin < debut) {
      this.dateError = 'La date de fin doit être postérieure ou égale à la date de début.';
    }
  }

  canSubmit(form: any): boolean {
    const baseValid    = form?.form?.valid === true; // requireds du template
    const statusValid  = !!this.project.status;
    const membresOk    = this.project.membres.length > 0;
    const datesOk      = !this.dateError && !this.dateDebutError;
    const filesOk      = !this.fileError; // fichiers facultatifs; on bloque seulement si erreur détectée
    return baseValid && statusValid && membresOk && datesOk && filesOk;
  }

  // ========== EVENTS ==========

  onMemberChecked(event: Event, matricule: string) {
    const input = event.target as HTMLInputElement;
    if (input.checked) {
      if (!this.project.membres.includes(matricule)) {
        this.project.membres.push(matricule);
      }
    } else {
      this.project.membres = this.project.membres.filter((m) => m !== matricule);
    }
  }

  onFilesChanged(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.tryAddFiles(Array.from(input.files));
    }
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    (event.target as HTMLElement).classList.add('drag-over');
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    (event.target as HTMLElement).classList.remove('drag-over');
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    (event.target as HTMLElement).classList.remove('drag-over');

    if (event.dataTransfer && event.dataTransfer.files) {
      this.tryAddFiles(Array.from(event.dataTransfer.files));
    }
  }

  openFileDialog() {
    const input = document.querySelector<HTMLInputElement>('.hidden-input');
    input?.click();
  }

  removeFile(index: number, event: Event) {
    event.stopPropagation();
    this.selectedFiles = this.selectedFiles.filter((_, i) => i !== index);
  }

  getChipClass(status: string): string {
    switch (status) {
      case 'EN_ATTENTE': return 'chip-waiting';
      case 'EN_COURS':   return 'chip-progress';
      case 'TERMINE':    return 'chip-done';
      case 'ANNULE':     return 'chip-cancel';
      default:           return '';
    }
  }

  selectStatus(status: string) {
    this.project.status = status;
    this.validateDates(); // pour ajuster min de dateFin côté template
  }

  // ========== SUBMIT ==========

  onSubmit() {
    // Garde UX
    this.validateDates();
    if (this.dateDebutError || this.dateError) {
      Swal.fire({
        icon: 'error',
        title: 'Erreur',
        text: this.dateDebutError || this.dateError,
        confirmButtonColor: '#d33'
      });
      return;
    }

    // Garde défensive (double-check)
    if (this.project.dateDebut) {
      const debutMid = this.toMidnight(new Date(this.project.dateDebut));
      const todayMid = this.toMidnight(new Date());
      if (debutMid < todayMid) {
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'La date de début doit être aujourd’hui ou plus tard.',
          confirmButtonColor: '#d33'
        });
        return;
      }
    }
    if (this.project.dateDebut && this.project.dateFin) {
      const d = this.toMidnight(new Date(this.project.dateDebut));
      const f = this.toMidnight(new Date(this.project.dateFin));
      if (f < d) {
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'La date de fin doit être postérieure ou égale à la date de début.',
          confirmButtonColor: '#d33'
        });
        return;
      }
    }

    // Build form data
    const formData = new FormData();
    formData.append('titre', this.project.titre);
    formData.append('description', this.project.description);
    formData.append('status', this.project.status);
    if (this.project.dateDebut) formData.append('dateDebut', this.project.dateDebut);
    if (this.project.dateFin)   formData.append('dateFin', this.project.dateFin);

    this.project.membres.forEach(m => formData.append('membres', m));
    this.selectedFiles.forEach(f => formData.append('fichiers', f));

    const matriculeChef = localStorage.getItem('matricule') || '';

    this.projectService.createProject(matriculeChef, formData).subscribe({
      next: () => {
        Swal.fire('Succès', 'Projet créé avec succès', 'success')
          .then(() => this.router.navigate(['/manager/listProjet']));
      },
      error: (err) => {
        console.error('Erreur lors de la création du projet :', err);
        Swal.fire('Erreur', 'Impossible de créer le projet', 'error');
      }
    });
  }

  // ========== PRIVATE HELPERS ==========
  private tryAddFiles(files: File[]) {
    const validExtensions = ['pdf', 'doc', 'docx', 'png', 'jpg', 'jpeg', 'xlsx', 'csv', 'txt'];
    this.fileError = '';

    for (const file of files) {
      const ext = file.name.split('.').pop()?.toLowerCase();
      if (!ext || !validExtensions.includes(ext)) {
        this.fileError = 'Extension invalide : ' + file.name;
        return;
      }
    }

    if (this.selectedFiles.length + files.length > 5) {
      this.fileError = 'Vous ne pouvez pas sélectionner plus de 5 fichiers.';
      return;
    }

    this.selectedFiles = [...this.selectedFiles, ...files];
  }
}
