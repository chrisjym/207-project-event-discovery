package use_case.display_local_events;

import entity.Event;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Output data for the Display Local Events use case
 * Contains all the results from executing the use case
 */
public class DisplayLocalEventsOutputData {
    private final List<Event> events;
    private final String message;
    private final int totalEvents;
    private final Map<String, Double> eventDistances;

    public DisplayLocalEventsOutputData(List<Event> events, String message) {
        this.events = events != null ? events : List.of();
        this.message = message != null ? message : "";
        this.totalEvents = this.events.size();
        this.eventDistances = new HashMap<>(); // 可以在Interactor中填充具体距离
    }

    /**
     * Constructor with distance information
     * @param events the list of events to display
     * @param message the result message for the user
     * @param eventDistances map of event IDs to their distances from user
     */
    public DisplayLocalEventsOutputData(List<Event> events, String message, Map<String, Double> eventDistances) {
        this.events = events != null ? events : List.of();
        this.message = message != null ? message : "";
        this.totalEvents = this.events.size();
        this.eventDistances = eventDistances != null ? eventDistances : new HashMap<>();
    }

    // Getter methods
    public List<Event> getEvents() {
        return events;
    }

    public String getMessage() {
        return message;
    }

    public int getTotalEvents() {
        return totalEvents;
    }

    public Map<String, Double> getEventDistances() {
        return eventDistances;
    }

    /**
     * Gets the distance for a specific event
     * @param eventId the ID of the event
     * @return the distance in kilometers, or -1 if not found
     */
    public double getDistanceForEvent(String eventId) {
        return eventDistances.getOrDefault(eventId, -1.0);
    }

    /**
     * Checks if there are any events to display
     * @return true if there are events, false otherwise
     */
    public boolean hasEvents() {
        return !events.isEmpty();
    }

    /**
     * Checks if distance information is available
     * @return true if distance data exists, false otherwise
     */
    public boolean hasDistanceData() {
        return !eventDistances.isEmpty();
    }

    @Override
    public String toString() {
        return String.format(
                "DisplayLocalEventsOutputData{totalEvents=%d, message='%s', hasDistances=%s}",
                totalEvents, message, hasDistanceData()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DisplayLocalEventsOutputData)) return false;

        DisplayLocalEventsOutputData that = (DisplayLocalEventsOutputData) o;

        if (totalEvents != that.totalEvents) return false;
        if (!events.equals(that.events)) return false;
        return message.equals(that.message);
    }

    @Override
    public int hashCode() {
        int result = events.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + totalEvents;
        return result;
    }
}

