import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { UserService } from '../../services/user.service';
import { OrdineService } from '../../services/ordine.service';
import { IndirizzoService } from '../../services/indirizzo.service';
import { MetodoPagamentoService } from '../../services/metodo-pagamento.service';
import { Indirizzo } from '../../Models/indirizzo';
import { MetodoPagamento } from '../../Models/metodo-pagamento';
import { Ordine } from '../../Models/ordine';

@Component({
  selector: 'app-profilo',
  templateUrl: './profilo.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  styleUrls: ['./profilo.component.css']
})
export class ProfiloComponent implements OnInit {
  // Dati utente
  user: any;
  userId: number | null = null;

  // Dati profilo
  orders: Ordine[] = [];
  addresses: Indirizzo[] = [];
  paymentMethods: MetodoPagamento[] = [];

  // Gestione interfaccia
  activeTab: string = 'profile';
  loading: boolean = true;
  error: string | null = null;
  successMessage: string | null = null;

  // Per aggiungere un nuovo indirizzo
  newAddress: Indirizzo = {
    id: 0,
    nomeVia: '',
    civico: '',
    citta: '',
    cap: '',
    provincia: '',
    regione: '',
    idUtente: 0
  };

  // Per aggiungere un nuovo metodo di pagamento
  newPaymentMethod: MetodoPagamento = {
    id: 0,
    tipoPagamento: 'Carta di credito',
    titolare: '',
    tipoCarta: 'VISA',
    numeroCarta: '',
    dataScadenza: '',
    cvv: '',
    idUtente: 0
  };

  // Per la modalità di modifica del profilo
  editMode = false;
  editableUser: any = {};

  // Stati per i form
  showNewAddressForm = false;
  showNewPaymentForm = false;

  constructor(
    private userService: UserService,
    private ordineService: OrdineService,
    private indirizzoService: IndirizzoService,
    private metodoPagamentoService: MetodoPagamentoService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // Ottieni il tab attivo dalla rotta (se disponibile)
    this.route.paramMap.subscribe(params => {
      const tab = params.get('tab');
      if (tab) {
        this.setActiveTab(tab);
      }
    });

    // Verifica se l'utente è loggato
    const userIdStr = localStorage.getItem('userId');
    if (!userIdStr) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/profilo' } });
      return;
    }

    this.userId = parseInt(userIdStr);
    this.loadUserData();
  }

  loadUserData() {
    if (!this.userId) return;

    this.loading = true;

    // Carica i dati dell'utente
    this.userService.getUserProfile(this.userId).subscribe({
      next: (data) => {
        this.user = data;
        this.editableUser = {...this.user};
        this.loading = false;
      },
      error: (err) => {
        console.error('Errore caricamento dati utente:', err);
        this.error = 'Impossibile caricare i dati del profilo.';
        this.loading = false;
      }
    });

    // Carica gli ordini
    this.loadUserOrders();

    // Carica gli indirizzi
    this.loadUserAddresses();

    // Carica i metodi di pagamento
    this.loadUserPaymentMethods();
  }

  loadUserOrders(): void {
    if (!this.userId) return;

    this.ordineService.getUserOrders(this.userId).subscribe({
      next: (data) => this.orders = data,
      error: (err) => console.error('Errore caricamento ordini:', err)
    });
  }

  loadUserAddresses(): void {
    if (!this.userId) return;

    this.indirizzoService.findByUtenteId(this.userId).subscribe({
      next: (data) => this.addresses = data,
      error: (err) => console.error('Errore caricamento indirizzi:', err)
    });
  }

  loadUserPaymentMethods(): void {
    if (!this.userId) return;

    this.metodoPagamentoService.getMetodiPagamentoUtente(this.userId).subscribe({
      next: (data) => this.paymentMethods = data,
      error: (err) => console.error('Errore caricamento metodi di pagamento:', err)
    });
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  toggleEditMode(): void {
    this.editMode = !this.editMode;
    if (!this.editMode) {
      this.editableUser = {...this.user};
    }
  }

  saveUserChanges(): void {
    this.userService.updateUserProfile(this.user.id, this.editableUser).subscribe({
      next: () => {
        this.user = {...this.editableUser};
        this.editMode = false;
        this.successMessage = 'Profilo aggiornato con successo';
        setTimeout(() => this.successMessage = null, 3000);
      },
      error: (err) => {
        console.error('Errore aggiornamento profilo:', err);
        this.error = 'Impossibile aggiornare il profilo.';
        setTimeout(() => this.error = null, 3000);
      }
    });
  }

  toggleNewAddressForm(): void {
    this.showNewAddressForm = !this.showNewAddressForm;
    if (this.showNewAddressForm && this.userId) {
      this.newAddress = {
        id: 0,
        nomeVia: '',
        civico: '',
        citta: '',
        cap: '',
        provincia: '',
        regione: '',
        idUtente: this.userId
      };
    }
  }

  toggleNewPaymentForm(): void {
    this.showNewPaymentForm = !this.showNewPaymentForm;
    if (this.showNewPaymentForm && this.userId) {
      this.newPaymentMethod = {
        id: 0,
        tipoPagamento: 'Carta di credito',
        titolare: '',
        tipoCarta: 'VISA',
        numeroCarta: '',
        dataScadenza: '',
        cvv: '',
        idUtente: this.userId
      };
    }
  }

  addNewAddress(): void {
    if (!this.userId) return;

    // Validazione basilare
    if (!this.newAddress.nomeVia || !this.newAddress.civico || !this.newAddress.citta ||
      !this.newAddress.cap || !this.newAddress.provincia || !this.newAddress.regione) {
      this.error = 'Tutti i campi sono obbligatori';
      setTimeout(() => this.error = null, 3000);
      return;
    }

    this.indirizzoService.addIndirizzo(this.newAddress, this.userId).subscribe({
      next: () => {
        this.successMessage = 'Indirizzo aggiunto con successo';
        setTimeout(() => this.successMessage = null, 3000);
        this.loadUserAddresses();
        this.toggleNewAddressForm();
      },
      error: (err) => {
        console.error('Errore aggiunta indirizzo:', err);
        this.error = 'Impossibile aggiungere l\'indirizzo.';
        setTimeout(() => this.error = null, 3000);
      }
    });
  }

  addNewPaymentMethod(): void {
    if (!this.userId) return;

    // Validazione basilare
    if (!this.newPaymentMethod.titolare || !this.newPaymentMethod.numeroCarta ||
      !this.newPaymentMethod.dataScadenza || !this.newPaymentMethod.cvv) {
      this.error = 'Tutti i campi sono obbligatori';
      setTimeout(() => this.error = null, 3000);
      return;
    }

    this.metodoPagamentoService.salvaMetodoPagamento(this.newPaymentMethod, this.userId).subscribe({
      next: () => {
        this.successMessage = 'Metodo di pagamento aggiunto con successo';
        setTimeout(() => this.successMessage = null, 3000);
        this.loadUserPaymentMethods();
        this.toggleNewPaymentForm();
      },
      error: (err) => {
        console.error('Errore aggiunta metodo di pagamento:', err);
        this.error = 'Impossibile aggiungere il metodo di pagamento.';
        setTimeout(() => this.error = null, 3000);
      }
    });
  }

  viewOrderDetails(orderId: number): void {
    this.router.navigate(['/ordini', orderId]);
  }

  /*deleteAddress(addressId: number): void {
    if (!confirm('Sei sicuro di voler eliminare questo indirizzo?')) return;

    this.indirizzoService.deleteIndirizzo(addressId).subscribe({
      next: () => {
        this.successMessage = 'Indirizzo eliminato con successo';
        setTimeout(() => this.successMessage = null, 3000);
        this.loadUserAddresses();
      },
      error: (err) => {
        console.error('Errore eliminazione indirizzo:', err);
        this.error = 'Impossibile eliminare l\'indirizzo.';
        setTimeout(() => this.error = null, 3000);
      }
    });
  }

  deletePaymentMethod(paymentId: number): void {
    if (!confirm('Sei sicuro di voler eliminare questo metodo di pagamento?')) return;

    this.metodoPagamentoService.deleteMetodoPagamento(paymentId).subscribe({
      next: () => {
        this.successMessage = 'Metodo di pagamento eliminato con successo';
        setTimeout(() => this.successMessage = null, 3000);
        this.loadUserPaymentMethods();
      },
      error: (err) => {
        console.error('Errore eliminazione metodo di pagamento:', err);
        this.error = 'Impossibile eliminare il metodo di pagamento.';
        setTimeout(() => this.error = null, 3000);
      }
    });
  }*/
}
