import {Component, EventEmitter, OnInit, Output} from '@angular/core';
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
export class LoginComponent implements OnInit{
  @Output() close = new EventEmitter<void>();
  @Output() loginSuccess = new EventEmitter<void>();

  authForm!: FormGroup;
  isLoginMode = true;
  showLoginPopup: boolean = true;
  isSubmitting = false;


  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.authForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      ruolo: [false]
    });
    this.cambiaLarghezza(this.isLoginMode);
  }

  toggleMode() {
    this.isLoginMode = !this.isLoginMode;
    this.cambiaLarghezza(this.isLoginMode);

    if (!this.isLoginMode) {
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
      this.authForm.removeControl('nome');
      this.authForm.removeControl('cognome');
      this.authForm.removeControl('confirmPassword');
    }
  }

  authenticate() {
    const { nome, cognome, email, password, confirmPassword, ruolo } = this.authForm.value;

    if (this.authForm.invalid) {
      return;
    }


    if (!this.isLoginMode && password !== confirmPassword) {
      alert('Le password non coincidono!');
      return;
    }

    this.isSubmitting = true;

    if (this.isLoginMode) {
      this.authService.loginWithJWT(email, password).subscribe({
        next: (token) => {
          console.log('LOGIN OK, token:', token);

          // MODIFICA: Emetti evento di successo PRIMA di navigare
          this.loginSuccess.emit();

          // AGGIUNTA: Aspetta un momento per permettere all'interfaccia di aggiornarsi
          setTimeout(() => {
            // Chiudi il popup
            this.closePopup();

            // Naviga alla homepage
            this.router.navigate(['/']);
          }, 100);
        },
        error: (err) => {
          console.error('LOGIN ERROR:', err);
          alert('Credenziali errate');
        }
      });
    } else {
      const newUser = new User(nome, cognome, email, password, false);
      this.authService.register(newUser).subscribe({
        next: () => {
          alert('Registrazione avvenuta con successo!');
          this.isSubmitting = false;
          this.toggleMode();
        },
        error: (error) => {
          console.error('REGISTRATION ERROR:', error);
          this.isSubmitting = false;

          let errorMessage = 'Errore durante la registrazione';
          if (error.error && typeof error.error === 'string') {
            errorMessage += ': ' + error.error;
          } else if (error.status === 403) {
            errorMessage += ': Accesso negato';
          }

          alert(errorMessage);
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
