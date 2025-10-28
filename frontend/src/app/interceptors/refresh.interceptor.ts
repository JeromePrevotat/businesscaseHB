import { HttpErrorResponse, HttpInterceptorFn, HttpResponse, HttpClient, HttpBackend } from '@angular/common/http';
import { accessTokenSubject, AuthService } from '../services/auth.service';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { INTERCEPTOR_PROTECTED_URL } from '../utils/interceptorUtils';
import { SsrService } from '../services/ssr.service';
import { ROUTE_PATHS } from '../utils/routeMapping';
import { API_URL } from '../utils/apiUrl';
import { Router } from '@angular/router';
import { RefreshResponse } from '../models/refresh-response';

export const refreshInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const ssrService = inject(SsrService);
  const router = inject(Router);
  const httpBackend = inject(HttpBackend);
  const http = new HttpClient(httpBackend); // Bypass interceptors for refresh request
  
  const protectedUrls = INTERCEPTOR_PROTECTED_URL;
  const requiresAuthentication = protectedUrls.some(url => req.url.includes(url));
  
  // SSR Check
  if(ssrService.isServerSide) return next(req);

  // Protected URL needs an Access Token
  if (requiresAuthentication) {
    // New Observable handling Responses
    return new Observable(observer => {
      
      // Send original request (JWT interceptor will add the token)
      next(req).subscribe({
        next: (response) => {
          // Request passed, ignore status 0 SENT
          if (response instanceof HttpResponse) {
            observer.next(response);
            observer.complete();
          }
        },
        error: (error: HttpErrorResponse) => {
          // Error detected
          if (error.status === 401) {
            console.log(`${error.status} trying to refresh Access Token`);
            const currentAccessToken = authService.currentAccessToken;
            
            // Get refresh token
            const refreshToken = localStorage.getItem('token');
            if (!refreshToken) {
              console.log('No refresh token found, redirecting to login');
              accessTokenSubject.next(null);
              router.navigate([ROUTE_PATHS.login]);
              observer.error(error);
              return;
            }

            // Refresh Access Token using HttpBackend to avoid circular dependency
            http.post<RefreshResponse>(`${API_URL.AUTH}/refresh`, { token: refreshToken }).subscribe({
              next: (response) => {                
                const newAccessToken = response.token;
                if (!newAccessToken) {
                  console.error('No access token in refresh response:', response);
                  accessTokenSubject.next(null);
                  localStorage.removeItem('token');
                  router.navigate([ROUTE_PATHS.login]);
                  observer.error(error);
                  return;
                }
                // Update token
                console.log('Access Token refreshed successfully:', newAccessToken);
                accessTokenSubject.next(newAccessToken);
                
                // Subscribe to token changes and retry when updated
                const subscription = accessTokenSubject.subscribe(newToken => {
                  if (newToken && newToken === newAccessToken) {
                    console.log('Refresh successful, retrying with new token:', newToken);
                    // Clone request to update token
                    const retryReq = req.clone({
                      setHeaders: { Authorization: `Bearer ${newToken}` }
                    });
                    // Send the updated request
                    next(retryReq).subscribe({
                      next: (retryResponse) => {
                        if (retryResponse instanceof HttpResponse) {
                          observer.next(retryResponse);
                          observer.complete();
                          subscription.unsubscribe();
                        }
                      },
                      error: (retryError) => {
                        observer.error(retryError);
                        subscription.unsubscribe();
                      }
                    });
                  }
                });
              },
              error: (refreshError) => {
                console.error('Error refreshing access token:', refreshError);
                accessTokenSubject.next(null);
                localStorage.removeItem('token');
                router.navigate([ROUTE_PATHS.login]);
                observer.error(error);
              }
            });
          } else {
            // Other errors to handle
            observer.error(error);
          }
        }
      });
    });
  }
  // Public URL doesnt need any Access Token
  return next(req);
}
