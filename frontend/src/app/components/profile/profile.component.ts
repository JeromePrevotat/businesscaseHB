import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from "@angular/router";
import { ROUTE_PATHS } from '../../utils/routeMapping';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [RouterLink, RouterOutlet],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  readonly ROUTE_PATHS = ROUTE_PATHS;
}
