package use_case.display_local_events;
import entity.Location;
import entity.EventCategory;

public class DisplayLocalEventsInputData {
    private final Location userLocation;
    private final double radiusKm;
    private final EventCategory filterCategory;

    public DisplayLocalEventsInputData(Location userLocation, double radiusKm, EventCategory filterCategory) {
        this.userLocation = userLocation;
        this.radiusKm = radiusKm;
        this.filterCategory = filterCategory;
    }

    // Getter methods
    public Location getUserLocation() {
        return userLocation;
    }

    public double getRadiusKm() {
        return radiusKm;
    }

    public EventCategory getFilterCategory() {
        return filterCategory;
    }

    /**
     * Validates the input data
     * @return true if the input data is valid, false otherwise
     */
    public boolean isValid() {
        return userLocation != null && radiusKm > 0;
    }

    /**
     * Checks if a category filter is specified
     * @return true if a category filter is set, false otherwise
     */
    public boolean hasCategoryFilter() {
        return filterCategory != null;
    }

    @Override
    public String toString() {
        return String.format(
                "DisplayLocalEventsInputData{userLocation=%s, radiusKm=%.1f, filterCategory=%s}",
                userLocation, radiusKm, filterCategory
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DisplayLocalEventsInputData)) return false;

        DisplayLocalEventsInputData that = (DisplayLocalEventsInputData) o;

        if (Double.compare(that.radiusKm, radiusKm) != 0) return false;
        if (!userLocation.equals(that.userLocation)) return false;
        return filterCategory != null ? filterCategory.equals(that.filterCategory) : that.filterCategory == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = userLocation.hashCode();
        temp = Double.doubleToLongBits(radiusKm);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (filterCategory != null ? filterCategory.hashCode() : 0);
        return result;
    }
}

