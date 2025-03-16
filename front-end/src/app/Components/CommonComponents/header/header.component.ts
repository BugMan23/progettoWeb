import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { CategoriaService } from '../../../services/categoria.service';
import { CarrelloService } from '../../../services/carrello.service';
import { AuthService } from '../../../services/auth.service';
import { LoginComponent } from '../../login/login.component';

// TODO: AGGIUNGERE LA POSSIBILITA DI FAR REGISTRARE L'UTENTE, SISTEMARE LA GRAFICA

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, LoginComponent, RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {
  // Proprietà
  isLoggedIn: boolean = false;
  isAdmin: boolean = false;
  userName: string = '';
  cartItemCount: number = 0;
  categorie: any[] = [];

  // Popup login
  showPopup: boolean = false;
  userRole: string | null = null;

  private cartSub!: Subscription;

  constructor(
    private router: Router,
    protected authService: AuthService,
    private categoriaService: CategoriaService,
    private cartService: CarrelloService
  ) { }

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

    // Redirect alla homepage
    this.router.navigate(['/']);
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
