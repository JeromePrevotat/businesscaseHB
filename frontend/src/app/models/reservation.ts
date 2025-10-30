import { ReservationState } from './reservationState';

export interface Reservation {
  readonly id: number | null;
  user_id: number;
  station_id: number;
  createdAt: Date;
  validatedAt?: Date;
  startDate: Date;
  endDate: Date;
  hourlyRateLog: number;
  payed: boolean;
  datePayed?: Date;
  state: ReservationState;
}