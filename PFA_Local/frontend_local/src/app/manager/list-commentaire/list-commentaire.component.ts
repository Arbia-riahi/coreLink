import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { CommentaireService, Commentaire } from '../../services/commentaire.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-list-commentaire',
  templateUrl: './list-commentaire.component.html',
  styleUrls: ['./list-commentaire.component.css']
})
export class ListCommentaireComponent implements OnInit, OnDestroy {
  @Input() tacheId!: number;
  commentaires: Commentaire[] = [];
  private commentaireSub?: Subscription;

  editingCommentId: number | null = null;
  editContent: string = '';
  currentUserName = localStorage.getItem('nom') || ''; // Suppose qu'on stocke le nom de l'utilisateur

  constructor(private commentaireService: CommentaireService) {}

  ngOnInit(): void {
    this.commentaireService.loadCommentaires(this.tacheId);
    this.commentaireService.connectWebSocket(this.tacheId);

    this.commentaireSub = this.commentaireService.commentaires$().subscribe(data => {
      this.commentaires = data;
    });
  }

  ngOnDestroy(): void {
    this.commentaireService.disconnectWebSocket();
    this.commentaireSub?.unsubscribe();
  }

  startEdit(comment: Commentaire) {
    this.editingCommentId = comment.id!;
    this.editContent = comment.contenu;
  }

  cancelEdit() {
    this.editingCommentId = null;
    this.editContent = '';
  }

  saveEdit(commentId: number) {
    if (!this.editContent.trim()) return;

    this.commentaireService.modifierCommentaire(commentId, this.editContent).subscribe(updatedComment => {
      const index = this.commentaires.findIndex(c => c.id === commentId);
      if (index !== -1) {
        this.commentaires[index] = updatedComment;
      }
      this.cancelEdit();
    });
  }
}
