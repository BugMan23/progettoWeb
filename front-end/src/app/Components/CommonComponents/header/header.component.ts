import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { CategoriaService } from '../../../services/categoria.service';
import { CarrelloService } from '../../../services/carrello.service';
import { AuthService } from '../../../services/auth.service';
import { LoginComponent } from '../../login/login.component';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, LoginComponent, RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css', '../../../../styles.css']
})
export class HeaderComponent implements OnInit, OnDestroy {
  // Proprietà originali di Sofia
  isLoggedIn: boolean = false;
  isAdmin: boolean = false;
  userName: string = '';
  cartItemCount: number = 0;
  categorie: any[] = [];

  // Proprietà di Alberto
  showPopup = false;
  userRole: string | null = null;

  private cartSub!: Subscription;

  constructor(
    private router: Router,
    protected authService: AuthService,
    private categoriaService: CategoriaService,
    private cartService: CarrelloService
  ) {
    // Inizializzazione di Alberto
    this.userRole = this.authService.getUserRole() ? 'admin' : 'user';
    console.log('HeaderComponent Initialized - showPopup:', this.showPopup);
  }

  ngOnInit(): void {
    this.checkAuthState();
    this.loadCategories();

    // Iscriviti agli aggiornamenti del carrello
    this.cartSub = this.cartService.cartChanged.subscribe(() => {
      if (this.isLoggedIn) {
        this.loadCartCount();
      }
    });
  }

  ngOnDestroy(): void {
    if (this.cartSub) this.cartSub.unsubscribe();
  }

  // Metodi originali di Sofia
  checkAuthState(): void {
    const userId = localStorage.getItem('userId');
    this.isLoggedIn = !!userId;
    this.isAdmin = localStorage.getItem('isAdmin') === 'true';
    this.userName = localStorage.getItem('userName') || '';

    if (this.isLoggedIn) {
      this.loadCartCount();
    }
  }

  loadCartCount(): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.cartService.getUserCart(Number(userId)).subscribe({
        next: (items) => this.cartItemCount = items.length,
        error: (err) => console.error('Errore nel caricamento del carrello', err)
      });
    }
  }

  loadCategories(): void {
    this.categoriaService.getAllCategories().subscribe({
      next: (data) => this.categorie = data,
      error: (err) => console.error('Errore nel caricamento delle categorie', err)
    });
  }

  logout(): void {
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    localStorage.removeItem('isAdmin');

    this.isLoggedIn = false;
    this.isAdmin = false;
    this.userName = '';
    this.cartItemCount = 0;
  }

  // Metodi di Alberto
  goHomepage() {
    console.log('Navigating to homepage');
    this.router.navigate(['']);
  }

  openLoginPopup(): void {
    this.showPopup = true;
    console.log('Popup Opened - showPopup:', this.showPopup);
  }

  closeLoginPopup(): void {
    this.showPopup = false;
    console.log('Popup Closed - showPopup:', this.showPopup);
  }

  isAdminUser(): boolean {
    return this.userRole === 'admin';
  }

  isRegularUser(): boolean {
    return this.userRole === 'user';
  }
}
