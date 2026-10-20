import { Component, OnInit } from '@angular/core';
import { NotificationService, Notification } from '../../services/notification.service';

@Component({
  selector: 'app-list-notification',
  templateUrl: './list-notification.component.html',
  styleUrl: './list-notification.component.css'
})
export class ListNotificationComponent implements OnInit {
  notifications: Notification[] = [];
  matricule = localStorage.getItem('matricule') || 'user1';

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.notificationService.loadNotifications(this.matricule);
    this.notificationService.connectWebSocket(this.matricule);

    this.notificationService.notifications$().subscribe(data => {
      this.notifications = data;
    });
  }

  markAsRead(id: number): void {
    this.notificationService.markAsRead(id).subscribe(() => {
      this.notifications = this.notifications.map(n => n.id === id ? { ...n, lue: true } : n);
    });
  }

  markAllAsRead(): void {
    this.notificationService.markAllAsRead(this.matricule).subscribe(() => {
      this.notifications = this.notifications.map(n => ({ ...n, lue: true }));
    });
  }

  deleteNotification(id: number): void {
    this.notificationService.deleteNotification(id).subscribe(() => {
      this.notifications = this.notifications.filter(n => n.id !== id);
    });
  }
}

