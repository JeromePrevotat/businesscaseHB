import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { toTitleCase } from '../utils/strUtils';

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
      if (field.errors['server']) return field.errors['server'];
      if (field.errors['required']) return `${toTitleCase(fieldName)} is required`;
      if (field.errors['minlength']) return `Minimum ${field.errors['minlength'].requiredLength} characters`;
      if (field.errors['maxlength']) return `Maximum ${field.errors['maxlength'].requiredLength} characters`;
      if (field.errors['email']) return `Invalid email format`;
      if (field.errors['pattern']) return `Invalid format`;
      if (field.errors['passwordMismatch']) return `Passwords do not match`;

      // Fallback for any other errors
      return `${toTitleCase(fieldName)} is invalid`;
    }
      return '';
  }

  static clearServerErrors(form: FormGroup | null): void {
    if (!form) return;
    // Go through all Fields
    Object.keys(form.controls).forEach(fieldName => {
      const control = form.get(fieldName);

      // Check if the Field has a server error
      if (control && control.errors && control.errors['server']) {
        // Deletes it
        delete control.errors['server'];

        // If Field only had this error, set it back to valid
        const newErrors = Object.keys(control.errors).length > 0 ? control.errors : null;
        control.setErrors(newErrors);
      }
    });
    
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
