import { Routes } from '@angular/router';
import { ROUTE_PATHS } from './utils/routeMapping';
import { RegisterFormComponent } from './forms/register-form/register-form.component';
import { HomeComponent } from './components/home/home.component';
import { LoginFormComponent } from './forms/login-form/login-form.component';
import { ProfileComponent } from './components/profile/profile.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: ROUTE_PATHS.home, component: HomeComponent },
    { path: ROUTE_PATHS.register, component: RegisterFormComponent },
    { path: ROUTE_PATHS.login, component: LoginFormComponent },
    { path: ROUTE_PATHS.profile, component: ProfileComponent },
    // { path: ROUTE_PATHS.editStation, redirectTo: ROUTE_PATHS.home },
    { path: ROUTE_PATHS.wildcard, redirectTo: ROUTE_PATHS.home },

];
