import { Injectable } from '@angular/core';
import { Router, CanActivateFn, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {
  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    // Verifica se l'utente è loggato
    const userId = localStorage.getItem('userId');

    if (!userId) {
      // Utente non loggato, redirect al login
      this.router.navigate(['/login'], {
        queryParams: { returnUrl: state.url }
      });
      return false;
    }

    // Verifica se la rotta richiede i privilegi di admin
    if (route.data['requiresAdmin']) {
      const isAdmin = localStorage.getItem('userRole') === 'admin';

      if (!isAdmin) {
        // Utente non è admin, redirect alla home
        this.router.navigate(['/']);
        return false;
      }
    }

    return true;
  }
}
