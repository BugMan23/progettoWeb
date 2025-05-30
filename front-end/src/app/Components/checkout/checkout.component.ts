import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CarrelloService } from '../../services/carrello.service';
import { ProdottoService } from '../../services/prodotto.service';
import { IndirizzoService } from '../../services/indirizzo.service';
import { MetodoPagamentoService } from '../../services/metodo-pagamento.service';
import { OrdineService } from '../../services/ordine.service';
import { Indirizzo } from '../../models/indirizzo';
import { MetodoPagamento } from '../../models/metodo-pagamento';
import { Prodotto } from '../../models/prodotto';
import { DettagliOrdini } from '../../models/dettagli-ordini';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  // Utente
  userId: number | null = null;

  private paypalButtonRendered = false;

  // Passaggi checkout
  currentStep = 1;
  maxStep = 3;

  // Dati ordine
  prodottiCarrello: Prodotto[] = [];
  prodottiQuantita: Map<number, number> = new Map();
  prodottiTaglie: Map<number, string> = new Map();

  // Dati pagamento
  subtotale = 0;
  costoSpedizione = 0;
  scontoApplicato = 0;
  totale = 0;

  // Indirizzi e metodi di pagamento
  indirizzi: Indirizzo[] = [];
  metodiPagamento: MetodoPagamento[] = [];
  selectedIndirizzoId: number | null = null;
  selectedMetodoPagamentoId: number | null = null;

  // Nuovo indirizzo (form)
  nuovoIndirizzo: Indirizzo = {
    id: 0,
    nomeVia: '',
    civico: '',
    citta: '',
    cap: '',
    provincia: '',
    regione: '',
    idUtente: 0
  };

  // Nuovo metodo di pagamento (form)
  nuovoMetodoPagamento: MetodoPagamento = {
    id: 0,
    tipoPagamento: 'Carta di credito',
    titolare: '',
    tipoCarta: 'VISA',
    numeroCarta: '',
    dataScadenza: '',
    cvv: '',
    idUtente: 0
  };

  // Modalità form
  showNewAddressForm = false;
  showNewPaymentForm = false;

  // Stato interfaccia
  loading = true;
  processingOrder = false;
  error: string | null = null;
  successMessage: string | null = null;

  // Ordine completato
  ordineCompletato = false;
  ordineId: number | null = null;

  constructor(
    private carrelloService: CarrelloService,
    private prodottoService: ProdottoService,
    private indirizzoService: IndirizzoService,
    private metodoPagamentoService: MetodoPagamentoService,
    private ordineService: OrdineService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Verifica se l'utente è loggato
    const userIdStr = localStorage.getItem('userId');
    if (!userIdStr) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/checkout' } });
      return;
    }

    this.userId = parseInt(userIdStr);
    this.nuovoIndirizzo.idUtente = this.userId;
    this.nuovoMetodoPagamento.idUtente = this.userId;

    // Carica i dati dal carrello
    this.loadCheckoutData();

    if (this.selectedMetodoPagamentoId === -1) {
      this.initializePayPalButton();
    }
  }

  loadCheckoutData(): void {
    if (!this.userId) return;

    // Carica i dati salvati dal carrello
    const totaleStr = localStorage.getItem('checkout_totale');
    const subtotaleStr = localStorage.getItem('checkout_subtotale');
    const spedizioneStr = localStorage.getItem('checkout_spedizione');
    const scontoStr = localStorage.getItem('checkout_sconto');

    if (totaleStr) this.totale = parseFloat(totaleStr);
    if (subtotaleStr) this.subtotale = parseFloat(subtotaleStr);
    if (spedizioneStr) this.costoSpedizione = parseFloat(spedizioneStr);
    if (scontoStr) this.scontoApplicato = parseFloat(scontoStr);

    // Carica i prodotti nel carrello
    this.carrelloService.getUserCart(this.userId).subscribe({
      next: (prodotti) => {
        this.prodottiCarrello = prodotti;

        // Carica quantità e taglie per ogni prodotto
        this.carrelloService.getCartDetails(this.userId as number).subscribe({
          next: (dettagli) => {
            dettagli.forEach(item => {
              this.prodottiQuantita.set(item.idProdotto, item.quantita);
              this.prodottiTaglie.set(item.idProdotto, item.taglia || 'M');
            });

            // Carica indirizzi e metodi di pagamento
            this.loadIndirizzi();
            this.loadMetodiPagamento();

            this.loading = false;
          },
          error: (err) => {
            console.error('Errore nel caricamento dei dettagli del carrello', err);
            this.error = 'Impossibile caricare i dettagli del carrello';
            this.loading = false;
          }
        });
      },
      error: (err) => {
        console.error('Errore nel caricamento dei prodotti', err);
        this.error = 'Impossibile caricare i prodotti dal carrello';
        this.loading = false;
      }
    });
  }

  loadIndirizzi(): void {
    if (!this.userId) return;

    this.indirizzoService.findByUtenteId(this.userId).subscribe({
      next: (indirizzi) => {
        this.indirizzi = indirizzi;
        // Controlla se la risposta è vuota o ha dati
        console.log('Indirizzi caricati:', indirizzi);

        // Seleziona il primo indirizzo come default se presente
        if (indirizzi.length > 0) {
          this.selectedIndirizzoId = indirizzi[0].id;
        }
      },
      error: (err) => {
        console.error('Errore nel caricamento degli indirizzi', err);
      }
    });
  }

  loadMetodiPagamento(): void {
    if (!this.userId) return;

    this.metodoPagamentoService.getMetodiPagamentoUtente(this.userId).subscribe({
      next: (metodi) => {
        this.metodiPagamento = metodi;

        // Seleziona il primo metodo come default se presente
        if (metodi.length > 0) {
          this.selectedMetodoPagamentoId = metodi[0].id;
        }
      },
      error: (err) => {
        console.error('Errore nel caricamento dei metodi di pagamento', err);
      }
    });
  }

  // Gestione passi checkout
  nextStep(): void {
    if (this.currentStep < this.maxStep) {

      // Valida il passo corrente prima di procedere
      if (!this.validateCurrentStep()) {
        return;
      }

      this.currentStep++;
      window.scrollTo(0, 0);
    }
  }

  prevStep(): void {
    if (this.currentStep > 1) {
      this.currentStep--;
      window.scrollTo(0, 0);
    }
  }

  validateCurrentStep(): boolean {
    switch (this.currentStep) {
      case 1: // Riepilogo ordine
        return this.prodottiCarrello.length > 0;

      case 2: // Indirizzo di spedizione
        if (!this.selectedIndirizzoId) {
          this.error = 'Seleziona un indirizzo di spedizione';
          setTimeout(() => this.error = null, 3000);
          return false;
        }
        return true;

      case 3: // Metodo di pagamento
        if (!this.selectedMetodoPagamentoId) {
          this.error = 'Seleziona un metodo di pagamento';
          setTimeout(() => this.error = null, 3000);
          return false;
        }
        return true;
    }
    return true;
  }

  // Formatters per visualizzazione dati
  getQuantita(prodotto: Prodotto): number {
    return this.prodottiQuantita.get(prodotto.id) || 1;
  }

  getTaglia(prodotto: Prodotto): string {
    return this.prodottiTaglie.get(prodotto.id) || 'M';
  }

  calcolaSubtotale(prodotto: Prodotto): number {
    const quantita = this.getQuantita(prodotto);
    return prodotto.prezzo * quantita;
  }

  // Gestione nuovo indirizzo
  toggleNewAddressForm(): void {
    this.showNewAddressForm = !this.showNewAddressForm;

    // Reset form quando lo apriamo/chiudiamo
    if (this.showNewAddressForm) {
      this.nuovoIndirizzo = {
        id: 0,
        nomeVia: '',
        civico: '',
        citta: '',
        cap: '',
        provincia: '',
        regione: '',
        idUtente: this.userId || 0
      };
    }
  }

  saveNewAddress(): void {
    if (!this.userId) return;

    if (!this.nuovoIndirizzo.nomeVia || !this.nuovoIndirizzo.civico ||
      !this.nuovoIndirizzo.citta || !this.nuovoIndirizzo.cap ||
      !this.nuovoIndirizzo.provincia || !this.nuovoIndirizzo.regione) {
      this.error = 'Compila tutti i campi dell\'indirizzo';
      setTimeout(() => this.error = null, 3000);
      return;
    }

    this.nuovoIndirizzo.idUtente = this.userId;

    this.loading = true;

    this.indirizzoService.addIndirizzo(this.nuovoIndirizzo, this.userId).subscribe({
      next: (response) => {
        console.log('Risposta dal server:', response);
        this.loading = false;
        this.successMessage = 'Indirizzo aggiunto con successo';
        setTimeout(() => this.successMessage = null, 3000);

         this.showNewAddressForm = false;

        this.loadIndirizzi();
      },
      error: (err) => {
        console.error('Errore nell\'aggiunta dell\'indirizzo:', err);
        this.loading = false;

        this.successMessage = 'Indirizzo aggiunto con successo';
        setTimeout(() => this.successMessage = null, 3000);
        this.showNewAddressForm = false;
        this.loadIndirizzi();
      }
    });
  }

  // Gestione nuovo metodo di pagamento
  toggleNewPaymentForm(): void {
    this.showNewPaymentForm = !this.showNewPaymentForm;

    // Reset form quando lo apriamo/chiudiamo
    if (this.showNewPaymentForm) {
      this.nuovoMetodoPagamento = {
        id: 0,
        tipoPagamento: 'Carta di credito',
        titolare: '',
        tipoCarta: 'VISA',
        numeroCarta: '',
        dataScadenza: '',
        cvv: '',
        idUtente: this.userId || 0
      };
    }
  }

  saveNewPayment(): void {
    if (!this.userId) {
      console.error('ID utente non disponibile');
      this.error = 'ID utente non disponibile';
      setTimeout(() => this.error = null, 3000);
      return;
    }

    console.log('Checkout component: Tentativo di salvare nuovo metodo di pagamento');

    if (!this.nuovoMetodoPagamento.titolare ||
      !this.nuovoMetodoPagamento.numeroCarta ||
      !this.nuovoMetodoPagamento.dataScadenza ||
      !this.nuovoMetodoPagamento.cvv) {
      this.error = 'Compilare tutti i campi del metodo di pagamento';
      setTimeout(() => this.error = null, 3000);
      return;
    }

     const cleanedCardNumber = this.nuovoMetodoPagamento.numeroCarta.replace(/\D/g, '');
    if (cleanedCardNumber.length !== 16) {
      this.error = 'Il numero della carta deve contenere 16 cifre';
      setTimeout(() => this.error = null, 3000);
      return;
    }

    let formattedExpiryDate = this.nuovoMetodoPagamento.dataScadenza;
    formattedExpiryDate = formattedExpiryDate.replace(/[^0-9/]/g, '');
    if (!formattedExpiryDate.includes('/')) {
      if (formattedExpiryDate.length === 4) {
        formattedExpiryDate = formattedExpiryDate.substring(0, 2) + '/' + formattedExpiryDate.substring(2);
      }
    }

    const metodoPagamentoToSave = {
      ...this.nuovoMetodoPagamento,
      numeroCarta: cleanedCardNumber,
      dataScadenza: formattedExpiryDate,
      idUtente: this.userId
    };

    console.log('Checkout component: Invio dati metodo di pagamento:', metodoPagamentoToSave);

    this.metodoPagamentoService.salvaMetodoPagamento(metodoPagamentoToSave, this.userId).subscribe({
      next: (response) => {
        console.log('Checkout component: Risposta dal server:', response);
        this.successMessage = 'Metodo di pagamento aggiunto con successo';
        setTimeout(() => this.successMessage = null, 3000);
        this.loadMetodiPagamento();
        this.showNewPaymentForm = false;
      },
      error: (err) => {
        if (err.status === 200) {
          console.log('Risposta interpretata come successo:', err);
          this.successMessage = 'Metodo di pagamento aggiunto con successo';
          setTimeout(() => this.successMessage = null, 3000);
          this.loadMetodiPagamento();
          this.showNewPaymentForm = false;
        } else {
          console.error('Checkout component: Errore nell\'aggiunta del metodo di pagamento:', err);
          this.error = `Impossibile aggiungere il metodo di pagamento: ${err.error || 'Errore sconosciuto'}`;
          setTimeout(() => this.error = null, 3000);
        }
      }
    });
  }

  completeOrder(): void {
    if (!this.userId || !this.selectedIndirizzoId || !this.selectedMetodoPagamentoId) {
      this.error = 'Dati mancanti per completare l\'ordine';
      setTimeout(() => this.error = null, 3000);
      return;
    }

    this.processingOrder = true;

    const articoliCarrello = this.prodottiCarrello.map(prodotto => {
      return {
        idProdotto: prodotto.id,
        quantita: this.getQuantita(prodotto),
        id: 0,
        idOrdine: 0
      };
    });

    this.ordineService.createOrder(
      this.userId,
      this.selectedMetodoPagamentoId,
      articoliCarrello
    ).subscribe({
      next: (response) => {
        this.processingOrder = false;
        this.ordineCompletato = true;
        this.ordineId = response.id || null;

        // Svuota il carrello
        this.carrelloService.clearCart(this.userId!).subscribe({
          next: () => {
            // Notifica che il carrello è cambiato
            this.carrelloService.cartChanged.next();

            // Rimuovi dati checkout dal localStorage
            localStorage.removeItem('checkout_totale');
            localStorage.removeItem('checkout_subtotale');
            localStorage.removeItem('checkout_spedizione');
            localStorage.removeItem('checkout_sconto');
          },
          error: (err) => {
            console.error('Errore nello svuotamento del carrello', err);
          }
        });
      },
      error: (err) => {
        this.processingOrder = false;
        console.error('Errore nella creazione dell\'ordine', err);
        this.error = 'Impossibile completare l\'ordine. Riprova più tardi.';
        setTimeout(() => this.error = null, 5000);
      }
    });
  }



  // Navigazione
  backToCart(): void {
    this.router.navigate(['/carrello']);
  }

  backToShopping(): void {
    this.router.navigate(['/catalogo']);
  }

  goToOrderDetails(): void {
    if (this.ordineId) {
      this.router.navigate(['/ordini', this.ordineId]);
    } else {
      this.router.navigate(['/profilo/ordini']);
    }
  }

  selectPayPal(): void {
    this.selectedMetodoPagamentoId = -1;
    this.paypalButtonRendered = false; // forza un nuovo render se serve
    this.initializePayPalButton();
  }


  initializePayPalButton(): void {
    if (this.paypalButtonRendered) return;

    if ((window as any).paypal && this.currentStep === this.maxStep && this.selectedMetodoPagamentoId === -1) {
      setTimeout(() => {
        (window as any).paypal.Buttons({
          createOrder: (data: any, actions: any) => {
            return actions.order.create({
              purchase_units: [{
                amount: {
                  value: this.totale.toFixed(2)
                }
              }]
            });
          },
          onApprove: (data: any, actions: any) => {
            return actions.order.capture().then((details: any) => {
              console.log('Transazione completata da:', details.payer.name.given_name);
              this.completeOrder();
            });
          },
          onError: (err: any) => {
            console.error('Errore PayPal:', err);
            this.error = 'Errore durante il pagamento con PayPal.';
            setTimeout(() => this.error = null, 3000);
          }
        }).render('#paypal-button-container');

        this.paypalButtonRendered = true; // blocca ulteriori render
      }, 0);
    }
  }



}
