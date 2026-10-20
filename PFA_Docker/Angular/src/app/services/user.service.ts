import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment.development';

import { Observable } from 'rxjs';
import { Member } from './tache.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {


  private readonly baseUrl = environment.baseUrl + 'api/users';


  constructor(private http: HttpClient) {}

// Création d'un utilisateur par admin
 private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  // Vérification matricule
  checkMatriculeExists(matricule: string): Observable<boolean> {
  return this.http.get<boolean>(`${this.baseUrl}/check-matricule/${matricule}`, {
    headers: this.getAuthHeaders()
  });
}


 
  addUserAsAdmin(userFormData: FormData): Observable<any> {
    return this.http.post(`${this.baseUrl}/admin/create`, userFormData, {
      //headers: this.getAuthHeaders()
    });
  }

getAllUsers(): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/admin/all`, {
    headers: this.getAuthHeaders()
  });
}


getUsersWithPagination(page: number, size: number, search: string = ''): Observable<any> {
  const params = new HttpParams()
    .set('page', page)
    .set('size', size)
    .set('search', (search || '').trim());

  return this.http.get<any>(`${this.baseUrl}/admin/page`, {
    headers: this.getAuthHeaders(),
    params
  });
}

 
getUserByMatricule(matricule: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/admin/${matricule}`, {
      headers: this.getAuthHeaders()
    });
  }

  updateUser(matricule: string, formData: FormData): Observable<any> {
    return this.http.put(`${this.baseUrl}/admin/update/${matricule}`, formData, {
      headers: this.getAuthHeaders()
    });
  }

  deleteUser(matricule: string): Observable<string> {
    return this.http.delete(`${this.baseUrl}/admin/delete/${matricule}`, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }

 // ---------- MEMBRES ----------
getAllMembers(): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/members`, {
    headers: this.getAuthHeaders()
  });
}

  // ---------- PROFIL UTILISATEUR ----------
  getMyProfile(): Observable<any> {
    return this.http.get(`${this.baseUrl}/me`, {
      headers: this.getAuthHeaders()
    });
  }

  updateMyProfile(formData: FormData): Observable<any> {
    return this.http.put(`${this.baseUrl}/me/update`, formData, {
      headers: this.getAuthHeaders()
    });
  }

  changePassword(oldPassword: string, newPassword: string): Observable<any> {
    let params = new HttpParams()
      .set('oldPassword', oldPassword)
      .set('newPassword', newPassword);

    return this.http.put(`${this.baseUrl}/change-password`, null, {
      headers: this.getAuthHeaders(),
      params,
      responseType: 'text'
    });
  }

  // ---------- MOT DE PASSE OUBLIÉ ----------
  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/forgot-password?email=${email}`, {}, {
      headers: this.getAuthHeaders()
    });
  }

  resetPassword(token: string, newPassword: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/reset-password?token=${token}&newPassword=${newPassword}`, {}, {
      headers: this.getAuthHeaders()
    });
  }

  forceChangePassword(newPassword: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/force-change-password`, null, {
      headers: this.getAuthHeaders(),
      params: { newPassword },
      responseType: 'text'
    });
  }

  validateResetToken(token: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/validate-reset-token?token=${token}`, {
      headers: this.getAuthHeaders()
    });
  }


  getRoleDistribution(): Observable<any> {
  return this.http.get(environment.baseUrl + 'users/role-distribution');
}


getAllUsersForChatMention(): Observable<Member[]> {
  return this.http.get<Member[]>(`${this.baseUrl}/all`);
}

verifyOldPassword(oldPassword: string) {
  return this.http.post<{ valid: boolean }>(
    `${this.baseUrl}/verify-old-password`,
    { oldPassword },
    { headers: this.getAuthHeaders() }
  );
}










  

  

}

