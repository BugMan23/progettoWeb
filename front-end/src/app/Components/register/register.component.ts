import { Component } from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {User} from '../../models/user';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  standalone: true,
  imports: [
    FormsModule
  ],
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  nome = '';
  cognome = '';
  email = '';
  password = '';

  constructor(private authService: AuthService, private router : Router) {}

  register(){
    const user = new User(this.nome, this.cognome, this.email, this.password);
    this.authService.register(user).subscribe(
      () => this.router.navigate(['/login']),
      (error) => alert('Errore nella registrazione')
    );
  }
}
