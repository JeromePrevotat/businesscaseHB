import { Component, inject } from '@angular/core';
import { RouterLink, RouterOutlet } from "@angular/router";
import { ROUTE_PATHS } from '../../utils/routeMapping';
import { StationService } from '../../services/station.service';
import { stationState } from '../../models/stationState';
import { Station } from '../../models/station';
import { Observable } from 'rxjs/internal/Observable';
import { AsyncPipe } from '@angular/common';
import { ListDisplayComponent } from "../list-display/list-display.component";
import { ListType } from '../../models/listType';

@Component({
  selector: 'app-stations',
  standalone: true,
  imports: [RouterLink, RouterOutlet, AsyncPipe, ListDisplayComponent],
  templateUrl: './stations.component.html',
  styleUrl: './stations.component.css'
})
export class StationsComponent {
  readonly ROUTE_PATHS = ROUTE_PATHS;
  stationsService: StationService = inject(StationService);
  stationStates = Object.values(stationState);
  stationsList: Observable<Station[]> = this.stationsService.getStationList();
  listType = ListType.STATION;
}
