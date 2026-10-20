import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment.development';

// Interface pour les réactions
export interface ReactionPayload {
  emoji: string;
  user: {
    matricule: string;
    nom?: string;
    prenom?: string;
    image?: string;
  };
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private stompClient: Client | null = null;
  private connected = false;

  private messageSubject = new BehaviorSubject<any | null>(null);
  private reactionSubject = new BehaviorSubject<any | null>(null);
  private onlineUsersSubject = new BehaviorSubject<any[]>([]);
  private connectionStatus = new BehaviorSubject<boolean>(false);
  private seenUpdatesSubject = new BehaviorSubject<any | null>(null);


  private username: string =
    localStorage.getItem('matricule') || 'User_' + Math.floor(Math.random() * 10000);

  constructor(private http: HttpClient) {}

  /** Connexion WebSocket */
 connect() {
    if (this.connected) return;

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/chat-websocket'),
      connectHeaders: { username: this.username },
      reconnectDelay: 5000,
      debug: (msg) => console.log('STOMP Debug:', msg),
    });

    this.stompClient.onConnect = () => {
      console.log('✅ Connecté au WebSocket');
      this.connected = true;
      this.connectionStatus.next(true);

      /** Abonnements WebSocket */
      this.stompClient?.subscribe('/topic/messages', (message: IMessage) => {
        const data = JSON.parse(message.body);
        this.messageSubject.next(data);
      });

      this.stompClient?.subscribe('/topic/reactions', (message: IMessage) => {
        const reaction = JSON.parse(message.body);
        console.log('Réaction reçue via WS:', reaction);
        this.reactionSubject.next(reaction);
      });

      this.stompClient?.subscribe('/topic/message-seen', (message: IMessage) => {
        const seenUpdate = JSON.parse(message.body);
        this.seenUpdatesSubject.next(seenUpdate);
      });

      this.stompClient?.subscribe('/topic/online-users', (message: IMessage) => {
        const users = JSON.parse(message.body);
        this.onlineUsersSubject.next(users);
      });

      this.notifyUserConnected();
    };

    this.stompClient.onStompError = () => this.handleDisconnect();
    this.stompClient.onWebSocketClose = () => this.handleDisconnect();

    this.stompClient.activate();
  }

  private notifyUserConnected() {
    if (this.stompClient && this.connected) {
      this.stompClient.publish({
        destination: '/app/userConnected',
        body: this.username
      });
    }
  }

  private handleDisconnect() {
    this.connected = false;
    this.connectionStatus.next(false);
  }

 /** Envoi d’un message */
  sendMessage(msg: any) {
    if (this.stompClient && this.connected) {
      this.stompClient.publish({
        destination: '/app/sendMessage',
        body: JSON.stringify(msg),
      });
    } else {
      console.warn('⚠️ Impossible d’envoyer : WebSocket non connecté.');
    }
  }

  /** Envoi d'une réaction (toggle) */
  sendReactionWebSocket(reactionPayload: ReactionPayload) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: '/app/reaction',
        body: JSON.stringify(reactionPayload)
      });
    } else {
      console.warn('⚠️ WebSocket non connecté pour réaction');
    }
  }



  /** Marquer un message comme lu */
  markMessageAsRead(messageId: number, readerMatricule: string): Observable<any> {
    const obs = this.http.put(
      `${environment.baseUrl}api/messages/${messageId}/read?readerMatricule=${readerMatricule}`, {}
    );
    
    obs.subscribe(() => {
      if (this.stompClient && this.connected) {
        this.stompClient.publish({
          destination: '/app/messageSeen',
          body: JSON.stringify({ messageId, readerMatricule })
        });
      }
    });

    return obs;
  }

  /** Récupération historique */
  getMessageHistory(): Observable<any[]> {
    return this.http.get<any[]>(`${environment.baseUrl}api/messages/history`);
  }

/** Observer les réactions */
getReactions(): Observable<any> {
  return this.reactionSubject.asObservable();
}




  /** Déconnexion */
  disconnect() {
    if (this.stompClient) {
      this.stompClient.publish({
        destination: '/app/userDisconnected',
        body: this.username
      });
      this.stompClient.deactivate();
      this.handleDisconnect();
    }
  }



  saveReactionREST(messageId: number, payload: ReactionPayload): Observable<any> {
  return this.http.post(`${environment.baseUrl}api/messages/${messageId}/reactions`, payload);
}



  /** Upload fichier */
  uploadFile(formData: FormData): Observable<string> {
    return this.http.post<{ fileUrl: string }>(
      `${environment.baseUrl}upload`, formData
    ).pipe(map((response) => response.fileUrl));
  }
  /** Observables */
  getMessages(): Observable<any> {
    return this.messageSubject.asObservable();
  }

 

  getOnlineUsers(): Observable<any[]> {
    return this.onlineUsersSubject.asObservable();
  }

  getConnectionStatus(): Observable<boolean> {
    return this.connectionStatus.asObservable();
  }

 


 /** Upload audio */
  uploadAudio(formData: FormData): Observable<string> {
    return this.http.post<{ fileUrl: string }>(
      `${environment.baseUrl}upload/audio`, formData
    ).pipe(map((response) => response.fileUrl));
  }

  /** Récupérer la liste des utilisateurs qui ont vu un message */
  getSeenBy(messageId: number): Observable<any[]> {
    return this.http.get<any[]>(`${environment.baseUrl}api/messages/${messageId}/seen-by`);
  }

  /** Compteur de messages non lus */
  getUnreadCount(matricule: string): Observable<number> {
    return this.http.get<number>(`${environment.baseUrl}api/messages/unread-count?matricule=${matricule}`);
  }

  /** Déconnexion */
  

  getSeenUpdates(): Observable<any> {
    return this.seenUpdatesSubject.asObservable();
  }



}


 
  

 

  