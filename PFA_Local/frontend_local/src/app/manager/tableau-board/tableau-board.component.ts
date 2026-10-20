// tableau-board.component.ts (extraits corrigés)
import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/projet.service';
import { UserService } from '../../services/user.service';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import 'chartjs-adapter-date-fns';

Chart.register(...registerables);

@Component({
  selector: 'app-tableau-board',
  templateUrl: './tableau-board.component.html',
  styleUrls: ['./tableau-board.component.css']
})
export class TableauBoardComponent implements OnInit {
  upcomingDeadlines: any[] = [];

  private comparisonChart?: Chart;
  private chefStatusChart?: Chart;
  private roleChart?: Chart;

  isChef = false;
  isAdmin = false;
  private matriculeChef: string | null = null;

  constructor(private projectService: ProjectService, private userService: UserService) {}

  ngOnInit(): void {
  // 1) Récup infos de session (sans toucher AuthService)
  this.matriculeChef = localStorage.getItem('matricule');

  let roles: string[] = [];
  const rolesRaw = localStorage.getItem('roles');
  try {
    roles = rolesRaw ? JSON.parse(rolesRaw) : [];
  } catch {
    roles = [];
  }

  // 2) Détection des rôles (variantes possibles)
  const rolesUpper = roles.map(r => String(r).toUpperCase());
  this.isAdmin = rolesUpper.some(r => ['ADMIN'].includes(r));
  this.isChef  = rolesUpper.some(r => ['MANAGER'].includes(r));

  // 3) Comparaison & Deadlines
  // - Chef connecté => endpoints filtrés par chef
  // - Sinon => endpoints globaux
  if (this.matriculeChef && this.isChef) {
    this.loadComparisonChartForChef(this.matriculeChef);
    this.loadUpcomingDeadlinesForChef(this.matriculeChef);
  } else {
    this.loadComparisonChartGlobal();
    this.loadUpcomingDeadlinesGlobal();
  }

  // 4) Autres widgets (inchangés)
  this.loadRoleDistribution();

  // Répartition des Projets par Chef & État : toujours global (pas de condition)
  this.loadProjectsByChefAndStatus();
}


  /** ==================== COMPARAISON ==================== */

  // GLOBAL (admins uniquement)
  private loadComparisonChartGlobal() {
    this.projectService.getProjectsComparison().subscribe((data: any[]) => {
      this.drawComparisonChart(data, false);
    });
  }

  // FILTRÉ CHEF
  private loadComparisonChartForChef(matricule: string) {
    this.projectService.getComparaisonForChef(matricule).subscribe((data: any[]) => {
      this.drawComparisonChart(data, true);
    });
  }

  private drawComparisonChart(data: any[], isMine: boolean) {
    const labels = data.map(item => item.projet);
    const progressValues = data.map(item => item.progression);

    if (this.comparisonChart) this.comparisonChart.destroy();

    const chartConfig: ChartConfiguration = {
      type: 'bar',
      data: {
        labels,
        datasets: [{
          label: 'Progression (%)',
          data: progressValues,
          backgroundColor: 'rgba(75, 192, 192, 0.6)',
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: isMine ? 'Progression de MES projets' : 'Progression des projets'
          },
          tooltip: { mode: 'index', intersect: false }
        },
        scales: {
          y: { beginAtZero: true, max: 100, title: { display: true, text: 'Pourcentage (%)' } }
        }
      }
    };

    const ctx = document.getElementById('projectsChart') as HTMLCanvasElement;
    this.comparisonChart = new Chart(ctx, chartConfig);
  }

  /** ==================== DEADLINES ==================== */

  // GLOBAL (admins uniquement)
  private loadUpcomingDeadlinesGlobal() {
    this.projectService.getUpcomingDeadlines().subscribe(data => {
      this.upcomingDeadlines = Array.isArray(data) ? data : [];
    });
  }

  // FILTRÉ CHEF
  private loadUpcomingDeadlinesForChef(matricule: string) {
    this.projectService.getUpcomingDeadlinesForChef(matricule, 7)
      .subscribe(data => {
        this.upcomingDeadlines = Array.isArray(data) ? data : [];
      });
  }

  getCardColor(jours: number): string {
    if (jours === 0) return 'bg-danger-card';
    if (jours <= 3) return 'bg-warning-card';
    return 'bg-success-card';
  }

  /** ==================== AUTRES (inchangés) ==================== */

  loadRoleDistribution() {
    this.userService.getRoleDistribution().subscribe((data) => {
      const labels = Object.keys(data);
      const values = Object.values(data).map(val => Number(val));

      if (this.roleChart) this.roleChart.destroy();

      const chartConfig: ChartConfiguration = {
        type: 'doughnut',
        data: {
          labels,
          datasets: [{
            data: values,
            backgroundColor: ['rgba(54, 162, 235, 0.6)', 'rgba(255, 206, 86, 0.6)'],
            borderColor: ['rgba(54, 162, 235, 1)', 'rgba(255, 206, 86, 1)'],
            borderWidth: 1
          }]
        },
        options: { responsive: true, plugins: { legend: { position: 'bottom' } } }
      };

      const ctx = document.getElementById('roleDistributionChart') as HTMLCanvasElement;
      this.roleChart = new Chart(ctx, chartConfig);
    });
  }

  

  getStatusColor(status: string): string {
    switch (status) {
      case 'EN_ATTENTE': return 'rgba(255, 223, 128, 0.6)';
      case 'EN_COURS':   return 'rgba(135, 206, 250, 0.6)';
      case 'TERMINE':    return 'rgba(144, 238, 144, 0.6)';
      case 'ANNULE':     return 'rgba(255, 182, 193, 0.6)';
      default:           return 'rgba(211, 211, 211, 0.6)';
    }
  }



   /** Projets par Chef de projet + status */
  loadProjectsByChefAndStatus() {
    this.projectService.getProjectsByChefAndStatus().subscribe((data) => {
      const chefs = Object.keys(data);

      const allStatuses = new Set<string>();
      chefs.forEach(chef => {
        Object.keys(data[chef]).forEach(status => allStatuses.add(status));
      });

      const datasets = Array.from(allStatuses).map(status => ({
        label: status,
        data: chefs.map(chef => data[chef][status] || 0),
        backgroundColor: this.getStatusColor(status),
        borderWidth: 1
      }));

      if (this.chefStatusChart) this.chefStatusChart.destroy();

      const chartConfig: ChartConfiguration = {
        type: 'bar',
        data: {
          labels: chefs,
          datasets: datasets
        },
        options: {
          responsive: true,
          plugins: {
            title: {
              display: true,
              text: 'Répartition des projets par Chef (par état)'
            },
            tooltip: { mode: 'index', intersect: false },
            legend: { position: 'bottom' }
          },
          scales: {
            x: { stacked: true },
            y: { stacked: true, beginAtZero: true }
          }
        }
      };

      const ctx = document.getElementById('projectsByChefChart') as HTMLCanvasElement;
      this.chefStatusChart = new Chart(ctx, chartConfig);
    });
  }

}
