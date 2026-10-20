import { Component, OnInit, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../services/projet.service';
import { TacheService } from '../../services/tache.service';
import { Chart, registerables } from 'chart.js';

import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-view-project',
  templateUrl: './view-project.component.html',
  styleUrl: './view-project.component.css'
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
  ngAfterViewInit(): void {
    throw new Error('Method not implemented.');
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

 
  loadProjectStats(numeroProjet: number): void {
    this.projectService.getProjectStats(numeroProjet).subscribe({
      next: (stats) => {
        this.projectStats = stats;
       
      },
      error: (err) => console.error('Erreur chargement stats projet', err)
    });

    // Charger le taux de blocage
    this.tacheService.getTauxBlocage(numeroProjet).subscribe({
      next: (res) => {
        this.tauxBlocage = res.tauxBlocage;
       
      },
      error: (err) => console.error('Erreur chargement taux blocage', err)
    });
  }





  getFileName(filePath: string): string {
    if (!filePath) return '';
    const fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
    const firstUnderscoreIndex = fileName.indexOf('_');
    return firstUnderscoreIndex !== -1 ? fileName.substring(firstUnderscoreIndex + 1) : fileName;
  }

  
getMemberPhoto(img: string | null | undefined): string {
  if (!img || img.trim() === '') {
    return '../../assets/avatar.webp';
  }

  // si l’image est une URL complète
  if (img.startsWith('http')) {
    return img;
  }

  // sinon backend local
  return `${this.urlUpload}/${img}`;
}

setDefaultMemberImage(event: any) {
  event.target.src = '../../assets/avatar.webp';
}

 








}











