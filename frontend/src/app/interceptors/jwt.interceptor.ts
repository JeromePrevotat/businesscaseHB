import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { SsrService } from '../services/ssr.service';
import { Router } from '@angular/router';
import { accessTokenSubject } from '../services/auth.service';
import { INTERCEPTOR_PROTECTED_URL, INTERCEPTOR_PUBLIC_URL } from '../utils/interceptorUtils';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const ssrService = inject(SsrService);
  const router = inject(Router);
  
  const protectedUrls = INTERCEPTOR_PROTECTED_URL;

  const publicUrls = INTERCEPTOR_PUBLIC_URL;

  const requiresAuthentication = protectedUrls.some(url => req.url.startsWith(url));
  const isPublicUrl = publicUrls.some(url => req.url.includes(url));

  // SSR Check
  if(ssrService.isServerSide) return next(req);
  // Public URL pass
  if (isPublicUrl) return next(req);
  // Authentication check
  // Retrieves Token & check its validity
  const accessToken = accessTokenSubject.value;
  const isAuthenticated = accessToken && accessToken.length > 0;
  // Token present & Valid
  if (isAuthenticated) {
    const authRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${accessToken}`
      }
    });
    return next(authRequest);
  }
  return next(req);
};
