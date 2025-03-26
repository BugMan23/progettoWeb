import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ProdottoService } from '../../services/prodotto.service';
import { CategoriaService } from '../../services/categoria.service';
import { Prodotto } from '../../models/prodotto';
import { Categoria } from '../../models/categoria';

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
  filtroScontati: boolean = false;
  filtroNuovi: boolean = false;

  // Array unici per filtri
  marche: string[] = [];
  colori: string[] = [];

  // Titolo della pagina in base al filtro attivo
  titoloPagina: string = 'Catalogo';

  constructor(
    private prodottoService: ProdottoService,
    private categoriaService: CategoriaService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Prima carica i dati base (prodotti e categorie)
    this.loadData();

    // Poi ascolta i cambiamenti nei parametri dell'URL
    this.route.queryParams.subscribe(params => {
      // Reset dei filtri
      this.resetFilters(false); // Non ricaricare ancora i prodotti

      // Leggi i parametri dalla URL
      const categoriaId = params['categoria'] ? parseInt(params['categoria']) : null;
      const scontati = params['scontati'] === 'true';
      const nuovi = params['nuovi'] === 'true';
      const marca = params['marca'];
      const colore = params['colore'];
      const minPrezzo = params['minPrezzo'] ? parseInt(params['minPrezzo']) : null;
      const maxPrezzo = params['maxPrezzo'] ? parseInt(params['maxPrezzo']) : null;
      const query = params['q'];

      // Imposta i filtri in base ai parametri
      if (categoriaId) {
        this.filtroCategoria = categoriaId;
        // Aggiorna il titolo se c'è una categoria
        this.updateTitleByCategory(categoriaId);
      }

      if (scontati) {
        this.filtroScontati = true;
        this.titoloPagina = 'Prodotti Scontati';
      }

      if (nuovi) {
        this.filtroNuovi = true;
        this.titoloPagina = 'Nuovi Arrivi';
      }

      if (marca) {
        this.filtroMarche = Array.isArray(marca) ? marca : [marca];
      }

      if (colore) {
        this.filtroColori = Array.isArray(colore) ? colore : [colore];
      }

      if (minPrezzo) this.filtroMinPrezzo = minPrezzo;
      if (maxPrezzo) this.filtroMaxPrezzo = maxPrezzo;

      if (query) {
        this.ricerca = query;
        this.titoloPagina = `Risultati per: ${query}`;
      }

      // Applica i filtri
      this.applyFilters();
    });
  }

  // Aggiorna il titolo in base alla categoria selezionata
  private updateTitleByCategory(categoriaId: number) {
    const categoria = this.categorie.find(c => c.id === categoriaId);
    if (categoria) {
      this.titoloPagina = `Categoria: ${categoria.nome}`;
    }
  }

  loadData() {
    // Carica categorie
    this.categoriaService.getAllCategories().subscribe({
      next: (data) => {
        this.categorie = data;
        // Aggiorna titolo se necessario (nel caso in cui i parametri URL siano già stati elaborati)
        if (this.filtroCategoria) {
          this.updateTitleByCategory(this.filtroCategoria);
        }
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
        this.extractFilters();
        this.applyFilters(); // Applica filtri dopo che i dati sono stati caricati
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
    // Inizia con tutti i prodotti
    let prodottiFiltrati = [...this.prodotti];

    // Filtro categoria
    if (this.filtroCategoria !== null) {
      prodottiFiltrati = prodottiFiltrati.filter(p => p.idCategoria === this.filtroCategoria);
    }

    // Filtro prodotti scontati
    if (this.filtroScontati) {
      prodottiFiltrati = prodottiFiltrati.filter(p => p.scontato);
    }

    // Filtro nuovi arrivi (implementazione di esempio - nella realtà dovresti avere un campo per determinare i nuovi arrivi)
    if (this.filtroNuovi) {
      // Ordina per ID in ordine decrescente (assumendo che gli ID più alti siano i prodotti più recenti)
      prodottiFiltrati = prodottiFiltrati.sort((a, b) => b.id - a.id).slice(0, 10);
    }

    // Filtro prezzo
    if (this.filtroMinPrezzo !== null) {
      prodottiFiltrati = prodottiFiltrati.filter(p => p.prezzo >= (this.filtroMinPrezzo || 0));
    }

    if (this.filtroMaxPrezzo !== null) {
      prodottiFiltrati = prodottiFiltrati.filter(p => p.prezzo <= (this.filtroMaxPrezzo || Infinity));
    }

    // Filtro marche
    if (this.filtroMarche.length > 0) {
      prodottiFiltrati = prodottiFiltrati.filter(p => this.filtroMarche.includes(p.marca));
    }

    // Filtro colori
    if (this.filtroColori.length > 0) {
      prodottiFiltrati = prodottiFiltrati.filter(p => this.filtroColori.includes(p.colore));
    }

    // Filtro ricerca
    if (this.ricerca && this.ricerca.trim() !== '') {
      const searchTerm = this.ricerca.toLowerCase();
      prodottiFiltrati = prodottiFiltrati.filter(p =>
        p.nome.toLowerCase().includes(searchTerm) ||
        p.descrizione.toLowerCase().includes(searchTerm) ||
        p.marca.toLowerCase().includes(searchTerm)
      );
    }

    this.filteredProdotti = prodottiFiltrati;

    // Aggiorna l'URL con i filtri (opzionale, ma utile per condividere link filtrati)
    this.updateUrlWithFilters();
  }

  // Reset filtri
  resetFilters(applyAfterReset: boolean = true) {
    this.filtroCategoria = null;
    this.filtroMinPrezzo = null;
    this.filtroMaxPrezzo = null;
    this.filtroMarche = [];
    this.filtroColori = [];
    this.ricerca = '';
    this.filtroScontati = false;
    this.filtroNuovi = false;
    this.titoloPagina = 'Catalogo';

    if (applyAfterReset) {
      this.filteredProdotti = [...this.prodotti];
      // Rimuovi i parametri dell'URL
      this.router.navigate(['/catalogo']);
    }
  }

  // Aggiorna l'URL con i filtri correnti
  updateUrlWithFilters() {
    const queryParams: any = {};

    if (this.filtroCategoria !== null) queryParams.categoria = this.filtroCategoria;
    if (this.filtroScontati) queryParams.scontati = true;
    if (this.filtroNuovi) queryParams.nuovi = true;
    if (this.filtroMinPrezzo !== null) queryParams.minPrezzo = this.filtroMinPrezzo;
    if (this.filtroMaxPrezzo !== null) queryParams.maxPrezzo = this.filtroMaxPrezzo;
    if (this.filtroMarche.length > 0) queryParams.marca = this.filtroMarche;
    if (this.filtroColori.length > 0) queryParams.colore = this.filtroColori;
    if (this.ricerca) queryParams.q = this.ricerca;

    // Aggiorna l'URL senza ricaricare la pagina
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
      queryParamsHandling: 'merge', // mantiene i parametri esistenti se non vengono sovrascritti
      replaceUrl: true // sostituisce l'URL invece di aggiungere una nuova entry nella history
    });
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

  // Filtra per categoria (funzione pubblica che può essere chiamata dal template)
  filtraPerCategoria(categoriaId: number) {
    this.resetFilters(false);
    this.filtroCategoria = categoriaId;
    this.updateTitleByCategory(categoriaId);
    this.applyFilters();
  }

  // Filtra prodotti scontati
  filtraProdottiScontati() {
    this.resetFilters(false);
    this.filtroScontati = true;
    this.titoloPagina = 'Prodotti Scontati';
    this.applyFilters();
  }

  // Filtra nuovi arrivi
  filtraNuoviArrivi() {
    this.resetFilters(false);
    this.filtroNuovi = true;
    this.titoloPagina = 'Nuovi Arrivi';
    this.applyFilters();
  }
}
