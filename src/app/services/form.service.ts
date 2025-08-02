import { Injectable } from '@angular/core';
import { Form, FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class FormService {

  static isFieldInvalid(form: FormGroup | null, fieldName: string, isSubmitted: boolean): boolean {
    if (!form) return true;
    const field = form.get(fieldName);
    // Retourne true si TOUTES ces conditions sont vraies :
    //    champ existe ET champ invalide ET (champ dirty OU touched OU formulaire est soumis)
    return Boolean(field && field.invalid && (field.dirty || field.touched || isSubmitted));
  }

  static getFieldError(form: FormGroup | null, fieldName: string): string {
    if (!form) return '';
    const field = form.get(fieldName);
    if (field && field.errors) {
      if (field.errors['required']) return `${fieldName} is required`;
      if (field.errors['minlength']) return `Minimum ${field.errors['minlength'].requiredLength} characters`;
      if (field.errors['maxlength']) return `Maximum ${field.errors['maxlength'].requiredLength} characters`;
      if (field.errors['email']) return `Invalid email format`;
      if (field.errors['pattern']) return `Invalid format`;
      if (field.errors['passwordMismatch']) return `Passwords do not match`;

      // Fallback for any other errors
      return `${fieldName} is invalid`;
    }
      return '';
  }

  // Claude suggestions
  // // Méthode bonus pour valider tout le formulaire
  // static getFormErrors(form: FormGroup | null): string[] {
  //   // Vérification si le formulaire est null ou undefined
  //   if (!form) return [];
    
  //   const errors: string[] = [];
  //   Object.keys(form.controls).forEach(key => {
  //     const error = this.getFieldError(form, key);
  //     if (error) errors.push(error);
  //   });
  //   return errors;
  // }

  // // Méthode utilitaire pour vérifier si un formulaire est valide
  // static isFormValid(form: FormGroup | null): boolean {
  //   return form ? form.valid : false;
  // }

  // // Méthode utilitaire pour vérifier si un formulaire a été touché
  // static isFormTouched(form: FormGroup | null): boolean {
  //   return form ? form.touched : false;
  // }
}
