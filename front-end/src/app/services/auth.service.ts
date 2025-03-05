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
          sessionStorage.setItem('token', token);
          return token;
        })
      );
  }

  // Controlla se l'utente è autenticato
  isLoggedIn(): boolean {
    const token = sessionStorage.getItem('token');
    if(!token) return false;

    const decoded: any = jwtDecode(token);
    const currentTime = Math.floor(Date.now() / 1000);
    return decoded.exp > currentTime;
  }

  //Ottiene il ruolo dell'utente dal token
  getUserRole(): boolean {
    const token = sessionStorage.getItem('token');
    if(token){
      const decoded: any = jwtDecode(token);
      return decoded.role === 'admin';
    }
    return false;
  }

  // Controlla se l'utente è admin
  isAdmin(): boolean {
    return this.getUserRole();
  }

  // Logout dell'utente
  logout(): void {
    sessionStorage.removeItem('token');
  }
}
