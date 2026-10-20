import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface Actualite {
  id: number;
  titre: string;
  description?: string;
  photos?: string[];
  dateCreation?: string; // ISO
}

@Injectable({ providedIn: 'root' })
export class ActualiteService {
  constructor(private Http: HttpClient) {}

  createActualite(actualite: any) {
    return this.Http.post(environment.baseUrl + 'actualites', actualite);
  }

 // service
getAllActualites(): Observable<Actualite[]> {
  const url = `${environment.baseUrl}actualites?size=9999&page=0`;
  return this.Http.get<any>(url).pipe(
    map(res => Array.isArray(res) ? res : (res?.content ?? [])),
    map(list => [...list].sort((a, b) => {
      const tb = b?.dateCreation ? Date.parse(b.dateCreation) : 0;
      const ta = a?.dateCreation ? Date.parse(a.dateCreation) : 0;
      if (tb !== ta) return tb - ta;
      return (Number(b?.id) || 0) - (Number(a?.id) || 0);
    }))
  );
}


  deleteActu(idActu: any) {
    return this.Http.delete(environment.baseUrl + 'actualites/' + idActu);
  }

  updateActualite(id: any, actualite: any) {
    return this.Http.put(`${environment.baseUrl}actualites/${id}`, actualite);
  }

  getActualiteById(id: string) {
    return this.Http.get(environment.baseUrl + 'actualites/' + id);
  }
  getPagedActualites(page: number, size: number = 5): Observable<any> {
  return this.Http.get<any>(`${environment.baseUrl}actualites?page=${page}&size=${size}`);
}

}

