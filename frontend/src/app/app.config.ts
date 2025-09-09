import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideServerRendering } from '@angular/platform-server';
import { jwtInterceptor } from './interceptors/jwt.interceptor';
import { refreshInterceptor } from './interceptors/refresh.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideServerRendering(),
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes), 
    provideClientHydration(),
    provideHttpClient(
      withFetch(),
      withInterceptors([
        jwtInterceptor,
        refreshInterceptor
      ])
    )
  ]
};
