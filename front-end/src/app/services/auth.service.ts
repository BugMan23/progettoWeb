// auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, map } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { User } from '../Models/user'; // Assicurati che questo percorso sia corretto
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

  // Implementazione login di Sofia
  /*login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, { email, password })
      .pipe(
        tap((response: any) => {
          // Salviamo i dati dell'utente nel localStorage
          localStorage.setItem('userId', response.id.toString());
          localStorage.setItem('userName', response.nome);
          localStorage.setItem('isAdmin', response.isAdmin.toString());

          // Flag di autenticazione
          localStorage.setItem('authenticated', 'true');

          // Aggiorniamo lo stato di autenticazione
          this.isAuthenticated.next(true);
          this.userRole.next(response.isAdmin);
          this.userName.next(response.nome);
        })
      );
  }*/

  // Implementazione login di Alberto usando JWT (alternativa)
  loginWithJWT(email: string, password: string): Observable<string> {
    return this.http
      .post(`${this.apiUrl}/login`, { email, password }, { responseType: 'text' })
      .pipe(
        map((token: string) => {
          // Salva il token
          sessionStorage.setItem('token', token);

          // Estrai informazioni dal token JWT
          const decoded: any = jwtDecode(token);

          // IMPORTANTE: Salva anche i dati utente nel localStorage come fa il metodo login()
          localStorage.setItem('userId', decoded.sub || ''); // o un'altra proprietà che contiene l'ID
          localStorage.setItem('userName', decoded.sub || ''); // o un'altra proprietà che contiene il nome
          localStorage.setItem('isAdmin', (decoded.role === 'admin').toString());
          localStorage.setItem('authenticated', 'true');

          // Aggiorna gli stati
          this.isAuthenticated.next(true);
          this.userRole.next(decoded.role === 'admin');
          this.userName.next(decoded.sub); // Imposta il nome utente dal token

          return token;
        })
      );
  }

  // Registrazione utente comune a entrambe le implementazioni
  register(user: User): Observable<string> {
    return this.http.post(`${this.baseUrl}/registrazione`, user, {
      responseType: 'text' // Specifica che ti aspetti una risposta di testo
    });
  }


  // Registrazione alternativa di Alberto
  registerWithJWT(user: User): Observable<string> {
    return this.http.post(`${this.apiUrl}/register`, user, { responseType: 'text' });
  }

  // Logout combinato
  logout(): void {
    // Rimuoviamo tutti i dati dal localStorage
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    localStorage.removeItem('isAdmin');
    localStorage.removeItem('authenticated');

    // Rimuovi anche dal sessionStorage (Alberto)
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
}
