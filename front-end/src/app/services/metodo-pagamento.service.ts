// In metodo-pagamento.service.ts
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

  getMetodiPagamentoUtente(userId: number): Observable<MetodoPagamento[]> {
    return this.http.get<MetodoPagamento[]>(`${this.apiUrl}/utente/${userId}`);
  }

  getMetodoPagamentoByID(id: number): Observable<MetodoPagamento> {
    return this.http.get<MetodoPagamento>(`${this.apiUrl}/${id}`);
  }

  salvaMetodoPagamento(metodoPagamento: MetodoPagamento, userId: number): Observable<any> {
    console.log('Frontend: Inviando metodo di pagamento al server:', metodoPagamento);

    // Assicurati che l'ID utente sia impostato nell'oggetto
    const metodoPagamentoConUtente = { ...metodoPagamento, idUtente: userId };

    return this.http.post(this.apiUrl, metodoPagamentoConUtente);
  }
}
