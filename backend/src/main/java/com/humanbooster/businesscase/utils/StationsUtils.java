package com.humanbooster.businesscase.utils;

public final class StationsUtils {

    private static final double EARTH_RADIUS_METERS = 6371000; // Earth Radius in meters

    private StationsUtils() {}

    // Haversine formula to calculate distance between two gps points in meters
    public static double distanceMeters(
            double lat1, double lon1,
            double lat2, double lon2
    ) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_METERS * c;
    }
}
