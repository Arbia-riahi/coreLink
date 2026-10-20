import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TacheService, Tache } from '../../services/tache.service';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';
import Swal from 'sweetalert2';
import { ProjectService } from '../../services/projet.service';

@Component({
  selector: 'app-create-tache',
  templateUrl: './create-tache.component.html',
  styleUrls: ['./create-tache.component.css']
})
export class CreateTacheComponent implements OnInit {
  projetId!: number;
  tacheForm!: FormGroup;
  loading = false;
  dateFinProjet!: string;

  priorites = ['BASSE', 'MOYENNE', 'HAUTE', 'CRITIQUE'];
  etats = ['A_FAIRE', 'EN_COURS', 'EN_REVISION', 'TERMINE', 'BLOQUE', 'ANNULE'];
  members: { matricule: string, nom: string, prenom: string }[] = [];

  constructor(
    private fb: FormBuilder,
    private tacheService: TacheService,
    private projetService: ProjectService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.projetId = +this.route.snapshot.paramMap.get('numeroProjet')!;

    this.tacheService.getMembers().subscribe({
      next: (data) => (this.members = data),
      error: (err) => console.error('Erreur chargement membres', err)
    });

    this.projetService.getProjectByNumero(this.projetId).subscribe({
      next: (projet) => (this.dateFinProjet = projet.dateFin),
      error: (err) => console.error('Erreur chargement projet', err)
    });

    // Tous les champs deviennent facultatifs
    this.tacheForm = this.fb.group({
      titre: [''],
      description: [''],
      priorite: [''],
      etat: [''],
      dateFin: [''],
      autresAssignes: [[]]
    });
  }

  // Gestion des membres sélectionnés
  onCheckboxChange(event: Event) {
    const checkbox = event.target as HTMLInputElement;
    const selectedValues = this.tacheForm.get('autresAssignes')?.value || [];

    if (checkbox.checked) {
      selectedValues.push(checkbox.value);
    } else {
      const index = selectedValues.indexOf(checkbox.value);
      if (index >= 0) selectedValues.splice(index, 1);
    }

    this.tacheForm.get('autresAssignes')?.setValue(selectedValues);
  }

  // Vérifie uniquement les dates
  private validateDateFin(dateFin: string): string | null {
    if (!dateFin) return null;

    const dateFinTache = new Date(dateFin);
    const now = new Date();

    if (dateFinTache <= now) {
      return 'La date de fin doit être postérieure à la date actuelle.';
    }

    if (this.dateFinProjet && new Date(this.dateFinProjet) < dateFinTache) {
      return `La date de fin ne peut pas dépasser celle du projet (${this.dateFinProjet}).`;
    }

    return null;
  }

  onSubmit(): void {
    const { titre, description, priorite, etat, dateFin, autresAssignes } = this.tacheForm.value;

    // 🕓 Vérification uniquement des dates
    const errorDate = this.validateDateFin(dateFin);

    if (errorDate) {
      Swal.fire({
        icon: 'warning',
        title: 'Erreur de Date',
        text: errorDate,
        confirmButtonColor: '#f1c40f',
        confirmButtonText: 'OK'
      });
      return;
    }

    // --- Création de la tâche ---
    this.loading = true;

    const autres = (autresAssignes || []).map((matricule: string) => ({
      matricule,
      nom: '',
      prenom: '',
      image: ''
    }));

    const newTache: Tache = {
      titre,
      description,
      priorite,
      etat,
      dateFin,
      assigneeMatricule: null,
      autresAssignes: autres,
      projetId: this.projetId
    };

    this.tacheService.createTache(newTache).subscribe({
      next: () => {
        this.loading = false;
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: 'La tâche a été créée avec succès !',
          confirmButtonColor: '#3085d6',
          confirmButtonText: 'OK'
        }).then(() => {
          this.router.navigate(['/manager/listTaches', this.projetId]);
        });
      },
      error: (err) => {
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: 'Erreur serveur',
          text: 'Impossible de créer la tâche. Veuillez réessayer plus tard.',
          confirmButtonColor: '#d33',
          confirmButtonText: 'OK'
        });
        console.error('Erreur création tâche', err);
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