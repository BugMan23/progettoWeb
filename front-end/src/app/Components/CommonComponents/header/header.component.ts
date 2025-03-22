import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { CategoriaService } from '../../../services/categoria.service';
import { CarrelloService } from '../../../services/carrello.service';
import { AuthService } from '../../../services/auth.service';
import { LoginComponent } from '../../login/login.component';
import { Prodotto } from '../../../Models/prodotto';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, LoginComponent, RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {
  // Proprietà di autenticazione
  isLoggedIn: boolean = false;
  isAdmin: boolean = false;
  userName: string = '';

  // Proprietà carrello
  cartItemCount: number = 0;
  cartItems: Prodotto[] = [];
  cartTotal: number = 0;
  showCartPreview: boolean = false;

  // Categorie
  categorie: any[] = [];

  // Popup login
  showPopup: boolean = false;
  userRole: string | null = null;

  // Subscriptions
  private cartSub!: Subscription;
  private cartItemsSub!: Subscription;
  private authStateSub!: Subscription;
  private userNameSub!: Subscription;
  private userRoleSub!: Subscription;

  constructor(
    private router: Router,
    protected authService: AuthService,
    private categoriaService: CategoriaService,
    private cartService: CarrelloService
  ) { }

  ngOnInit(): void {
    // Sottoscrivi agli stati di autenticazione
    this.authStateSub = this.authService.isAuthenticatedUser().subscribe(
      isAuthenticated => {
        console.log('Auth state changed:', isAuthenticated);
        this.isLoggedIn = isAuthenticated;
        if (isAuthenticated) {
          this.loadCartData();
        }
      }
    );

    this.userNameSub = this.authService.getUserName().subscribe(
      userName => this.userName = userName
    );

    this.userRoleSub = this.authService.isAdminUser().subscribe(
      isAdmin => this.isAdmin = isAdmin
    );

    // Carica stato iniziale
    this.checkAuthState();
    this.loadCategories();

    // Iscriviti agli aggiornamenti del carrello
    this.cartSub = this.cartService.cartChanged.subscribe(() => {
      if (this.isLoggedIn) {
        this.loadCartData();
      }
    });
  }

  ngOnDestroy(): void {
    // AGGIUNTA: Codice completo per pulire le sottoscrizioni
    if (this.cartSub) this.cartSub.unsubscribe();
    if (this.cartItemsSub) this.cartItemsSub.unsubscribe();
    if (this.authStateSub) this.authStateSub.unsubscribe();
    if (this.userNameSub) this.userNameSub.unsubscribe();
    if (this.userRoleSub) this.userRoleSub.unsubscribe();
  }

  checkAuthState(): void {
    const userId = localStorage.getItem('userId');
    const isLoggedIn = !!userId;
    // AGGIUNTA: Log di debug
    console.log('Checking auth state: userId =', userId, 'isLoggedIn =', isLoggedIn);
    this.isLoggedIn = isLoggedIn;
    this.isAdmin = localStorage.getItem('isAdmin') === 'true';
    this.userName = localStorage.getItem('userName') || '';

    if (this.isLoggedIn) {
      this.loadCartData();
    }
  }

  loadCartData(): void {
    const userId = localStorage.getItem('userId');
    if (!userId) return;

    // Carica il conteggio e i prodotti nel carrello
    this.cartService.getUserCart(parseInt(userId)).subscribe({
      next: (items) => {
        this.cartItems = items;
        this.cartItemCount = items.length;
        this.calculateCartTotal();
      },
      error: (err) => console.error('Errore nel caricamento del carrello', err)
    });
  }

  calculateCartTotal(): void {
    // Semplice calcolo del totale - in un caso reale dovresti tenere conto delle quantità
    this.cartTotal = this.cartItems.reduce((total, item) => total + item.prezzo, 0);
  }

  loadCategories(): void {
    this.categoriaService.getAllCategories().subscribe({
      next: (data) => this.categorie = data,
      error: (err) => console.error('Errore nel caricamento delle categorie', err)
    });
  }

  navigateToCart(): void {
    // AGGIUNTA: Verifica che l'utente sia già loggato
    if (this.isLoggedIn) {
      // Nascondi il popup del carrello
      this.showCartPreview = false;
      // Naviga direttamente alla pagina del carrello
      this.router.navigate(['/carrello']);
    } else {
      // Se non è loggato, apri il popup di login
      this.openLoginPopup();
    }
  }

  logout(): void {
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    localStorage.removeItem('isAdmin');

    this.isLoggedIn = false;
    this.isAdmin = false;
    this.userName = '';
    this.cartItemCount = 0;
    this.cartItems = [];
    this.cartTotal = 0;

    // Redirect alla homepage
    this.router.navigate(['/login']);
  }

  goHomepage() {
    console.log('Navigating to homepage');
    this.router.navigate(['']);
  }

  openLoginPopup(): void {
    console.log('Opening login popup');
    this.showPopup = true;
  }

  closeLoginPopup(): void {
    console.log('Closing login popup');
    this.showPopup = false;
  }

  // Questo metodo può essere chiamato dal componente login quando l'autenticazione ha successo
  onLoginSuccess(): void {
    console.log('Login success received in header component');
    this.showPopup = false;

    // AGGIUNTA: Forza l'aggiornamento dello stato
    this.isLoggedIn = true;
    this.userName = localStorage.getItem('userName') || '';
    this.isAdmin = localStorage.getItem('isAdmin') === 'true';

    // Carica i dati del carrello
    this.loadCartData();
  }
}
