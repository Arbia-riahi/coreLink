import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export interface Commentaire {
  id?: number;
  contenu: string;
  date?: string;
  auteurNom?: string;
  auteurPrenom?: string;
  auteurImage?: string; // <-- ajout
  tache?: any;
  parentComment?: Commentaire | null;
  mentions?: any[];
  modifie?: boolean;
  supprime?: boolean;
}




@Injectable({
  providedIn: 'root'
})
export class CommentaireService {

  private apiUrl = 'http://localhost:8080/api/commentaires';
  private stompClient: Client | null = null;
  private commentairesSubject = new BehaviorSubject<Commentaire[]>([]);

  constructor(private http: HttpClient) {}

  // ===========================
  // CRUD via HTTP
  // ===========================
  getCommentairesByTache(tacheId: number): Observable<Commentaire[]> {
    return this.http.get<Commentaire[]>(`${this.apiUrl}/tache/${tacheId}`);
  }

  ajouterCommentaire(tacheId: number, matriculeAuteur: string, contenu: string, parentCommentId?: number): Observable<Commentaire> {
    const payload: any = { matriculeAuteur, contenu };
    if (parentCommentId !== undefined && parentCommentId !== null) {
      payload.parentCommentId = parentCommentId;
    }
    return this.http.post<Commentaire>(`${this.apiUrl}/tache/${tacheId}/add`, payload);
  }

 modifierCommentaire(commentaireId: number, contenu: string): Observable<Commentaire> {
  return this.http.put<Commentaire>(`${this.apiUrl}/${commentaireId}`, { contenu });
}

  supprimerCommentaire(commentaireId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${commentaireId}`);
  }

  // ===========================
  // WebSocket pour temps réel
  // ===========================
  connectWebSocket(tacheId: number): void {
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/chat-websocket'),
      reconnectDelay: 5000
    });

    this.stompClient.onConnect = () => {
      console.log(`WebSocket connecté pour tacheId=${tacheId}.`);
      this.stompClient?.subscribe(`/topic/commentaires/${tacheId}`, (message: IMessage) => {
        const newComment: Commentaire = JSON.parse(message.body);
        this.addCommentaire(newComment);
      });
    };

    this.stompClient.activate();
  }

  disconnectWebSocket(): void {
    this.stompClient?.deactivate();
  }

  private addCommentaire(comment: Commentaire): void {
    const current = this.commentairesSubject.getValue();
    if (!current.some(c => c.id === comment.id)) {
      this.commentairesSubject.next([...current, comment]);
    }
  }

 


  commentaires$(): Observable<Commentaire[]> {
    return this.commentairesSubject.asObservable();
  }

  loadCommentaires(tacheId: number): void {
    this.getCommentairesByTache(tacheId).subscribe((data) => {
      this.commentairesSubject.next(data);
    });
  }

  refreshCommentaires(tacheId: number): void {
    this.loadCommentaires(tacheId);
  }
}
