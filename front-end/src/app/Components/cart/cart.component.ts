import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CarrelloService } from '../../services/carrello.service';
import { Prodotto } from '../../Models/prodotto';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CarrelloComponent implements OnInit {
  prodottiCarrello: Prodotto[] = [];
  loading = true;
  error: string | null = null;
  totale = 0;
  userId: number | null = null;
  processingCheckout = false;

  constructor(
    private carrelloService: CarrelloService,
    private router: Router
  ) { }

  ngOnInit(): void {
    const userIdStr = localStorage.getItem('userId');
    if (!userIdStr) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/carrello' } });
      return;
    }

    this.userId = parseInt(userIdStr);
    this.loadCarrello();
  }

  loadCarrello(): void {
    if (!this.userId) return;

    this.carrelloService.getUserCart(this.userId).subscribe({
      next: (data) => {
        this.prodottiCarrello = data;
        this.calcolaTotale();
        this.loading = false;
      },
      error: (err) => {
        console.error('Errore caricamento carrello', err);
        this.error = 'Impossibile caricare il carrello';
        this.loading = false;
      }
    });
  }

  calcolaTotale(): void {
    this.totale = this.prodottiCarrello.reduce((acc, prodotto) => acc + prodotto.prezzo, 0);
  }

  rimuoviProdotto(prodottoId: number): void {
    // In un'implementazione reale, qui chiameresti un'API per rimuovere il prodotto
    this.prodottiCarrello = this.prodottiCarrello.filter(p => p.id !== prodottoId);
    this.calcolaTotale();
  }

  svuotaCarrello(): void {
    if (!this.userId) return;

    this.carrelloService.clearCart(this.userId).subscribe({
      next: () => {
        this.prodottiCarrello = [];
        this.totale = 0;
      },
      error: (err) => {
        console.error('Errore svuotamento carrello', err);
        this.error = 'Impossibile svuotare il carrello';
      }
    });
  }

  procediCheckout(): void {
    this.processingCheckout = true;

    // Simuliamo il caricamento
    setTimeout(() => {
      this.processingCheckout = false;
      this.router.navigate(['/checkout']);
    }, 1000);
  }

  continuaShopping(): void {
    this.router.navigate(['/catalogo']);
  }
}
