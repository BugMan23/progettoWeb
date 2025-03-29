// auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, BehaviorSubject, map, of} from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { User } from '../models/user'; // Assicurati che questo percorso sia corretto
import { jwtDecode } from 'jwt-decode'; // Importa jwtDecode se necessario

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // URL da entrambe le implementazioni
  private baseUrl = 'http://localhost:8080/api/utenti';
  private apiUrl = 'http://localhost:8080/auth';

  // BehaviorSubject da Sofia
  isAuthenticated = new BehaviorSubject<boolean>(this.hasToken());
  private userRole = new BehaviorSubject<boolean>(this.isAdmin());
  private userName = new BehaviorSubject<string>(localStorage.getItem('userName') || '');

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  loginWithJWT(email: string, password: string): Observable<any> {
    console.log('Attempting login with:', { email, password });

    // Cambia la risposta da 'text' a 'json'
    return this.http
      .post<any>(`${this.apiUrl}/login`, { email, password })
      .pipe(
        map((response) => {
          // Gestiamo sia il caso in cui la risposta è un semplice token (stringa)
          // sia il caso in cui è un oggetto complesso
          let token: string;
          let userId: number | null = null;
          let userName: string | null = null;
          let isAdmin: boolean = false;

          if (typeof response === 'string') {
            // La risposta è direttamente il token
            token = response;
          } else {
            // La risposta è un oggetto che contiene token e altre informazioni
            token = response.token;
            userId = response.userId;
            userName = response.nome;
            isAdmin = response.isAdmin;
          }

          // Salva i dati nel localStorage
          localStorage.setItem('token', token);
          sessionStorage.setItem('token', token);
          localStorage.setItem('authenticated', 'true');

          // Salva l'ID utente se è stato fornito nella risposta
          if (userId !== null && userId !== undefined) {
            localStorage.setItem('userId', userId.toString());
            console.log('User ID from response:', userId);
          } else {
            console.error('No user ID in response');
          }

          // Salva il nome utente se è stato fornito, altrimenti usa l'email
          localStorage.setItem('userName', userName || email);

          // Salva il ruolo admin
          localStorage.setItem('isAdmin', isAdmin ? 'true' : 'false');

          // Aggiorna gli stati
          this.isAuthenticated.next(true);
          this.userRole.next(isAdmin);
          this.userName.next(userName || email);

          return token;
        })
      );
  }

  private getUserIdByEmail(email: string): Observable<number> {
    console.error('getUserIdByEmail called, but endpoint is not available');
    return of(-1);
  }

  register(user: User): Observable<string> {
    return this.http.post(`${this.apiUrl}/register`, user, { responseType: 'text' });
  }


  // Logout combinato
  logout(): void {
    // Rimuoviamo tutti i dati dal localStorage
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    localStorage.removeItem('isAdmin');
    localStorage.removeItem('authenticated');

    sessionStorage.removeItem('token');

    // Aggiorniamo lo stato di autenticazione
    this.isAuthenticated.next(false);
    this.userRole.next(false);
    this.userName.next('');

    // Reindirizziamo l'utente alla pagina di login
    this.router.navigate(['/login']);
  }

  // Metodi per verificare autenticazione
  isLoggedIn(): boolean {
    // Prima verifica l'implementazione di Sofia
    if (localStorage.getItem('authenticated') === 'true') {
      return true;
    }

    // Quindi verifica l'implementazione JWT di Alberto
    const token = sessionStorage.getItem('token');
    if (!token) return false;

    try {
      const decoded: any = jwtDecode(token);
      const currentTime = Math.floor(Date.now() / 1000);
      return decoded.exp > currentTime;
    } catch (error) {
      return false;
    }
  }

  // Getter per lo stato di autenticazione come Observable
  isAuthenticatedUser(): Observable<boolean> {
    return this.isAuthenticated.asObservable();
  }

  // Getter per il ruolo dell'utente
  isAdminUser(): Observable<boolean> {
    return this.userRole.asObservable();
  }

  // Metodo per verificare se l'utente è admin
  isAdmin(): boolean {
    // Prima controlla l'implementazione di Sofia
    if (localStorage.getItem('isAdmin') === 'true') {
      return true;
    }

    // Poi controlla l'implementazione JWT di Alberto
    return this.getUserRole();
  }

  // Getter per il ruolo utente da JWT (implementazione di Alberto)
  getUserRole(): boolean {
    // Prima controlla l'implementazione di Sofia
    if (localStorage.getItem('isAdmin') === 'true') {
      return true;
    }

    // Poi controlla l'implementazione JWT di Alberto
    const token = sessionStorage.getItem('token');
    if (token) {
      try {
        const decoded: any = jwtDecode(token);
        return decoded.role === 'admin';
      } catch (error) {
        return false;
      }
    }
    return false;
  }

  // Getter per il nome dell'utente
  getUserName(): Observable<string> {
    return this.userName.asObservable();
  }

  // Controllo se esiste un token/flag di autenticazione (Sofia)
  private hasToken(): boolean {
    return localStorage.getItem('authenticated') === 'true';
  }

  // Metodo per ottenere l'ID dell'utente attuale
  getUserId(): string | null {
    return localStorage.getItem('userId');
  }

  initializeUserCart(userId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/carrello/initialize/${userId}`, {});
  }
}
