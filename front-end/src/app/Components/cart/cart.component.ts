import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CarrelloService } from '../../services/carrello.service';
import { ProdottoService } from '../../services/prodotto.service';
import { DisponibilitaService } from '../../services/disponibilita.service';
import { Prodotto } from '../../Models/prodotto';
import { Disponibilita } from '../../Models/disponibilita';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  // Dati del carrello
  prodottiCarrello: Prodotto[] = [];
  itemsQuantita: Map<number, number> = new Map(); // Mappa idProdotto -> quantità
  itemsTaglie: Map<number, string> = new Map(); // Mappa idProdotto -> taglia
  disponibilitaProdotti: Map<number, Disponibilita[]> = new Map(); // Mappa idProdotto -> disponibilità

  // Stato dell'interfaccia
  loading = true;
  error: string | null = null;
  successMessage: string | null = null;
  totale = 0;
  userId: number | null = null;
  processingCheckout = false;

  // Opzioni aggiuntive
  costoSpedizione = 4.99;
  spedizioneGratuita = 50; // Soglia per spedizione gratuita
  scontoApplicato = 0;
  codiceScontoInput = '';
  scontoValido = false;

  constructor(
    private carrelloService: CarrelloService,
    private prodottoService: ProdottoService,
    private disponibilitaService: DisponibilitaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Verifica se l'utente è loggato
    const userIdStr = localStorage.getItem('userId');
    if (!userIdStr) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/carrello' } });
      return;
    }

    this.userId = parseInt(userIdStr);
    this.loadCarrello();
  }

  // Carica i dati del carrello dal server
  loadCarrello(): void {
    if (!this.userId) return;

    this.carrelloService.getUserCart(this.userId).subscribe({
      next: (prodotti) => {
        this.prodottiCarrello = prodotti;

        // Carica la quantità per ogni prodotto dal server
        // Nota: questo dipende da come è strutturata la tua API
        this.carrelloService.getCartDetails(this.userId as number).subscribe({
          next: (dettagli) => {
            dettagli.forEach(item => {
              this.itemsQuantita.set(item.idProdotto, parseInt(item.quantita));
              this.itemsTaglie.set(item.idProdotto, item.taglia || 'M');

              // Carica la disponibilità per ogni prodotto
              this.caricaDisponibilita(item.idProdotto);
            });

            this.calcolaTotale();
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
        console.error('Errore nel caricamento del carrello', err);
        this.error = 'Impossibile caricare il carrello';
        this.loading = false;
      }
    });
  }

  // Carica la disponibilità di un prodotto
  caricaDisponibilita(idProdotto: number): void {
    this.disponibilitaService.getDisponibilitaProdotto(idProdotto).subscribe({
      next: (disponibilita) => {
        this.disponibilitaProdotti.set(idProdotto, disponibilita);
      },
      error: (err) => {
        console.error(`Errore nel caricamento della disponibilità per il prodotto ${idProdotto}`, err);
      }
    });
  }

  // Calcola il totale del carrello
  calcolaTotale(): void {
    let subtotale = 0;

    this.prodottiCarrello.forEach(prodotto => {
      const quantita = this.itemsQuantita.get(prodotto.id) || 1;
      subtotale += prodotto.prezzo * quantita;
    });

    // Applica lo sconto se valido
    if (this.scontoValido) {
      this.scontoApplicato = Math.round(subtotale * 0.1); // 10% di sconto
      subtotale -= this.scontoApplicato;
    } else {
      this.scontoApplicato = 0;
    }

    // Calcola il costo di spedizione
    this.costoSpedizione = subtotale >= this.spedizioneGratuita ? 0 : 4.99;

    // Calcola il totale finale
    this.totale = subtotale + this.costoSpedizione;
  }

  // Aggiorna la quantità di un prodotto
  updateQuantita(prodotto: Prodotto, nuovaQuantita: number): void {
    if (nuovaQuantita < 1) return;

    // Verifica la disponibilità
    const taglia = this.itemsTaglie.get(prodotto.id) || 'M';
    const disponibilitaProdotto = this.disponibilitaProdotti.get(prodotto.id);
    if (disponibilitaProdotto) {
      const disponibilitaTaglia = disponibilitaProdotto.find(d => d.taglia === taglia);
      if (disponibilitaTaglia && disponibilitaTaglia.quantita < nuovaQuantita) {
        this.error = `Disponibilità massima per taglia ${taglia}: ${disponibilitaTaglia.quantita}`;
        setTimeout(() => this.error = null, 3000);
        return;
      }
    }

    // Salva la vecchia quantità
    const vecchiaQuantita = this.itemsQuantita.get(prodotto.id) || 1;

    // Aggiorna localmente
    this.itemsQuantita.set(prodotto.id, nuovaQuantita);

    // Aggiorna sul server
    if (this.userId) {
      this.carrelloService.updateCartItem(this.userId, prodotto.id, nuovaQuantita, taglia).subscribe({
        next: () => {
          this.calcolaTotale();
          this.successMessage = 'Quantità aggiornata';
          setTimeout(() => this.successMessage = null, 2000);
          this.carrelloService.cartChanged.next();
        },
        error: (err) => {
          // In caso di errore, ripristina la vecchia quantità
          this.itemsQuantita.set(prodotto.id, vecchiaQuantita);
          this.calcolaTotale();
          this.error = 'Impossibile aggiornare la quantità';
          setTimeout(() => this.error = null, 3000);
          console.error('Errore nell\'aggiornamento della quantità', err);
        }
      });
    }
  }

  // Aggiorna la taglia di un prodotto
  updateTaglia(prodotto: Prodotto, nuovaTaglia: string): void {
    // Salva la vecchia taglia
    const vecchiaTaglia = this.itemsTaglie.get(prodotto.id) || 'M';
    if (vecchiaTaglia === nuovaTaglia) return;

    // Verifica la disponibilità
    const quantita = this.itemsQuantita.get(prodotto.id) || 1;
    const disponibilitaProdotto = this.disponibilitaProdotti.get(prodotto.id);
    if (disponibilitaProdotto) {
      const disponibilitaTaglia = disponibilitaProdotto.find(d => d.taglia === nuovaTaglia);
      if (!disponibilitaTaglia || disponibilitaTaglia.quantita < quantita) {
        this.error = `Disponibilità insufficiente per taglia ${nuovaTaglia}`;
        setTimeout(() => this.error = null, 3000);
        return;
      }
    }

    // Aggiorna localmente
    this.itemsTaglie.set(prodotto.id, nuovaTaglia);

    // Aggiorna sul server
    if (this.userId) {
      this.carrelloService.updateCartItemTaglia(this.userId, prodotto.id, nuovaTaglia).subscribe({
        next: () => {
          this.successMessage = 'Taglia aggiornata';
          setTimeout(() => this.successMessage = null, 2000);
        },
        error: (err) => {
          // In caso di errore, ripristina la vecchia taglia
          this.itemsTaglie.set(prodotto.id, vecchiaTaglia);
          this.error = 'Impossibile aggiornare la taglia';
          setTimeout(() => this.error = null, 3000);
          console.error('Errore nell\'aggiornamento della taglia', err);
        }
      });
    }
  }

  // Restituisce la quantità di un prodotto
  getQuantita(prodotto: Prodotto): number {
    return this.itemsQuantita.get(prodotto.id) || 1;
  }

  // Restituisce la taglia di un prodotto
  getTaglia(prodotto: Prodotto): string {
    return this.itemsTaglie.get(prodotto.id) || 'M';
  }

  // Restituisce le taglie disponibili per un prodotto
  getTaglieDisponibili(prodotto: Prodotto): string[] {
    const disponibilita = this.disponibilitaProdotti.get(prodotto.id);
    if (!disponibilita) return ['S', 'M', 'L']; // Default

    return disponibilita
      .filter(d => d.quantita > 0)
      .map(d => d.taglia);
  }

  // Rimuove un prodotto dal carrello
  rimuoviProdotto(prodotto: Prodotto): void {
    if (!this.userId) return;

    this.carrelloService.removeFromCart(this.userId, prodotto.id).subscribe({
      next: () => {
        this.prodottiCarrello = this.prodottiCarrello.filter(p => p.id !== prodotto.id);
        this.itemsQuantita.delete(prodotto.id);
        this.itemsTaglie.delete(prodotto.id);
        this.disponibilitaProdotti.delete(prodotto.id);
        this.calcolaTotale();
        this.successMessage = 'Prodotto rimosso dal carrello';
        setTimeout(() => this.successMessage = null, 2000);
        this.carrelloService.cartChanged.next();
      },
      error: (err) => {
        this.error = 'Impossibile rimuovere il prodotto dal carrello';
        setTimeout(() => this.error = null, 3000);
        console.error('Errore nella rimozione del prodotto', err);
      }
    });
  }

  // Svuota il carrello
  svuotaCarrello(): void {
    if (!this.userId || this.prodottiCarrello.length === 0) return;

    this.carrelloService.clearCart(this.userId).subscribe({
      next: () => {
        this.prodottiCarrello = [];
        this.itemsQuantita.clear();
        this.itemsTaglie.clear();
        this.disponibilitaProdotti.clear();
        this.calcolaTotale();
        this.successMessage = 'Carrello svuotato';
        setTimeout(() => this.successMessage = null, 2000);
        this.carrelloService.cartChanged.next();
      },
      error: (err) => {
        this.error = 'Impossibile svuotare il carrello';
        setTimeout(() => this.error = null, 3000);
        console.error('Errore nello svuotamento del carrello', err);
      }
    });
  }

  // Applica un codice sconto
  applicaSconto(): void {
    const codiceValido = this.codiceScontoInput.toUpperCase() === 'SPORT10';

    if (codiceValido) {
      this.scontoValido = true;
      this.successMessage = 'Sconto del 10% applicato!';
      setTimeout(() => this.successMessage = null, 3000);
    } else {
      this.scontoValido = false;
      this.error = 'Codice sconto non valido';
      setTimeout(() => this.error = null, 3000);
    }

    this.calcolaTotale();
  }

  // Calcola il subtotale di un prodotto
  calcolaSubtotale(prodotto: Prodotto): number {
    const quantita = this.itemsQuantita.get(prodotto.id) || 1;
    return prodotto.prezzo * quantita;
  }

  // Procedi al checkout
  procediCheckout(): void {
    if (this.prodottiCarrello.length === 0) {
      this.error = 'Il carrello è vuoto';
      setTimeout(() => this.error = null, 3000);
      return;
    }

    // Salva i dati per il checkout
    localStorage.setItem('checkout_totale', this.totale.toString());
    localStorage.setItem('checkout_subtotale', (this.totale - this.costoSpedizione).toString());
    localStorage.setItem('checkout_spedizione', this.costoSpedizione.toString());
    localStorage.setItem('checkout_sconto', this.scontoApplicato.toString());

    this.processingCheckout = true;

    // Simula un breve caricamento
    setTimeout(() => {
      this.processingCheckout = false;
      this.router.navigate(['/checkout']);
    }, 800);
  }

  // Torna al catalogo
  continuaShopping(): void {
    this.router.navigate(['/catalogo']);
  }
}
