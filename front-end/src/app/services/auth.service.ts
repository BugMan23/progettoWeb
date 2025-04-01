import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, BehaviorSubject, map, of} from 'rxjs';
import { Router } from '@angular/router';
import { User } from '../models/user';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/utenti';
  private apiUrl = 'http://localhost:8080/auth';

  isAuthenticated = new BehaviorSubject<boolean>(this.hasToken());
  private userRole = new BehaviorSubject<boolean>(this.isAdmin());
  private userName = new BehaviorSubject<string>(localStorage.getItem('userName') || '');

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  loginWithJWT(email: string, password: string): Observable<any> {
    return this.http
      .post<any>(`${this.apiUrl}/login`, { email, password })
      .pipe(
        map((response) => {
          let token: string;
          let userId: number | null = null;
          let userName: string | null = null;
          let isAdmin: boolean = false;

          if (typeof response === 'string') {
            token = response;
          } else {
            token = response.token;
            userId = response.userId;
            userName = response.nome;
            isAdmin = response.isAdmin;
          }

          localStorage.setItem('token', token);
          sessionStorage.setItem('token', token);
          localStorage.setItem('authenticated', 'true');

          if (userId !== null && userId !== undefined) {
            localStorage.setItem('userId', userId.toString());
          } else {
            console.error('No user ID in response');
          }

          localStorage.setItem('userName', userName || email);
          localStorage.setItem('isAdmin', isAdmin ? 'true' : 'false');

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

  logout(): void {
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    localStorage.removeItem('isAdmin');
    localStorage.removeItem('authenticated');

    sessionStorage.removeItem('token');

    this.isAuthenticated.next(false);
    this.userRole.next(false);
    this.userName.next('');

    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    if (localStorage.getItem('authenticated') === 'true') {
      return true;
    }

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

  isAuthenticatedUser(): Observable<boolean> {
    return this.isAuthenticated.asObservable();
  }

  isAdminUser(): Observable<boolean> {
    return this.userRole.asObservable();
  }

  isAdmin(): boolean {
    if (localStorage.getItem('isAdmin') === 'true') {
      return true;
    }

    return this.getUserRole();
  }

  getUserRole(): boolean {
    if (localStorage.getItem('isAdmin') === 'true') {
      return true;
    }

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

  getUserName(): Observable<string> {
    return this.userName.asObservable();
  }

  private hasToken(): boolean {
    return localStorage.getItem('authenticated') === 'true';
  }

  getUserId(): string | null {
    return localStorage.getItem('userId');
  }

  initializeUserCart(userId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/carrello/initialize/${userId}`, {});
  }
}
