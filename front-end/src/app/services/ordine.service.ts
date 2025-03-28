import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, of} from 'rxjs';
import { Ordine } from '../models/ordine';
import { DettagliOrdini } from '../models/dettagli-ordini';
import {catchError, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class OrdineService {
  private apiUrl = 'http://localhost:8080/api/ordini';

  constructor(private http: HttpClient) { }

  /**
   * Crea un nuovo ordine
   */
  createOrder(userId: number, idMetodoPagamento: number, articoliCarrello: any[]): Observable<any> {
    return this.http.post(this.apiUrl, {
      userId,
      idMetodoPagamento,
      articoliCarrello
    });
  }

  /**
   * Ottiene gli ordini di un utente
   */
  getUserOrders(userId: number): Observable<Ordine[]> {
    return this.http.get<Ordine[]>(`${this.apiUrl}/utente/${userId}`);
  }

  getOrderDetails(orderId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${orderId}`);
  }

  /**
   * Ottiene i dettagli degli articoli dell'ordine
   */
  getOrderItems(orderId: number): Observable<DettagliOrdini[]> {
    return this.http.get<DettagliOrdini[]>(`${this.apiUrl}/${orderId}/items`);
  }

  /**
   * Ottiene i dati del metodo di pagamento
   */
  getPaymentMethod(paymentId: number): Observable<any> {
    return this.http.get(`http://localhost:8080/api/metodipagamento/${paymentId}`);
  }

  /**
   * Aggiorna lo stato di un ordine (solo admin)
   */
  updateOrderStatus(orderId: number, status: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/${orderId}/status`, { status });
  }

  isProdottoAcquistato(userId: number, prodottoId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/utente/${userId}/prodotto/${prodottoId}/acquistato`);
  }
}
