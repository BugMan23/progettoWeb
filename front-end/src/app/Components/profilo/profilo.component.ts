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

    // Load user profile data
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

    // Load user addresses
    this.indirizzoService.findByUtenteId(this.userId).subscribe({
      next: (data) => {
        this.indirizzi = data;
      },
      error: (err) => {
        console.error('Error loading addresses:', err);
      }
    });

    // Load payment methods
    this.metodoPagamentoService.getMetodiPagamentoUtente(this.userId).subscribe({
      next: (data) => {
        this.metodiPagamento = data;
      },
      error: (err) => {
        console.error('Error loading payment methods:', err);
      }
    });
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }
}
