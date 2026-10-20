import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/projet.service';
import { UserService } from '../../services/user.service';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import 'chartjs-adapter-date-fns';

Chart.register(...registerables);

@Component({
  selector: 'app-tableau-board',
  templateUrl: './tableau-board.component.html',
  styleUrl: './tableau-board.component.css'
})
export class TableauBoardComponent implements OnInit {
  upcomingDeadlines: any[] = [];
  
  private comparisonChart?: Chart;
  private chefStatusChart?: Chart;
  private roleChart?: Chart;

  constructor(private projectService: ProjectService, private userService: UserService) {}

  ngOnInit(): void {
    this.loadComparisonChart();
    this.loadUpcomingDeadlines();
    this.loadRoleDistribution();
    this.loadProjectsByChefAndStatus();
   
  }


 /** Comparaison de progression des projets */
  loadComparisonChart() {
    this.projectService.getProjectsComparison().subscribe((data: any[]) => {
      const labels = data.map(item => item.projet);
      const progressValues = data.map(item => item.progression);

      if (this.comparisonChart) this.comparisonChart.destroy();

      const chartConfig: ChartConfiguration = {
        type: 'bar',
        data: {
          labels: labels,
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
              text: 'Progression des projets'
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              max: 100,
              title: { display: true, text: 'Pourcentage (%)' }
            }
          }
        }
      };

      const ctx = document.getElementById('projectsChart') as HTMLCanvasElement;
      this.comparisonChart = new Chart(ctx, chartConfig);
    });
  }

  /** Projets proches de la deadline */
  loadUpcomingDeadlines() {
    this.projectService.getUpcomingDeadlines().subscribe(data => this.upcomingDeadlines = data);
  }

 getCardColor(jours: number): string {
  if (jours === 0) return 'bg-danger-card';      // Urgent
  if (jours <= 3) return 'bg-warning-card';      // Attention
  return 'bg-success-card';                      // OK
}


  /** Répartition des rôles (Donut Chart) */
loadRoleDistribution() {
    this.userService.getRoleDistribution().subscribe((data) => {
      const labels = Object.keys(data);
      const values = Object.values(data).map(val => Number(val));

      if (this.roleChart) this.roleChart.destroy();

      const chartConfig: ChartConfiguration = {
        type: 'doughnut',
        data: {
          labels: labels,
          datasets: [{
            data: values,
            backgroundColor: ['rgba(54, 162, 235, 0.6)', 'rgba(255, 206, 86, 0.6)'],
            borderColor: ['rgba(54, 162, 235, 1)', 'rgba(255, 206, 86, 1)'],
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          plugins: { legend: { position: 'bottom' } }
        }
      };

      const ctx = document.getElementById('roleDistributionChart') as HTMLCanvasElement;
      this.roleChart = new Chart(ctx, chartConfig);
    });
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

 getStatusColor(status: string): string {
  switch (status) {
    case 'EN_ATTENTE': return 'rgba(255, 223, 128, 0.6)';    // Jaune pastel doux
    case 'EN_COURS':   return 'rgba(135, 206, 250, 0.6)';    // Bleu ciel pastel
    case 'TERMINE':    return 'rgba(144, 238, 144, 0.6)';    // Vert pastel
    case 'ANNULE':     return 'rgba(255, 182, 193, 0.6)';    // Rose pastel
    default:           return 'rgba(211, 211, 211, 0.6)';    // Gris clair
  }
}

}




