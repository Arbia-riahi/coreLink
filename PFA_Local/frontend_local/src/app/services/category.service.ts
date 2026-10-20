

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  
  url = environment.baseUrl + 'api/categories';

  constructor(private http: HttpClient) {}

  addCategory(category: any): Observable<any> {
    return this.http.post(this.url, category);
  }

  getCategories(): Observable<any[]> {
    return this.http.get<any[]>(this.url);
  }

  getCategoryById(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${id}`);
  }

  updateCategory(id: number, category: any): Observable<any> {
    return this.http.put(`${this.url}/${id}`, category);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}


