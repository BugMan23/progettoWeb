import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MetodoPagamento } from '../Models/metodo-pagamento';

@Injectable({
  providedIn: 'root'
})
export class MetodoPagamentoService {
  private apiUrl = 'http://localhost:8080/api/metodipagamento';

  constructor(private http: HttpClient) { }

  /**
   * Ottiene tutti i metodi di pagamento per un utente
   */
  getMetodiPagamentoUtente(userId: number): Observable<MetodoPagamento[]> {
    return this.http.get<MetodoPagamento[]>(`${this.apiUrl}/utente/${userId}`);
  }

  /**
   * Ottiene un metodo di pagamento specifico
   */
  getMetodoPagamentoByID(id: number): Observable<MetodoPagamento> {
    return this.http.get<MetodoPagamento>(`${this.apiUrl}/${id}`);
  }

  /**
   * Salva un nuovo metodo di pagamento
   */
  salvaMetodoPagamento(metodoPagamento: MetodoPagamento, userId: number): Observable<any> {
    const metodoPagamentoConUtente = { ...metodoPagamento, idUtente: userId };
    return this.http.post(this.apiUrl, metodoPagamentoConUtente);
  }

  /**
   * Aggiorna un metodo di pagamento esistente
   */
  updateMetodoPagamento(metodoPagamento: MetodoPagamento): Observable<any> {
    return this.http.put(`${this.apiUrl}/${metodoPagamento.id}`, metodoPagamento);
  }

  /**
   * Elimina un metodo di pagamento
   */
  deleteMetodoPagamento(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
