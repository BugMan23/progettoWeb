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

  // Proprietà user menu
  showUserMenu: boolean = false;

  // Categorie
  categorie: any[] = [];

  // Popup login
  showPopup: boolean = false;
  userRole: string | null = null;

  // Subscriptions
  private cartSub!: Subscription;
  private cartItemsSub!: Subscription;

  constructor(
    private router: Router,
    protected authService: AuthService,
    private categoriaService: CategoriaService,
    private cartService: CarrelloService
  ) { }

  ngOnInit(): void {
    // Sottoscrivi agli stati di autenticazione
    this.authService.isAuthenticatedUser().subscribe(
      isAuthenticated => this.isLoggedIn = isAuthenticated
    );

    this.authService.getUserName().subscribe(
      userName => this.userName = userName
    );

    this.authService.isAdminUser().subscribe(
      isAdmin => this.isAdmin = isAdmin
    );

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
    if (this.cartSub) this.cartSub.unsubscribe();
    if (this.cartItemsSub) this.cartItemsSub.unsubscribe();
  }

  checkAuthState(): void {
    const userId = localStorage.getItem('userId');
    this.isLoggedIn = !!userId;
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
    if (this.isLoggedIn) {
      this.showCartPreview = false;
      this.router.navigate(['/carrello']);
    }
  }

  navigateToProfile(): void {
    if (this.isLoggedIn) {
      this.showUserMenu = false;
      this.router.navigate(['/profilo']);
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
    this.showPopup = false;
    this.checkAuthState();
  }
}
