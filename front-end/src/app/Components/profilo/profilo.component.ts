// profile.component.ts
import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { OrdineService } from '../../services/ordine.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profilo',
  templateUrl: './profilo.component.html',
  standalone: true,
  styleUrls: ['./profilo.component.css']
})
export class ProfiloComponent implements OnInit {
  user: any;
  orders: any[] = [];
  addresses: any[] = [];
  paymentMethods: any[] = [];
  activeTab: string = 'profile';

  // Per aggiungere un nuovo indirizzo
  newAddress: any = {
    nomeVia: '',
    civico: '',
    citta: '',
    cap: '',
    provincia: '',
    regione: ''
  };

  // Per la modalità di modifica del profilo
  editMode = false;
  editableUser: any = {};

  constructor(
    private userService: UserService,
    private orderService: OrdineService,
    private router: Router
  ) { }

  ngOnInit(): void {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      this.router.navigate(['/login']);
      return;
    }

    this.loadUserData(Number(userId));
    this.loadUserOrders(Number(userId));
    this.loadUserAddresses(Number(userId));
    this.loadUserPaymentMethods(Number(userId));
  }

  loadUserData(userId: number): void {
    this.userService.getUserProfile(userId).subscribe(
      (data) => {
        this.user = data;
        this.editableUser = {...this.user};
      },
      (error) => console.error('Errore nel caricamento dei dati utente:', error)
    );
  }

  loadUserOrders(userId: number): void {
    this.orderService.getUserOrders(userId).subscribe(
      (data) => this.orders = data,
      (error) => console.error('Errore nel caricamento degli ordini:', error)
    );
  }

  loadUserAddresses(userId: number): void {
    this.userService.getUserAddresses(userId).subscribe(
      (data) => this.addresses = data,
      (error) => console.error('Errore nel caricamento degli indirizzi:', error)
    );
  }

  loadUserPaymentMethods(userId: number): void {
    this.userService.getUserPaymentMethods(userId).subscribe(
      (data) => this.paymentMethods = data,
      (error) => console.error('Errore nel caricamento dei metodi di pagamento:', error)
    );
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
    this.userService.updateUserProfile(this.user.id, this.editableUser).subscribe(
      () => {
        this.user = {...this.editableUser};
        this.editMode = false;
        alert('Profilo aggiornato con successo');
      },
      (error) => console.error('Errore durante l\'aggiornamento del profilo:', error)
    );
  }

  addNewAddress(): void {
    const userId = localStorage.getItem('userId');
    this.newAddress.idUtente = Number(userId);

    this.userService.addAddress(this.newAddress).subscribe(
      () => {
        alert('Indirizzo aggiunto con successo');
        this.loadUserAddresses(Number(userId));
        this.newAddress = {
          nomeVia: '',
          civico: '',
          citta: '',
          cap: '',
          provincia: '',
          regione: ''
        };
      },
      (error) => console.error('Errore durante l\'aggiunta dell\'indirizzo:', error)
    );
  }

  viewOrderDetails(orderId: number): void {
    this.router.navigate(['/ordini', orderId]);
  }
}
