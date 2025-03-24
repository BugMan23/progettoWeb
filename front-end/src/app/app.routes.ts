import { Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  // Homepage
  {
    path: '',
    loadComponent: () => import('./Components/homepage/homepage.component').then(m => m.HomepageComponent)
  },

  // Autenticazione
  {
    path: 'login',
    loadComponent: () => import('./Components/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./Components/register/register.component').then(m => m.RegisterComponent)
  },

  // Catalogo e prodotti
  {
    path: 'catalogo',
    loadComponent: () => import('./Components/catalogo/catalogo.component').then(m => m.CatalogoComponent)
  },
  {
    path: 'prodotto/:id',
    loadComponent: () => import('./Components/product-detail/product-detail.component').then(m => m.DettaglioProdottoComponent)
  },

  // Carrello e checkout - protetti da AuthGuard
  {
    path: 'carrello',
    loadComponent: () => import('./Components/cart/cart.component').then(m => m.CartComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'checkout',
    loadComponent: () => import('./Components/checkout/checkout.component').then(m => m.CheckoutComponent),
    canActivate: [AuthGuard]
  },

  // Profilo utente
  {
    path: 'profilo',
    loadComponent: () => import('./Components/profilo/profilo.component').then(m => m.ProfiloComponent),
    canActivate: [AuthGuard]
  },

  // Visualizzazione ordine specifico - usa lo stesso componente OrdiniComponent
  {
    path: 'ordini/:id',
    loadComponent: () => import('./Components/profilo/ordini/ordini.component').then(m => m.OrdiniComponent),
    canActivate: [AuthGuard]
  },

  // Area admin
  {
    path: 'admin',
    loadComponent: () => import('./Components/admin/admin.component').then(m => m.AdminComponent),
    canActivate: [AuthGuard],
    data: { requiresAdmin: true },
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () => import('./Components/admin/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'prodotti',
        loadComponent: () => import('./Components/admin/prodotti/prodotti.component').then(m => m.ProdottiComponent)
      },
      {
        path: 'ordini',
        loadComponent: () => import('./Components/admin/ordini/ordini.component').then(m => m.OrdiniComponent)
      },
      {
        path: 'utenti',
        loadComponent: () => import('./Components/admin/utenti/utenti.component').then(m => m.UtentiComponent)
      }
    ]
  },

  // Fallback per rotte non trovate
  {
    path: '**',
    redirectTo: ''
  }
];
