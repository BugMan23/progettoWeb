import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { CommonModule } from '@angular/common';
import { LoginComponent } from '../../login/login.component';

@Component({
  selector: 'app-header',
  imports: [CommonModule, LoginComponent],
  templateUrl: './header.component.html',
  standalone: true,
  styleUrls: ['./header.component.css', '../../../../styles.css']
})
export class HeaderComponent {
  showPopup = false;
  userRole: string | null = null;

  constructor(private router: Router, protected authService: AuthService) {
    this.userRole = this.authService.getUserRole() ? 'admin' : 'user';
    console.log('HeaderComponent Initialized - showPopup:', this.showPopup);
  }

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

  isAdmin(): boolean {
    return this.userRole === 'admin';
  }

  isUser(): boolean {
    return this.userRole === 'user';
  }
}
