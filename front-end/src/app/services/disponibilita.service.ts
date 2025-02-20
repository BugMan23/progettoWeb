import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Disponibilita } from '../Models/disponibilita';

@Injectable({
  providedIn: 'root'
})
export class DisponibilitaService {
  private apiUrl = 'http://localhost:8080/api/disponibilita';

  constructor(private http: HttpClient) { }

  getDisponibilitaProdotto(prodottoId: number): Observable<Disponibilita[]> {
    return this.http.get<Disponibilita[]>(`${this.apiUrl}/prodotto/${prodottoId}`);
  }

  updateDisponibilita(prodottoId: number, quantita: number, taglia: string): Observable<any> {
    return this.http.put(`${this.apiUrl}`, { prodottoId, quantita, taglia });
  }

  decrementaQuantita(prodottoId: number, quantita: number, taglia: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/decrementa`, { prodottoId, quantita, taglia });
  }
}
