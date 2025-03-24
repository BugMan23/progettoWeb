import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ordine } from '../Models/ordine';
import { DettagliOrdini } from '../Models/dettagli-ordini';

@Injectable({
  providedIn: 'root'
})
export class OrdineService {
  private apiUrl = 'http://localhost:8080/api/ordini';

  constructor(private http: HttpClient) { }

  /**
   * Crea un nuovo ordine
   */
  createOrder(userId: number, idMetodoPagamento: number, articoliCarrello: DettagliOrdini[]): Observable<any> {
    return this.http.post(this.apiUrl, {
      userId,
      idMetodoPagamento,
      articoliCarrello
    }, { responseType: 'json' }); // Assicurati che il tipo di risposta sia json
  }

  /**
   * Ottiene gli ordini di un utente
   */
  getUserOrders(userId: number): Observable<Ordine[]> {
    return this.http.get<Ordine[]>(`${this.apiUrl}/utente/${userId}`);
  }

  /**
   * Ottiene i dettagli di un ordine specifico
   */
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
   * Aggiorna lo stato di un ordine (solo admin)
   */
  updateOrderStatus(orderId: number, status: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/${orderId}/status`, { status });
  }
}
