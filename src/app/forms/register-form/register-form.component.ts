import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user';
import { FormService } from '../../services/form.service';

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
        },
        error: (error) => {
          console.log("USER: ", newUser);
          console.error('Error creating user:', error);
        }
      });
      this.isLoading = false;
      this.registerForm.reset();
      this.isSubmitted = false;
    } else {
      console.error('Form is invalid:', this.registerForm.errors);
    }
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
