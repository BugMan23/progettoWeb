import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Prodotto } from '../Models/prodotto';
import { Carrello } from '../Models/carrello';

@Injectable({
  providedIn: 'root'
})
export class CarrelloService {
  private apiUrl = 'http://localhost:8080/api/carrello';
  public cartChanged = new Subject<void>(); // Per notificare i cambiamenti nel carrello

  constructor(private http: HttpClient) { }

  /**
   * Aggiunge un prodotto al carrello dell'utente
   */
  addToCart(request: {
    userId: number;
    productId: number;
    quantity: number;
    taglia: string;
  }): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, request);
  }

  /**
   * Ottiene i prodotti nel carrello dell'utente
   */
  getUserCart(userId: number): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(`${this.apiUrl}/${userId}`);
  }

  /**
   * Ottiene i dettagli (quantità, taglia) per ogni prodotto nel carrello
   */
  getCartDetails(userId: number): Observable<Carrello[]> {
    return this.http.get<Carrello[]>(`${this.apiUrl}/details/${userId}`);
  }

  /**
   * Calcola il totale del carrello
   */
  getCartTotal(userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${userId}/total`);
  }

  /**
   * Aggiorna la quantità di un prodotto nel carrello
   */
  updateCartItem(userId: number, productId: number, quantity: number, taglia: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/update`, {
      userId,
      productId,
      quantity,
      taglia
    });
  }

  /**
   * Aggiorna solo la taglia di un prodotto nel carrello
   */
  updateCartItemTaglia(userId: number, productId: number, taglia: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/update-taglia`, {
      userId,
      productId,
      taglia
    });
  }

  /**
   * Rimuove un prodotto dal carrello
   */
  removeFromCart(userId: number, productId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${userId}/product/${productId}`);
  }

  /**
   * Svuota il carrello dell'utente
   */
  clearCart(userId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${userId}`);
  }

  /**
   * Ottiene il numero di prodotti nel carrello
   */
  getCartCount(userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${userId}/count`);
  }
}
