import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/projet.service';
import { UserService } from '../../services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-update-projet',
  templateUrl: './update-projet.component.html',
  styleUrls: ['./update-projet.component.css']
})
export class UpdateProjetComponent implements OnInit {

  project = {
    titre: '',
    description: '',
    dateDebut: '',
    dateFin: '',
    status: '',
    membres: [] as string[],
    fichiers: [] as string[]   // valeurs DB: "/uploads/files/uuid_nom.ext"
  };

  /** NEW: fichiers existants marqués pour suppression côté serveur */
  removedFiles: string[] = [];

  private originalProject: any = null;
  isModified = false;

  selectedFiles: File[] = [];
  membersList: any[] = [];
  projectId!: number;

  fileError = '';
  dateErrorDebut = '';
  dateErrorFin = '';

  urlUpload = 'http://localhost:8081/uploads';

  statuses = [
    { value: 'EN_ATTENTE', label: 'En Attente' },
    { value: 'EN_COURS',   label: 'En cours'   },
    { value: 'TERMINE',    label: 'Terminé'    },
    { value: 'ANNULE',     label: 'Annulé'     }
  ];

  constructor(
    private projectService: ProjectService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.projectId = Number(this.route.snapshot.paramMap.get('numeroProjet'));
    this.loadProject(this.projectId);
    this.loadMembers();
  }

  loadProject(id: number) {
    this.projectService.getProjectByNumero(id).subscribe({
      next: (data) => {
        const statusValue = this.statuses.find(
          s => s.label.toLowerCase() === (data.status || '').toLowerCase()
        )?.value || 'EN_ATTENTE';

        this.project = {
          titre: data.titre || '',
          description: data.description || '',
          dateDebut: data.dateDebut || '',
          dateFin: data.dateFin || '',
          status: statusValue,
          membres: (data.membres || []).map((m: any) => m.matricule),
          fichiers: data.fichiers || []         // chemins DB
        };

        this.removedFiles = [];                 // reset important
        this.originalProject = this.normalizeProject(this.project);
        this.isModified = false;
      },
      error: () => Swal.fire('Erreur', 'Impossible de charger le projet.', 'error')
    });
  }

  loadMembers() {
    this.userService.getAllMembers().subscribe({
      next: (data: any[]) => this.membersList = data || [],
      error: () => Swal.fire('Erreur', 'Impossible de charger les membres.', 'error')
    });
  }

  private normalizeProject(p: any) {
    return {
      titre: (p.titre || '').trim(),
      description: (p.description || '').trim(),
      dateDebut: p.dateDebut || '',
      dateFin: p.dateFin || '',
      status: p.status || '',
      membres: [...(p.membres || [])].sort(),
      fichiers: [...(p.fichiers || [])].sort()
    };
  }

  private checkIfModified() {
    const current = this.normalizeProject(this.project);
    const original = this.originalProject;
    const changed = JSON.stringify(current) !== JSON.stringify(original);
    const hasNewFiles = this.selectedFiles.length > 0;
    const hasRemoved = this.removedFiles.length > 0;      // NEW
    this.isModified = changed || hasNewFiles || hasRemoved;
  }

  onAnyFieldChanged() {
    this.checkIfModified();
  }

  onMemberChecked(event: Event, matricule: string) {
    const input = event.target as HTMLInputElement;
    if (input.checked) {
      if (!this.project.membres.includes(matricule)) {
        this.project.membres.push(matricule);
      }
    } else {
      this.project.membres = this.project.membres.filter(m => m !== matricule);
    }
    this.checkIfModified();
  }

  removeFile(index: number, event: Event) {
    event.stopPropagation();
    this.selectedFiles.splice(index, 1);
    this.checkIfModified();
  }

  /** Suppression d'un fichier existant (marquage + UI) */
  removeExistingFile(index: number) {
    const removedPath = this.project.fichiers[index]; // valeur DB exacte
    // on le cache de l'UI
    this.project.fichiers.splice(index, 1);
    // on le marque pour suppression côté serveur
    if (removedPath && !this.removedFiles.includes(removedPath)) {
      this.removedFiles.push(removedPath);
    }
    this.checkIfModified();
  }

  selectStatus(statusValue: string) {
    if (this.project.status !== statusValue) {
      this.project.status = statusValue;
      this.checkIfModified();
      this.validateDates();
    }
  }

  getChipClass(statusValue: string): string {
    switch (statusValue) {
      case 'EN_ATTENTE': return 'chip-waiting';
      case 'EN_COURS':   return 'chip-progress';
      case 'TERMINE':    return 'chip-done';
      case 'ANNULE':     return 'chip-cancel';
      default:           return '';
    }
  }

  onDateChanged() {
    this.validateDates();
    this.checkIfModified();
  }

  validateDates() {
    this.dateErrorDebut = '';
    this.dateErrorFin = '';

    const today = new Date(); today.setHours(0,0,0,0);
    const debut = this.project.dateDebut ? new Date(this.project.dateDebut) : null;
    const fin   = this.project.dateFin   ? new Date(this.project.dateFin)   : null;

    if (debut) debut.setHours(0,0,0,0);
    if (fin)   fin.setHours(0,0,0,0);

    if (debut && (this.project.status === 'EN_ATTENTE' || this.project.status === 'EN_COURS')) {
      if (debut < today) {
        this.dateErrorDebut = "La date de début doit être aujourd'hui ou dans le futur.";
      }
    }
    if (debut && fin && fin <= debut) {
      this.dateErrorFin = "La date de fin doit être postérieure à la date de début.";
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
      this.selectedFiles = Array.from(event.dataTransfer.files);
      this.fileError = '';
      this.validateSelectedFiles(this.selectedFiles);
      this.checkIfModified();
    }
  }

  openFileDialog() {
    const input = document.querySelector<HTMLInputElement>('.hidden-input');
    if (input) input.click();
  }

  onFilesChanged(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      const files = Array.from(input.files);
      this.fileError = '';
      if (!this.validateSelectedFiles(files)) return;
      this.selectedFiles = [...this.selectedFiles, ...files];
      this.checkIfModified();
    }
  }

  /** Validation des fichiers (quota total = existants restants + nouveaux) */
  private validateSelectedFiles(files: File[]): boolean {
    this.fileError = '';

    const total = this.project.fichiers.length /* restants */
                + this.selectedFiles.length
                + files.length;
    if (total > 5) {
      this.fileError = "Vous ne pouvez pas avoir plus de 5 fichiers au total.";
      return false;
    }

    for (const file of files) {
      const extension = file.name.split('.').pop()?.toLowerCase();
      if (!['pdf','doc','docx','png','jpg','jpeg','txt','xlsx','csv'].includes(extension || '')) {
        this.fileError = "Extension de fichier non autorisée.";
        return false;
      }
    }
    return true;
  }

  /** Soumission du formulaire */
  onSubmit() {
    this.project.titre = this.project.titre.trim();
    this.project.description = this.project.description.trim();

    if (this.project.dateDebut && this.project.dateFin) {
      const debut = new Date(this.project.dateDebut);
      const fin   = new Date(this.project.dateFin);
      if (fin <= debut) {
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'La date de fin doit être postérieure à la date de début.',
          confirmButtonColor: '#d33'
        });
        return;
      }
    }

    const formData = new FormData();
    formData.append('titre', this.project.titre);
    formData.append('description', this.project.description);
    formData.append('status', this.project.status);
    formData.append('dateDebut', this.project.dateDebut || '');
    formData.append('dateFin', this.project.dateFin || '');

    this.project.membres.forEach(m => formData.append('membres', m));
    this.selectedFiles.forEach(file => formData.append('fichiers', file));

    // NEW: envoyer les chemins EXACTS à supprimer (côté DB)
    this.removedFiles.forEach(path => formData.append('removedFiles', path));

    this.projectService.updateProject(this.projectId, formData).subscribe({
      next: () => {
        Swal.fire('Succès', 'Projet modifié avec succès', 'success')
          .then(() => this.router.navigate(['/manager/listProjet']));
      },
      error: (err) => {
        console.error('Erreur lors de la modification :', err);
        Swal.fire('Erreur', 'Impossible de modifier le projet', 'error');
      }
    });
  }

  getPrettyFileName(path: string): string {
    if (!path) return '';
    const last = decodeURIComponent(path.split('/').pop() || '');
    return last.replace(/^[^_]+_/, '');
  }

  getFileUrl(file: string): string {
    return file.startsWith('http') || file.startsWith('/')
      ? `${file}`
      : `${this.urlUpload}/${file}`;
  }
}
