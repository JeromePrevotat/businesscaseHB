import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';

export const refreshInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  // New Observable handling Responses
  return new Observable(observer => {
    
    // Send original request
    next(req).subscribe({
      next: (response) => {
        // Request passed
        observer.next(response);
        observer.complete();
      },
      error: (error: HttpErrorResponse) => {
        // Error detected
        if (error.status === 403) {
          console.log('403 trying to refresh Access Token');
          authService.refresh().subscribe({
            next: (authResponse) => {
              console.log('Refresh successful');
              // Retry original request with new token
              next(req).subscribe({
                next: (retryResponse) => {
                  observer.next(retryResponse);
                  observer.complete();
                },
                error: (retryError) => {
                  observer.error(retryError);
                }
              });
            },
            error: (refreshError) => {
              console.log('Refresh failed');
              authService.logout();
              observer.error(refreshError);
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
