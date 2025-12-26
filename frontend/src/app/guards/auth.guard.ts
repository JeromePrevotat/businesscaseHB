import { inject } from '@angular/core';
import { CanActivateFn, ActivatedRouteSnapshot, RouterStateSnapshot, RedirectCommand, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { ROUTE_PATHS } from '../utils/routeMapping';

export const AuthGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated) {
    return new RedirectCommand(router.parseUrl(ROUTE_PATHS.login));
  }
  return true;
};
