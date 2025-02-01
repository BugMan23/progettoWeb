import { Component } from '@angular/core';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";

@Component({
  selector: 'app-homepage',
  templateUrl: '../Components/homepage.component.html',
  standalone: true,
  imports: [MatProgressSpinnerModule],
  styleUrl: '../Components/homepage.component.css'
})
export class HomepageComponent {
  constructor() {
    console.log("Homepage component caricato")
  }


}
