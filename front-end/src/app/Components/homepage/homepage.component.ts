import { Component } from '@angular/core';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-homepage',
  templateUrl: 'homepage.component.html',
  standalone: true,
  imports: [CommonModule, MatProgressSpinnerModule],
  styleUrl: 'homepage.component.css'
})
export class HomepageComponent {
  constructor() {
    console.log("Homepage component caricato")
  }


}
