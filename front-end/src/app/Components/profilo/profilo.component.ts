import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { OrdineService } from '../../services/ordine.service';
import { IndirizzoService } from '../../services/indirizzo.service';
import { MetodoPagamentoService } from '../../services/metodo-pagamento.service';
import { ProdottoService } from '../../services/prodotto.service';
import { User } from '../../models/user';
import { Indirizzo } from '../../models/indirizzo';
import { MetodoPagamento } from '../../models/metodo-pagamento';
import { Ordine } from '../../models/ordine';
import { DettagliOrdini } from '../../models/dettagli-ordini';
import { Prodotto } from '../../models/prodotto';

@Component({
  selector: 'app-profilo',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './profilo.component.html',
  styleUrls: ['./profilo.component.css']
})
export class ProfiloComponent implements OnInit {
  // User data
  userId: number | null = null;
  user: User | null = null;

  // Tab management
  activeTab: string = 'profile';

  // Status flags
  loading: boolean = true;
  error: string | null = null;

  // User data collections
  indirizzi: Indirizzo[] = [];
  metodiPagamento: MetodoPagamento[] = [];
  ordini: Ordine[] = [];

  // Orders details
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
    private prodottoService: ProdottoService
  ) {}

  ngOnInit(): void {
    // Check if user is logged in
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

    // Carica dati del profilo
    this.userService.getUserProfile(this.userId).subscribe({
      next: (data) => {
        this.user = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading user data:', err);
        this.error = 'Impossibile caricare i dati del profilo.';
        this.loading = false;
      }
    });

    // Carica indirizzi
    this.indirizzoService.findByUtenteId(this.userId).subscribe({
      next: (data) => {
        this.indirizzi = data;
      },
      error: (err) => {
        console.error('Error loading addresses:', err);
      }
    });

    // Carica metodi di pagamento
    this.metodoPagamentoService.getMetodiPagamentoUtente(this.userId).subscribe({
      next: (data) => {
        console.log('Metodi di pagamento ricevuti:', data);
        this.metodiPagamento = data;
      },
      error: (error) => {
        console.error('Errore nel caricamento dei metodi di pagamento:', error);
      }
    });
  }

  setActiveTab(tab: string): void {
    console.log('Cambio tab a:', tab);
    this.activeTab = tab;
  }

  removeAddress(addressId: number): void {
    if (!confirm('Sei sicuro di voler rimuovere questo indirizzo?')) {
      return;
    }

    this.indirizzoService.disattivaIndirizzo(addressId).subscribe({
      next: (response) => {
        console.log('Indirizzo rimosso con successo:', response);
        this.successMessage = 'Indirizzo rimosso con successo';

        // Rimuovi l'indirizzo dalla lista locale
        this.indirizzi = this.indirizzi.filter(addr => addr.id !== addressId);

        // Nascondi il messaggio dopo 3 secondi
        setTimeout(() => {
          this.successMessage = null;
        }, 3000);
      },
      error: (error) => {
        console.error('Errore durante la rimozione dell\'indirizzo:', error);
        this.errorMessage = 'Errore durante la rimozione dell\'indirizzo';

        // Nascondi il messaggio dopo 3 secondi
        setTimeout(() => {
          this.errorMessage = null;
        }, 3000);
      }
    });
  }

  removePaymentMethod(methodId: number): void {
    if (!confirm('Sei sicuro di voler rimuovere questo metodo di pagamento?')) {
      return;
    }

    this.metodoPagamentoService.disattivaMetodoPagamento(methodId).subscribe({
      next: (response) => {
        console.log('Metodo di pagamento rimosso con successo:', response);
        this.successMessage = 'Metodo di pagamento rimosso con successo';

        // Rimuovi il metodo di pagamento dalla lista locale
        this.metodiPagamento = this.metodiPagamento.filter(method => method.id !== methodId);

        // Nascondi il messaggio dopo 3 secondi
        setTimeout(() => {
          this.successMessage = null;
        }, 3000);
      },
      error: (error) => {
        console.error('Errore durante la rimozione del metodo di pagamento:', error);
        this.errorMessage = 'Errore durante la rimozione del metodo di pagamento';

        // Nascondi il messaggio dopo 3 secondi
        setTimeout(() => {
          this.errorMessage = null;
        }, 3000);
      }
    });
  }

  getUltime4Cifre(carta: string): string {
    return carta?.slice(-4);
  }

}
