import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReservationState } from '../../models/reservationState';
import { Reservation } from '../../models/reservation';
import { FormService } from '../../services/form.service';
import { ValidateTypeDate } from '../../validators/validateTypeDate';

@Component({
  selector: 'app-reservation',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css']
})

export class ReservationComponent {
  reservationForm: FormGroup;
  isSubmitted = false;
  isLoading = false;
  reservationState = Object.values(ReservationState);

  constructor(private fb: FormBuilder) {
    this.reservationForm = this.fb.group({
      startDate: [''],
      endDate: [''],
    },
    { validators:
      ValidateTypeDate.validateDateIsFutureFrom()
    });
  }

  submitReservation(){
    this.isSubmitted = true;
    if (this.reservationForm.valid) {
      this.isLoading = true;
      const newReservation: Partial<Reservation> = this.reservationForm.value;
      console.log('Form Submitted', newReservation);
      // API Call
    } else {
      console.log('Form is invalid', this.reservationForm.errors);
    }
  }

  get getIsSubmitted(): boolean {
    return this.isSubmitted;
  }

  isFieldInvalid(fieldName: string): boolean {
    return FormService.isFieldInvalid(this.reservationForm, fieldName, this.isSubmitted);
  }

  getFieldError(fieldName: string): string {
    return FormService.getFieldError(this.reservationForm, fieldName);
  }
}
