import { Component, OnInit } from '@angular/core';
import { ActualiteService } from '../../services/actualite.service';

@Component({
  selector: 'app-actualite',
  templateUrl: './actualite.component.html',
  styleUrls: ['./actualite.component.css']
})
export class ActualiteComponent implements OnInit {
  actualites: any[] = [];
  isFullDescription: boolean[] = []; // Tableau pour gérer l'état de la description

  constructor(private service: ActualiteService) {}

  ngOnInit() {
    this.refreshListComptes();
  }

  refreshListComptes() {
    this.service.getAllActualites().subscribe(
      (response: any) => {
        this.actualites = response;
        this.isFullDescription = new Array(this.actualites.length).fill(false); // Initialisation à false
        console.log(this.actualites);
      }
    );
  }

  getPhotoUrl(photoName: string): string {
  // Si l'URL contient plusieurs répétitions de "http://localhost:8080/uploads/files/"
  const baseUrl = 'http://localhost:8080/uploads/files/';
  let cleanUrl = photoName;

  // Supprimer les répétitions
  while (cleanUrl.includes(baseUrl + baseUrl)) {
    cleanUrl = cleanUrl.replace(baseUrl + baseUrl, baseUrl);
  }

  return cleanUrl;
}


  // Découpe de la description pour l'affichage initial
  getShortDescription(description: string, index: number): string {
    const maxLength = 100; // Limiter la longueur de la description
    return description.length > maxLength && !this.isFullDescription[index] 
      ? description.substring(0, maxLength) + '...' 
      : description;
  }

  // Récupérer la description complète
  getFullDescription(description: string): string {
    return description;
  }

getPhotosList(photos: string | string[]): string[] {
  if (!photos) return [];
  if (Array.isArray(photos)) return photos;
  return photos.split(',').map(p => p.trim());
}


  // Fonction pour afficher/masquer la description complète
  toggleDescription(index: number): void {
    this.isFullDescription[index] = !this.isFullDescription[index];
  }




}
