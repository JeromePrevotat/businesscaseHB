import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  http = inject(HttpClient);
  userService = inject(UserService);
  public authService = inject(AuthService);
  currentUser: User | null = null;
  
  ngOnInit(){
    this.currentUser = this.authService.getCurrentUser;
    this.authService.user$.subscribe(user => {
      this.currentUser = user;
    });
  }
  onTestButtonClick() {
    return this.http.get<User>(`http://localhost:8080/api/users/me`).subscribe({
      next: (user) => {
        console.log(user);
      },
      error: (error) => {
        if (error.status === 403){
          this.authService.refresh();
        }
        console.error(error);
      }
    });
  }
}
