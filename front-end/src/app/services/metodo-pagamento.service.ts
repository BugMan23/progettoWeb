import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { MetodoPagamento } from '../models/metodo-pagamento';

@Injectable({
  providedIn: 'root'
})
export class MetodoPagamentoService {
  private apiUrl = 'http://localhost:8080/api/metodipagamento';

  constructor(private http: HttpClient) { }

  getMetodiPagamentoUtente(userId: number): Observable<MetodoPagamento[]> {
    console.log('Frontend service: Richiesta metodi di pagamento per utente ID:', userId);
    return this.http.get<MetodoPagamento[]>(`${this.apiUrl}/utente/${userId}`).pipe(
      catchError(this.handleError)
    );
  }

  getMetodoPagamentoByID(id: number): Observable<MetodoPagamento> {
    return this.http.get<MetodoPagamento>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  salvaMetodoPagamento(metodoPagamento: MetodoPagamento, userId: number): Observable<any> {
    console.log('Frontend service: Inviando metodo di pagamento al server per utente ID:', userId);
    console.log('Frontend service: Dati metodo di pagamento:', metodoPagamento);

    // Assicurati che l'ID utente sia impostato nell'oggetto
    const metodoPagamentoConUtente = {
      ...metodoPagamento,
      idUtente: userId
    };

    console.log('Frontend service: Oggetto finale da inviare:', metodoPagamentoConUtente);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post(this.apiUrl, metodoPagamentoConUtente, {
      responseType: 'text'  // Imposta il tipo di risposta a 'text' invece di 'json'
    }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: any) {
    console.error('Si è verificato un errore:', error);
    return throwError(() => error);
  }
}
