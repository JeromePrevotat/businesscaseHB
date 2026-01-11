import { Component, inject } from '@angular/core';
import { ListType } from '../../models/listType';
import { ROUTE_PATHS } from '../../utils/routeMapping';
import { UserService } from '../../services/user.service';
import { RouterOutlet } from "@angular/router";
import { ListDisplayComponent } from "../list-display/list-display.component";
import { ReservationService } from '../../services/reservation.service';
import { Observable } from 'rxjs/internal/Observable';
import { Reservation } from '../../models/reservation';

@Component({
  selector: 'app-reservations',
  standalone: true,
  imports: [RouterOutlet, ListDisplayComponent],
  templateUrl: './reservations.component.html',
  styleUrl: './reservations.component.css'
})
export class ReservationsComponent {
  readonly ROUTE_PATHS = ROUTE_PATHS;
  reservationService: ReservationService = inject(ReservationService);
  userService: UserService = inject(UserService);
  reservationsList: Observable<Reservation[]> = this.userService.getUserReservations();
  listType = ListType.RESERVATION;

  ngOnInit(): void {
    this.userService.refreshUserReservations();
  }
}
