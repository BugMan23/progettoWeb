// login.component.ts
import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  @Output() loginSuccess = new EventEmitter<void>();

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  login(): void {
    this.http.post('http://localhost:8080/api/utenti/login', {
      email: this.email,
      password: this.password
    }).subscribe({
      next: (response: any) => {
        // Salva i dati dell'utente
        localStorage.setItem('userId', response.id.toString());
        localStorage.setItem('userName', response.nome);
        localStorage.setItem('isAdmin', response.isAdmin.toString());

        // Emetti l'evento di successo
        this.loginSuccess.emit();

        // Reindirizza alla home o alla pagina precedente
        this.router.navigate(['/']);
      },
      error: (error) => {
        this.errorMessage = 'Email o password non validi';
        console.error('Errore login:', error);
      }
    });
  }
}
