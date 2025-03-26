import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { OrdineService } from '../../../services/ordine.service';
import { ProdottoService } from '../../../services/prodotto.service';
import { MetodoPagamentoService } from '../../../services/metodo-pagamento.service';
import { Ordine } from '../../../Models/ordine';
import { DettagliOrdini } from '../../../Models/dettagli-ordini';
import { Prodotto } from '../../../Models/prodotto';

// Interfaccia per i dettagli ordine con prodotti
interface OrderDetailsResponse {
  ordine: Ordine;
  dettagli: DettagliOrdineInfo[];
}

// Interfaccia per i dettagli degli ordini
interface DettagliOrdineInfo {
  id: number;
  idOrdine: number;
  idProdotto: number;
  nomeProdotto?: string;
  prezzoProdotto?: number;
  quantita: number;
}

@Component({
  selector: 'app-ordini',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './ordini.component.html',
  styleUrls: ['./ordini.component.css']
})
export class OrdiniComponent implements OnInit {
  // Input dal componente padre (ProfiloComponent)
  @Input() inProfileView: boolean = true;

  // Stato dell'interfaccia
  loading = true;
  error: string | null = null;

  // Vista dettaglio o vista lista
  isDetailView: boolean = false;

  // Dati degli ordini
  orderId: number | null = null;
  ordini: Ordine[] = [];
  ordineSelezionato: Ordine | null = null;

  // Dettagli ordine per vista dettaglio
  dettagliOrdine: DettagliOrdini[] = [];
  prodotti: Map<number, Prodotto> = new Map();

  // Proprietà mancanti
  orderDetails: OrderDetailsResponse | null = null;
  orderTotal: number = 0;

  // Info metodo di pagamento
  metodoPagamento: any = null;

  // ID utente corrente
  userId: number | null = null;

  constructor(
    private ordineService: OrdineService,
    private prodottoService: ProdottoService,
    private metodoPagamentoService: MetodoPagamentoService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Ottieni l'ID utente
    const userIdStr = localStorage.getItem('userId');
    if (!userIdStr) {
      this.error = 'Utente non autenticato';
      this.loading = false;
      return;
    }

    this.userId = parseInt(userIdStr);

    // Controlla se siamo in modalità dettaglio o lista
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');

      if (idParam) {
        // Vista dettaglio ordine
        this.orderId = parseInt(idParam);
        this.isDetailView = true;
        this.inProfileView = false;
        this.loadOrderInfo(this.orderId);
      } else {
        // Vista lista ordini
        this.isDetailView = false;
        this.loadAllOrders();
      }
    });
  }

  // Metodo per caricare gli ordini dell'utente
  loadAllOrders(): void {
    if (!this.userId) return;

    this.ordineService.getUserOrders(this.userId).subscribe({
      next: (data) => {
        this.ordini = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento degli ordini', err);
        this.error = 'Impossibile caricare gli ordini';
        this.loading = false;
      }
    });
  }

  // Metodo unificato per caricare i dettagli dell'ordine
  loadOrderInfo(orderId: number): void {
    this.ordineService.getOrderDetails(orderId).subscribe({
      next: (response: any) => {
        console.log('Dettagli ordine ricevuti:', response);
        this.orderDetails = response;
        this.ordineSelezionato = response.ordine;
        this.dettagliOrdine = response.dettagli;

        // Carica il metodo di pagamento se l'ordine contiene l'ID
        if (this.ordineSelezionato && this.ordineSelezionato.idMetodoPagamento) {
          this.loadPaymentMethod(this.ordineSelezionato.idMetodoPagamento);
        }

        // Carica i dettagli dei prodotti
        this.loadProductDetails();

        // Calcola il totale
        this.calculateTotals();

        this.loading = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento dei dettagli ordine', err);
        this.error = 'Impossibile caricare i dettagli dell\'ordine';
        this.loading = false;
      }
    });
  }

  // Calcola il totale dell'ordine
  calculateTotals(): void {
    this.orderTotal = 0;

    if (this.dettagliOrdine.length > 0) {
      this.dettagliOrdine.forEach(item => {
        const prodotto = this.getProdotto(item.idProdotto);
        if (prodotto) {
          const prezzo = prodotto.prezzo || 0;
          const quantita = item.quantita || 0;
          this.orderTotal += prezzo * quantita;
        }
      });
    } else if (this.orderDetails && this.orderDetails.dettagli) {
      this.orderDetails.dettagli.forEach(item => {
        const prezzo = Number(item.prezzoProdotto) || 0;
        const quantita = Number(item.quantita) || 0;
        const subtotale = prezzo * quantita;

        if (!isNaN(subtotale)) {
          this.orderTotal += subtotale;
        }
      });
    }
  }

  // Carica i dettagli dei prodotti
  loadProductDetails(): void {
    // Array di promesse per caricare tutti i prodotti
    const productPromises = this.dettagliOrdine.map(dettaglio => {
      return new Promise<void>((resolve, reject) => {
        this.prodottoService.getProductById(dettaglio.idProdotto).subscribe({
          next: (product) => {
            if (product) {
              this.prodotti.set(dettaglio.idProdotto, product);
            }
            resolve();
          },
          error: (err) => {
            console.error(`Errore caricamento prodotto ${dettaglio.idProdotto}`, err);
            resolve(); // Risolvi comunque per non bloccare gli altri prodotti
          }
        });
      });
    });

    // Quando tutti i prodotti sono caricati
    Promise.all(productPromises)
      .then(() => {
        this.calculateTotals();
      })
      .catch(err => {
        console.error('Errore nel caricamento dei prodotti', err);
      });
  }

  // Carica i dettagli del metodo di pagamento
  loadPaymentMethod(paymentId: number): void {
    if (!paymentId) {
      console.warn('ID metodo di pagamento non valido');
      this.metodoPagamento = { titolare: 'Non disponibile', tipoCarta: 'Non disponibile' };
      return;
    }

    this.metodoPagamentoService.getMetodoPagamentoByID(paymentId).subscribe({
      next: (data) => {
        console.log('Metodo di pagamento caricato:', data);
        this.metodoPagamento = data;
      },
      error: (err) => {
        console.error('Errore nel caricamento del metodo di pagamento', err);
        // Imposta un valore di fallback per una migliore esperienza utente
        this.metodoPagamento = { titolare: 'Non disponibile', tipoCarta: 'Non disponibile' };
      }
    });
  }

  // Metodi helper per vista dettaglio
  getProdotto(idProdotto: number): Prodotto | undefined {
    return this.prodotti.get(idProdotto);
  }

  calcolaTotaleRiga(dettaglio: DettagliOrdini): number {
    const prodotto = this.getProdotto(dettaglio.idProdotto);
    if (prodotto) {
      return prodotto.prezzo * dettaglio.quantita;
    }
    return 0;
  }

  getStatusClass(stato: string): string {
    switch (stato) {
      case 'IN_ELABORAZIONE': return 'bg-primary';
      case 'SPEDITO': return 'bg-warning text-dark';
      case 'CONSEGNATO': return 'bg-success';
      case 'ANNULLATO': return 'bg-danger';
      default: return 'bg-secondary';
    }
  }

  // Navigazione
  viewOrderDetails(orderId: number): void {
    this.router.navigate(['/ordini', orderId]);
  }

  backToOrders(): void {
    this.router.navigate(['/profilo'], { queryParams: { tab: 'orders' } });
  }

  getProductPrice(idProdotto: number): string {
    const prodotto = this.getProdotto(idProdotto);
    if (prodotto && prodotto.prezzo !== undefined) {
      return prodotto.prezzo.toFixed(2);
    }
    return '0.00';
  }
}
