import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {User} from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8080/api/utenti';

  constructor(private http: HttpClient) { }

  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, { email, password });
  }

  register(utente: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/registrazione`, utente);
  }

  getUserProfile(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/profile/${id}`);
  }

  updateUserProfile(id: number, userData: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/profile/${id}`, userData);
  }

  getUserAddresses(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/${id}/indirizzi`);
  }

  addAddress(indirizzo: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/indirizzi`, indirizzo);
  }

  /*deleteAddress(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/indirizzi/${id}`);
  }*/

  getUserPaymentMethods(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/${id}/pagamenti`);
  }

  addPaymentMethod(metodoPagamento: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/pagamenti`, metodoPagamento);
  }

  /*deletePaymentMethod(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/pagamenti/${id}`);
  }*/

  getCustomers(): Observable<User[]> {
    const token = localStorage.getItem('token');
    return this.http.get<User[]>('http://localhost:8080/api/utenti/customers', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }


  deactivateUser(id: number | undefined): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.delete(`http://localhost:8080/api/utenti/admin/${id}`, { headers });
  }



}
