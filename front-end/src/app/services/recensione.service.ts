import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Recensione } from '../Models/recensione';

@Injectable({
  providedIn: 'root'
})
export class RecensioneService {
  private apiUrl = 'http://localhost:8080/api/reviews';

  constructor(private http: HttpClient) { }

  addReview(recensione: Recensione): Observable<any> {
    return this.http.post(this.apiUrl, recensione);
  }

  getProductReviews(productId: number): Observable<Recensione[]> {
    return this.http.get<Recensione[]>(`${this.apiUrl}/product/${productId}`);
  }

  deleteReview(reviewId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/admin/${reviewId}`);
  }
}
