import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { User } from '../models/user';
import {jwtDecode} from 'jwt-decode'

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth'; // URL del tuo backend

  // Proprietà per tenere traccia del ruolo
  private isUserAdmin = false;

  constructor(private http: HttpClient) {}

  // Registrazione di un nuovo utente
  register(user: User): Observable<string> {
    return this.http.post(`${this.apiUrl}/register`, user, { responseType: 'text' });
  }

  // Login utente e ricezione del token
  login(email: string, password: string): Observable<string> {
    return this.http
      .post(`${this.apiUrl}/login`, { email, password }, { responseType: 'text' })
      .pipe(
        map((token: string) => {
          // Salvo il token in localStorage
          localStorage.setItem('token', token);
          // Decodifica il token con decodeJwt
          const decoded: any = jwtDecode(token);
          // Assumiamo che 'role' sia un boolean nel token
          this.isUserAdmin = decoded.role === true;

          return token;
        })
      );
  }

  // Controlla se l'utente è autenticato
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  // Controlla se l'utente è admin
  isAdmin(): boolean {
    return this.isUserAdmin;
  }

  // Logout dell'utente
  logout(): void {
    localStorage.removeItem('token');
    this.isUserAdmin = false;
  }
}
