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

  /**
   * Ottiene tutti gli indirizzi per un utente
   */
  findByUtenteId(utenteId: number): Observable<Indirizzo[]> {
    return this.http.get<Indirizzo[]>(`${this.apiUrl}/utente/${utenteId}`);
  }

  /**
   * Aggiunge un nuovo indirizzo per un utente
   */
  addIndirizzo(indirizzo: Indirizzo, idUtente: number): Observable<any> {
    const indirizzoConUtente = { ...indirizzo, idUtente };
    console.log('Invio indirizzo al server:', indirizzoConUtente);
    return this.http.post(this.apiUrl, indirizzoConUtente);
  }

  /**
   * Aggiorna un indirizzo esistente
   */
  updateIndirizzo(indirizzo: Indirizzo): Observable<any> {
    return this.http.put(`${this.apiUrl}/${indirizzo.id}`, indirizzo);
  }

  /**
   * Elimina un indirizzo
   */
  deleteIndirizzo(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
