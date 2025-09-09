import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormService } from '../../services/form.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user';
import { UserService } from '../../services/user.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-security',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './security.component.html',
  styleUrl: './security.component.css'
})
export class SecurityComponent {
  authService = inject(AuthService);
  userService = inject(UserService);
  changePwdForm: FormGroup;
  isSubmitted = false;
  isLoading = false;
  currentUser: User | null = null;

  constructor(private fb: FormBuilder) {
    this.authService.user$
      // Cancel Subscription after the component is destroyed
      // Prevents Memory Leaks
      .pipe(takeUntilDestroyed())
      .subscribe(user =>{
        this.currentUser = user;
    })

    this.changePwdForm = this.fb.group({
      oldpwd: ['', [Validators.required, Validators.minLength(6)]],
      newpwd: ['', [Validators.required, Validators.minLength(6)]],
      newpwdconfirm: ['', [Validators.required, Validators.minLength(6)]]
    }, { validators: this.passwordMatchValidator });
  }

  submitPwdChange(): void {
    this.isSubmitted = true;
    console.log('Form submitted:', this.changePwdForm.value);
    if (this.changePwdForm.valid) {
      this.isLoading = true;
      console.log('Form submitted successfully:', this.changePwdForm.value);
      this.userService.changeUserPwd(this.currentUser, this.changePwdForm.value).subscribe({
        next: (response) => {
          console.log('Password changed successfully:', response);
          this.changePwdForm.reset();
          this.isLoading = false;
          this.isSubmitted = false;
        },
        error: (error) => {
          const oldpwd = this.changePwdForm.get("oldpwd");
          const newpwd = this.changePwdForm.get("newpwd");
          const newpwdconfirm = this.changePwdForm.get("newpwdconfirm");
          if (oldpwd && error.error && error.error.errors && error.error.errors.oldpwd) {
            oldpwd.setErrors({ server: error.error.errors.oldpwd });
          }
          if (newpwd && error.error && error.error.errors && error.error.errors.newpwd) {
            newpwd.setErrors({ server: error.error.errors.newpwd });
          }
          if (newpwdconfirm && error.error && error.error.errors && error.error.errors.newpwdconfirm) {
            newpwdconfirm.setErrors({ server: error.error.errors.newpwdconfirm });
          }
          this.isLoading = false;
        }
      });
    } else {
      console.error('Form is invalid:', this.changePwdForm.errors);
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
