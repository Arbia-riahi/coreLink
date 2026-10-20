import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment.development';

type Role = 'ADMIN' | 'MANAGER' | 'MEMBER';

interface LoginResponse {
  token: string;
  matricule: string;
  roles: Role[];
  nom?: string;
  prenom?: string;
  email?: string;
  image?: string;
  mustChangePassword?: boolean;
}

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  private readonly urlLogin = environment.baseUrl + 'api/auth/signin';

  // liste des clés que l’app écrit dans localStorage
  private readonly STORAGE_KEYS = [
    'token', 'matricule', 'roles', 'nom', 'prenom', 'email', 'image', 'mustChangePassword'
  ];

  constructor(private httpClient: HttpClient) {}

  authenticate(matricule: string, password: string) {
    return this.httpClient
      .post<LoginResponse>(this.urlLogin, { matricule, motDePasse: password })
      .pipe(
        tap((data) => this.persist(data))
      );
  }

  private persist(data: LoginResponse) {
    localStorage.setItem('token', data.token);
    localStorage.setItem('matricule', data.matricule);
    localStorage.setItem('roles', JSON.stringify(data.roles ?? []));
    localStorage.setItem('nom', data.nom ?? '');
    localStorage.setItem('prenom', data.prenom ?? '');
    localStorage.setItem('email', data.email ?? '');
    localStorage.setItem('image', data.image ?? '');
    localStorage.setItem('mustChangePassword', String(!!data.mustChangePassword));
  }

  getRoles(): Role[] {
    const roles = localStorage.getItem('roles');
    if (!roles) return [];
    try {
      const parsed = JSON.parse(roles);
      return Array.isArray(parsed) ? parsed as Role[] : [];
    } catch {
      return [];
    }
  }

  getPrimaryRole(): Role | null {
    const roles = this.getRoles();
    if (roles.includes('ADMIN')) return 'ADMIN';
    if (roles.includes('MANAGER')) return 'MANAGER';
    if (roles.includes('MEMBER')) return 'MEMBER';
    return null;
  }

  hasRole(role: Role): boolean {
    return this.getRoles().includes(role);
  }

  routeForRole(): string {
    const role = this.getPrimaryRole();
    switch (role) {
      case 'ADMIN':   return '/backoffice';
      case 'MANAGER': return '/manager';
      case 'MEMBER':  return '/memeber'; // garde l’orthographe que tu utilises
      default:        return '/home';
    }
  }

  isUserLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  mustChangePassword(): boolean {
    return localStorage.getItem('mustChangePassword') === 'true';
  }

  // Nettoyage ciblé (évite d'effacer d’autres clés éventuelles du même domaine)
logOut(): void {
  localStorage.clear();
  localStorage.setItem('logout', Date.now().toString());
  localStorage.removeItem('logout');
}


}
