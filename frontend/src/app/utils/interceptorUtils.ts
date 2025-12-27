import { API_URL } from "./apiUrl";

export const INTERCEPTOR_PUBLIC_URL = [
    `${API_URL.AUTH}/login`,
    `${API_URL.AUTH}/register`,
    `${API_URL.AUTH}/refresh`,
];
export const INTERCEPTOR_PROTECTED_URL = [
    `${API_URL.USERS}`,
    `${API_URL.STATIONS}`,
    `${API_URL.RESERVATIONS}`,
    `${API_URL.NOMINATIM}`,
    // /me
    // /:id
];