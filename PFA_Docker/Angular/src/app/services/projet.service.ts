import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment.development';

import { Observable } from 'rxjs';



export interface Project {
chefNomComplet: any;
membres: any;
  numeroProjet: number;
  titre: string;
  description: string;
  dateDebut: string;
  dateFin: string;
  status: string; // EN_ATTENTE, EN_COURS, TERMINE, ANNULE
  chefProjet: string;
  fichiers: string[];
  archived: boolean;
}

@Injectable({
  providedIn: 'root'
})

 
export class ProjectService {
 

 private baseUrl = environment.baseUrl + 'api/projets';

constructor(private http: HttpClient) {}
 

  getAllProjects(): Observable<any> {
     return this.http.get(this.baseUrl);
  }

  createProject(matricule: string, formData: FormData): Observable<any> {
    return this.http.post(`${this.baseUrl}/create/${matricule}`, formData);
  }

getProjectByNumero(numeroProjet: number): Observable<any> {
  return this.http.get(`${this.baseUrl}/${numeroProjet}`);
}




 // Pagination
 getProjetsWithPagination(page: number, size: number): Observable<any> {
  return this.http.get<any>(`${this.baseUrl}/pagination?page=${page}&size=${size}`);
}




updateProject(numeroProjet: number, formData: FormData): Observable<any> {
  return this.http.put(`${this.baseUrl}/update/${numeroProjet}`, formData);
}

archiveProject(numeroProjet: number): Observable<any> {
  return this.http.put(`${this.baseUrl}/archive/${numeroProjet}`, {});
}

getArchivedProjects(): Observable<any> {
  return this.http.get(`${this.baseUrl}/archived`);
}

getArchivedProjectsWithPagination(page: number, size: number): Observable<any> {
  return this.http.get<any>(`${this.baseUrl}/archived/pagination?page=${page}&size=${size}`);
}


getProjectStats(numeroProjet: number): Observable<any> {
  return this.http.get(`${this.baseUrl}/${numeroProjet}/stats`);
}


getGlobalProjectStats(): Observable<any> {
  return this.http.get(`${this.baseUrl}/global-stats`);
}

getProjectsComparison(): Observable<any> {
  return this.http.get(`${this.baseUrl}/comparaison`);
}

getUpcomingDeadlines(days: number = 10): Observable<any> {
  return this.http.get(`${this.baseUrl}/upcoming-deadlines?days=${days}`);
}

getProjectsByChefAndStatus(): Observable<any> {
  return this.http.get(`${this.baseUrl}/projects-by-chef-status`);
}
getProjectsForGantt(): Observable<any> {
  return this.http.get(`${this.baseUrl}/gantt-projects`);
}

getProjectsByMember(matricule: string): Observable<any> {
  return this.http.get(`${this.baseUrl}/member/${matricule}`);
}


getNumeroProjetById(id: number): Observable<string> {
  return this.http.get(`${this.baseUrl}/numero-by-id/${id}`, { responseType: 'text' });
}

// projet.service.ts
getProjetsForChefWithPagination(matricule: string, page: number, size: number): Observable<any> {
  return this.http.get<any>(`${this.baseUrl}/chef/${matricule}/pagination?page=${page}&size=${size}`);
}

getArchivedProjetsForChefWithPagination(matricule: string, page: number, size: number): Observable<any> {
  return this.http.get<any>(`${this.baseUrl}/chef/${matricule}/archived/pagination?page=${page}&size=${size}`);
}


// projet.service.ts
getComparaisonForChef(matricule: string) {
  return this.http.get<any>(`${this.baseUrl}/chef/${matricule}/comparaison`);
}

getUpcomingDeadlinesForChef(matricule: string, days: number = 10) {
  return this.http.get<any>(`${this.baseUrl}/chef/${matricule}/upcoming-deadlines?days=${days}`);
}



searchProjetsForChef(opts: {
  chefMatricule: string;
  page?: number; size?: number;
  q?: string; status?: string;
  dateDebutFrom?: string; dateDebutTo?: string;
  dateFinFrom?: string;   dateFinTo?: string;
  hasFiles?: boolean; archived?: boolean;
  sortBy?: string; sortDir?: 'asc' | 'desc';
}): Observable<any> {
  let params = new HttpParams()
    .set('chefMatricule', opts.chefMatricule)
    .set('page', String(opts.page ?? 0))
    .set('size', String(opts.size ?? 5))
    .set('sortBy', opts.sortBy ?? 'dateDebut')
    .set('sortDir', opts.sortDir ?? 'asc');

  const add = (k: string, v: any) => {
    if (v !== undefined && v !== null && v !== '') params = params.set(k, String(v));
  };
  add('q', opts.q);
  add('status', opts.status);
  add('dateDebutFrom', opts.dateDebutFrom);
  add('dateDebutTo', opts.dateDebutTo);
  add('dateFinFrom', opts.dateFinFrom);
  add('dateFinTo', opts.dateFinTo);
  if (opts.hasFiles !== undefined) add('hasFiles', opts.hasFiles);
  if (opts.archived !== undefined) add('archived', opts.archived);

  // ⬇️ IMPORTANT: on RETOURNE l’Observable
  return this.http.get<any>(`${this.baseUrl}/search`, { params });
}


  
}




















