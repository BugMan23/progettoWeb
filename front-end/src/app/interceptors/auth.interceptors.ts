// auth.interceptor.ts (crea questo file in src/app/interceptors)
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private router: Router, private authService: AuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    // Se l'utente è autenticato, aggiungiamo l'ID utente come header
    if (this.authService.getUserId()) {
      request = request.clone({
        setHeaders: {
          'X-User-Id': this.authService.getUserId() || ''
        }
      });
    }

    // Proseguiamo con la richiesta e gestiamo eventuali errori
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        // Se riceviamo un errore 401 (Unauthorized), facciamo il logout
        if (error.status === 401) {
          this.authService.logout();
        }
        return throwError(error);
      })
    );
  }
}
