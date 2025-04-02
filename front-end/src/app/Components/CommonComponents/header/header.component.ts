import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { CategoriaService } from '../../../services/categoria.service';
import { CarrelloService } from '../../../services/carrello.service';
import { AuthService } from '../../../services/auth.service';
import { LoginComponent } from '../../login/login.component';
import { Prodotto } from '../../../models/prodotto';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, LoginComponent, RouterModule, FormsModule],
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

  // User dropdown
  showUserMenu: boolean = false;

  // Categorie
  categorie: any[] = [];

  // Popup login
  showPopup: boolean = false;
  userRole: string | null = null;


  searchTerm: string = '';
  private readonly MAX_RECENT_SEARCHES = 5;

  private cartSub!: Subscription;
  private cartItemsSub!: Subscription;

  constructor(
    private router: Router,
    protected authService: AuthService,
    private categoriaService: CategoriaService,
    private cartService: CarrelloService
  ) {
  }

  ngOnInit(): void {
    this.authService.isAuthenticatedUser().subscribe(
      isAuthenticated => this.isLoggedIn = isAuthenticated
    );

    this.authService.getUserName().subscribe(
      userName => this.userName = userName
    );

    this.checkAuthState();
    this.loadCategories();

    this.cartSub = this.cartService.cartChanged.subscribe(() => {
      if (this.isLoggedIn) {
        this.loadCartData();
      }
    });

    // Aggiungi un listener per chiudere il menu utente quando si clicca altrove
    document.addEventListener('click', this.closeUserMenuOnClickOutside.bind(this));
  }

  ngOnDestroy(): void {
    if (this.cartSub) this.cartSub.unsubscribe();
    if (this.cartItemsSub) this.cartItemsSub.unsubscribe();
    document.removeEventListener('click', this.closeUserMenuOnClickOutside.bind(this));
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
    this.showUserMenu = false;

    // Redirect alla homepage
    this.router.navigate(['/']);
  }

  toggleUserMenu(): void {
    this.showUserMenu = !this.showUserMenu;
  }

  closeUserMenuOnClickOutside(event: any): void {
    if (this.showUserMenu) {
      const userDropdown = document.querySelector('.user-dropdown');
      if (userDropdown && !userDropdown.contains(event.target)) {
        this.showUserMenu = false;
      }
    }
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

  onLoginSuccess(): void {
    this.showPopup = false;
    console.log('isAdmin from localStorage:', this.isAdmin);
    this.checkAuthState();
  }

  search(): void {
    if (this.searchTerm && this.searchTerm.trim().length > 0) {
      this.router.navigate(['/catalogo'], {
        queryParams: { q: this.searchTerm.trim() }
      });
      this.searchTerm = '';
    }
  }


  onSearchKeyUp(event: KeyboardEvent): void {
    if (event.key === 'Enter') {
      this.search();
    }
  }

}
