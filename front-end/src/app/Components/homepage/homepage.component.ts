import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProdottoService } from '../../services/prodotto.service';
import { CategoriaService } from '../../services/categoria.service';
import { Prodotto } from '../../Models/prodotto';
import { Categoria } from '../../Models/categoria';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-homepage',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit, OnDestroy {
  // Prodotti e categorie dal database
  prodotti: Prodotto[] = [];
  prodottiScontati: Prodotto[] = [];
  nuoviArrivi: Prodotto[] = [];
  categorie: Categoria[] = [];

  // Stati di caricamento e errori
  loading = true;
  error: string | null = null;

  // Carosello
  currentSlideIndex = 0;
  slides = [
    {
      id: 1,
      titolo: 'Nuova Collezione',
      sottotitolo: 'Scopri le ultime novità',
      immagine: 'assets/images/slide1.jpg',
      link: '/catalogo',
      queryParams: { nuovi: true }
    },
    {
      id: 2,
      titolo: 'Prodotti Scontati',
      sottotitolo: 'Approfitta delle offerte',
      immagine: 'assets/images/slide2.jpg',
      link: '/catalogo',
      queryParams: { scontati: true }
    }
  ];
  private slideInterval?: Subscription;

  constructor(
    private prodottoService: ProdottoService,
    private categoriaService: CategoriaService
  ) { }

  ngOnInit(): void {
    this.loadData();
    this.startSlideshow();
  }

  ngOnDestroy(): void {
    if (this.slideInterval) {
      this.slideInterval.unsubscribe();
    }
  }

  private loadData(): void {
    // Carica prodotti dal database
    this.prodottoService.getAllProducts().subscribe({
      next: (data) => {
        this.prodotti = data;

        // Filtra prodotti scontati
        this.prodottiScontati = data.filter(p => p.scontato).slice(0, 4);

        // Nuovi arrivi: ordina per ID in ordine decrescente (assumendo che gli ID più alti siano i prodotti più recenti)
        this.nuoviArrivi = data
          .sort((a, b) => b.id - a.id)
          .slice(0, 4);

        this.loading = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento dei prodotti', err);
        this.error = 'Impossibile caricare i prodotti dal database';
        this.loading = false;
      }
    });

    // Carica categorie dal database
    this.categoriaService.getAllCategories().subscribe({
      next: (data) => {
        this.categorie = data;
      },
      error: (err) => {
        console.error('Errore nel caricamento delle categorie', err);
        this.error = 'Impossibile caricare le categorie dal database';
      }
    });
  }

  private startSlideshow(): void {
    this.slideInterval = interval(5000).subscribe(() => {
      this.nextSlide();
    });
  }

  nextSlide(): void {
    this.currentSlideIndex = (this.currentSlideIndex + 1) % this.slides.length;
  }

  prevSlide(): void {
    this.currentSlideIndex = (this.currentSlideIndex - 1 + this.slides.length) % this.slides.length;
  }

  setSlide(index: number): void {
    this.currentSlideIndex = index;
  }

  // Calcola prezzo scontato (20% di sconto)
  getPrezzoScontato(prezzo: number): number {
    return Math.round(prezzo * 0.8);
  }
}
