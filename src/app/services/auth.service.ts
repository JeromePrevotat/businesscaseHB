import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from 'express';
import { SsrService } from './ssr.service';
import { BehaviorSubject } from 'rxjs';
import { User } from '../models/user';
import { UserService } from './user.service';
import { AuthResponse } from '../models/auth-reponse';

export const accessTokenSubject = new BehaviorSubject<string | null>(null);

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = "http://localhost:8080/api/auth";
  private http = inject(HttpClient);
  private router = inject(Router);
  private ssrService = inject(SsrService);
  private userService = inject(UserService);

  // Subjects == emits its value when subscribed to
  private initializedSubject = new BehaviorSubject<boolean>(false);
  public initialized$ = this.initializedSubject.asObservable();

  private userSubject = new BehaviorSubject<User | null>(null);
  public user$ = this.userSubject.asObservable();

  private currentAccessToken$ = accessTokenSubject.asObservable();

  // private currentRefreshToken: string | null = null;
  private authenticated: boolean = false;

  constructor() {
    this.verifyAuth(null);
   }

  verifyAuth(redirectRoute: string | null) {
    // Prevents running this logic Server Side (because of SSR active)
    if (this.ssrService.isServerSide) return;
    
    const token: string | null = localStorage.getItem('token');
    if(token){
      this.userService.getUserByToken(token).subscribe({
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

  login({ username, password }: { username: string; password: string }){
    const response = this.http.post<AuthResponse>(`${this.apiUrl}/login`, { username, password });
    response.subscribe({
      next: (authResponse) => {
        console.log('Login successful, token received:', authResponse.accessToken);
        this.currentRefreshToken = authResponse.refreshToken;
        accessTokenSubject.next(authResponse.accessToken);
        this.authenticated = true;
        // Store the token in localStorage
        if(this.ssrService.isClientSide) localStorage.setItem('token', this.currentRefreshToken);
        this.userService.getUserByToken(this.currentRefreshToken).subscribe({
          next: (user) => {
            this.setCurrentUser = user;
          },
          error: (error) => {
            console.error('Error fetching user by token:', error);
          }
        });
      },
      error: (error) => {
        console.error('Login failed:', error);
      }
    });
  }

  logout() {
    if(this.ssrService.isClientSide) {
      localStorage.removeItem('token');
      this.setCurrentUser = null;
      this.router.navigate(['/login']);
    }
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
    return this.currentRefreshToken;
  }

  set currentRefreshToken(token: string | null) {
    this.currentRefreshToken = token;
  }

  get currentAccessToken(): string | null {
    return accessTokenSubject.value;
  }

  set currentAccessToken(token: string | null) {
    accessTokenSubject.next(token);
  }
}
