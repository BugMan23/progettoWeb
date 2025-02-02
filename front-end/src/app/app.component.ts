import { Component } from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {HomepageComponent} from './Components/homepage/homepage.component';
import {CommonModule} from '@angular/common';
import {HeaderComponent} from './Components/CommonComponents/header/header.component';
import {FooterComponent} from './Components/CommonComponents/footer/footer.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  imports: [RouterOutlet, CommonModule, HomepageComponent, HeaderComponent, FooterComponent],
  standalone: true,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'front-end';

}
