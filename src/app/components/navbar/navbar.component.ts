import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Observable } from 'rxjs';
import { User } from '../../models/user';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  private authService = inject(AuthService);
  currentUser: User | null = null;

  ngOnInit(){
    this.currentUser = this.authService.getCurrentUser;
    this.authService.user$.subscribe(user => {
      this.currentUser = user;
    });
  }
}
