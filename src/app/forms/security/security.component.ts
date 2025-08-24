import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormService } from '../../services/form.service';

@Component({
  selector: 'app-security',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './security.component.html',
  styleUrl: './security.component.css'
})
export class SecurityComponent {
  changePwdForm: FormGroup;
  isSubmitted = false;
  isLoading = false;

  constructor(private fb: FormBuilder) {
    this.changePwdForm = this.fb.group({
      oldpwd: ['', [Validators.required, Validators.minLength(6)]],
      newpwd: ['', [Validators.required, Validators.minLength(6)]],
      newpwdconfirm: ['', [Validators.required, Validators.minLength(6)]]
    }, { validators: this.passwordMatchValidator });
  }

  submitPwdChange(): void {
    this.isSubmitted = true;
    if (this.changePwdForm.valid) {
      // Handle password change logic here
    }
  }

  private passwordMatchValidator(formGroup: FormGroup): { [key: string]: boolean } | null {
      const password = formGroup.get('newpwd');
      const confirmPassword = formGroup.get('newpwdconfirm');
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
      return FormService.isFieldInvalid(this.changePwdForm, fieldName, this.isSubmitted);
    }
  
    getFieldError(fieldName: string): string {
      return FormService.getFieldError(this.changePwdForm, fieldName);
    }

}
