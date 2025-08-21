import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { accessTokenSubject, AuthService } from '../services/auth.service';
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
          const currentAccessToken = authService.currentAccessToken;
          authService.refresh();
          accessTokenSubject.subscribe(newAccessToken => {
            if (newAccessToken && newAccessToken !== currentAccessToken) {
              console.log('Refresh successfull');
              next(req).subscribe({
                next: (retryResponse) => {
                  observer.next(retryResponse);
                  observer.complete();
                },
                error: (retryError) => {
                  observer.error(retryError);
                }
              });
            }
            else if (newAccessToken === null && currentAccessToken !== null){
              console.log('Refresh failed');
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
