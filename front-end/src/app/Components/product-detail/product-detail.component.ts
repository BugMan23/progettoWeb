import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ProdottoService } from '../../services/prodotto.service';
import { RecensioneService } from '../../services/recensione.service';
import { CarrelloService } from '../../services/carrello.service';
import { DisponibilitaService } from '../../services/disponibilita.service';
import { Prodotto } from '../../Models/prodotto';
import { Recensione } from '../../Models/recensione';
import { Disponibilita } from '../../Models/disponibilita';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class DettaglioProdottoComponent implements OnInit {
  prodotto: Prodotto | null = null;
  recensioni: Recensione[] = [];
  disponibilita: Disponibilita[] = [];
  loading = true;
  error: string | null = null;
  addingToCart = false;
  cartSuccess = false;
  cartError = false;
  isLoggedIn = false;

  // Form aggiungi al carrello
  selectedTaglia: string = '';
  selectedQuantita: number = 1;

  constructor(
    private route: ActivatedRoute,
    public router: Router, // Cambiato a public per consentirne l'uso nel template
    private prodottoService: ProdottoService,
    private recensioneService: RecensioneService,
    private carrelloService: CarrelloService,
    private disponibilitaService: DisponibilitaService
  ) { }

  ngOnInit(): void {
    // Verifica se l'utente è loggato
    this.checkAuthState();

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.loadProdotto(parseInt(id));
      } else {
        this.error = 'ID prodotto non valido';
        this.loading = false;
      }
    });
  }

  // Verifica lo stato di autenticazione
  checkAuthState(): void {
    const userId = localStorage.getItem('userId');
    this.isLoggedIn = !!userId;
  }

  loadProdotto(id: number): void {
    this.prodottoService.getProductById(id).subscribe({
      next: (data: any) => {
        console.log('Dati ricevuti:', data); // Log per debug
        // Controllo più difensivo sulla struttura dei dati
        if (data) {
          if (data.prodotto && data.recensioni) {
            // Se abbiamo la struttura nidificata
            this.prodotto = data.prodotto;
            this.recensioni = data.recensioni;
          } else if (data.id !== undefined) {
            // Se è direttamente un oggetto prodotto
            this.prodotto = data;
            // Carica recensioni separatamente se necessario
            this.loadRecensioni(id);
          } else {
            // Struttura dati non riconosciuta
            console.error('Struttura dati non valida:', data);
            this.error = 'Formato dati non valido dal server';
            this.loading = false;
            return;
          }

          this.loadDisponibilita(id);
        } else {
          this.error = 'Nessun dato ricevuto dal server';
          this.loading = false;
        }
      },
      error: (err) => {
        console.error('Errore caricamento prodotto', err);
        this.error = 'Impossibile caricare i dettagli del prodotto: ' + (err.error || err.message || 'Errore sconosciuto');
        this.loading = false;
      }
    });
  }

  loadRecensioni(prodottoId: number): void {
    this.recensioneService.getProductReviews(prodottoId).subscribe({
      next: (data) => {
        this.recensioni = data;
      },
      error: (err) => {
        console.error('Errore caricamento recensioni', err);
        // Continuiamo senza recensioni
        this.recensioni = [];
      }
    });
  }

  loadDisponibilita(prodottoId: number): void {
    this.disponibilitaService.getDisponibilitaProdotto(prodottoId).subscribe({
      next: (data) => {
        this.disponibilita = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Errore caricamento disponibilità', err);
        this.loading = false;
      }
    });
  }

  // Controlla se la taglia è disponibile nella quantità richiesta
  isTagliaDisponibile(taglia: string): boolean {
    const disponibilitaTaglia = this.disponibilita.find(d => d.taglia === taglia);
    return disponibilitaTaglia ? disponibilitaTaglia.quantita >= this.selectedQuantita : false;
  }

  // Aggiunge al carrello
  addToCart(): void {
    if (!this.prodotto) return;

    // Verifica se l'utente è autenticato
    const userId = localStorage.getItem('userId');
    if (!userId) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.url } });
      return;
    }

    // Assicurati di avere una taglia selezionata
    if (!this.selectedTaglia) {
      this.error = 'Seleziona una taglia prima di aggiungere al carrello';
      return;
    }

    this.addingToCart = true;
    this.cartSuccess = false;
    this.cartError = false;

    const request = {
      userId: parseInt(userId),
      productId: this.prodotto.id,
      quantity: this.selectedQuantita || 1,
      taglia: this.selectedTaglia
    };

    console.log('Request per aggiunta al carrello:', request); // Per debug

    this.carrelloService.addToCart(request).subscribe({
      next: () => {
        this.addingToCart = false;
        this.cartSuccess = true;
        this.carrelloService.cartChanged.next();
        setTimeout(() => this.cartSuccess = false, 3000);
      },
      error: (err) => {
        console.error('Errore aggiunta al carrello', err);
        this.addingToCart = false;
        this.cartError = true;
        setTimeout(() => this.cartError = false, 3000);
      }
    });
  }

  // Media recensioni
  get mediaRecensioni(): number {
    if (this.recensioni.length === 0) return 0;
    const somma = this.recensioni.reduce((acc, rec) => acc + rec.valutazione, 0);
    return somma / this.recensioni.length;
  }

  // Stella piena o vuota
  isStellaAttiva(index: number, valutazione: number): boolean {
    return index <= valutazione;
  }
}
