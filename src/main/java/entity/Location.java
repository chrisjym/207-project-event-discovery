package entity;

import java.util.Objects;

public class Location {
    private final String address;
    private final double latitude;
    private final double longitude;

    /**
     * Convenience constructor when you only care about coordinates.
     * Address will be stored as an empty string.
     */
    public Location(double latitude, double longitude) {
        this("", latitude, longitude);
    }

    /**
     * Full constructor with address and coordinates.
     */
    public Location(String address, double latitude, double longitude) {
        this.address = validateAddress(address);
        this.latitude = validateCoordinate(latitude, -90, 90, "Latitude");
        this.longitude = validateCoordinate(longitude, -180, 180, "Longitude");
    }

    private String validateAddress(String address) {
        // Address can be empty but not null
        if (address == null) {
            return "";
        }
        return address.trim();
    }

    private double validateCoordinate(double value, double min, double max, String fieldName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(fieldName + " must be between " + min + " and " + max);
        }
        return value;
    }

    /**
     * Distance to another Location using the Haversine formula.
     * Returns distance in kilometers.
     */
    public double calculateDistance(Location other) {
        if (other == null) return Double.MAX_VALUE;

        final double EARTH_RADIUS_KM = 6371.0;

        double lat1 = Math.toRadians(this.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lat2 = Math.toRadians(other.latitude);
        double lon2 = Math.toRadians(other.longitude);

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Double.compare(location.latitude, latitude) == 0
                && Double.compare(location.longitude, longitude) == 0
                && Objects.equals(address, location.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, latitude, longitude);
    }

    @Override
    public String toString() {
        return String.format("Location{address='%s', latitude=%.6f, longitude=%.6f}",
                address, latitude, longitude);
    }
}
