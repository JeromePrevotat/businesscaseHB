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
                { timeout: 5000, enableHighAccuracy: true, maximumAge: 2000 }
            );
        });
}

export async function adressLookUp(adresseInput:string): Promise<{ latitude: number; longitude: number; } | undefined> {
    const endpoint = 'https://nominatim.openstreetmap.org/search?q=';
    const queryParam = sanitizeInput(adresseInput) + '&format=json';

    try {
        // Send the GET request to Nominatim API
        let response = await fetch(endpoint + queryParam, {
            method: 'GET',
            headers: {
                'User-Agent': 'School Project Human Booster'
            }
        });
        // Error handling
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        // Parse the JSON response
        const data = await response.json();
        console.log("Response from Nominatim:", data);
        // Check if data is not empty and set the map location
        if (data.length == 0) {
            console.warn("No results found for the provided address.");
            return ;
        }
        // Center the map on the first result
        const latitude = data[0].lat;
        const longitude = data[0].lon;
        return {latitude, longitude};
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

export function setMapLocation(map: any, latitude: number, longitude: number) {
    if (latitude && longitude) {
        map.setView([latitude, longitude], 14);
    } else {
        console.error("Invalid latitude or longitude provided.");
    }
}

export async function addCircleToMap(L: any, map: any, latitude: number, longitude: number, radius = 5000) {
    const c = L.circle([latitude, longitude], {
        color: 'red',
        fillColor: '#f03',
        fillOpacity: 0.1,
        radius: radius
    }).addTo(map);
}

export async function addMarkersToMap(L: any, map: any, latitude: number, longitude: number) {
    L.marker([latitude, longitude]).addTo(map);
    console.log(`Marker added at Latitude: ${latitude}, Longitude: ${longitude}`);
}