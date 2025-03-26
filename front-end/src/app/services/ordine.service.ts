import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, of} from 'rxjs';
import { Ordine } from '../Models/ordine';
import { DettagliOrdini } from '../Models/dettagli-ordini';
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
    // Assicuriamoci che la struttura corrisponda esattamente a OrdineRequest.java
    const orderData = {
      userId: userId,
      idMetodoPagamento: idMetodoPagamento,
      articoliCarrello: articoliCarrello
    };
    console.log('Invio ordine al server:', orderData);
    return this.http.post(this.apiUrl, orderData);
  }

  /**
   * Ottiene gli ordini di un utente
   */
  getUserOrders(userId: number): Observable<Ordine[]> {
    // Aggiungi log per debug
    console.log(`Richiesta ordini per utente ID: ${userId}`);

    return this.http.get<Ordine[]>(`${this.apiUrl}/utente/${userId}`)
      .pipe(
        tap(orders => console.log('Ordini ricevuti:', orders)),
        catchError(err => {
          console.error('Errore nel recupero ordini:', err);
          // Ritorna un array vuoto in caso di errore invece di propagare l'errore
          return of([]);
        })
      );
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
}
