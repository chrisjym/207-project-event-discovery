package use_case.save_event;

import entity.Event;
import java.util.List;

/**
 * Interface for saved events data access.
 * Following Clean Architecture - the use case defines what it needs,
 * and the data access layer implements it.
 */
public interface SaveEventDataAccessInterface {

    /**
     * Save an event for a user.
     */
    void saveEvent(String username, Event event);

    /**
     * Get all saved events for a user.
     */
    List<Event> getSavedEvents(String username);

    /**
     * Remove a saved event for a user.
     */
    void unsaveEvent(String username, Event event);

    /**
     * Check if an event is already saved by a user.
     */
    boolean isEventSaved(String username, String eventId);
}