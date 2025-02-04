import { Component } from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../../../services/auth.service';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-header',
  imports: [CommonModule],
  templateUrl: './header.component.html',
  standalone: true,
  styleUrls: ['./header.component.css', '../../../../styles.css']
})
export class HeaderComponent {

  constructor(private router : Router, protected authService: AuthService) {
  }

  goHomepage(){
    this.router.navigate(['']);
  }

  openLoginPopup(): void {
    // Logica per aprire una modale di login
    console.log('Apertura popup di login...');
  }


}
