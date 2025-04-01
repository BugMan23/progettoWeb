import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import { Recensione} from '../models/recensione';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RecensioneService {
  private apiUrl = 'http://localhost:8080/api/reviews';

  constructor(private http: HttpClient) { }

  addReview(recensione: Recensione): Observable<any> {
    return this.http.post<any>(this.apiUrl, recensione).pipe(
      catchError(error => {
        console.error('Errore nel servizio durante l\'aggiunta della recensione:', error);
        return throwError(() => error);
      })
    );
  }

  // Ottiene le recensioni di un prodotto
  getProductReviews(productId: number): Observable<Recensione[]> {
    return this.http.get<Recensione[]>(`${this.apiUrl}/product/${productId}`);
  }

  // Elimina una recensione (admin)
  deleteReview(reviewId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/admin/${reviewId}`);
  }

  // Verifica se un utente ha già recensito un prodotto specifico
  getUserProductReview(userId: number, productId: number): Observable<Recensione[]> {
    return this.http.get<Recensione[]>(`${this.apiUrl}/user/${userId}/product/${productId}`);
  }

  // Ottiene tutte le recensioni di un utente
  getUserReviews(userId: number | undefined): Observable<Recensione[]> {
    return this.http.get<Recensione[]>(`${this.apiUrl}/user/${userId}`);
  }
}
