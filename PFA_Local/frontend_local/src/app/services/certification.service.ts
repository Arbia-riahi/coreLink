import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CertificationService {

  private apiUrl = environment.baseUrl + 'api/certifications';


  constructor(private http: HttpClient) {}

  getAllCertifications(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  getCertificationById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  createCertification(formData: FormData): Observable<any> {
    return this.http.post(this.apiUrl, formData);
  }

  updateCertification(id: number, formData: FormData): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, formData);
  }

  deleteCertification(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}

