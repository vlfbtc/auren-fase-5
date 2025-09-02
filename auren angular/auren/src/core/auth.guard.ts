import { inject, PLATFORM_ID } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { isPlatformBrowser } from '@angular/common';

export const authGuard: CanActivateFn = () => {
  const platformId = inject(PLATFORM_ID);
  if (!isPlatformBrowser(platformId)) return true; // SSR: permite tudo

  const auth = inject(AuthService);
  const router = inject(Router);

  // Permite acesso livre à rota de login
  if (router.url.startsWith('/login')) return true;

  const logged = auth.isLogged();
  if (logged) return true;

  router.navigate(['/login']);
  return false;
};