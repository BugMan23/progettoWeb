import { Component, EventEmitter, Output } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule, FormGroup, Validators, FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { User } from '../../models/user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule
  ],
  standalone: true,
  styleUrls: ['./login.component.css', '../../../styles.css']
})
export class LoginComponent {
  @Output() close = new EventEmitter<void>();

  authForm!: FormGroup;
  isLoginMode = true;

  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    // Inizialmente (login) creiamo un form con i soli campi necessari
    this.authForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      ruolo: [false]
    });
    // Se preferisci, assegna la larghezza "login"
    this.cambiaLarghezza(this.isLoginMode);
  }

  toggleMode() {
    this.isLoginMode = !this.isLoginMode;
    this.cambiaLarghezza(this.isLoginMode);

    if (!this.isLoginMode) {
      // Passiamo a registrazione: aggiungiamo i campi
      this.authForm.addControl(
        'nome',
        this.fb.control('', Validators.required)
      );
      this.authForm.addControl(
        'cognome',
        this.fb.control('', Validators.required)
      );
      this.authForm.addControl(
        'confirmPassword',
        this.fb.control('', Validators.required)
      );
    } else {
      // Torniamo a login: rimuoviamo i campi
      this.authForm.removeControl('nome');
      this.authForm.removeControl('cognome');
      this.authForm.removeControl('confirmPassword');
    }
  }

  authenticate() {
    const { nome, cognome, email, password, confirmPassword, ruolo } = this.authForm.value;

    if (!this.isLoginMode && password !== confirmPassword) {
      alert('Le password non coincidono!');
      return;
    }

    if (this.isLoginMode) {
      this.authService.login(email, password).subscribe({
        next: (token) => {
          console.log('LOGIN OK, token:', token);
          localStorage.setItem('token', token);
          this.router.navigate(['']);
          this.closePopup();
        },
        error: (err) => {
          console.error('LOGIN ERROR:', err);
          alert('Credenziali errate');
        }
      });
    } else {
      const newUser = new User(nome, cognome, email, password, ruolo);
      this.authService.register(newUser).subscribe({
        next: () => {
          alert('Registrazione avvenuta con successo!');
          this.toggleMode();
          this.closePopup();
        },
        error: (error) => {
          console.error(error);
          alert('Errore durante la registrazione: ' + JSON.stringify(error));
        }
      });
    }
  }

  cambiaLarghezza(isLogin: boolean) {
    const container = document.querySelector(".container-fluidos") as HTMLElement;
    if (!container) return;
    container.style.maxWidth = isLogin ? "35%" : "60%";
  }

  togglePassword(fieldId: string) {
    const input = document.getElementById(fieldId) as HTMLInputElement;
    input.type = input.type === "password" ? "text" : "password";
  }

  closePopup() {
    this.close.emit();
  }
}
