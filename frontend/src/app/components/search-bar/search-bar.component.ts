import { Component } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { adressLookUp } from '../../utils/mapUtils';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { FormService } from '../../services/form.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [CurrencyPipe, ReactiveFormsModule],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.css'
})
export class SearchBarComponent {
  searchForm: FormGroup;
  isSubmitted = false;
  isLoading = false;

  constructor(private fb: FormBuilder, private http: HttpClient) {
      this.searchForm = this.fb.group({
        adress: ['', ],
        radius: ['', [Validators.min(1), Validators.max(50)]],
        date: ['', ],
        time: ['', ],
        maxPrice: ['', [Validators.min(0), Validators.max(20)]],
      });
    }

  submitSearch(){
    this.isSubmitted = true;
    if (this.searchForm.valid) {
      this.isLoading = true;
      // Process form data
      const formData = this.searchForm.value;
      console.log('Form Data:', formData);
      try {
        const coords = adressLookUp(formData.adress, this.http);
        console.log('Coordinates:', coords);
      } catch (error) {
        console.error('Error during address lookup:', error);
      }
    } else {
      console.log('Form is invalid', this.searchForm.errors);
    }
  }
    
  get getIsSubmitted(): boolean{
    return this.isSubmitted;
  }

  isFieldInvalid(fieldName: string): boolean {
    return FormService.isFieldInvalid(this.searchForm, fieldName, this.isSubmitted);
  }

  getFieldError(fieldName: string): string {
    return FormService.getFieldError(this.searchForm, fieldName);
  }
}
