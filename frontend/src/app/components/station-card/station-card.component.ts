import { Component, inject, Input } from '@angular/core';
import { Station } from '../../models/station';
import { SsrService } from '../../services/ssr.service';
import { ReservationComponent } from "../../forms/reservation-form/reservation.component";

@Component({
  selector: 'app-station-card',
  standalone: true,
  imports: [ReservationComponent],
  templateUrl: './station-card.component.html',
  styleUrls: ['./station-card.component.css']
})

export class StationCardComponent {
  ssrService = inject(SsrService);
  @Input() station: Station | undefined;

  test(){
    console.log("MODAL TEST: ", this.station);
  }
  
}