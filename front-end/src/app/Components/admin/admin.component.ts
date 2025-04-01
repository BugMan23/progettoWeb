import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { OrdineService } from '../../services/ordine.service';
import { IndirizzoService } from '../../services/indirizzo.service';
import { MetodoPagamentoService } from '../../services/metodo-pagamento.service';
import { ProdottoService } from '../../services/prodotto.service';
import { RecensioneService } from '../../services/recensione.service';
import { CategoriaService } from '../../services/categoria.service';
import { User } from '../../models/user';
import { Indirizzo } from '../../models/indirizzo';
import { MetodoPagamento } from '../../models/metodo-pagamento';
import { Ordine } from '../../models/ordine';
import { DettagliOrdini } from '../../models/dettagli-ordini';
import { Prodotto } from '../../models/prodotto';
import { Categoria } from '../../models/categoria';

@Component({
  selector: 'app-admin',
  standalone : true,
  imports: [ CommonModule, FormsModule, RouterModule ],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent implements OnInit {
  dashboardData: any = null;
  products: Prodotto[] = [];
  productFormModel: Prodotto = this.resetProductForm();
  editingProduct: boolean = false;
  categorie: Categoria[] = [];

  orders: Ordine[] = [];
  users: User[] = [];
  reviews: any[] = [];
  recensioniUtente: Record<number, any[]> = {};


  nuoveDisponibilita: { taglia: string, quantita: number }[] = [];
  tagliaTemp: string = '';
  quantitaTemp: number = 0;



  loadingAdmin: boolean = false;

  userId: number | null = null;
  user: User | null = null;

  activeTab: string = 'profile';

  loading: boolean = true;
  error: string | null = null;

  indirizzi: Indirizzo[] = [];
  metodiPagamento: MetodoPagamento[] = [];
  ordini: Ordine[] = [];

  dettagliOrdini: { [orderId: number]: DettagliOrdini[] } = {};
  loadingDetails: { [orderId: number]: boolean } = {};
  prodotti: { [prodottoId: number]: Prodotto } = {};

  successMessage: string | null = null;
  errorMessage: string | null = null;


  constructor(
    private router: Router,
    private userService: UserService,
    private ordineService: OrdineService,
    private indirizzoService: IndirizzoService,
    private metodoPagamentoService: MetodoPagamentoService,
    private prodottoService: ProdottoService,
    private recensioneService: RecensioneService,
    private categoriaService: CategoriaService
  ) {}

  ngOnInit(): void {
    const userIdStr = localStorage.getItem('userId');
    if (!userIdStr) {
      this.router.navigate(['/login']);
      return;
    }

    this.userId = parseInt(userIdStr);
    this.loadUserData();
  }

  loadUserData(): void {
    if (!this.userId) return;
    this.loading = true;

    this.userService.getUserProfile(this.userId).subscribe({
      next: (data) => {
        this.user = data;
        this.loading = false;

        // @ts-ignore
        if (!this.user.ruolo) {
          this.router.navigate(['/profilo']);
        }
      },
      error: (err) => {
        console.error('Error loading user data:', err);
        this.error = 'Impossibile caricare i dati del profilo.';
        this.loading = false;
      }
    });

    this.indirizzoService.findByUtenteId(this.userId).subscribe({
      next: (data) => { this.indirizzi = data; },
      error: (err) => { console.error('Error loading addresses:', err); }
    });

    this.metodoPagamentoService.getMetodiPagamentoUtente(this.userId).subscribe({
      next: (data) => { this.metodiPagamento = data; },
      error: (error) => { console.error('Errore nel caricamento dei metodi di pagamento:', error); }
    });
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
    switch(tab) {
      case 'dashboard': this.loadDashboardData(); break;
      case 'manage-products':
        this.loadProducts();
        this.loadCategorie();
        break;
      case 'manage-users': this.loadUsers(); break;
    }
  }

  loadCategorie(): void {
    this.categoriaService.getAllCategories().subscribe({
      next: (data) => { this.categorie = data; },
      error: (err) => { console.error('Errore caricamento categorie:', err); }
    });
  }

  loadDashboardData(): void {
    this.loadingAdmin = true;
    setTimeout(() => {
      this.dashboardData = { utentiTotali: 120, ordiniTotali: 350 };
      this.loadingAdmin = false;
    }, 1000);
  }

  loadProducts(): void {
    this.loadingAdmin = true;
    this.prodottoService.getAllProducts().subscribe({
      next: (data) => { this.products = data; },
      error: (err) => { console.error(err); this.error = 'Errore caricamento prodotti'; },
      complete: () => { this.loadingAdmin = false; }
    });
  }

  editProduct(prod: Prodotto): void {
    this.productFormModel = { ...prod };
    this.editingProduct = true;
  }

  cancelEdit(): void {
    this.editingProduct = false;
    this.productFormModel = this.resetProductForm();
  }

  onSubmitProduct(): void {
    // Applica lo sconto se attivo
    if (this.productFormModel.scontato && this.productFormModel.percentualeSconto) {
      const sconto = (this.productFormModel.prezzo * this.productFormModel.percentualeSconto) / 100;
      this.productFormModel.prezzo = +(this.productFormModel.prezzo - sconto).toFixed(2);
    }

    // Controlla che ci siano disponibilità da inviare
    if (this.nuoveDisponibilita.length === 0) {
      alert("Aggiungi almeno una taglia con quantità per il prodotto.");
      return;
    }

    // Crea un oggetto unificato per il backend
    const payload = {
      prodotto: this.productFormModel,
      disponibilita: this.nuoveDisponibilita
    };

    if (this.editingProduct) {
      this.prodottoService.updateProduct(this.productFormModel).subscribe({
        next: () => {
          this.loadProducts();
          this.cancelEdit();
        },
        error: (err) => console.error('Errore aggiornamento prodotto:', err)
      });
    } else {
      this.prodottoService.createProduct(payload).subscribe({
        next: (res) => {
          console.log('✅ Prodotto creato con risposta:', res);
          this.loadProducts();
          this.productFormModel = this.resetProductForm();
          this.nuoveDisponibilita = [];
          this.successMessage = 'Prodotto aggiunto con successo!';
          setTimeout(() => this.successMessage = null, 3000);
        },
        error: (err) => {
          console.error('❌ Errore creazione prodotto:', err);
          this.errorMessage = 'Errore durante la creazione del prodotto.';
          setTimeout(() => this.errorMessage = null, 3000);
        }
      });
    }
  }



  aggiungiDisponibilita() {
    if (this.tagliaTemp && this.quantitaTemp > 0) {
      this.nuoveDisponibilita.push({
        taglia: this.tagliaTemp,
        quantita: this.quantitaTemp
      });
      this.tagliaTemp = '';
      this.quantitaTemp = 0;
    }
  }



  deleteProduct(id: number): void {
    if (confirm('Sei sicuro di voler eliminare questo prodotto?')) {
      this.prodottoService.deleteProduct(id).subscribe({
        next: () => {
          // ✅ Messaggio di conferma
          console.log('Prodotto eliminato con successo');
          alert('Prodotto eliminato con successo');

          // ✅ Ricarica l’elenco dei prodotti
          this.loadProducts();
        },
        error: (err) => {
          console.error('Errore eliminazione prodotto:', err.message || err);
          alert('Errore durante l\'eliminazione del prodotto');
        }
      });
    }
  }


  resetProductForm(): Prodotto {
    return {
      id: 0,
      nome: '',
      marca: '',
      colore: '#000000',
      prezzo: 0,
      descrizione: '',
      scontato: false,
      percentualeSconto: 0,
      urlImage: '',
      idCategoria: 1
    };
  }

  loadUsers(): void {
    this.loadingAdmin = true;
    this.userService.getCustomers().subscribe({
      next: (data) => { this.users = data; },
      error: (err) => { console.error(err); this.error = 'Errore caricamento utenti'; },
      complete: () => { this.loadingAdmin = false; }
    });
  }

  promuoviAAdmin(id: number | undefined): void {
    this.userService.promuoviAAdmin(id).subscribe({
      next: () => {
        const utente = this.users.find(u => u.id === id);
        if (utente) utente.ruolo = true;
      },
      error: (err) => { console.error('Errore promozione:', err); }
    });
  }

  bannaUtente(id: number | undefined): void {
    if (!confirm('Sei sicuro di voler bannare questo utente?')) return;

    this.userService.deactivateUser(id).subscribe({
      next: () => { this.users = this.users.filter(u => u.id !== id); },
      error: (err) => { console.error('Errore bannando utente:', err); }
    });
  }

  mostraRecensioni(userId: number | undefined): void {
    // @ts-ignore
    if (this.recensioniUtente[userId]) {
      // @ts-ignore
      delete this.recensioniUtente[userId];
      return;
    }

    this.recensioneService.getUserReviews(userId).subscribe({
      next: (data) => { // @ts-ignore
        this.recensioniUtente[userId] = data; },
      error: (err) => { console.error('Errore nel caricamento recensioni:', err); }
    });
  }

  removeAddress(addressId: number): void {
    if (!confirm('Sei sicuro di voler rimuovere questo indirizzo?')) return;

    this.indirizzoService.disattivaIndirizzo(addressId).subscribe({
      next: () => {
        this.successMessage = 'Indirizzo rimosso con successo';
        this.indirizzi = this.indirizzi.filter(addr => addr.id !== addressId);
        setTimeout(() => this.successMessage = null, 3000);
      },
      error: () => {
        this.errorMessage = 'Errore durante la rimozione dell\'indirizzo';
        setTimeout(() => this.errorMessage = null, 3000);
      }
    });
  }

  removePaymentMethod(methodId: number): void {
    if (!confirm('Sei sicuro di voler rimuovere questo metodo di pagamento?')) return;

    this.metodoPagamentoService.disattivaMetodoPagamento(methodId).subscribe({
      next: () => {
        this.successMessage = 'Metodo di pagamento rimosso con successo';
        this.metodiPagamento = this.metodiPagamento.filter(method => method.id !== methodId);
        setTimeout(() => this.successMessage = null, 3000);
      },
      error: () => {
        this.errorMessage = 'Errore durante la rimozione del metodo di pagamento';
        setTimeout(() => this.errorMessage = null, 3000);
      }
    });
  }

  getUltime4Cifre(carta: string): string {
    return carta?.slice(-4);
  }

}
