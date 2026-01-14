import { Component, inject, OnInit } from '@angular/core';
import { SsrService } from '../../services/ssr.service';

@Component({
  selector: 'app-cookie-popup',
  standalone: true,
  imports: [],
  templateUrl: './cookie-popup.component.html',
  styleUrl: './cookie-popup.component.css'
})
export class CookiePopupComponent implements OnInit {
  private ssrService = inject(SsrService);
  showBanner = false;

  ngOnInit(): void {
    if (this.ssrService.isClientSide) {
      const consent = localStorage.getItem('cookieConsent');
      this.showBanner = !consent;
    }
  }

  acceptCookies(): void {
    if (this.ssrService.isClientSide) {
      localStorage.setItem('cookieConsent', 'accepted');
      this.showBanner = false;
    }
  }

  declineCookies(): void {
    if (this.ssrService.isClientSide) {
      localStorage.setItem('cookieConsent', 'declined');
      this.showBanner = false;
    }
  }
}
