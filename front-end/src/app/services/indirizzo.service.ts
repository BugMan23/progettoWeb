import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Indirizzo } from '../models/indirizzo';

@Injectable({
  providedIn: 'root'
})
export class IndirizzoService {
  private apiUrl = 'http://localhost:8080/api/indirizzi';

  constructor(private http: HttpClient) { }

  findByUtenteId(utenteId: number): Observable<Indirizzo[]> {
    console.log('Frontend: Richiesta indirizzi per utente ID:', utenteId);
    return this.http.get<Indirizzo[]>(`${this.apiUrl}/utente/${utenteId}`);
  }

  addIndirizzo(indirizzo: Indirizzo, idUtente: number): Observable<any> {
    console.log('Frontend: Invio nuovo indirizzo per utente ID:', idUtente);
    console.log('Frontend: Dati indirizzo:', indirizzo);
    const indirizzoConUtente = { ...indirizzo, idUtente };
    return this.http.post(this.apiUrl, indirizzoConUtente);
  }
}
