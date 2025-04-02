import { Component } from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {CommonModule} from '@angular/common';
import {HeaderComponent} from './Components/CommonComponents/header/header.component';
import {FooterComponent} from './Components/CommonComponents/footer/footer.component';
import {AuthInterceptor} from './interceptors/auth.interceptors';
import {HTTP_INTERCEPTORS} from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  imports: [RouterOutlet, CommonModule, HeaderComponent, FooterComponent],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }  // ✔️ REGISTRA INTERCEPTOR
  ],
  standalone: true,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'front-end';


}
