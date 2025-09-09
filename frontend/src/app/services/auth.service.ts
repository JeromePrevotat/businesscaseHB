import { HttpBackend, HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { SsrService } from './ssr.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../models/user';
import { AuthResponse } from '../models/auth-reponse';
import { ROUTE_PATHS } from '../utils/routeMapping';
import { API_URL } from '../utils/apiUrl';

export const accessTokenSubject = new BehaviorSubject<string | null>(null);

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private router = inject(Router);
  private ssrService = inject(SsrService);
  private httpBackend = inject(HttpBackend);
  private http = new HttpClient(this.httpBackend);

  // Subjects == emits its value when subscribed to
  private initializedSubject = new BehaviorSubject<boolean>(false);
  public initialized$ = this.initializedSubject.asObservable();

  private userSubject = new BehaviorSubject<User | null>(null);
  public user$ = this.userSubject.asObservable();

  private _currentRefreshToken: string | null = null;
  private currentAccessToken$ = accessTokenSubject.asObservable();

  private authenticated: boolean = false;

  constructor() {
    this.verifyAuth(ROUTE_PATHS.home);
   }

  verifyAuth(redirectRoute: string | null) {
    // Prevents running this logic Server Side (because of SSR active)
    if (this.ssrService.isServerSide) return;
    
    const token: string | null = localStorage.getItem('token');
    if(token){
      this.getUserByToken(token).subscribe({
        next: (data: User) => {
          const user: User = data;
          this.setCurrentUser = user;
          this.isInitialized = true;
          // Redirect to the specified route after successful authentication
          if (redirectRoute) {
            this.router.navigate([redirectRoute]);
          }
        },
        error: (error) => {
          console.error('Error fetching user by token:', error);
          this.isInitialized = false;
          this.logout();
        }
      });
    } else {
      this.isInitialized = true;
    }
  }

  login({ username, password }: { username: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${API_URL.AUTH}/login`, { username, password });
  }

  handleLoginSuccess(authResponse: AuthResponse): void {
    console.log('Login successful, token received:', authResponse.accessToken);
      this.currentRefreshToken = authResponse.refreshToken;
      accessTokenSubject.next(authResponse.accessToken);
      this.authenticated = true;
      if(this.ssrService.isClientSide) localStorage.setItem('token', this.currentRefreshToken);
      
      this.getUserByToken(this.currentRefreshToken).subscribe({
        next: (user) => {
          this.setCurrentUser = user;
          this.router.navigate([ROUTE_PATHS.home]);
        },
        error: (error) => {
          console.error('Error fetching user by token:', error);
        }
      });
  }

  refresh(): void {
    const refreshToken = localStorage.getItem('token');
    if (!refreshToken){
      // No Refresh Token, redirect to login
      this.router.navigate([ROUTE_PATHS.login]);
      return;
    }
    const response = this.http.post<AuthResponse>(`${API_URL.AUTH}/refresh`, { token: refreshToken });
    response.subscribe({
      next: (authResponse) => {
        console.log('Access Token refreshed successfully:', authResponse.accessToken);
        this.currentAccessToken = authResponse.accessToken;
        accessTokenSubject.next(authResponse.accessToken);
      },
      error: (error) => {
        console.error('Error refreshing access token:', error);
        this.logout();
      }
    });
  }

  logout() {
    if(this.ssrService.isClientSide) {
      // Clean tokens
      localStorage.removeItem('token');
      this._currentRefreshToken = null;
      accessTokenSubject.next(null);
      this.currentAccessToken = null;
      // Clean User
      this.setCurrentUser = null;
      // Set Authenticated to False
      this.isAuthenticated = false;

      this.router.navigate([ROUTE_PATHS.home]);
    }
  }

  confirmAccount(user_id: number, code: string): Observable<any> {
    return this.http.post<any>(`${API_URL.AUTH}/account-confirmation`, { user_id, code });
  }

  getUserByToken(token: string): Observable<User> {
    return this.http.get<User>(`${API_URL.USERS}/me`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }

  get getCurrentUser(): User | null {
    return this.userSubject.value;
  }

  set setCurrentUser(user: User | null) {
    this.userSubject.next(user);
  }

  get isInitialized(): boolean {
    return this.initializedSubject.value;
  }

  set isInitialized(value: boolean) {
    this.initializedSubject.next(value);
  }

  get isAuthenticated(): boolean {
    return this.authenticated;
  }

  set isAuthenticated(value: boolean) {
    this.authenticated = value;
  }

  get currentRefreshToken(): string | null {
    return this._currentRefreshToken;
  }

  set currentRefreshToken(token: string | null) {
    this._currentRefreshToken = token;
  }

  get currentAccessToken(): string | null {
    return accessTokenSubject.value;
  }

  set currentAccessToken(token: string | null) {
    accessTokenSubject.next(token);
  }
}
