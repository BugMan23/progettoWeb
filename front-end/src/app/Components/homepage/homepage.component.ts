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

  // Numero massimo di prodotti da mostrare nella preview
  readonly previewCount = 4;

  // Carosello
  currentSlideIndex = 0;
  slides = [
    {
      id: 1,
      titolo: 'Nuova Collezione',
      sottotitolo: 'Scopri le ultime novità',
      immagine: 'assets/images/slide1.jpg',
      link: '/catalogo'
    },
    {
      id: 2,
      titolo: 'Prodotti Scontati',
      sottotitolo: 'Approfitta delle offerte',
      immagine: 'assets/images/slide2.jpg',
      link: '/catalogo'
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
        this.prodottiScontati = data.filter(p => p.scontato).slice(0, this.previewCount);

        // Prendi gli ultimi prodotti come "nuovi arrivi"
        // In un'app reale dovresti ordinarli per data di inserimento
        this.nuoviArrivi = [...data].sort(() => 0.5 - Math.random()).slice(0, this.previewCount);

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

  // Naviga alla pagina del catalogo con filtro per categoria
  navigateToCategory(categoriaId: number): void {
    // La navigazione viene gestita tramite l'attributo [routerLink] nel template
  }
}
