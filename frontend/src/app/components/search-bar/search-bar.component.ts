import { Component } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { adressLookUp } from '../../utils/mapUtils';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormService } from '../../services/form.service';
import { HttpClient } from '@angular/common/http';
import { MapService } from '../../services/map.service';

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

  constructor(private fb: FormBuilder, private http: HttpClient, private mapService: MapService) {
      this.searchForm = this.fb.group({
        adress: ['', ],
        radius: ['', [Validators.min(1), Validators.max(50)]],
        date: ['', ],
        time: ['', ],
        maxPrice: ['', [Validators.min(0), Validators.max(20)]],
      });
    }

  async submitSearch(){
    this.isSubmitted = true;
    if (this.searchForm.valid) {
      this.isLoading = true;
      // Process form data
      const formData = this.searchForm.value;
      console.log('Form Data:', formData);
      try {
        const coords = await adressLookUp(formData.adress, this.http);
        console.log('Coordinates:', coords);
        if (coords) {
          this.mapService.updateCoords(coords);
        }
        if (formData.radius) {
          this.mapService.updateRadius(formData.radius * 1000);
        }
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
