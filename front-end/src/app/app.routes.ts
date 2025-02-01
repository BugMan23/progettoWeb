import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent} from './Components/homepage/homepage.component';

export const routes: Routes = [
  { path: '', component: HomepageComponent }, // Homepage visibile all'avvio
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
