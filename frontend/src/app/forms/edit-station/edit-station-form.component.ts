import { Component, inject } from '@angular/core';
import { FormGroup, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { FormService } from '../../services/form.service';
import { StationState } from '../../models/StationState';
import { Station } from '../../models/station';
import { StationService } from '../../services/station.service';
import { Router } from '@angular/router';
import { ROUTE_PATHS } from '../../utils/routeMapping';

@Component({
  selector: 'app-station-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './edit-station-form.component.html',
  styleUrls: ['./edit-station-form.component.css']
})
export class EditStationFormComponent {
  StationService = inject(StationService);
  editStationForm: FormGroup;
  isSubmitted = false;
  isLoading = false;
  stationStates = Object.values(StationState);
  station: Station |null = null;

  constructor(private fb: FormBuilder, private router: Router) {
    const nav = this.router.getCurrentNavigation();
    const stationData = nav?.extras.state?.['station'] as Station;
    this.station = stationData;

    this.editStationForm = this.fb.group({
      stationName: [this.station.stationName ?? '', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      latitude: [this.station.latitude ?? '', [Validators.required, Validators.min(-90), Validators.max(90)]],
      longitude: [this.station.longitude ?? '', [Validators.required, Validators.min(-180), Validators.max(180)]],
      priceRate: [this.station.priceRate ?? '', [Validators.required, Validators.min(0.1)]],
      powerOutput: [this.station.powerOutput ?? '', [Validators.required, Validators.min(0.1)]],
      state: [this.station.state ?? '', [Validators.required]],
      spot_id: [''],
      plugType: [[]],
      grounded: [this.station.grounded ?? false],
      wired: [this.station.wired ?? false],
      manual: [this.station.manual ?? '']
    });
  }

  submitEditStation(){
    this.isSubmitted = true;
    if (this.editStationForm.valid) {
      this.isLoading = true;
      const editedStation: Partial<Station> = this.editStationForm.value;
      this.StationService.editStation(this.station!.id!, editedStation).subscribe({
        next: (response) => {
          this.editStationForm.reset();
          this.isLoading = false;
          this.isSubmitted = false;
          // Redirection to station list
          this.router.navigate([ROUTE_PATHS.stations]);
        },
        error: (error) => {
          console.log("STATION: ", editedStation);
          console.error('Error creating station:', error);
          this.isLoading = false;
        }
      });
    } else {
      console.log('Form is invalid', this.editStationForm.errors);
    }
  }

  get getIsSubmitted(): boolean{
    return this.isSubmitted;
  }

  isFieldInvalid(fieldName: string): boolean {
    return FormService.isFieldInvalid(this.editStationForm, fieldName, this.isSubmitted);
  }

  getFieldError(fieldName: string): string {
    return FormService.getFieldError(this.editStationForm, fieldName);
  }
}
