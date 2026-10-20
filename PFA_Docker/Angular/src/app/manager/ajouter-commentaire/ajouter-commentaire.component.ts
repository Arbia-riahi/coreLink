import { Component, Input } from '@angular/core';
import { CommentaireService } from '../../services/commentaire.service';

@Component({
  selector: 'app-ajouter-commentaire',
  templateUrl: './ajouter-commentaire.component.html',
  styleUrls: ['./ajouter-commentaire.component.css']
})
export class AjouterCommentaireComponent {
  @Input() tacheId!: number;
  contenu: string = '';
  matriculeAuteur = localStorage.getItem('matricule') || 'user1';

  constructor(private commentaireService: CommentaireService) {}

  ajouter() {
    if (!this.contenu.trim()) return;

    this.commentaireService
      .ajouterCommentaire(this.tacheId, this.matriculeAuteur, this.contenu)
      .subscribe(() => {
        this.contenu = '';
        this.commentaireService.refreshCommentaires(this.tacheId);
      });
  }
}
