import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Prodotto } from '../models/prodotto';

@Injectable({
  providedIn: 'root'
})
export class ProdottoService {
  private apiUrl = 'http://localhost:8080/api/prodotti';

  constructor(private http: HttpClient) { }

  /**
   * Ottiene tutti i prodotti
   */
  getAllProducts(): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  /**
   * Ottiene un prodotto specifico per ID
   */
  getProductById(id: number): Observable<Prodotto> {
    return this.http.get<Prodotto>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Ottiene prodotti filtrati per categoria
   */
  getProductsByCategory(categoria: string): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(`${this.apiUrl}/categoria/${categoria}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Ottiene prodotti filtrati per range di prezzo
   */
  getProductsByPriceRange(min: number, max: number): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(`${this.apiUrl}/prezzoRange?min=${min}&max=${max}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Ottiene prodotti filtrati per nome
   */
  getProductsByName(nome: string): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(`${this.apiUrl}/nome?q=${encodeURIComponent(nome)}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Ottiene prodotti filtrati per colore
   */
  getProductsByColor(colore: string): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(`${this.apiUrl}/colore?c=${encodeURIComponent(colore)}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Ottiene prodotti con filtri avanzati
   */
  getFilteredProducts(filters: {
    categoria?: number,
    scontati?: boolean,
    minPrezzo?: number,
    maxPrezzo?: number,
    marca?: string[],
    colore?: string[],
    ricerca?: string
  }): Observable<Prodotto[]> {
    let params = new HttpParams();

    // Aggiungi tutti i filtri come parametri della richiesta
    if (filters.categoria) {
      params = params.append('categoria', filters.categoria.toString());
    }

    if (filters.scontati) {
      params = params.append('scontati', 'true');
    }

    if (filters.minPrezzo) {
      params = params.append('minPrezzo', filters.minPrezzo.toString());
    }

    if (filters.maxPrezzo) {
      params = params.append('maxPrezzo', filters.maxPrezzo.toString());
    }

    if (filters.marca && filters.marca.length > 0) {
      filters.marca.forEach(m => {
        params = params.append('marca', m);
      });
    }

    if (filters.colore && filters.colore.length > 0) {
      filters.colore.forEach(c => {
        params = params.append('colore', c);
      });
    }

    if (filters.ricerca) {
      params = params.append('q', filters.ricerca);
    }

    return this.http.get<Prodotto[]>(`${this.apiUrl}/filtro`, { params })
      .pipe(catchError(this.handleError));
  }

  /**
   * Gestione degli errori HTTP
   */
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Si è verificato un errore';
    if (error.error instanceof ErrorEvent) {
      // Errore client-side
      errorMessage = error.error.message;
    } else {
      // Errore backend
      errorMessage = `Errore: ${error.status}, ${error.error}`;
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
