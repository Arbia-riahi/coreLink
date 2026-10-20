import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { CdkDragDrop, transferArrayItem } from '@angular/cdk/drag-drop';
import Swal from 'sweetalert2';
import { TacheService, Tache, Member } from '../../services/tache.service';
import { ProjectService, Project } from '../../services/projet.service';
import { CommentaireService, Commentaire } from '../../services/commentaire.service';

@Component({
  selector: 'app-list-tache',
  templateUrl: './list-tache.component.html',
  styleUrls: ['./list-tache.component.css']
})


export class ListTacheComponent implements OnInit {
  taches: Tache[] = [];
  filteredTaches: Tache[] = [];
  groupedTaches: { [etat: string]: Tache[] } = {};
  etats = ['A_FAIRE', 'EN_COURS', 'EN_REVISION', 'TERMINE', 'BLOQUE', 'ANNULE'];
  priorites = ['BASSE', 'MOYENNE', 'HAUTE', 'CRITIQUE'];
  commentaires: Commentaire[] = [];
  contenuCommentaire: string = '';

  projetId!: number;
  projetStatus: string | null = null;

  loading = true;
  viewMode: 'table' | 'kanban' = 'kanban';
  showArchived: boolean = false;

  // Filtres
  selectedEtat: string = '';
  selectedPriorite: string = '';
  selectedTacheId: number | null = null;
  suggestions: any[] = []; // Liste des membres à suggérer
   showSuggestions: boolean = false;
   cursorPosition: number = 0;
   mentionSuggestions: any[] = [];
showMentionSuggestions: boolean = false;
currentTacheId: number | null = null;
 highlightedTacheId: number | null = null;
 highlightedCommentaireId: number | null = null;
 private routeSub!: Subscription;


 
 
 constructor(
    private tacheService: TacheService,
    private projectService: ProjectService,
    private route: ActivatedRoute,
    private commentaireService: CommentaireService
  ) {}

  

 

 ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      this.projetId = +params['numeroProjet'];
      this.highlightedTacheId = +this.route.snapshot.queryParamMap.get('highlightedTacheId')!;
      this.highlightedCommentaireId = +this.route.snapshot.queryParamMap.get('highlightedCommentaireId')!;
      this.loadTaches();
      this.loadProjetStatus();
    });
  }

  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
  }
 

ngAfterViewInit(): void {
  setTimeout(() => {
    if (this.highlightedTacheId !== null) {
      const el = document.getElementById('tache-' + this.highlightedTacheId);
      if (el) {
        el.scrollIntoView({ behavior: 'smooth', block: 'center' });
      }
    }
  }, 500);
}

 /** Récupère toutes les tâches actives */
  loadTaches(): void {
    this.loading = true;
    this.tacheService.getTachesByProjet(this.projetId).subscribe({
      next: (data: Tache[]) => {
        this.taches = data;
        this.filteredTaches = [...this.taches];
       
        this.groupByEtat(this.filteredTaches);
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur chargement des tâches', err);
        this.loading = false;
      }
    });
  }




toggleCommentaires(tacheId: number) {
  this.selectedTacheId = this.selectedTacheId === tacheId ? null : tacheId;
}

  /** Récupère le statut du projet */
  loadProjetStatus(): void {
    this.projectService.getProjectByNumero(this.projetId).subscribe({
      next: (projet: Project) => (this.projetStatus = projet.status),
      error: (err) => console.error('Erreur chargement statut projet', err)
    });
  }

  /** Filtrage (état/priorité) */
applyFilters(): void {
  // TOUJOURS repartir de la source
  const base = this.taches;

  this.filteredTaches = base.filter(tache => {
    const matchEtat = this.selectedEtat ? tache.etat === this.selectedEtat : true;
    const matchPriorite = this.selectedPriorite ? tache.priorite === this.selectedPriorite : true;
    return matchEtat && matchPriorite;
  });

  this.groupByEtat(this.filteredTaches);
}


  /** Regroupe les tâches par état */
  groupByEtat(taches: Tache[] = this.filteredTaches): void {
    this.groupedTaches = {};
    this.etats.forEach((etat) => (this.groupedTaches[etat] = []));
    taches.forEach((t) => {
      if (this.groupedTaches[t.etat]) {
        this.groupedTaches[t.etat].push(t);
      }
    });
  }

  toggleView(): void {
    this.viewMode = this.viewMode === 'table' ? 'kanban' : 'table';
  }

  toggleArchivedView(): void {
    this.showArchived = !this.showArchived;
    if (this.showArchived) {
      this.tacheService.getArchivedTachesByProjet(this.projetId).subscribe({
        next: (data: Tache[]) => {
          this.taches = data;
          this.filteredTaches = [...this.taches];
    
          this.groupByEtat(this.filteredTaches);
        },
        error: (err) => console.error('Erreur chargement des tâches archivées', err)
      });
    } else {
      this.loadTaches();
    }
  }




  /** Drag and Drop */
  drop(event: CdkDragDrop<Tache[]>, nouvelEtat: string): void {
    if (event.previousContainer !== event.container) {
      const tache = event.previousContainer.data[event.previousIndex];
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );

      this.tacheService.updateEtatTache(tache.id!, nouvelEtat).subscribe({
        next: () => {
          tache.etat = nouvelEtat;
          this.loadProjetStatus(); // Mise à jour du statut projet
          
        },
        error: (err) => console.error('Erreur PATCH:', err)
      });
    }
  }

 
  getMembres(tache: Tache): Member[] {
  return Array.isArray(tache.autresAssignes) ? tache.autresAssignes : [];
}


  /** CSS dynamique */
  getBadgeEtat(etat: string): string {
    switch (etat) {
      case 'A_FAIRE': return 'badge bg-secondary';
      case 'EN_COURS': return 'badge bg-primary';
      case 'EN_REVISION': return 'badge bg-warning text-dark';
      case 'TERMINE': return 'badge bg-success';
      case 'BLOQUE': return 'badge bg-danger';
      case 'ANNULE': return 'badge bg-dark';
      default: return 'badge bg-light';
    }
  }

  getKanbanCardClass(etat: string): string {
    switch (etat) {
      case 'A_FAIRE': return 'kanban-card-afaire';
      case 'EN_COURS': return 'kanban-card-encours';
      case 'EN_REVISION': return 'kanban-card-enrevision';
      case 'TERMINE': return 'kanban-card-termine';
      case 'BLOQUE': return 'kanban-card-bloque';
      case 'ANNULE': return 'kanban-card-annule';
      default: return 'kanban-card-default';
    }
  }

  getPriorityIcon(priorite: string): string {
    switch (priorite) {
      case 'BASSE': return 'fas fa-flag text-secondary';
      case 'MOYENNE': return 'fas fa-flag text-primary';
      case 'HAUTE': return 'fas fa-flag text-warning';
      case 'CRITIQUE': return 'fas fa-flag text-danger';
      default: return 'fas fa-flag text-muted';
    }
  }

  getHeaderClass(etat: string): string {
    switch (etat) {
      case 'A_FAIRE': return 'header-afaire';
      case 'EN_COURS': return 'header-encours';
      case 'EN_REVISION': return 'header-enrevision';
      case 'TERMINE': return 'header-termine';
      case 'BLOQUE': return 'header-bloque';
      case 'ANNULE': return 'header-annule';
      default: return 'header-default';
    }
  }

  /** Badge pour statut projet */
  getProjetStatusClass(status: string): string {
    switch (status) {
      case 'EN_ATTENTE': return 'badge bg-secondary';
      case 'EN_COURS': return 'badge bg-primary';
      case 'TERMINE': return 'badge bg-success';
      case 'ANNULE': return 'badge bg-dark';
      default: return 'badge bg-light';
    }
  }









// Ouvre la modale des commentaires avec mentions
openCommentairesModal(tacheId: number) {
  this.currentTacheId = tacheId;

  this.commentaireService.getCommentairesByTache(tacheId).subscribe((data) => {
    this.commentaires = data;

   const commentsHtml = this.commentaires.map(c => {
  const isHighlighted = c.id === this.highlightedCommentaireId;
  return `
    <div class="comment-item ${isHighlighted ? 'highlighted-commentaire' : ''}" 
         id="commentaire-${c.id}" 
         style="margin-bottom:10px; padding:10px; background:#f9f9f9; border-radius:8px;">
      <div style="display:flex; align-items:center; margin-bottom:5px;">
        <img src="${c.auteurImage || 'assets/default-avatar.png'}" 
             style="width:40px; height:40px; border-radius:50%; object-fit:cover; margin-right:10px;">
        <div style="flex:1;">
          <b>${c.auteurNom} ${c.auteurPrenom}</b>
          <br><small style="color:#888;">${new Date(c.date || '').toLocaleString()}</small>
        </div>
      </div>
      <div style="margin-left:50px; color:#333;">${c.contenu}</div>
    </div>
  `;
}).join('');


    Swal.fire({
      title: '💬 Commentaires',
      html: `
        <div style="text-align:left; max-height:300px; overflow-y:auto; padding-right:10px;">${commentsHtml}</div>
        <textarea id="newComment" class="swal2-textarea" 
                  oninput="angularComponentRef.onCommentInput(event)"
                  style="margin-top:10px;" placeholder="Ajouter un commentaire..."></textarea>
        <div id="mentionSuggestions" 
             class="mention-suggestions" 
             style="display:none; background:white; border:1px solid #ccc; max-height:100px; overflow-y:auto; margin-top:5px; border-radius:4px;">
        </div>
      `,
      showCancelButton: true,
      confirmButtonText: 'Ajouter',
      cancelButtonText: 'Fermer',
      didRender: () => {
        (window as any).angularComponentRef = this; // Liaison Angular <-> JS
      },
      preConfirm: () => {
        const contenu = (document.getElementById('newComment') as HTMLTextAreaElement)?.value;
        if (!contenu || !contenu.trim()) {
          Swal.showValidationMessage('Le commentaire ne peut pas être vide.');
          return false;
        }
        return contenu;
      }
    }).then((result) => {
      if (result.isConfirmed && result.value) {
        this.addCommentaire(tacheId, result.value);
      }


      setTimeout(() => {
  if (this.highlightedCommentaireId !== null) {
    const el = document.getElementById('commentaire-' + this.highlightedCommentaireId);
    if (el) {
      el.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
  }
}, 300);

    });
  });
}

// Gestion de l'input pour @mention
onCommentInput(event: Event) {
  const input = event.target as HTMLTextAreaElement;
  const caretPosition = input.selectionStart || 0;
  const textBeforeCaret = input.value.substring(0, caretPosition);
  const lastAt = textBeforeCaret.lastIndexOf('@');

  if (lastAt !== -1) {
    this.showMentionSuggestions = true;
    if (this.currentTacheId) {
      this.mentionSuggestions = this.getMembresForTache(this.currentTacheId);
    }
  } else {
    this.showMentionSuggestions = false;
  }

  this.renderMentionSuggestions();
}

// Récupérer les membres de la tâche
getMembresForTache(tacheId: number): any[] {
  const tache = this.taches.find(t => t.id === tacheId);
  return tache && Array.isArray(tache.autresAssignes) ? tache.autresAssignes : [];
}

// Affichage des suggestions
renderMentionSuggestions() {
  const suggestionDiv = document.getElementById('mentionSuggestions');
  if (!suggestionDiv) return;

  if (this.showMentionSuggestions && this.mentionSuggestions.length > 0) {
    suggestionDiv.style.display = 'block';
    suggestionDiv.innerHTML = this.mentionSuggestions
      .map((m, index) => 
        `<div class="mention-item" onclick="angularComponentRef.insertMentionByIndex(${index})" 
             style="padding:5px; cursor:pointer; border-bottom:1px solid #eee;">
           @${m.nom} ${m.prenom}
         </div>`
      )
      .join('');
  } else {
    suggestionDiv.style.display = 'none';
  }
}

// Insérer une mention choisie
insertMentionByIndex(index: number) {
  const member = this.mentionSuggestions[index];
  this.insertMention(member);
}

insertMention(member: any) {
  const textarea = document.getElementById('newComment') as HTMLTextAreaElement;
  if (!textarea) return;

  const caretPosition = textarea.selectionStart || 0;
  const text = textarea.value;
  const lastAt = text.lastIndexOf('@', caretPosition - 1);

  const beforeAt = text.substring(0, lastAt + 1);
  const afterAt = text.substring(caretPosition);

  textarea.value = beforeAt + member.matricule + ' ' + afterAt;
  this.contenuCommentaire = textarea.value;
  this.showMentionSuggestions = false;
  this.renderMentionSuggestions();
}




  addCommentaire(tacheId: number, contenu: string) {
    const matriculeAuteur = localStorage.getItem('matricule') || 'user1';
    this.commentaireService.ajouterCommentaire(tacheId, matriculeAuteur, contenu).subscribe(() => {
      Swal.fire('Ajouté', 'Votre commentaire a été ajouté.', 'success');
    });
  }


/** Ouvre une modale avec les membres supplémentaires (au-delà de 3) */
openExtraMembersModal(tache: Tache): void {
  // Sécurité : vérifier que la liste existe et contient plus de 3 éléments
  if (!tache.autresAssignes || tache.autresAssignes.length <= 3) {
    return;
  }

  const extraMembers = tache.autresAssignes.slice(3); // membres 4+

  // Génération du contenu HTML
  const htmlContent = extraMembers.map(member => `
  <div style="display:flex; align-items:center; margin-bottom:10px;">
    <img src="${this.getMemberPhoto(member.image)}"
         onerror="this.src='../../assets/avatar.webp'"
         style="width:40px; height:40px; border-radius:50%; margin-right:10px;">
    <span>${member.nom} ${member.prenom}</span>
  </div>
`).join('');


  // Affichage avec SweetAlert
  Swal.fire({
    title: 'Membres supplémentaires',
    html: htmlContent,
    confirmButtonText: 'Fermer'
  });
}
getMemberPhoto(img: string | null | undefined): string {
  if (!img || img.trim() === '') {
    return '../../assets/avatar.webp';
  }

  // Si c'est une URL complète
  if (img.startsWith('http')) {
    return img;
  }

  // Sinon c'est un fichier local du backend
  return `http://localhost:8081/uploads/files/${img}`;
}

setDefaultMemberImage(event: any) {
  event.target.src = '../../assets/avatar.webp';
}


}







