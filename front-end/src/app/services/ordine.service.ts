import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ordine } from '../Models/ordine';
import { DettagliOrdini } from '../Models/dettagli-ordini';

@Injectable({
  providedIn: 'root'
})
export class OrdineService {
  private apiUrl = 'http://localhost:8080/api/ordini';

  constructor(private http: HttpClient) { }

  createOrder(userId: number, idMetodoPagamento: number, articoliCarrello: DettagliOrdini[]): Observable<any> {
    return this.http.post(this.apiUrl, {
      userId,
      idMetodoPagamento,
      articoliCarrello
    });
  }

  getUserOrders(userId: number): Observable<Ordine[]> {
    return this.http.get<Ordine[]>(`${this.apiUrl}/utente/${userId}`);
  }

  getOrderDetails(orderId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${orderId}`);
  }
}
