import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class TokenInterceptorService implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token'); // Récupération du token depuis le localStorage
    let modifiedReq = req;

    // Ajouter le header Authorization si le token existe
    if (token) {
      modifiedReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
          Accept: 'application/json'
        }
      });
    } else {
      // Si pas de token, au moins on force l'Accept JSON
      modifiedReq = req.clone({
        setHeaders: {
          Accept: 'application/json'
        }
      });
    }

    return next.handle(modifiedReq);
  }
}
