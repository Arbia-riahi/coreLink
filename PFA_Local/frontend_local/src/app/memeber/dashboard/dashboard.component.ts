 import { Component, OnInit, OnDestroy } from '@angular/core';
  import { UserService } from '../../services/user.service';
  import { environment } from '../../../environments/environment.development';
  import { NotificationService, Notification } from '../../services/notification.service';
  import { ProjectService } from '../../services/projet.service';
  import { Router } from '@angular/router';
  import { Tache, TacheService } from '../../services/tache.service';
  import { ViewChild, ElementRef } from '@angular/core';
  import { AuthenticationService } from '../../services/authentication.service';


  interface NotificationItem {
  // عرّف الخصائص حسب حالتك
  date: string | Date;
  type: string;
  message: string;
  lue?: boolean;
}
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
  matricule = localStorage.getItem('matricule') ||'user1';
  


  nbEnAttente: number = 0;
  nbEnCours: number = 0;
  nbTermines: number = 0;
  nbAnnules: number = 0;
 @ViewChild('notifScroll') notifScroll!: ElementRef;
  showAll = false;
notification: any;

// ✅ Variables KPI
  tachesAFaire = 0;
  tachesEnCours = 0;
  tachesBloque = 0;
  tachesTermine = 0;
 
constructor(
    private userService: UserService,
    private notificationService: NotificationService,
    private projectService: ProjectService,
    private router:Router,
    private tacheService:TacheService,
    private auth:AuthenticationService
  ) {}


  
  
  
     
  
  
     onLogout(event: Event): void {
      event.preventDefault();                    // empêcher la nav du <a> avant cleanup
      try { this.notificationService.disconnectWebSocket(); } catch {}
      this.auth.logOut();                        // ← vide le localStorage (voir plus bas)
      this.router.navigate(['/home/login']);     // redirige vers la page de login
      
    }
  ngOnInit(): void {
     this.loadTaskKpis();
    this.loadUserProfile();
    this.loadProjectStats();
    this.notificationService.loadNotifications(this.matricule);
    this.notificationService.connectWebSocket(this.matricule);

    this.notificationService.notifications$().subscribe(data => {
      this.notifications = data;
      this.unreadCount = this.notifications.filter(n => !n.lue).length;
    });
  }

   
  get totalCount(): number {
    return this.notifications?.length ?? 0;
  }


  get displayedNotifications(): NotificationItem[] {
    const list = this.notifications ?? [];
    return this.showAll ? list : list.slice(0, 3);
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
    this.refreshUnreadCount(); // ✅ Ajout ici
  });
}


  markAllAsRead(): void {
  this.notificationService.markAllAsRead(this.matricule).subscribe(() => {
    this.notifications = this.notifications.map(n => ({ ...n, lue: true }));
    this.refreshUnreadCount(); // ✅ Ajout ici
  });
}


 deleteNotification(id: number): void {
  this.notificationService.deleteNotification(id).subscribe(() => {
    this.notifications = this.notifications.filter(n => n.id !== id);
    this.refreshUnreadCount(); // ✅ Ajout ici
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
goToNotification(notification: Notification): void {
  this.markAsRead(notification.id);
  const role = 'memeber';
  console.log('🔔 Notification reçue :', notification);

  

  // ✅ Cas 1 : Notification liée à une tâche (sans/avec commentaire)
  if (notification.tacheId) {
    this.tacheService.getTacheById(notification.tacheId).subscribe(
      tache => {
        const numeroProjet = tache.projetId;
        this.router.navigate([`/${role}/listTaches`, numeroProjet], {
          queryParams: { highlightedTacheId: notification.tacheId }
        });
      },
      error => {
        console.error('❌ Erreur lors de la récupération de la tâche', error);
      }
    );
    return;
  }

  // ✅ Cas 2 : Notification liée uniquement à un projet
  if (notification.projetId) {
    this.projectService.getNumeroProjetById(notification.projetId).subscribe(
      numeroProjet => {
        this.router.navigate([`/${role}/ViewProjet`, numeroProjet]);
      },
      error => {
        console.error('❌ Erreur récupération projet :', error);
      }
    );
    return;
  }

  // 🔴 Aucun cas correspondant
  console.warn('🔔 Notification sans lien exploitable :', notification);
}


refreshUnreadCount(): void {
  this.notificationService.getUnreadCount(this.matricule).subscribe(
    count => this.unreadCount = count,
    err => console.error('❌ Erreur refresh unreadCount', err)
  );
}



showAllAndScroll(): void {
    this.showAll = true;
    // خليه يستنّى DOM يتحدّث
    setTimeout(() => {
      const el: HTMLElement | undefined = this.notifScroll?.nativeElement;
      if (!el) { return; }
      el.scrollTo({ top: el.scrollHeight, behavior: 'smooth' });
    });
  }



  onVoirToutes(event: Event) {
  event.preventDefault();
  event.stopPropagation();
  this.showAllAndScroll(); 
}

// ✅ Calcule les KPI en fonction des états
  private computeTaskKpis(taches: Tache[]) {
    const norm = (s?: string) => (s || '').toUpperCase();

    this.tachesAFaire = 0;
    this.tachesEnCours = 0;
    this.tachesBloque = 0;
    this.tachesTermine = 0;

    for (const t of taches || []) {
      switch (norm(t.etat)) {
        case 'A_FAIRE':   this.tachesAFaire++; break;
        case 'EN_COURS':  this.tachesEnCours++; break;
        case 'BLOQUE':    this.tachesBloque++; break;
        case 'TERMINE':   this.tachesTermine++; break;
      }
    }
  }

  // ✅ Récupère les tâches depuis le backend et calcule les KPI
  loadTaskKpis(): void {
    this.tacheService.getAllTachesRespectingRole().subscribe({
      next: (taches) => this.computeTaskKpis(taches),
      error: (e) => console.error('Erreur chargement KPI tâches', e)
    });
  }

}















