import { Component, Input } from '@angular/core';
import { Reservation } from '../../models/reservation';
import { DatePipe } from '@angular/common';
import { TotalPricePipe } from "../../pipes/total-price.pipe";
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'app-reservation-card',
  standalone: true,
  imports: [DatePipe, TotalPricePipe, CurrencyPipe],
  templateUrl: './reservation-card.component.html',
  styleUrl: './reservation-card.component.css'
})
export class ReservationCardComponent {
  @Input() item: Reservation | undefined;
}
