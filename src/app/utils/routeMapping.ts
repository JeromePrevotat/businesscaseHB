// ROUTE MAPPING UTILS
// Dans app.routes
export const ROUTE_PATHS = {
    home: 'home',
    // Auth routes
    login: 'auth/login',
    register: 'auth/register',
    refresh: 'auth/refresh',

    // Users routes
    profile: 'profile',

    // Stations routes
    stations: 'stations',
    editStation: 'stations/:stationId/edit',

    // Wildcard redirect to home
    wildcard: '**',
};

// Dans les composants ts
export const ROUTE_GENERATORS = {
    editStation: (stationId: string) => `/stations/${stationId}/edit`,
};

/*  Dans les Templates avec routerLink
    Créer une méthode dans le composant :

    getEditStationPath(stationId: string): string {
        return ROUTE_GENERATORS.editStation(stationId);
    }

Utilisation dans le template :
<a [routerLink]="getEditStationPath('1')">Edit Station</a>

*/
