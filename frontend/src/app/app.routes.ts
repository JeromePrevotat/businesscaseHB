import { Routes } from '@angular/router';
import { ROUTE_PATHS } from './utils/routeMapping';
import { RegisterFormComponent } from './forms/register-form/register-form.component';
import { HomeComponent } from './components/home/home.component';
import { LoginFormComponent } from './forms/login-form/login-form.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AccountConfirmationFormComponent } from './forms/account-confirmation-form/account-confirmation-form.component';
import { SettingsComponent } from './forms/settings/settings.component';
import { BillingInformationComponent } from './forms/billing-information/billing-information.component';
import { SecurityComponent } from './forms/security/security.component';
import { UserInfosComponent } from './forms/user-infos/user-infos.component';
import { StationFormComponent } from './forms/create-station-form/create-station-form.component';
import { AuthGuard } from './guards/auth.guard';
import { StationsComponent } from './components/stations/stations.component';
import { ReservationFormComponent } from './forms/reservation-form/reservation-form.component';
import { CguComponent } from './components/cgu/cgu.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: ROUTE_PATHS.home, component: HomeComponent },
    { path: ROUTE_PATHS.cgu, component: CguComponent },
    { path: ROUTE_PATHS.register, component: RegisterFormComponent },
    { path: ROUTE_PATHS.login, component: LoginFormComponent },
    { path: ROUTE_PATHS.accountConfirmation, component: AccountConfirmationFormComponent },
    { path: ROUTE_PATHS.profile, component: ProfileComponent, children:[
        { path: '', redirectTo: ROUTE_PATHS.userInfos, pathMatch: 'full' },
        { path: ROUTE_PATHS.userInfos, component: UserInfosComponent },
        { path: ROUTE_PATHS.settings, component: SettingsComponent },
        { path: ROUTE_PATHS.billingInformation, component: BillingInformationComponent },
        { path: ROUTE_PATHS.security, component: SecurityComponent },
    ]},

    // Station Routes
    { path: ROUTE_PATHS.stations, component: StationsComponent, children:
    [
        {
            path: ROUTE_PATHS.createStation, component: StationFormComponent,
            canActivate: [AuthGuard]
        },
    ]},
    { path: ROUTE_PATHS.wildcard, redirectTo: ROUTE_PATHS.home },

    // Reservation Routes
    { 
        path: ROUTE_PATHS.createReservation, component: ReservationFormComponent,
        canActivate: [AuthGuard]
    },
    { path: ROUTE_PATHS.wildcard, redirectTo: ROUTE_PATHS.home },
    
];
