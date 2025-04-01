import { Injectable } from '@angular/core';
import { Router, CanActivateFn, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {
  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const userId = localStorage.getItem('userId');

    if (!userId) {
      this.router.navigate(['/login'], {
        queryParams: { returnUrl: state.url }
      });
      return false;
    }

    if (route.data['requiresAdmin']) {
      const isAdmin = localStorage.getItem('isAdmin') === 'true';

      if (!isAdmin) {
        this.router.navigate(['/']);
        return false;
      }
    }

    return true;
  }
}
