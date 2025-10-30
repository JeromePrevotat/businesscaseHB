import { Component, ElementRef, inject, Input, OnInit, ViewChild } from '@angular/core';
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

export class StationCardComponent implements OnInit{
  ssrService = inject(SsrService);
  @Input() station: Station | undefined;
  
  ngOnInit(): void {
    console.log("Station: ", this.station);
    if (this.station)
      console.log("Station ID: ", this.station.id);
  }

  test(){
    console.log("MODAL TEST: ", this.station);
  }
  
}