import { Component, OnInit, OnDestroy } from '@angular/core';
import { UserService } from '../../services/user.service';
import { environment } from '../../../environments/environment.development';
import { NotificationService, Notification } from '../../services/notification.service';
import { ProjectService } from '../../services/projet.service';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})


export class DashbordComponent implements OnInit, OnDestroy {
  sidebarVisible: boolean = true;
  user: any = {};
  baseUrlImage = environment.baseUrl + 'uploads/files/';
  selectedImage: File | null = null;

  notifications: Notification[] = [];
  unreadCount = 0;
  matricule = localStorage.getItem('matricule') || 'user1';

  nbEnAttente: number = 0;
  nbEnCours: number = 0;
  nbTermines: number = 0;
  nbAnnules: number = 0;

  constructor(
    private userService: UserService,
    private notificationService: NotificationService,
    private projectService: ProjectService,
    private router: Router,
    private auth:AuthenticationService
  ) {}



 

  onLogout(event: Event): void {
    event.preventDefault();                    // empêcher la nav du <a> avant cleanup
    try { this.notificationService.disconnectWebSocket(); } catch {}
    this.auth.logOut();                        // ← vide le localStorage (voir plus bas)
    this.router.navigate(['/home/login']);     // redirige vers la page de login
    
  }
  ngOnInit(): void {
    this.loadUserProfile();
    this.loadProjectStats();
    this.notificationService.loadNotifications(this.matricule);
    this.notificationService.connectWebSocket(this.matricule);

    this.notificationService.notifications$().subscribe(data => {
      this.notifications = data;
      this.unreadCount = this.notifications.filter(n => !n.lue).length;
    });
  }

  ngOnDestroy(): void {
    this.notificationService.disconnectWebSocket();
  }

  toggleSidebar() {
    this.sidebarVisible = !this.sidebarVisible;
    document.body.classList.toggle('sidebar-toggled', !this.sidebarVisible);
  }

  loadUserProfile() {
    this.userService.getMyProfile().subscribe(data => {
      this.user = data;
    });
  }

  loadProjectStats() {
    this.projectService.getGlobalProjectStats().subscribe(stats => {
      this.nbEnAttente = stats.nbEnAttente || 0;
      this.nbEnCours = stats.nbEnCours || 0;
      this.nbTermines = stats.nbTermines || 0;
      this.nbAnnules = stats.nbAnnules || 0;
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

  getIconClass(type: string): string {
    switch (type) {
      case 'TACHE_ASSIGNATION': return 'icon-circle bg-primary';
      case 'NOUVEAU_COMMENTAIRE': return 'icon-circle bg-success';
      case 'AUTRE': return 'icon-circle bg-warning';
      default: return 'icon-circle bg-secondary';
    }
  }

  getIcon(type: string): string {
    switch (type) {
      case 'TACHE_ASSIGNATION': return 'fas fa-tasks text-white';
      case 'NOUVEAU_COMMENTAIRE': return 'fas fa-comment text-white';
      case 'AUTRE': return 'fas fa-info-circle text-white';
      default: return 'fas fa-bell text-white';
    }
  }
}

















