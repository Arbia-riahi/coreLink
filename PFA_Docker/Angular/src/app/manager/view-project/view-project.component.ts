import { Component, OnInit, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../services/projet.service';
import { TacheService } from '../../services/tache.service';
import { Chart, registerables } from 'chart.js';

import { HttpClient } from '@angular/common/http';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import html2canvas from 'html2canvas';







@Component({
  selector: 'app-view-project',
  templateUrl: './view-project.component.html',
  styleUrls: ['./view-project.component.css']
})
export class ViewProjectComponent implements OnInit, AfterViewInit {
  project: any;
  urlUpload = 'http://localhost:8081/uploads/files';
  projectStats: any = { progression: 0, nbParEtat: {} };
  tauxBlocage: number = 0;

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private tacheService: TacheService,
    private http: HttpClient
  ) {
    Chart.register(...registerables); // Charger Chart.js
  }

  ngOnInit(): void {
    const numeroProjet = this.route.snapshot.paramMap.get('numeroProjet');
    if (numeroProjet) {
      this.projectService.getProjectByNumero(+numeroProjet).subscribe({
        next: (data) => this.project = data,
        error: (err) => console.error(err)
      });

      this.loadProjectStats(+numeroProjet);
    }
  }

  ngAfterViewInit(): void {
    setTimeout(() => this.createChart(), 800); // Donne le temps de récupérer les stats
  }

  loadProjectStats(numeroProjet: number): void {
    this.projectService.getProjectStats(numeroProjet).subscribe({
      next: (stats) => {
        this.projectStats = stats;
        this.createChart();          // Pie Chart
        this.createVelocityChart();  // Velocity Chart
        this.createBurnDownChart();  // Burn-down Chart
      },
      error: (err) => console.error('Erreur chargement stats projet', err)
    });

    // Charger le taux de blocage
    this.tacheService.getTauxBlocage(numeroProjet).subscribe({
      next: (res) => {
        this.tauxBlocage = res.tauxBlocage;
        this.createBlockageChart();
      },
      error: (err) => console.error('Erreur chargement taux blocage', err)
    });
  }

createBlockageChart(): void {
  const ctx = document.getElementById('blockageChart') as HTMLCanvasElement;
  if (!ctx) return;

  Chart.getChart('blockageChart')?.destroy();

  const value = Math.min(Math.max(this.tauxBlocage, 0), 100); // Clamp 0-100
  const formattedValue = value.toFixed(2); // 2 chiffres après la virgule

  const gaugeColors = ['#FFADAD', '#EAEAEA']; // Pastel colors

  new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: ['Blocage', 'Disponible'],
      datasets: [
        {
          data: [value, 100 - value],
          backgroundColor: gaugeColors,
          borderWidth: 0
        }
      ]
    },
    options: {
      responsive: true,
      circumference: 180,
      rotation: -90,
      cutout: '70%',
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            label: (context) => `${context.label}: ${context.parsed.toFixed(2)}%`
          }
        }
      }
    },
    plugins: [
      {
        id: 'centerText',
        afterDraw(chart) {
          const { ctx, chartArea } = chart;
          const centerX = (chartArea.left + chartArea.right) / 2;
          const centerY = chartArea.bottom;

          ctx.save();
          ctx.font = 'bold 18px Arial';
          ctx.fillStyle = '#FF6B6B';
          ctx.textAlign = 'center';
          ctx.textBaseline = 'middle';
          ctx.fillText(`${formattedValue}%`, centerX, centerY - 20);
        }
      }
    ]
  });
}



createChart(): void {
  const ctx = document.getElementById('taskChart') as HTMLCanvasElement;
  if (!ctx || !this.projectStats.nbParEtat) return;

  // Supprimer un ancien graphique si présent
  Chart.getChart('taskChart')?.destroy();

  const total = Object.values(this.projectStats.nbParEtat)
    .reduce((acc: number, val: any) => acc + Number(val), 0);

  // Couleurs plus significatives + soft
  const softColors = [
    '#A8E6CF', // Vert pastel (Terminé)
    '#FFD3B6', // Orange pastel (En cours)
    '#FFAAA5', // Rouge pastel (Bloqué)
    '#DCE6F5', // Bleu pastel (À faire)
    '#EADCF8'  // Violet pastel (Autres)
  ];

  new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: Object.keys(this.projectStats.nbParEtat),
      datasets: [
        {
          data: Object.values(this.projectStats.nbParEtat),
          backgroundColor: softColors,
          borderWidth: 1.5,
          borderColor: '#ffffff'
        }
      ]
    },
    options: {
      responsive: true,
      cutout: '70%',
      plugins: {
        legend: { position: 'bottom', labels: { color: '#333', font: { size: 13 } } },
        tooltip: {
          callbacks: {
            label: (context) =>
              `${context.label}: ${context.parsed} tâches (${((context.parsed / total) * 100).toFixed(1)}%)`
          }
        }
      }
    },
    plugins: [
      {
        id: 'centerText',
        beforeDraw(chart) {
          const { width, height, ctx } = chart;
          ctx.save();
          ctx.font = 'bold 18px Arial';
          ctx.textBaseline = 'middle';
          ctx.textAlign = 'center';
          ctx.fillStyle = '#444';
          ctx.fillText(`${total} Tâches`, width / 2, height / 2);
        }
      }
    ]
  });
}



  getFileName(filePath: string): string {
    if (!filePath) return '';
    const fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
    const firstUnderscoreIndex = fileName.indexOf('_');
    return firstUnderscoreIndex !== -1 ? fileName.substring(firstUnderscoreIndex + 1) : fileName;
  }

  createVelocityChart(): void {
    const ctx = document.getElementById('velocityChart') as HTMLCanvasElement;
    if (!ctx || !this.projectStats.velocityByWeek) return;

    Chart.getChart('velocityChart')?.destroy();

    const labels = Object.keys(this.projectStats.velocityByWeek);
    const data = Object.values(this.projectStats.velocityByWeek);

    new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Tâches terminées',
          data: data,
          backgroundColor: '#0d6efd'
        }]
      },
      options: {
        responsive: true,
        plugins: { legend: { display: false } },
        scales: {
          y: { beginAtZero: true, title: { display: true, text: 'Tâches' } },
          x: { title: { display: true, text: 'Semaine' } }
        }
      }
    });
  }

  createBurnDownChart(): void {
    const ctx = document.getElementById('burnDownChart') as HTMLCanvasElement;
    if (!ctx || !this.projectStats.burnDown) return;

    Chart.getChart('burnDownChart')?.destroy();

    const labels = this.projectStats.burnDown.map((item: any) => item.date);
    const data = this.projectStats.burnDown.map((item: any) => item.remainingTasks);

    new Chart(ctx, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: 'Tâches restantes',
          data: data,
          borderColor: '#dc3545',
          fill: false,
          tension: 0.1
        }]
      },
      options: {
        responsive: true,
        plugins: { legend: { display: false } },
        scales: {
          y: { beginAtZero: true, title: { display: true, text: 'Tâches restantes' } },
          x: { title: { display: true, text: 'Date' } }
        }
      }
    });
  }







async generateAndSecurePDF() {
  console.log("=== Génération du PDF sécurisée démarrée ===");
  const pdf = new jsPDF('p', 'mm', 'a4');
  let y = 10;

  // Fonction utilitaire pour souligner un titre
  const drawUnderlinedTitle = (text: string, x: number, y: number, color: [number, number, number]) => {
    pdf.setFont('helvetica', 'bold');
    pdf.setTextColor(...color);
    pdf.text(text, x, y);
    const textWidth = pdf.getTextWidth(text);
    pdf.setDrawColor(...color);
    pdf.setLineWidth(0.7);
    pdf.line(x, y + 1, x + textWidth, y + 1);
  };

  // --- TITRE ---
  pdf.setFontSize(20);
  drawUnderlinedTitle('Rapport du Projet', 105 - pdf.getTextWidth('Rapport du Projet') / 2, y, [0, 51, 204]);
  y += 20;

  // --- INFORMATIONS PRINCIPALES ---
  pdf.setFontSize(14);
  drawUnderlinedTitle('Informations Principales :', 10, y, [0, 51, 204]);
  y += 10;

  pdf.setFontSize(12);
  pdf.setTextColor(0, 0, 0);
  pdf.text(`Titre : ${this.project.titre}`, 10, y); y += 7;
  pdf.text(`Chef de Projet : ${this.project.chefNomComplet}`, 10, y); y += 7;
  pdf.text(`Statut : ${this.project.status}`, 10, y); y += 7;
  pdf.text(`Date de génération : ${new Date().toISOString().slice(0, 10)}`, 10, y); y += 15;

  // --- DESCRIPTION ---
-
pdf.setFontSize(14);
drawUnderlinedTitle('Description :', 10, y, [0, 51, 204]); 
y += 7;

// On repasse en noir pour le contenu
pdf.setFontSize(11);
pdf.setTextColor(0, 0, 0);  // Texte en noir
const description = this.project.description || 'Aucune description.';
const descriptionLines = pdf.splitTextToSize(description, 180);
pdf.text(descriptionLines, 10, y);
y += descriptionLines.length * 6 + 10;


  // --- PROGRESSION ---
  pdf.setFontSize(14);
  drawUnderlinedTitle('Progression :', 10, y, [0, 51, 204]); y += 7;

  pdf.setFontSize(12);
  pdf.setTextColor(0, 0, 0);
  pdf.text(`${this.projectStats.progression.toFixed(1)} %`, 10, y);
  y += 15;

  // --- TABLEAU : Répartition des Tâches ---
  const nbParEtatData: any[][] = Object.entries(this.projectStats.nbParEtat).map(([etat, nb]) => [etat, nb]);
  if (nbParEtatData.length > 0) {
    drawUnderlinedTitle('Répartition des Tâches', 10, y, [0, 51, 204]);
    y += 5;
    autoTable(pdf, {
      head: [['État', 'Nombre']],
      body: nbParEtatData,
      startY: y,
      headStyles: { fillColor: [0, 51, 204], textColor: [255, 255, 255], fontStyle: 'bold' },
      bodyStyles: { fontSize: 11 }
    });
    y = (pdf as any).lastAutoTable.finalY + 10;
  }

  // --- TABLEAU : Velocity ---
  const velocityData: any[][] = Object.entries(this.projectStats.velocityByWeek || {}).map(([week, nb]) => [week, nb]);
  if (velocityData.length > 0) {
    drawUnderlinedTitle('Vélocité des Équipes', 10, y, [0, 51, 204]);
    y += 5;
    autoTable(pdf, {
      head: [['Semaine', 'Tâches terminées']],
      body: velocityData,
      startY: y,
      headStyles: { fillColor: [0, 51, 204], textColor: [255, 255, 255], fontStyle: 'bold' }
    });
    y = (pdf as any).lastAutoTable.finalY + 10;
  }

  // --- TABLEAU : Membres ---
  if (this.project.membres && this.project.membres.length > 0) {
    drawUnderlinedTitle('Membres du Projet', 10, y, [0, 51, 204]);
    y += 5;
    const membresData: any[][] = this.project.membres.map((m: any) => [m.nom, m.prenom]);
    autoTable(pdf, {
      head: [['Nom', 'Prénom']],
      body: membresData,
      startY: y,
      headStyles: { fillColor: [0, 51, 204], textColor: [255, 255, 255], fontStyle: 'bold' }
    });
    y = (pdf as any).lastAutoTable.finalY + 10;
  }

  // --- TABLEAU : Fichiers attachés ---
  if (this.project.fichiers && this.project.fichiers.length > 0) {
    drawUnderlinedTitle('Fichiers Attachés', 10, y, [0, 51, 204]);
    y += 5;
    const fichiersData: any[][] = this.project.fichiers.map((f: any) => [this.getFileName(f)]);
    autoTable(pdf, {
      head: [['Fichiers Attachés']],
      body: fichiersData,
      startY: y,
      headStyles: { fillColor: [0, 51, 204], textColor: [255, 255, 255], fontStyle: 'bold' }
    });
    y = (pdf as any).lastAutoTable.finalY + 10;
  }

  // --- GRAPHIQUES ---
 
    console.log("=== Capture des graphiques démarrée ===");
  const chartSections = [
    { id: 'taskChart', title: 'Répartition Globale des Tâches' },
    { id: 'velocityChart', title: 'Vélocité des Équipes (Tâches Hebdomadaires)' },
    { id: 'burnDownChart', title: 'Diagramme Burn-down (Suivi des Tâches Restantes)' },
    { id: 'blockageChart', title: 'Taux de Blocage des Tâches' }

  ];

  for (const section of chartSections) {
    const chartElement = document.getElementById(section.id);
    if (chartElement) {
      console.log(`Capture de : ${section.id}`);
      await new Promise(resolve => setTimeout(resolve, 400));
      pdf.addPage();
      pdf.setFontSize(16);
      pdf.setFont('helvetica', 'bold');
      pdf.setTextColor(0, 51, 153);
      pdf.text(section.title, 105, 15, { align: 'center' });
      const canvas = await html2canvas(chartElement as HTMLElement, { scale: 2 });
      const chartImage = canvas.toDataURL('image/png');
      pdf.addImage(chartImage, 'PNG', 10, 25, 180, 120);
    }
  }

  console.log("=== Toutes les sections et graphiques ont été ajoutés au PDF ===");

  // --- ENVOI AU BACKEND ---
  const pdfBase64 = pdf.output('datauristring').split(',')[1];
  this.http.post(`http://localhost:8081/api/projets/secure-pdf`, { pdfBase64 }, { responseType: 'arraybuffer' })
    .subscribe((data: ArrayBuffer) => {
      const blob = new Blob([data], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `Rapport_Projet_${this.project.titre}_Secure.pdf`;
      link.click();
      window.URL.revokeObjectURL(url);
      console.log("=== PDF sécurisé téléchargé ===");
    });
}


getMemberPhoto(img: string | null | undefined): string {
  if (!img || img.trim() === '') {
    return '../../assets/avatar.webp';
  }

  // Si l'image est une URL absolute
  if (img.startsWith('http')) {
    return img;
  }

  // Sinon backend local
  return `${this.urlUpload}/${img}`;
}

setDefaultMemberImage(event: any) {
  event.target.src = '../../assets/avatar.webp';
}


}