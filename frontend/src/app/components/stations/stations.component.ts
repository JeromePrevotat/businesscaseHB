import { Component, inject } from '@angular/core';
import { RouterLink, RouterOutlet } from "@angular/router";
import { ROUTE_PATHS } from '../../utils/routeMapping';
import { StationService } from '../../services/station.service';
import { StationState } from '../../models/StationState';
import { Station } from '../../models/station';
import { Observable } from 'rxjs/internal/Observable';
import { ListDisplayComponent } from "../list-display/list-display.component";
import { ListType } from '../../models/listType';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-stations',
  standalone: true,
  imports: [RouterLink, RouterOutlet, ListDisplayComponent],
  templateUrl: './stations.component.html',
  styleUrl: './stations.component.css'
})
export class StationsComponent {
  readonly ROUTE_PATHS = ROUTE_PATHS;
  stationsService: StationService = inject(StationService);
  userService: UserService = inject(UserService);
  stationStates = Object.values(StationState);
  stationsList: Observable<Station[]> = this.userService.getUserStations();
  listType = ListType.STATION;
}
