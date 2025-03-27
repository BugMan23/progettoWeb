import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { OrdineService} from '../../../services/ordine.service';
import { ProdottoService} from '../../../services/prodotto.service';
import { MetodoPagamentoService} from '../../../services/metodo-pagamento.service';
import { RecensioneService } from '../../../services/recensione.service';
import { Ordine} from '../../../models/ordine';
import { DettagliOrdini} from '../../../models/dettagli-ordini';
import { Prodotto} from '../../../models/prodotto';
import { MetodoPagamento} from '../../../models/metodo-pagamento';
import { Recensione } from '../../../models/recensione';


@Component({
  selector: 'app-ordini',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './ordini.component.html',
  styleUrls: ['./ordini.component.css']
})
export class OrdiniComponent implements OnInit {
  userId: number | null = null;
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

  filtroAttivo: string = 'tutti';
  ordiniNonFiltrati: Ordine[] = [];

  // Proprietà per le recensioni
  hasReviewed: { [prodottoId: number]: boolean } = {};
  reviewForm: FormGroup;
  selectedProductId: number | null = null;
  selectedProduct: Prodotto | null = null;
  hoverRating = 0;
  submittingReview = false;
  successMessage: string | null = null;

  constructor(
    private ordineService: OrdineService,
    private prodottoService: ProdottoService,
    private metodoPagamentoService: MetodoPagamentoService,
    private recensioneService: RecensioneService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {
    // Inizializza il form per la recensione
    this.reviewForm = this.fb.group({
      valutazione: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
      testo: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

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
        this.ordiniNonFiltrati = [...this.ordini];  // Salva anche la lista non filtrata
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
            // Controlla se l'utente ha già recensito questo prodotto
            this.checkIfReviewed(dettaglio.idProdotto);
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

  // Controlla se un prodotto è già stato recensito dall'utente
  checkIfReviewed(prodottoId: number): void {
    if (!this.userId) return;

    this.recensioneService.getUserProductReview(this.userId, prodottoId).subscribe({
      next: (data) => {
        this.hasReviewed[prodottoId] = data && data.length > 0;
      },
      error: (err) => {
        console.error(`Errore nel controllo delle recensioni per il prodotto ${prodottoId}`, err);
        // In caso di errore, assumiamo che l'utente non abbia recensito
        this.hasReviewed[prodottoId] = false;
      }
    });
  }

  showReviewForm(prodottoId: number): void {
    this.selectedProductId = prodottoId;
    this.selectedProduct = this.prodottiOrdine.get(prodottoId) || null;
    this.resetReviewForm();

    setTimeout(() => {
      const reviewForm = document.getElementById('reviewFormHeader');
      if (reviewForm) {
        reviewForm.scrollIntoView({ behavior: 'smooth' });
      }
    }, 100);
  }

// Aggiungi questo metodo per nascondere il form
  hideReviewForm(): void {
    this.selectedProductId = null;
    this.selectedProduct = null;
  }


  submitReview(): void {
    if (this.reviewForm.invalid || !this.userId || !this.selectedProductId) return;

    this.submittingReview = true;

    const newReview: Recensione = {
      id: 0,
      idProdotto: this.selectedProductId,
      idUtente: this.userId,
      valutazione: this.reviewForm.value.valutazione,
      testo: this.reviewForm.value.testo,
      data: new Date().toISOString().split('T')[0]
    };

    this.recensioneService.addReview(newReview).subscribe({
      next: (response: any) => {
        console.log('Recensione salvata con successo:', response);
        this.submittingReview = false;
        this.hasReviewed[this.selectedProductId!] = true;
        this.successMessage = 'Recensione pubblicata con successo';
        setTimeout(() => this.successMessage = null, 5000);

        // Nascondi il form
        this.hideReviewForm();
      },
      error: (err) => {
        console.error('Errore completo:', err);
        this.submittingReview = false;
        this.errore = `Errore nella pubblicazione della recensione: ${err.error?.message || err.message || 'Si è verificato un errore'}`;
        setTimeout(() => this.errore = null, 5000);
      }
    });
  }


  resetReviewForm(): void {
    this.reviewForm.reset({
      valutazione: 0,
      testo: ''
    });
    this.hoverRating = 0;
  }


  setRating(rating: number): void {
    this.reviewForm.patchValue({ valutazione: rating });
  }


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


// Determina se un punto di stato è attivo
  isStatoAttivo(stato: string): boolean {
    if (!this.ordineSelezionato) return false;

    const statoOrdine = this.ordineSelezionato.stato;

    switch (stato) {
      case 'CONFERMATO':
        return true; // Sempre attivo
      case 'IN_PREPARAZIONE':
        return statoOrdine === 'IN_PREPARAZIONE' || statoOrdine === 'SPEDITO' || statoOrdine === 'CONSEGNATO';
      case 'SPEDITO':
        return statoOrdine === 'SPEDITO' || statoOrdine === 'CONSEGNATO';
      case 'CONSEGNATO':
        return statoOrdine === 'CONSEGNATO';
      default:
        return false;
    }
  }

// Restituisce una descrizione leggibile dello stato
  getStatoDescrizione(stato: string): string {
    switch (stato) {
      case 'IN_ELABORAZIONE':
        return 'Ordine ricevuto';
      case 'IN_PREPARAZIONE':
        return 'In preparazione';
      case 'SPEDITO':
        return 'Spedito';
      case 'CONSEGNATO':
        return 'Consegnato';
      default:
        return stato;
    }
  }

// Calcola la data stimata di consegna
  calcolaDataConsegnaStimata(dataOrdine: string): string {
    if (!dataOrdine) return 'Data non disponibile';

    try {
      const data = new Date(dataOrdine);
      if (isNaN(data.getTime())) return 'Data non valida';

      // Aggiungi 5 giorni lavorativi (approssimati a 7 giorni di calendario)
      data.setDate(data.getDate() + 7);

      return data.toLocaleDateString('it-IT', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      });
    } catch (e) {
      return 'Data non calcolabile';
    }
  }

// Calcola la percentuale di progresso dell'ordine
  calcolaProgressoOrdine(): number {
    if (!this.ordineSelezionato) return 0;

    const statoOrdine = this.ordineSelezionato.stato;

    switch (statoOrdine) {
      case 'IN_ELABORAZIONE':
        return 25;
      case 'IN_PREPARAZIONE':
        return 50;
      case 'SPEDITO':
        return 75;
      case 'CONSEGNATO':
        return 100;
      default:
        return 0;
    }
  }

  applicaFiltro(filtro: string): void {
    this.filtroAttivo = filtro;

    // Salva la lista completa se non l'abbiamo già salvata
    if (this.ordiniNonFiltrati.length === 0 && this.ordini.length > 0) {
      this.ordiniNonFiltrati = [...this.ordini];
    }

    if (filtro === 'tutti') {
      this.ordini = this.ordiniNonFiltrati;
      return;
    }

    // Filtra gli ordini in base al criterio selezionato
    switch (filtro) {
      case 'ultimi30giorni':
        this.filtrarPerData(30);
        break;
      case 'ultimi6mesi':
        this.filtrarPerData(180);
        break;
      case 'inElaborazione':
        this.filtrarPerStato(['IN_ELABORAZIONE', 'IN_PREPARAZIONE', 'SPEDITO']);
        break;
      default:
        this.ordini = this.ordiniNonFiltrati;
    }
  }

// Filtra per data (giorni indietro)
  private filtrarPerData(giorni: number): void {
    const oggi = new Date();
    const dataLimite = new Date();
    dataLimite.setDate(oggi.getDate() - giorni);

    this.ordini = this.ordiniNonFiltrati.filter(ordine => {
      try {
        const dataOrdine = new Date(ordine.data);
        return dataOrdine >= dataLimite;
      } catch (e) {
        return false;
      }
    });
  }

// Filtra per stato
  private filtrarPerStato(stati: string[]): void {
    this.ordini = this.ordiniNonFiltrati.filter(ordine =>
      stati.includes(ordine.stato)
    );
  }
}
