
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';




export interface ProjetRef {
  numeroProjet: number;
}

export interface UserRef {
  matricule: string;
}


export interface Member {
  matricule: string;
  nom: string;
  prenom: string;
  image: string;
}

export interface Tache {


  id?: number;
  titre: string;
  description?: string;
  priorite: string;
  etat: string;
 
  dateCreation?: string | Date; // ajout pour affichage correct
  dateFin?: string | Date;       // garde la compatibilité
  assigneeMatricule?: string | null;
  assigneeImage?: string | null;
  autresAssignes?: Member[];
  projetId?: number;
   archived?: boolean; // <-- deviens optionnelle
}





@Injectable({
  providedIn: 'root'
})
export class TacheService {


  private apiUrl = 'http://localhost:8080/api/taches';
  


  constructor(private http: HttpClient) {}

getTachesByProjet(projetId: number, archived: boolean = false): Observable<Tache[]> {
  return this.http.get<Tache[]>(`${this.apiUrl}/byProjet/${projetId}?archived=${archived}`);
}



  getTacheById(id: number): Observable<Tache> {
    return this.http.get<Tache>(`${this.apiUrl}/${id}`);
  }



createTache(tache: Tache): Observable<Tache> {
  return this.http.post<Tache>(`${this.apiUrl}`, tache);
}



  updateTache(id: number, tache: Tache): Observable<Tache> {
    return this.http.put<Tache>(`${this.apiUrl}/${id}`, tache);
  }

archiveTache(id: number): Observable<void> {
  return this.http.patch<void>(`${this.apiUrl}/${id}/archive`, {});
}


updateEtatTache(id: number, nouvelEtat: string): Observable<any> {
  return this.http.patch<any>(`${this.apiUrl}/${id}/etat`, { etat: nouvelEtat });
}
getMembers(): Observable<{
  [x: string]: string; matricule: string, nom: string, prenom: string 
}[]> {
  return this.http.get<{ matricule: string, nom: string, prenom: string }[]>(
    'http://localhost:8080/users/members'
    
  );
}


getArchivedTachesByProjet(projetId: number) {
  return this.http.get<Tache[]>(`${this.apiUrl}/byProjet/${projetId}/archived`);
}



getTauxBlocage(projetId: number): Observable<{ tauxBlocage: number }> {
  return this.http.get<{ tauxBlocage: number }>(
    `${this.apiUrl}/byProjet/${projetId}/tauxBlocage`
  );
}





getTachesByMember(matricule: string): Observable<any> {
  return this.http.get(`${this.apiUrl}/byMember/${matricule}`);
}





// ✅ Récupère mes tâches (utilisateur connecté)
  getMyTaches(): Observable<Tache[]> {
    return this.http.get<Tache[]>(`${this.apiUrl}/me`);
  }

 // ✅ Récupère toutes les tâches en fonction du rôle
  // (MEMBER → seulement ses tâches, ADMIN/MANAGER → toutes les tâches)
  getAllTachesRespectingRole(): Observable<Tache[]> {
    return this.http.get<Tache[]>(`${this.apiUrl}/all`);
  }

}




