// homepage.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProdottoService } from '../../services/prodotto.service';
import { CategoriaService } from '../../services/categoria.service';
import { Prodotto } from '../../Models/prodotto';
import { Categoria } from '../../Models/categoria';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ]
})
export class HomepageComponent implements OnInit {
  prodotti: Prodotto[] = [];
  prodottiScontati: Prodotto[] = [];
  categorie: Categoria[] = [];
  loading = true;
  error = '';

  constructor(
    private prodottoService: ProdottoService,
    private categoriaService: CategoriaService
  ) { }

  ngOnInit() {

    console.log('Chiamata API in corso...');
    this.prodottoService.getAllProducts().subscribe({
      next: (data) => console.log('Dati ricevuti:', data),
      error: (err) => console.error('Errore API:', err)
    });

    // Carica tutti i prodotti
    this.prodottoService.getAllProducts().subscribe({
      next: (data) => {
        this.prodotti = data;
        // Filtra i prodotti scontati
        this.prodottiScontati = data.filter(p => p.scontato);
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Errore nel caricamento dei prodotti';
        this.loading = false;
      }
    });

    // Carica le categorie
    this.categoriaService.getAllCategories().subscribe({
      next: (data) => {
        this.categorie = data;
      },
      error: (err) => {
        this.error = 'Errore nel caricamento delle categorie';
      }
    });
  }

}
