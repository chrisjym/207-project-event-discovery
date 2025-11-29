package interface_adapter.display_local_events;

import entity.Event;
import interface_adapter.ViewModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 简化的 ViewModel - 基本的 PropertyChange 支持
 */

public class DisplayLocalEventsViewModel extends ViewModel<DisplayLocalEventsState> {

    public static final String VIEW_NAME = "display local events";
    private LocalDate selectedDateFromCalendar;

    // Store actual Event objects for navigation purposes
    private List<Event> events = new ArrayList<>();

    public DisplayLocalEventsViewModel() {
        super(VIEW_NAME);
        this.setState(new DisplayLocalEventsState());
    }

    /**
     * EventCard - a view-specific data structure.
     *
     * CLEAN ARCHITECTURE NOTE:
     * This is a Data Transfer Object (DTO) for the View layer.
     * It contains only the data needed for display, formatted appropriately.
     * This separates the View's data needs from the domain Entity structure.
     */
    public static class EventCard {
        private final String id;
        private final String name;
        private final String dateTime;
        private final String address;
        private final String category;
        private final String distanceText;
        private final String imageUrl;

        public EventCard(String id, String name, String dateTime, String address,
                         String category, String distanceText, String imageUrl) {
            this.id = id;
            this.name = name;
            this.dateTime = dateTime;
            this.address = address;
            this.category = category;
            this.distanceText = distanceText;
            this.imageUrl = imageUrl;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDateTime() { return dateTime; }
        public String getAddress() { return address; }
        public String getCategory() { return category; }
        public String getDistanceText() { return distanceText; }
        public String getImageUrl() { return imageUrl; }
    }

    // Getter/setter for Event objects (for navigation)
    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events != null ? new ArrayList<>(events) : new ArrayList<>();
    }

    /**
     * Find an Event by its ID.
     * Used when user clicks on an EventCard to navigate to details.
     */
    public Event getEventById(String id) {
        for (Event event : events) {
            if (event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }

    // Basic getter/setter methods
    public List<EventCard> getEventCards() {
        return this.getState().getEventCards();
    }

    public void setEventCards(List<EventCard> eventCards) {
        this.getState().setEventCards(eventCards);
        this.firePropertyChange();
    }

    public String getMessage() {
        return this.getState().getMessage();
    }

    public void setMessage(String message) {
        this.getState().setMessage(message);
        this.firePropertyChange();
    }

    public String getError() {
        return this.getState().getError();
    }

    public void setError(String error) {
        this.getState().setError(error);
        this.firePropertyChange();
    }

    public boolean hasEvents() {
        return this.getState().hasEvents();
    }

    public boolean hasError() {
        return this.getState().hasError();
    }

    // Search parameter methods
    public void updateSearchParams(String location, String category, double radius) {
        this.getState().setLastSearchLocation(location);
        this.getState().setLastSearchCategory(category);
        this.getState().setLastSearchRadius(radius);
        this.firePropertyChange();
    }

    public String getLastSearchLocation() {
        return this.getState().getLastSearchLocation();
    }

    public String getLastSearchCategory() {
        return this.getState().getLastSearchCategory();
    }

    public double getLastSearchRadius() {
        return this.getState().getLastSearchRadius();
    }
}