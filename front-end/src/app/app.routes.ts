import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./Components/homepage/homepage.component')
      .then(m => m.HomepageComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./Components/login/login.component')
      .then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./Components/register/register.component')
      .then(m => m.RegisterComponent)
  },
  {
    path: '**',
    redirectTo: ''
  }
];
