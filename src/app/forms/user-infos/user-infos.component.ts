import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-user-infos',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './user-infos.component.html',
  styleUrls: ['./user-infos.component.css']
})
export class UserInfosComponent {
  authService = inject(AuthService);
  userService = inject(UserService);
  userInfosForm: FormGroup;
  isSubmited = false;
  isLoading = false;
  currentUser: User | null = null;

  constructor(private fb: FormBuilder){
    this.authService.user$
          // Cancel Subscription after the component is destroyed
          // Prevents Memory Leaks
          .pipe(takeUntilDestroyed())
          .subscribe(user =>{
            this.currentUser = user;
        })

    this.userInfosForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      firstname: ['', [Validators.required, Validators.minLength(2)]],
      lastname: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      birthDate: ['', Validators.required],
    });
  }
  // CRUD Address, Media
  // Show
  // account valid
  // inscription date
}