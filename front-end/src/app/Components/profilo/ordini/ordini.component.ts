import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { OrdineService} from '../../../services/ordine.service';
import { ProdottoService} from '../../../services/prodotto.service';
import { MetodoPagamentoService} from '../../../services/metodo-pagamento.service';
import { Ordine} from '../../../models/ordine';
import { DettagliOrdini} from '../../../models/dettagli-ordini';
import { Prodotto} from '../../../models/prodotto';
import { MetodoPagamento} from '../../../models/metodo-pagamento';

@Component({
  selector: 'app-ordini',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './ordini.component.html',
  styleUrls: ['./ordini.component.css']
})
export class OrdiniComponent implements OnInit {
  // Dati dell'utente
  userId: number | null = null;

  // Lista degli ordini per la visualizzazione nel profilo
  ordini: Ordine[] = [];

  // Dettaglio di un singolo ordine
  ordineSelezionato: Ordine | null = null;
  dettagliOrdine: DettagliOrdini[] = [];
  prodottiOrdine: Map<number, Prodotto> = new Map();
  metodoPagamento: MetodoPagamento | null = null;

  // Stato dell'interfaccia
  caricamento = true;
  errore: string | null = null;
  visualizzaDettaglio = false;

  constructor(
    private ordineService: OrdineService,
    private prodottoService: ProdottoService,
    private metodoPagamentoService: MetodoPagamentoService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Ottieni l'ID utente dal localStorage
    const userIdString = localStorage.getItem('userId');
    this.userId = userIdString ? parseInt(userIdString) : null;

    if (!this.userId) {
      this.router.navigate(['/login']);
      return;
    }

    // Controlla se stiamo visualizzando un ordine specifico
    this.route.paramMap.subscribe(params => {
      const ordineId = params.get('id');

      if (ordineId) {
        // Visualizza dettaglio singolo ordine
        this.caricaDettaglioOrdine(parseInt(ordineId));
      } else {
        // Visualizza lista ordini
        this.caricaOrdiniUtente();
      }
    });
  }

  // Carica la lista degli ordini dell'utente
  caricaOrdiniUtente(): void {
    if (!this.userId) return;

    this.caricamento = true;
    this.ordineService.getUserOrders(this.userId).subscribe({
      next: (ordini) => {
        this.ordini = ordini || [];
        this.caricamento = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento degli ordini', err);
        this.errore = 'Impossibile caricare gli ordini. Riprova più tardi.';
        this.caricamento = false;
      }
    });
  }

  // Carica il dettaglio di un singolo ordine
  caricaDettaglioOrdine(ordineId: number): void {
    this.caricamento = true;
    this.visualizzaDettaglio = true;

    // Carica l'ordine
    this.ordineService.getOrderDetails(ordineId).subscribe({
      next: (response: any) => {
        if (response && response.ordine) {
          this.ordineSelezionato = response.ordine;
          this.dettagliOrdine = response.dettagli || [];

          // Carica i dati del metodo di pagamento
          if (this.ordineSelezionato && this.ordineSelezionato.idMetodoPagamento) {
            this.caricaMetodoPagamento(this.ordineSelezionato.idMetodoPagamento);
          }

          // Carica i dati dei prodotti
          this.caricaProdottiOrdine();
        } else {
          this.errore = 'Dati dell\'ordine non disponibili';
        }
        this.caricamento = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento del dettaglio ordine', err);
        this.errore = 'Impossibile caricare i dettagli dell\'ordine. Riprova più tardi.';
        this.caricamento = false;
      }
    });
  }

  // Carica i dati del metodo di pagamento
  caricaMetodoPagamento(idMetodoPagamento: number): void {
    this.metodoPagamentoService.getMetodoPagamentoByID(idMetodoPagamento).subscribe({
      next: (metodo) => {
        this.metodoPagamento = metodo;
      },
      error: (err) => {
        console.error('Errore nel caricamento del metodo di pagamento', err);
        // Non blocchiamo la visualizzazione dell'ordine per questo errore
      }
    });
  }

  // Carica i dati dei prodotti nell'ordine
  caricaProdottiOrdine(): void {
    if (!this.dettagliOrdine || this.dettagliOrdine.length === 0) return;

    // Per ogni prodotto nell'ordine, carica i suoi dati
    const promises = this.dettagliOrdine.map(dettaglio => {
      return new Promise<void>((resolve) => {
        this.prodottoService.getProductById(dettaglio.idProdotto).subscribe({
          next: (prodotto) => {
            this.prodottiOrdine.set(dettaglio.idProdotto, prodotto);
            resolve();
          },
          error: () => {
            // Se fallisce il caricamento di un prodotto, continuiamo comunque
            resolve();
          }
        });
      });
    });

    // Quando tutti i prodotti sono stati caricati
    Promise.all(promises).then(() => {
      // Rendering completato
    });
  }

  // Ottieni un prodotto dal Map usando l'ID
  getProdotto(idProdotto: number): Prodotto | undefined {
    return this.prodottiOrdine.get(idProdotto);
  }

  // Calcola il subtotale di una linea dell'ordine
  calcolaSubtotale(dettaglio: DettagliOrdini): number {
    const prodotto = this.prodottiOrdine.get(dettaglio.idProdotto);
    if (!prodotto || !dettaglio || !dettaglio.quantita) return 0;

    return prodotto.prezzo * dettaglio.quantita;
  }

  // Formatta il numero della carta per mostrare solo gli ultimi 4 numeri
  mascheraNumeroCarta(numeroCarta: string | undefined): string {
    if (!numeroCarta) return '****';

    // Se la lunghezza è inferiore a 4, mostriamo asterischi
    if (numeroCarta.length <= 4) return '**** **** **** ' + numeroCarta;

    // Altrimenti mostriamo solo gli ultimi 4 numeri
    const ultimi4 = numeroCarta.slice(-4);
    return '**** **** **** ' + ultimi4;
  }

  formattaData(dataStr: string | undefined): string {
    if (!dataStr) return 'Data non disponibile';

    try {
      // Se il formato è SQL standard (YYYY-MM-DD)
      if (dataStr.includes('-')) {
        const data = new Date(dataStr);
        if (!isNaN(data.getTime())) {
          return data.toLocaleDateString('it-IT', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
          });
        }
      }

      // Se è nel formato dataoridne (probabile errore di battitura nel database)
      if (dataStr.includes('dataoridne')) {
        const valorePulito = dataStr.replace('dataoridne:', '').trim();
        const data = new Date(valorePulito);
        if (!isNaN(data.getTime())) {
          return data.toLocaleDateString('it-IT');
        }
      }

      // Se è nel formato italiano (DD/MM/YYYY)
      if (dataStr.includes('/')) {
        const parti = dataStr.split('/');
        if (parti.length === 3) {
          const data = new Date(parseInt(parti[2]), parseInt(parti[1]) - 1, parseInt(parti[0]));
          if (!isNaN(data.getTime())) {
            return data.toLocaleDateString('it-IT');
          }
        }
      }

      // Se è un timestamp numerico
      if (!isNaN(Number(dataStr))) {
        const data = new Date(Number(dataStr));
        if (!isNaN(data.getTime())) {
          return data.toLocaleDateString('it-IT');
        }
      }

      return dataStr; // Se non riusciamo a formattarla, mostriamo la stringa originale
    } catch (e) {
      console.error('Errore nella formattazione della data:', e);
      return 'Data non valida';
    }
  }

  // Torna alla lista degli ordini
  tornaAllaLista(): void {
    this.router.navigate(['/profilo/ordini']);
  }
}
