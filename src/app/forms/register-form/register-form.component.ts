import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user';
import { FormService } from '../../services/form.service';
import { Router } from '@angular/router';
import { ROUTE_PATHS } from '../../utils/routeMapping';
import { AuthResponse } from '../../models/auth-reponse';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-register-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './register-form.component.html',
  styleUrl: './register-form.component.css'
})
export class RegisterFormComponent {
  userService = inject(UserService);
  authService = inject(AuthService);
  registerForm: FormGroup;
  router = inject(Router);
  isSubmitted = false;
  isLoading = false;

  constructor(private fb: FormBuilder) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      firstname: ['', [Validators.required, Validators.minLength(2)]],
      lastname: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      birthdate: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
    }, { validators: this.passwordMatchValidator });
  }

  submitCreateUser(){
    this.isSubmitted = true;
    console.log('Form submitted:', this.registerForm.value);
    if (this.registerForm.valid) {
      this.isLoading = true;
      console.log('Form submitted successfully:', this.registerForm.value);
      const newUser: Partial<User> = this.registerForm.value;
      this.userService.createUser(newUser).subscribe({
        next: (response) => {
          console.log('User created successfully:', response);
          // Auto Login
          this.autoLogin().subscribe({
            next: (response) => {
              console.log('Auto login successful:', response);
              this.registerForm.reset();
              this.isLoading = false;
              this.isSubmitted = false;
              this.router.navigate([ROUTE_PATHS.home]);
            },
            error: (error) => {
              console.error('Error during auto login:', error);
              this.registerForm.reset();
              this.isLoading = false;
              this.isSubmitted = false;
              this.router.navigate([ROUTE_PATHS.login]);
            }
          });
        },
        error: (error) => {
          console.log("USER: ", newUser);
          console.error('Error creating user:', error);
          const username = this.registerForm.get("username");
          const email = this.registerForm.get("email");
          const birthdate = this.registerForm.get("birthdate");
          if (username && error.error && error.error.errors && error.error.errors.username) {
            username.setErrors({ server: error.error.errors.username });
          }
          if (email && error.error && error.error.errors && error.error.errors.email) {
            email.setErrors({ server: error.error.errors.email });
          }
          if (birthdate && error.error && error.error.errors && error.error.errors.birthdate) {
            birthdate.setErrors({ server: error.error.errors.birthdate });
          }
          this.isLoading = false;
        }
      });
    } else {
      console.error('Form is invalid:', this.registerForm.errors);
    }
  }

  autoLogin(): Observable<AuthResponse>{
      return this.authService.login({username: this.registerForm.value.username,
                                      password: this.registerForm.value.password});
  }

  private passwordMatchValidator(formGroup: FormGroup): { [key: string]: boolean } | null {
    const password = formGroup.get('password');
    const confirmPassword = formGroup.get('confirmPassword');
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ 'passwordMismatch': true });
    }
    // Reset error message
    else if (confirmPassword && confirmPassword.hasError('passwordMismatch')) {
      confirmPassword.setErrors(null);
    }
    return null;
  }

  get getIsSubmitted(): boolean{
    return this.isSubmitted;
  }

  isFieldInvalid(fieldName: string): boolean {
    return FormService.isFieldInvalid(this.registerForm, fieldName, this.isSubmitted);
  }

  getFieldError(fieldName: string): string {
    return FormService.getFieldError(this.registerForm, fieldName);
  }
}
