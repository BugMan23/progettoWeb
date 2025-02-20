import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Prodotto } from '../Models/prodotto';

@Injectable({
  providedIn: 'root'
})
export class ProdottoService {
  private apiUrl = 'http://localhost:8080/api/prodotti';

  constructor(private http: HttpClient) { }

  getAllProducts(): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(this.apiUrl);
  }

  getProductById(id: number): Observable<Prodotto> {
    return this.http.get<Prodotto>(`${this.apiUrl}/${id}`);
  }

  getProductsByCategory(categoria: string): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(`${this.apiUrl}/categoria/${categoria}`);
  }

  getProductsByPriceRange(min: number, max: number): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(`${this.apiUrl}/prezzoRange?min=${min}&max=${max}`);
  }
}
