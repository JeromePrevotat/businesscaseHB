import { Component } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { adressLookUp } from '../../utils/mapUtils';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormService } from '../../services/form.service';
import { HttpClient } from '@angular/common/http';
import { MapService } from '../../services/map.service';
import { StationService } from '../../services/station.service';
import { inject } from '@angular/core';

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
  private stationService = inject(StationService);
  displayPrice: string = '-';

  constructor(private fb: FormBuilder, private http: HttpClient, private mapService: MapService) {
      this.searchForm = this.fb.group({
        adress: ['', ],
        radius: ['', [Validators.min(1), Validators.max(50)]],
        date: ['', ],
        startTime: ['', ],
        endTime: ['', ],
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
        const radiusInMeters = formData.radius ? formData.radius * 1000 : 5000;
        this.mapService.updateRadius(radiusInMeters);
        // Call station service to search stations
        this.stationService.searchStation(
          radiusInMeters,
          coords ? coords.latitude : 0,
          coords ? coords.longitude : 0,
          formData.maxPrice ? formData.maxPrice : 20,
          formData.date ? formData.date : null,
          formData.startTime ? formData.startTime : null,
          formData.endTime ? formData.endTime : null
        ).subscribe({
          next: (stations) => {
            this.stationService.updateFilteredStations(stations);
            this.isLoading = false;
          },
          error: (err) => {
            console.error('Error during station search:', err);
            this.isLoading = false;
          }
        });
      } catch (error) {
        console.error('Error during address lookup:', error);
      }
    } else {
      console.log('Form is invalid', this.searchForm.errors);
    }
  }

  updatePriceDisplay(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.displayPrice = value ? `${value}` : '-';
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
