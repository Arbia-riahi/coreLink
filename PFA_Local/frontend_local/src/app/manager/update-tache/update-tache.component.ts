import { Component, OnInit, HostListener } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TacheService, Tache } from '../../services/tache.service';
import { ProjectService } from '../../services/projet.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-update-tache',
  templateUrl: './update-tache.component.html',
  styleUrls: ['./update-tache.component.css']
})
export class UpdateTacheComponent implements OnInit {
  tacheForm!: FormGroup;
  loading = false;
  projetId!: number;
  tacheId!: number;
  dateFinProjet!: string;
  initialTacheData: any;
  hasFormChanged = false; // 👈 ajouté

  priorites = ['BASSE', 'MOYENNE', 'HAUTE', 'CRITIQUE'];
  etats = ['A_FAIRE', 'EN_COURS', 'EN_REVISION', 'TERMINE', 'BLOQUE', 'ANNULE'];
  members: { matricule: string; nom: string; prenom: string }[] = [];

  constructor(
    private fb: FormBuilder,
    private tacheService: TacheService,
    private projetService: ProjectService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.projetId = +this.route.snapshot.paramMap.get('numeroProjet')!;
    this.tacheId = +this.route.snapshot.paramMap.get('id')!;

    this.tacheForm = this.fb.group({
      titre: ['', [Validators.required, Validators.maxLength(100)]],
      description: [''],
      priorite: ['BASSE', Validators.required],
      etat: ['A_FAIRE', Validators.required],
      dateFin: ['', [Validators.required, this.validateDateFin.bind(this)]],
      autresAssignes: [[]]
    });

    this.tacheService.getMembers().subscribe({
      next: (data) => (this.members = data),
      error: (err) => console.error('Erreur chargement membres', err)
    });

    this.projetService.getProjectByNumero(this.projetId).subscribe({
      next: (projet) => (this.dateFinProjet = projet.dateFin),
      error: (err) => console.error('Erreur chargement projet', err)
    });

    this.tacheService.getTacheById(this.tacheId).subscribe({
      next: (tache: Tache) => {
        this.fillForm(tache);
        this.initialTacheData = this.tacheForm.getRawValue();

        // 👇 Écoute les changements après remplissage
        this.tacheForm.valueChanges.subscribe(() => {
          this.hasFormChanged = this.hasChanges();
        });
      },
      error: (err) => console.error('Erreur chargement tâche', err)
    });
  }

  fillForm(tache: Tache): void {
    this.tacheForm.patchValue({
      titre: tache.titre,
      description: tache.description,
      priorite: tache.priorite,
      etat: tache.etat,
      dateFin: tache.dateFin,
      autresAssignes: tache.autresAssignes?.map((m) => m.matricule) || []
    });
  }

  private validateDateFin(control: AbstractControl) {
    if (!control.value) return null;
    const dateFinTache = new Date(control.value);
    const now = new Date();

    if (dateFinTache <= now) return { dateAvantCreation: true };
    if (this.dateFinProjet && new Date(this.dateFinProjet) < dateFinTache)
      return { dateDepasseProjet: true };

    return null;
  }

  private hasChanges(): boolean {
    const current = this.tacheForm.getRawValue();
    return JSON.stringify(current) !== JSON.stringify(this.initialTacheData);
  }

  @HostListener('window:beforeunload', ['$event'])
  unloadNotification($event: any) {
    if (this.hasChanges()) {
      $event.returnValue = true;
    }
  }

  onSubmit(): void {
    if (this.tacheForm.invalid) {
      this.tacheForm.markAllAsTouched();
      return;
    }

    if (!this.hasChanges()) {
      Swal.fire({
        title: 'Aucun changement détecté',
        text: 'Souhaitez-vous quitter la page ou rester pour modifier la tâche ?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Quitter',
        cancelButtonText: 'Rester',
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa'
      }).then((result) => {
        if (result.isConfirmed) {
          this.router.navigate(['/manager/listTaches', this.projetId]);
        }
      });
      return;
    }

    this.loading = true;

    const autres = (this.tacheForm.value.autresAssignes || []).map((matricule: string) => ({
      matricule,
      nom: '',
      prenom: ''
    }));

    const updatedTache: Tache = {
      id: this.tacheId,
      titre: this.tacheForm.value.titre,
      description: this.tacheForm.value.description,
      priorite: this.tacheForm.value.priorite,
      etat: this.tacheForm.value.etat,
      dateFin: this.tacheForm.value.dateFin,
      autresAssignes: autres,
      projetId: this.projetId
    };

    this.tacheService.updateTache(this.tacheId, updatedTache).subscribe({
      next: () => {
        this.loading = false;
        Swal.fire({
          icon: 'success',
          title: 'Tâche mise à jour',
          text: 'Les modifications ont été enregistrées avec succès.',
          confirmButtonColor: '#3085d6'
        }).then(() => {
          this.router.navigate(['/manager/listTaches', this.projetId]);
        });
      },
      error: (err) => {
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Impossible de mettre à jour la tâche.',
          confirmButtonColor: '#d33'
        });
        console.error('Erreur modification tâche', err);
      }
    });
  }

    getPriorityLabel(priority: string): string {
    switch (priority) {
      case 'BASSE': return 'Basse';
      case 'MOYENNE': return 'Moyenne';
      case 'HAUTE': return 'Haute';
      case 'CRITIQUE': return 'Critique';
      default: return priority;
    }
  }
   getFlagIconClass(priority: string): string {
    switch (priority) {
      case 'BASSE': return 'fas fa-flag text-secondary';
      case 'MOYENNE': return 'fas fa-flag text-primary';
      case 'HAUTE': return 'fas fa-flag text-warning';
      case 'CRITIQUE': return 'fas fa-flag text-danger';
      default: return 'fas fa-flag text-secondary';
    }
  }
}
