import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
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
}
