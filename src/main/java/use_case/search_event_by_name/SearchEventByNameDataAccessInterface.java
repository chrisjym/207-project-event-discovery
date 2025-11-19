package use_case.search_event_by_name;

import entity.Event;
import entity.Location;
import java.util.List;

public interface SearchEventByNameDataAccessInterface {
    /**
     * Search for events by keyword near a location.
     * @param keyword the search keyword
     * @param location the user's location
     * @param radiusKm the search radius in kilometers
     * @return list of events matching the search
     */
    List<Event> searchEventsByName(String keyword, Location location, double radiusKm);
}