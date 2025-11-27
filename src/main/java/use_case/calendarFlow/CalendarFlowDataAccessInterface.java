package use_case.calendarFlow;

import entity.Event;
import entity.Location;

import java.util.List;
import java.time.LocalDate;

/**
 * Data Access Interface for the Calendar Flow Use Case.
 * Defines methods for retrieving event data.
 */

public interface CalendarFlowDataAccessInterface {

    /**
     * Search for events by specific date
     * @param date the selected specific date
     * @param location the user's location
     * @param radiusKm the search radius in kilometers
     * @return list of events matching the search
     */
    List<Event> getEventsByDate(LocalDate date, Location location, double radiusKm);
}