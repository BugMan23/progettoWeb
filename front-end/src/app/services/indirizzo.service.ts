import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Indirizzo } from '../Models/indirizzo';

@Injectable({
  providedIn: 'root'
})
export class IndirizzoService {
  private apiUrl = 'http://localhost:8080/api/indirizzi';

  constructor(private http: HttpClient) { }

  findByUtenteId(utenteId: number): Observable<Indirizzo[]> {
    return this.http.get<Indirizzo[]>(`${this.apiUrl}/utente/${utenteId}`);
  }

  addIndirizzo(indirizzo: Indirizzo, idutente: number): Observable<any> {
    return this.http.post(`${this.apiUrl}`, { indirizzo, idutente });
  }
}
