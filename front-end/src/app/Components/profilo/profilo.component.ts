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

    // Load orders
    this.ordineService.getUserOrders(this.userId).subscribe({
      next: (data) => {
        this.ordini = data;
      },
      error: (err) => {
        console.error('Error loading orders:', err);
      }
    });
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  loadDettagliOrdine(ordineId: number): void {
    if (this.dettagliOrdini[ordineId]) return;

    this.loadingDetails[ordineId] = true;

    this.ordineService.getOrderItems(ordineId).subscribe({
      next: (data) => {
        this.dettagliOrdini[ordineId] = data;

        // Load products information for order items
        data.forEach(dettaglio => {
          this.loadProdottoInfo(dettaglio.idProdotto);
        });

        this.loadingDetails[ordineId] = false;
      },
      error: (err) => {
        console.error(`Error loading order details for order ${ordineId}:`, err);
        this.loadingDetails[ordineId] = false;
      }
    });
  }

  loadProdottoInfo(prodottoId: number): void {
    if (this.prodotti[prodottoId]) return;

    this.prodottoService.getProductById(prodottoId).subscribe({
      next: (data) => {
        this.prodotti[prodottoId] = data;
      },
      error: (err) => {
        console.error(`Error loading product info for product ${prodottoId}:`, err);
      }
    });
  }

  getNomeProdotto(prodottoId: number): string {
    return this.prodotti[prodottoId]?.nome || `Prodotto #${prodottoId}`;
  }

  getPrezzoUnitario(prodottoId: number): number {
    return this.prodotti[prodottoId]?.prezzo || 0;
  }

  getIndirizzoOrdine(ordineId: number): Indirizzo | null {
    const ordine = this.ordini.find(o => o.id === ordineId);
    if (!ordine) return null;

    // In a real application, you would have the address ID stored in the order
    // For now, we just return the first address
    return this.indirizzi.length > 0 ? this.indirizzi[0] : null;
  }

  getMetodoPagamentoOrdine(ordineId: number): MetodoPagamento | null {
    const ordine = this.ordini.find(o => o.id === ordineId);
    if (!ordine) return null;

    // Find payment method by ID (assuming the order has a payment method ID)
    return this.metodiPagamento.find(mp => mp.id === ordine.idMetodoPagamento) || null;
  }
}
