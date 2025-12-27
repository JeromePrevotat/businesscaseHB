import { HttpClient } from '@angular/common/http';
import { API_URL } from './apiUrl';
import { firstValueFrom } from 'rxjs/internal/firstValueFrom';

export async function getLocation(): Promise<{ latitude: number; longitude: number; } | undefined> {
    return new Promise((resolve, reject) => {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    resolve({
                        latitude: position.coords.latitude,
                        longitude: position.coords.longitude
                    });
                },
                (error) => {
                    console.error("Error getting location:", error);
                    reject(error);
                },
                { timeout: 20000, enableHighAccuracy: true, maximumAge: 2000 }
            );
        });
}

export async function adressLookUp(adresseInput:string, http: HttpClient): Promise<{ latitude: number; longitude: number; } | undefined> {
    const endpoint = API_URL.NOMINATIM;
    try {
        const data: any = await firstValueFrom(
            http.get(endpoint, {
                params: {
                    q: sanitizeInput(adresseInput),
                    format: 'json'
                }
            })
        );

        console.log('Response from Nominatim:', data);

        if (!data || data.length === 0) {
            console.warn('No results found for the provided address.');
            return;
        }

        const latitude = Number(data[0].lat);
        const longitude = Number(data[0].lon);

        return { latitude, longitude };
    }
    catch (error) {
        console.error("Error fetching data from Nominatim:", error);
        return ;
    }
}

function sanitizeInput(input: string): string {
    // Removes <HTML Tags> and trims whitespace
    return input.replace(/<[^>]*>/g, '').trim();
}

