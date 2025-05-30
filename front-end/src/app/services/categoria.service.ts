import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Categoria } from '../models/categoria';
import { Prodotto } from '../models/prodotto';

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {
  private apiUrl = 'http://localhost:8080/api/categorie';

  constructor(private http: HttpClient) { }

  getAllCategories(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(this.apiUrl);
  }

  getProductsByCategory(categoriaId: number): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(`${this.apiUrl}/${categoriaId}/prodotti`);
  }

  addCategory(categoria: Categoria): Observable<any> {
    return this.http.post(`${this.apiUrl}/admin`, categoria);
  }

  getCategoryById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }
}
