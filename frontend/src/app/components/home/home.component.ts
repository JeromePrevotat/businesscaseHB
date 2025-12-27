import { HttpClient } from '@angular/common/http';
import { Component, inject, ViewChild } from '@angular/core';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MapComponent } from "../map/map.component";
import { StationService } from '../../services/station.service';
import { SearchBarComponent } from "../search-bar/search-bar.component";
import { MapService } from '../../services/map.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [MapComponent, SearchBarComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent{
  http = inject(HttpClient);
  mapService = inject(MapService);
  userService = inject(UserService);
  stationService = inject(StationService);
  public authService = inject(AuthService);
  currentUser: User | null = null;
  @ViewChild(MapComponent) mapComp!: MapComponent;

  constructor() {
    this.authService.user$
      // Cancel Subscription after the component is destroyed
      // Prevents Memory Leaks
      .pipe(takeUntilDestroyed())
      .subscribe(user =>{
        this.currentUser = user;
    })
  }

  onTestButtonClick() {
    const {L,map} = this.mapComp.getMapInstance();
    this.stationService.getStationList()
      .subscribe(stations => {
        for (const station of stations) {
          console.log('Station:', station);
          this.mapService.addMarkersToMap(L, map, station.latitude, station.longitude);
        }
      });
  }
}
