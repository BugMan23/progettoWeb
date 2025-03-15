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

  salvaMetodoPagamento(metodoPagamento: MetodoPagamento, userId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}`, { metodoPagamento, userId });
  }

  getMetodiPagamentoUtente(userId: number): Observable<MetodoPagamento[]> {
    return this.http.get<MetodoPagamento[]>(`${this.apiUrl}/utente/${userId}`);
  }

  getMetodoPagamentoByID(id: number): Observable<MetodoPagamento> {
    return this.http.get<MetodoPagamento>(`${this.apiUrl}/${id}`);
  }
}
