import { Component, inject, Input } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ReservationState } from '../../models/ReservationState';
import { Reservation } from '../../models/reservation';
import { FormService } from '../../services/form.service';
import { ValidateTypeDate } from '../../validators/validateTypeDate';
import { ReservationService } from '../../services/reservation.service';
import { AuthService } from '../../services/auth.service';
import { Station } from '../../models/station';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-reservation-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './reservation-form.component.html',
  styleUrls: ['./reservation-form.component.css']
})

export class ReservationFormComponent {
  @Input() station: Station | null = null;
  reservationService = inject(ReservationService);
  authService = inject(AuthService);
  toastService = inject(ToastService);
  reservationForm: FormGroup;
  isSubmitted = false;
  isLoading = false;
  reservationState = Object.values(ReservationState);
  user = this.authService.user$;

  constructor(private fb: FormBuilder) {
    this.reservationForm = this.fb.group({
      startDate: [''],
      endDate: [''],
    },
    {
      validators: ValidateTypeDate.validateDateIsFutureFrom()
    });
  }

  getTotalPrice(): number {
    const startDate = this.reservationForm.get('startDate')?.value;
    const endDate = this.reservationForm.get('endDate')?.value;
    const priceRate = this.station?.priceRate ?? 0;

    if (!startDate || !endDate || !priceRate) {
      return priceRate;
    }

    const start = new Date(startDate);
    const end = new Date(endDate);
    const duration = (end.getTime() - start.getTime()) / (1000 * 60 * 60);

    if (duration <= 0) {
      return priceRate;
    }

    const total = duration * priceRate;
    // Round to 2 decimal places
    return Math.round(total * 100) / 100;
  }

  submitReservation(){
    this.isSubmitted = true;
    if (this.reservationForm.valid) {
      this.isLoading = true;
      let newReservation: Partial<Reservation>;
      if (this.station && this.station.id) {
        newReservation = {
          station_id: this.station.id,
          hourlyRateLog: this.station.priceRate,
          state: ReservationState.PENDING,
          ...this.reservationForm.value
        };
      }
      else {
        console.error('Station information is missing. Cannot create reservation.');
        this.isLoading = false;
        return;
      }
      console.log('Form Submitted', newReservation);
      // API Call
      this.reservationService.createReservation(newReservation).subscribe({
        next: (response) => {
          console.log('Reservation created successfully', response);
          this.isSubmitted = false;
          this.isLoading = false;
          this.reservationForm.reset();
          this.toastService.success('Réservation confirmée avec succès !');
          this.closeModal();
        },
        error: (error) => {
          console.error('Error creating reservation', error);
          this.toastService.error('Erreur lors de la réservation. Veuillez réessayer.');
          this.isLoading = false;
        }
      });
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

  private closeModal(): void {
    const modalElement = document.getElementById('reservationModal');
    if (modalElement) {
      import('bootstrap').then(({ Modal }) => {
        const modal = Modal.getInstance(modalElement);
        modal?.hide();
      });
    }
  }
}
