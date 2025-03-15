import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { CategoriaService } from '../../../services/categoria.service';
import { CarrelloService } from '../../../services/carrello.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule], // Aggiungi CommonModule e RouterModule
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {
  isLoggedIn: boolean = false;
  isAdmin: boolean = false;
  userName: string = '';
  cartItemCount: number = 0;
  categorie: any[] = [];

  private cartSub!: Subscription;

  constructor(
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
  }
}
