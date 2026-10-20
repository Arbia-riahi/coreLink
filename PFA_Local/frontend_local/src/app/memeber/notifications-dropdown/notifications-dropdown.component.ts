import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { NotificationService, Notification } from '../../services/notification.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-notifications-dropdown',
  templateUrl: './notifications-dropdown.component.html',
  styleUrl: './notifications-dropdown.component.css'
})
export class NotificationsDropdownComponent implements OnInit, OnDestroy {

  @Input() matricule: string = ''; // Matricule utilisateur
  notifications: Notification[] = [];
  unreadCount = 0;

  private notifSub?: Subscription;

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    // Charger les notifications au démarrage
    this.notificationService.loadNotifications(this.matricule);
    this.notificationService.connectWebSocket(this.matricule);

    // S'abonner aux notifications
    this.notifSub = this.notificationService.notifications$()
      .subscribe((data) => {
        this.notifications = data;
        this.unreadCount = this.notifications.filter(n => !n.lue).length;
      });
  }

  ngOnDestroy(): void {
    this.notifSub?.unsubscribe();
    this.notificationService.disconnectWebSocket();
  }

  markAsRead(id: number): void {
    this.notificationService.markAsRead(id).subscribe(() => {
      this.notifications = this.notifications.map(n => n.id === id ? { ...n, lue: true } : n);
      this.unreadCount = this.notifications.filter(n => !n.lue).length;
    });
  }

  markAllAsRead(): void {
    this.notificationService.markAllAsRead(this.matricule).subscribe(() => {
      this.notifications = this.notifications.map(n => ({ ...n, lue: true }));
      this.unreadCount = 0;
    });
  }

  deleteNotification(id: number): void {
    this.notificationService.deleteNotification(id).subscribe(() => {
      this.notifications = this.notifications.filter(n => n.id !== id);
      this.unreadCount = this.notifications.filter(n => !n.lue).length;
    });
  }

  getIconClass(type: string): string {
    switch (type) {
      case 'TACHE_ASSIGNATION': return 'icon-circle bg-primary';
      case 'NOUVEAU_COMMENTAIRE': return 'icon-circle bg-success';
      case 'REPONSE_COMMENTAIRE': return 'icon-circle bg-info';
      case 'MENTION_DANS_COMMENTAIRE': return 'icon-circle bg-warning';
      case 'AFFECTATION_MEMBRE_PROJET': return 'icon-circle bg-purple';
      case 'PROJET_DEADLINE_PROCHE': return 'icon-circle bg-danger';
      case 'PROJET_DEADLINE_DEPASSEE': return 'icon-circle bg-dark';
      default: return 'icon-circle bg-secondary';
    }
  }

  getIcon(type: string): string {
    switch (type) {
      case 'TACHE_ASSIGNATION': return 'fas fa-tasks text-white';
      case 'NOUVEAU_COMMENTAIRE': return 'fas fa-comment text-white';
      case 'REPONSE_COMMENTAIRE': return 'fas fa-reply text-white';
      case 'MENTION_DANS_COMMENTAIRE': return 'fas fa-at text-white';
      case 'AFFECTATION_MEMBRE_PROJET': return 'fas fa-users text-white';
      case 'PROJET_DEADLINE_PROCHE': return 'fas fa-exclamation-triangle text-white';
      case 'PROJET_DEADLINE_DEPASSEE': return 'fas fa-calendar-times text-white';
      default: return 'fas fa-bell text-white';
    }
  }
}

