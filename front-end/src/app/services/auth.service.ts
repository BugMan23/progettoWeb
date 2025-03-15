// auth.service.ts (crea questo file in src/app/services)
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/utenti';
  private isAuthenticated = new BehaviorSubject<boolean>(this.hasToken());
  private userRole = new BehaviorSubject<boolean>(this.isAdmin());
  private userName = new BehaviorSubject<string>(localStorage.getItem('userName') || '');

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, { email, password })
      .pipe(
        tap((response: any) => {
          // Salviamo i dati dell'utente nel localStorage
          localStorage.setItem('userId', response.id.toString());
          localStorage.setItem('userName', response.nome);
          localStorage.setItem('isAdmin', response.isAdmin.toString());

          // Non c'è un token vero e proprio nel tuo backend, ma possiamo creare un flag di autenticazione
          localStorage.setItem('authenticated', 'true');

          // Aggiorniamo lo stato di autenticazione
          this.isAuthenticated.next(true);
          this.userRole.next(response.isAdmin);
          this.userName.next(response.nome);
        })
      );
  }

  register(user: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/registrazione`, user);
  }

  logout(): void {
    // Rimuoviamo tutti i dati dal localStorage
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    localStorage.removeItem('isAdmin');
    localStorage.removeItem('authenticated');

    // Aggiorniamo lo stato di autenticazione
    this.isAuthenticated.next(false);
    this.userRole.next(false);
    this.userName.next('');

    // Reindirizziamo l'utente alla pagina di login
    this.router.navigate(['/login']);
  }

  // Getter per lo stato di autenticazione
  isAuthenticatedUser(): Observable<boolean> {
    return this.isAuthenticated.asObservable();
  }

  // Getter per il ruolo dell'utente
  isAdminUser(): Observable<boolean> {
    return this.userRole.asObservable();
  }

  // Getter per il nome dell'utente
  getUserName(): Observable<string> {
    return this.userName.asObservable();
  }

  // Controllo se esiste un token/flag di autenticazione
  private hasToken(): boolean {
    return localStorage.getItem('authenticated') === 'true';
  }

  // Controllo se l'utente è admin
  private isAdmin(): boolean {
    return localStorage.getItem('isAdmin') === 'true';
  }

  // Metodo per ottenere l'ID dell'utente attuale
  getUserId(): string | null {
    return localStorage.getItem('userId');
  }
}
