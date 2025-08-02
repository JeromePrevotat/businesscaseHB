import { isPlatformBrowser } from '@angular/common';
import { inject, Injectable, PLATFORM_ID } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SsrService {
  platformId = inject(PLATFORM_ID);

  constructor() { }

  // Check if the platform is server-side
  checkIsServerSide(): boolean {
    return !isPlatformBrowser(this.platformId);
  }

  // Check if the platform is client-side
  checkIsClientSide(): boolean {
    return isPlatformBrowser(this.platformId);
  }

  get isServerSide(): boolean {
    return this.checkIsServerSide();
  }

  get isClientSide(): boolean {
    return this.checkIsClientSide();
  }
}
