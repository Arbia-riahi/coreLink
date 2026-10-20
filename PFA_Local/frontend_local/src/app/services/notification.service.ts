import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export interface Notification {
  id: number;
  message: string;
  type: string;
  lue: boolean;
  date: string;
  projetId?: number;
  tacheId?: number;
  commentaireId?: number; // ✅ à ajouter
  projetLie?: any;
  tacheLiee?: any;
}






@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private apiUrl = 'http://localhost:8080/api/notifications';
  private stompClient: Client | null = null;
  private notificationsSubject = new BehaviorSubject<Notification[]>([]);

  constructor(private http: HttpClient) {}

  /** Charger toutes les notifications */
  getNotifications(matricule: string): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.apiUrl}/${matricule}`);
  }

  /** Charger uniquement les notifications non lues */
  getUnreadNotifications(matricule: string): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.apiUrl}/${matricule}/unread`);
  }

  /** Marquer une notification comme lue */
  markAsRead(id: number): Observable<Notification> {
    return this.http.patch<Notification>(`${this.apiUrl}/${id}/read`, {});
  }

  getUnreadCount(matricule: string): Observable<number> {
  return this.http.get<number>(`${this.apiUrl}/${matricule}/unreadCount`);
}


  /** Marquer toutes comme lues */
  markAllAsRead(matricule: string): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${matricule}/read-all`, {});
  }

  /** Supprimer une notification */
  deleteNotification(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /** Connexion WebSocket pour notifications en temps réel */
connectWebSocket(matricule: string): void {
  this.stompClient = new Client({
    webSocketFactory: () => new SockJS('http://localhost:8080/chat-websocket'),
    reconnectDelay: 5000,
    connectHeaders: {
      username: matricule  // <-- AJOUT ICI
    }
  });

  this.stompClient.onConnect = () => {
    console.log(`✅ WebSocket Notifications connecté pour ${matricule}`);
    
    this.stompClient?.subscribe(`/user/queue/notifications`, (message: IMessage) => {
      const notif: Notification = JSON.parse(message.body);
      console.log('📩 Notification reçue via WS:', notif);
      this.addNotification(notif);

      // ✅ Invoquer manuellement la mise à jour du compteur
      this.getUnreadCount(matricule).subscribe(count => {
        // Mettre à jour le BehaviorSubject en recréant un tableau mis à jour
        const updated = [notif, ...this.notificationsSubject.getValue()];
        this.notificationsSubject.next(updated);
        // Tu peux aussi propager le count via un autre BehaviorSubject si tu veux
      });
    });
  };

  this.stompClient.activate();
}

 




  /** Déconnexion WebSocket */
  disconnectWebSocket(): void {
    this.stompClient?.deactivate();
  }

  /** Ajouter la notification reçue */
  private addNotification(notif: Notification): void {
    const current = this.notificationsSubject.getValue();
    this.notificationsSubject.next([notif, ...current]);
  }

  /** Observable des notifications */
  notifications$(): Observable<Notification[]> {
    return this.notificationsSubject.asObservable();
  }

  /** Charger initialement les notifications */
  loadNotifications(matricule: string): void {
    this.getNotifications(matricule).subscribe((data) => {
      this.notificationsSubject.next(data);
    });
  }
}
