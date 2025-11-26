package data_access;

import entity.Event;
import entity.Location;
import use_case.search_event_by_name.SearchEventByNameDataAccessInterface;
import java.util.List;

public class SearchEventDataAccessObject implements SearchEventByNameDataAccessInterface {

    private final EventDataAccessObject eventDAO;

    public SearchEventDataAccessObject() {
        this.eventDAO = new EventDataAccessObject();
    }

    @Override
    public List<Event> searchEventsByName(String keyword, Location location, double radiusKm) {
        return eventDAO.searchEventsByName(keyword, location, radiusKm);
    }
}