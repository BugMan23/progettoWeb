import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { CarrelloService } from '../../../services/carrello.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ]
})
export class HeaderComponent implements OnInit {
  isLoggedIn: boolean = false;
  userName: string | null = null;
  isAdmin: boolean = false;
  numeroArticoli: number = 0;

  constructor(
    private router: Router,
    private authService: AuthService,
    private carrelloService: CarrelloService
  ) { }

  ngOnInit() {
    this.checkLoginStatus();
    if (this.isLoggedIn) {
      this.updateCarrello();
    }
  }

  private checkLoginStatus() {
    const userId = localStorage.getItem('userId');
    this.userName = localStorage.getItem('userName');
    this.isAdmin = localStorage.getItem('userRole') === 'admin';
    this.isLoggedIn = !!userId;
  }

  private updateCarrello() {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.carrelloService.getUserCart(Number(userId)).subscribe({
        next: (prodotti) => {
          this.numeroArticoli = prodotti.length;
        },
        error: (error) => {
          console.error('Errore nel recupero del carrello:', error);
        }
      });
    }
  }

  logout() {
    localStorage.clear();
    this.isLoggedIn = false;
    this.userName = null;
    this.isAdmin = false;
    this.router.navigate(['/login']);
  }
}
