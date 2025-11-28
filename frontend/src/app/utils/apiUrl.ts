import { environment } from '../../environments/environment';

export const API_URL = {
    AUTH: `${environment.apiUrl}/api/auth`,
    USERS: `${environment.apiUrl}/api/users`,
    STATIONS: `${environment.apiUrl}/api/stations`,
    RESERVATIONS: `${environment.apiUrl}/api/reservations`,
};