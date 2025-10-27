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
import { StationFormComponent } from './forms/station-form/station-form.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: ROUTE_PATHS.home, component: HomeComponent },
    { path: ROUTE_PATHS.register, component: RegisterFormComponent },
    { path: ROUTE_PATHS.login, component: LoginFormComponent },
    { path: ROUTE_PATHS.accountConfirmation, component: AccountConfirmationFormComponent },
    { path: ROUTE_PATHS.profile, component: ProfileComponent, children:[
        { path: '', redirectTo: ROUTE_PATHS.userInfos, pathMatch: 'full' },
        { path: ROUTE_PATHS.userInfos, component: UserInfosComponent },
        { path: ROUTE_PATHS.settings, component: SettingsComponent },
        { path: ROUTE_PATHS.billingInformation, component: BillingInformationComponent },
        { path: ROUTE_PATHS.security, component: SecurityComponent },
    ] },
    // Station Routes
    { path: ROUTE_PATHS.createStation, component: StationFormComponent,
        canActivate: [AuthGuard]
     },
    { path: ROUTE_PATHS.wildcard, redirectTo: ROUTE_PATHS.home },

];
