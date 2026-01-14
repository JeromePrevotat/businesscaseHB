import { Component, inject } from '@angular/core';
import { FormGroup, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { FormService } from '../../services/form.service';
import { StationState } from '../../models/StationState';
import { Station } from '../../models/station';
import { StationService } from '../../services/station.service';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { ROUTE_PATHS } from '../../utils/routeMapping';

@Component({
  selector: 'app-station-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './create-station-form.component.html',
  styleUrls: ['./create-station-form.component.css']
})
export class StationFormComponent {
  StationService = inject(StationService);
  userService = inject(UserService);
  createStationForm: FormGroup;
  isSubmitted = false;
  isLoading = false;
  stationStates = Object.values(StationState);

  constructor(private fb: FormBuilder, private router: Router) {
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
    if (this.createStationForm.valid) {
      this.isLoading = true;
      const newStation: Partial<Station> = this.createStationForm.value;
      this.StationService.createStation(newStation).subscribe({
        next: (response) => {
          this.createStationForm.reset();
          this.isLoading = false;
          this.isSubmitted = false;
          this.userService.refreshUserStations();
          // Redirection to station list
          this.router.navigate([ROUTE_PATHS.stations]);
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
