import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { error } from 'console';
import { FormService } from '../../services/form.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user';

@Component({
  selector: 'app-account-confirmation',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './account-confirmation.component.html',
  styleUrl: './account-confirmation.component.css'
})

export class AccountConfirmationComponent {
  confirmationForm: FormGroup;
  authService = inject(AuthService);
  isSubmitted = false;
  isLoading = false;

  constructor(private fb: FormBuilder) {
    this.confirmationForm = this.fb.group({
      confirmationCode: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]],
    });
  }

  submitCode(){
    this.isSubmitted = true;
      if (this.confirmationForm.valid) {
        this.isLoading = true;
        console.log('Form submitted:', this.confirmationForm.value);
        if (this.authService.getCurrentUser) {
          const user_id = this.authService.getCurrentUser.id;
        const code = this.confirmationForm.value;

        this.authService.confirmAccount(user_id!, code).subscribe({
          next: (response) => {
            console.log('Confirmation successful:', response);
            this.isLoading = false;
            this.isSubmitted = false;
            this.confirmationForm.reset();
          },
          error: (error) => {
            console.error('Confirmation failed:', error);
            this.isLoading = false;
  
            const code = this.confirmationForm.get("code");
  
            if (error.error && error.error.errors) {
              if (code && error.error.errors.code) {
                code.setErrors({ server: error.error.errors.code });
              }
            }
          }
        });
      } else {
        console.error('Form is invalid', this.confirmationForm.errors);
      }
    }
  }
  
    isFieldInvalid(fieldName: string): boolean {
      return FormService.isFieldInvalid(this.confirmationForm, fieldName, this.isSubmitted);
    }
  
    getFieldError(fieldName: string): string {
      return FormService.getFieldError(this.confirmationForm, fieldName);
    }
}
