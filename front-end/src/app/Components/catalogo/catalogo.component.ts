import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProdottoService } from '../../services/prodotto.service';
import { CategoriaService } from '../../services/categoria.service';
import { Prodotto } from '../../Models/prodotto';
import { Categoria } from '../../Models/categoria';

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent implements OnInit {
  prodotti: Prodotto[] = [];
  filteredProdotti: Prodotto[] = [];
  categorie: Categoria[] = [];
  loading = true;
  error: string | null = null;

  // Filtri
  filtroCategoria: number | null = null;
  filtroMinPrezzo: number | null = null;
  filtroMaxPrezzo: number | null = null;
  filtroMarche: string[] = [];
  filtroColori: string[] = [];
  ricerca: string = '';

  // Array unici per filtri
  marche: string[] = [];
  colori: string[] = [];

  constructor(
    private prodottoService: ProdottoService,
    private categoriaService: CategoriaService
  ) { }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    // Carica categorie
    this.categoriaService.getAllCategories().subscribe({
      next: (data) => {
        this.categorie = data;
      },
      error: (err) => {
        console.error('Errore caricamento categorie', err);
        this.error = 'Impossibile caricare le categorie';
      }
    });

    // Carica prodotti
    this.prodottoService.getAllProducts().subscribe({
      next: (data) => {
        this.prodotti = data;
        this.filteredProdotti = [...data];
        this.extractFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error('Errore caricamento prodotti', err);
        this.error = 'Impossibile caricare i prodotti';
        this.loading = false;
      }
    });
  }

  // Estrai valori unici per filtri
  extractFilters() {
    // Estrai marche uniche
    this.marche = [...new Set(this.prodotti.map(p => p.marca))];

    // Estrai colori unici
    this.colori = [...new Set(this.prodotti.map(p => p.colore))];
  }

  // Applica filtri
  applyFilters() {
    this.filteredProdotti = this.prodotti.filter(p => {
      // Filtro categoria
      if (this.filtroCategoria && p.idCategoria !== this.filtroCategoria) {
        return false;
      }

      // Filtro prezzo
      if (this.filtroMinPrezzo && p.prezzo < this.filtroMinPrezzo) {
        return false;
      }
      if (this.filtroMaxPrezzo && p.prezzo > this.filtroMaxPrezzo) {
        return false;
      }

      // Filtro marche
      if (this.filtroMarche.length > 0 && !this.filtroMarche.includes(p.marca)) {
        return false;
      }

      // Filtro colori
      if (this.filtroColori.length > 0 && !this.filtroColori.includes(p.colore)) {
        return false;
      }

      // Filtro ricerca
      if (this.ricerca && !p.nome.toLowerCase().includes(this.ricerca.toLowerCase()) &&
        !p.descrizione.toLowerCase().includes(this.ricerca.toLowerCase())) {
        return false;
      }

      return true;
    });
  }

  // Reset filtri
  resetFilters() {
    this.filtroCategoria = null;
    this.filtroMinPrezzo = null;
    this.filtroMaxPrezzo = null;
    this.filtroMarche = [];
    this.filtroColori = [];
    this.ricerca = '';
    this.filteredProdotti = [...this.prodotti];
  }

  // Toggle marca nel filtro
  toggleMarca(marca: string) {
    const index = this.filtroMarche.indexOf(marca);
    if (index === -1) {
      this.filtroMarche.push(marca);
    } else {
      this.filtroMarche.splice(index, 1);
    }
    this.applyFilters();
  }

  // Toggle colore nel filtro
  toggleColore(colore: string) {
    const index = this.filtroColori.indexOf(colore);
    if (index === -1) {
      this.filtroColori.push(colore);
    } else {
      this.filtroColori.splice(index, 1);
    }
    this.applyFilters();
  }

  // Cerca in tempo reale
  onSearch() {
    this.applyFilters();
  }
}
