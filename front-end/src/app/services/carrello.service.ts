import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Prodotto } from '../Models/prodotto';

@Injectable({
  providedIn: 'root'
})
export class CarrelloService {
  private apiUrl = 'http://localhost:8080/api/carrello';

  constructor(private http: HttpClient) { }

  addToCart(cartRequest: {
    userId: number;
    productId: number;
    quantity: number;
    taglia: string;
  }): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, cartRequest);
  }

  getUserCart(userId: number): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(`${this.apiUrl}/${userId}`);
  }

  getCartTotal(userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${userId}/total`);
  }

  clearCart(userId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${userId}`);
  }
}
