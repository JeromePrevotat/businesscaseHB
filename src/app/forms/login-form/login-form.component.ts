import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { FormService } from '../../services/form.service';
import { Router } from '@angular/router';
// import { ROUTE_PATHS } from '../../utils/routeMapping';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.css'
})
export class LoginFormComponent {
  userService = inject(UserService);
  authService = inject(AuthService);
  loginForm: FormGroup;
  router = inject(Router);
  isSubmitted = false;
  isLoading = false;

  constructor(private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

    submitLogin(){
    this.isSubmitted = true;
    if (this.loginForm.valid) {
      this.isLoading = true;
      console.log('Form submitted:', this.loginForm.value);
      const { username, password } = this.loginForm.value;
      this.authService.login({ username, password }).subscribe({
        next: (response) => {
          console.log('Login successful:', response);
          this.isLoading = false;
          this.isSubmitted = false;
          this.loginForm.reset();
        },
        error: (error) => {
          console.error('Login failed:', error);
          this.isLoading = false;

          const username = this.loginForm.get("username");
          const password = this.loginForm.get("password");

          if (error.error && error.error.errors) {
            if (username && error.error.errors.username) {
              username.setErrors({ server: error.error.errors.username });
            }
            if (password && error.error.errors.password) {
              password.setErrors({ server: error.error.errors.password });
            }
          } else {
            // Erreur générale (credentials invalides)
            if (username) username.setErrors({ server: 'Invalid credentials' });
          }
        }
      });
    } else {
      console.error('Form is invalid', this.loginForm.errors);
    }
  }

  isFieldInvalid(fieldName: string): boolean {
    return FormService.isFieldInvalid(this.loginForm, fieldName, this.isSubmitted);
  }

  getFieldError(fieldName: string): string {
    return FormService.getFieldError(this.loginForm, fieldName);
  }
}
