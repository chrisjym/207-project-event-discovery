package use_case.search_event_by_name;

import entity.Location;

public class SearchEventByNameInputData {
    private final String keyword;
    private final Location userLocation;
    private final double radiusKm;

    public SearchEventByNameInputData(String keyword, Location userLocation, double radiusKm) {
        this.keyword = keyword;
        this.userLocation = userLocation;
        this.radiusKm = radiusKm;
    }

    public String getKeyword() {
        return keyword;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public double getRadiusKm() {
        return radiusKm;
    }
}
