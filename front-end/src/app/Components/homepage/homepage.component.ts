import { Component } from '@angular/core';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {CommonModule} from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-homepage',
  templateUrl: 'homepage.component.html',
  standalone: true,
  imports: [CommonModule, MatProgressSpinnerModule],
  styleUrl: 'homepage.component.css'
})
export class HomepageComponent {

  userRole: string | null = null;

  constructor(private authService: AuthService) {
    console.log("Homepage component caricato")
    this.userRole = this.authService.getUserRole() ? 'admin' : 'user';
  }

  isAdmin(): boolean {
    return this.userRole === 'admin';
  }

  isUser(): boolean {
    return this.userRole === 'user';
  }
}
