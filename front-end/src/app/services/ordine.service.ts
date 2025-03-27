// In services/ordine.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Ordine } from '../models/ordine';
import { DettagliOrdini } from '../models/dettagli-ordini';

@Injectable({
  providedIn: 'root'
})
export class OrdineService {
  private apiUrl = 'http://localhost:8080/api/ordini';

  // Stati ordine
  private readonly STATO_CONFERMATO = 'Confermato';
  private readonly STATO_IN_PREPARAZIONE = 'In Preparazione';
  private readonly STATO_SPEDITO = 'Spedito';
  private readonly STATO_CONSEGNATO = 'Consegnato';

  constructor(private http: HttpClient) { }

  // Ottieni gli ordini dell'utente con stato calcolato
  getUserOrders(userId: number): Observable<Ordine[]> {
    return this.http.get<Ordine[]>(`${this.apiUrl}/utente/${userId}`)
      .pipe(
        map(ordini => {
          return ordini.map(ordine => this.calcolaStatoOrdine(ordine));
        })
      );
  }

  // Ottieni dettagli di un ordine specifico con stato calcolato
  getOrderDetails(orderId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${orderId}`)
      .pipe(
        map((response: any) => {
          if (response.ordine) {
            response.ordine = this.calcolaStatoOrdine(response.ordine);
          }
          return response;
        })
      );
  }

  // Altre funzioni esistenti...
  createOrder(userId: number, idMetodoPagamento: number, articoliCarrello: DettagliOrdini[]): Observable<any> {
    return this.http.post(this.apiUrl, {
      userId,
      idMetodoPagamento,
      articoliCarrello
    }, { responseType: 'json' });
  }

  // Funzione per calcolare lo stato dell'ordine in base alla data
  private calcolaStatoOrdine(ordine: Ordine): Ordine {
    if (!ordine.data) return ordine;

    try {
      // Calcola giorni lavorativi trascorsi
      const dataOrdine = new Date(ordine.data);
      const oggi = new Date();
      const giorniTrascorsi = this.calcolaGiorniLavorativi(dataOrdine, oggi);

      // Determina lo stato in base ai giorni trascorsi
      let statoCalcolato = this.STATO_CONFERMATO;
      let progressoPercentuale = 0;

      if (giorniTrascorsi >= 5) {
        statoCalcolato = this.STATO_CONSEGNATO;
        progressoPercentuale = 100;
      } else if (giorniTrascorsi >= 2) {
        statoCalcolato = this.STATO_SPEDITO;
        progressoPercentuale = 75;
      } else if (giorniTrascorsi >= 1) {
        statoCalcolato = this.STATO_IN_PREPARAZIONE;
        progressoPercentuale = 50;
      } else {
        statoCalcolato = this.STATO_CONFERMATO;
        progressoPercentuale = 25;
      }

      // Calcola data consegna prevista (5 giorni lavorativi dopo l'ordine)
      const dataConsegna = new Date(dataOrdine);
      dataConsegna.setDate(dataConsegna.getDate() + 7); // Approssimazione: +7 giorni calendario
      const dataConsegnaPrevista = dataConsegna.toLocaleDateString();

      // Aggiorna l'oggetto ordine con i valori calcolati
      return {
        ...ordine,
        statoCalcolato,
        progressoPercentuale,
        dataConsegnaPrevista
      };
    } catch (e) {
      console.error('Errore nel calcolo dello stato ordine:', e);
      return ordine;
    }
  }

  // Calcola giorni lavorativi tra due date
  private calcolaGiorniLavorativi(inizio: Date, fine: Date): number {
    if (inizio > fine) return 0;

    let giorni = 0;
    const dataCorrente = new Date(inizio);

    while (dataCorrente <= fine) {
      const giornoSettimana = dataCorrente.getDay();
      if (giornoSettimana !== 0 && giornoSettimana !== 6) { // 0 = domenica, 6 = sabato
        giorni++;
      }
      dataCorrente.setDate(dataCorrente.getDate() + 1);
    }

    return giorni;
  }
}
