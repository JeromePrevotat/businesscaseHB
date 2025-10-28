import { Component, inject } from '@angular/core';
import { FormGroup, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { FormService } from '../../services/form.service';
import { stationState } from '../../models/stationState';
import { Station } from '../../models/station';
import { StationService } from '../../services/station.service';

@Component({
  selector: 'app-station-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './create-station-form.component.html',
  styleUrls: ['./create-station-form.component.css']
})
export class StationFormComponent {
  StationService = inject(StationService);
  createStationForm: FormGroup;
  isSubmitted = false;
  isLoading = false;
  stationStates = Object.values(stationState);


  constructor(private fb: FormBuilder) {
    this.createStationForm = this.fb.group({
      stationName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      latitude: ['', [Validators.required, Validators.min(-90), Validators.max(90)]],
      longitude: ['', [Validators.required, Validators.min(-180), Validators.max(180)]],
      priceRate: ['', [Validators.required, Validators.min(0.1)]],
      powerOutput: ['', [Validators.required, Validators.min(0.1)]],
      state: ['', [Validators.required]],
      spot_id: [''],
      plugType: [[]],
      grounded: [false],
      wired: [false],
      manual: ['']
    });
  }

  submitCreateStation(){
    this.isSubmitted = true;
    console.log('Form submitted:', this.createStationForm.value);
    if (this.createStationForm.valid) {
      this.isLoading = true;
      console.log('Form submitted successfully:', this.createStationForm.value);
      const newStation: Partial<Station> = this.createStationForm.value;
      this.StationService.createStation(newStation).subscribe({
        next: (response) => {
          console.log('Station created successfully:', response);
          this.createStationForm.reset();
          this.isLoading = false;
          this.isSubmitted = false;
          // Redirection to station list
        },
        error: (error) => {
          console.log("STATION: ", newStation);
          console.error('Error creating station:', error);
          this.isLoading = false;
        }
      });
    } else {
      console.log('Form is invalid', this.createStationForm.errors);
    }
  }

  get getIsSubmitted(): boolean{
    return this.isSubmitted;
  }

  isFieldInvalid(fieldName: string): boolean {
    return FormService.isFieldInvalid(this.createStationForm, fieldName, this.isSubmitted);
  }

  getFieldError(fieldName: string): string {
    return FormService.getFieldError(this.createStationForm, fieldName);
  }
}
