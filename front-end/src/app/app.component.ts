import { Component } from '@angular/core';
import {Router, RouterOutlet } from '@angular/router';
import {HomepageComponent} from './Components/homepage/homepage.component';

@Component({
  selector: 'app-root',
  //templateUrl: './app.component.html',
  template: '<router-outlet></router-outlet>',
  imports: [
    RouterOutlet
  ],
  standalone: true,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'front-end';
  constructor(private router : Router) {
    this.router.navigate(['/']);
  }
}
