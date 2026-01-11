import { Pipe, PipeTransform } from '@angular/core';
import { Reservation } from '../models/reservation';

@Pipe({
  name: 'totalPrice',
  standalone: true
})
export class TotalPricePipe implements PipeTransform {

  transform(reservation: Reservation): number {
    const startTime = new Date(reservation.startDate);
    const endTime = new Date(reservation.endDate);
    // Convert to hours
    const duration = (endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60);
    return duration * reservation.hourlyRateLog;
  }
}
