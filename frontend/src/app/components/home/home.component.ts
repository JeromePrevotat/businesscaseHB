import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user';
import { API_URL } from '../../utils/apiUrl';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent{
  http = inject(HttpClient);
  userService = inject(UserService);
  public authService = inject(AuthService);
  currentUser: User | null = null;
  
  constructor() {
    this.authService.user$
      // Cancel Subscription after the component is destroyed
      // Prevents Memory Leaks
      .pipe(takeUntilDestroyed())
      .subscribe(user =>{
        this.currentUser = user;
    })
  }

  onTestButtonClick() {
    return this.http.get<User>(`${API_URL.USERS}/me`).subscribe({
      next: (user) => {
        console.log(user);
      },
      error: (error) => {
        console.log(error)
      }
    });
  }
}
