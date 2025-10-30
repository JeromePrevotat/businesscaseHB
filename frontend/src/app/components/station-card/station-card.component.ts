import { Component, ElementRef, inject, Input, OnInit, ViewChild } from '@angular/core';
import { Station } from '../../models/station';
import { SsrService } from '../../services/ssr.service';
import * as bootstrap from 'bootstrap';

@Component({
  selector: 'app-station-card',
  standalone: true,
  imports: [],
  templateUrl: './station-card.component.html',
  styleUrls: ['./station-card.component.css']
})

export class StationCardComponent implements OnInit{
  ssrService = inject(SsrService);
  @Input() station: Station | undefined;
  // @ViewChild('reservationModal', { static: false }) reservationModal!: ElementRef<HTMLDivElement>;
  
  ngOnInit(): void {
    console.log("Station: ", this.station);
    if (this.station)
      console.log("Station ID: ", this.station.id);
  }

  // openModal() {
  //   if (!this.ssrService.isServerSide) {    
  //     console.log("Opening modal for station: ", this.station);
  //     if (!this.reservationModal) {
  //       console.warn('reservationModal element not found');
  //       return;
  //     }
  //     const modal = new bootstrap.Modal(this.reservationModal.nativeElement);
  //     modal.show();
  //   }
  // }

  test(){
    console.log("MODAL TEST: ", this.station);
  }
  
}