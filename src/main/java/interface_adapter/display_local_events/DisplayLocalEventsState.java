package interface_adapter.display_local_events;

import java.util.ArrayList;
import java.util.List;

/**
 * State class for DisplayLocalEvents feature.
 * Holds all the data needed for the view.
 */
public class DisplayLocalEventsState {
    private List<DisplayLocalEventsViewModel.EventCard> eventCards = new ArrayList<>();
    private String message = "";
    private String error = "";


    private String lastSearchLocation = "";
    private String lastSearchCategory = "";
    private double lastSearchRadius = 0.0;

    private boolean isLoading = false;
    private String selectedEventId = null;


    public List<DisplayLocalEventsViewModel.EventCard> getEventCards() {
        return new ArrayList<>(eventCards); // Return copy for immutability
    }

    public void setEventCards(List<DisplayLocalEventsViewModel.EventCard> cards) {
        this.eventCards = (cards != null ? new ArrayList<>(cards) : new ArrayList<>());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = (msg != null ? msg : "");
    }

    public String getError() {
        return error;
    }

    public void setError(String err) {
        this.error = (err != null ? err : "");
    }


    public boolean hasEvents() {
        return !eventCards.isEmpty();
    }

    public boolean hasError() {
        return !error.isEmpty();
    }


    public String getLastSearchLocation() {
        return lastSearchLocation;
    }

    public void setLastSearchLocation(String location) {
        this.lastSearchLocation = (location != null ? location : "");
    }

    public String getLastSearchCategory() {
        return lastSearchCategory;
    }

    public void setLastSearchCategory(String category) {
        this.lastSearchCategory = (category != null ? category : "");
    }

    public double getLastSearchRadius() {
        return lastSearchRadius;
    }

    public void setLastSearchRadius(double radius) {
        this.lastSearchRadius = radius;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
    }

    public String getSelectedEventId() {
        return selectedEventId;
    }

    public void setSelectedEventId(String eventId) {
        this.selectedEventId = eventId;
    }

    public DisplayLocalEventsState copy() {
        DisplayLocalEventsState newState = new DisplayLocalEventsState();
        newState.setEventCards(this.eventCards);
        newState.setMessage(this.message);
        newState.setError(this.error);
        newState.setLastSearchLocation(this.lastSearchLocation);
        newState.setLastSearchCategory(this.lastSearchCategory);
        newState.setLastSearchRadius(this.lastSearchRadius);
        newState.setLoading(this.isLoading);
        newState.setSelectedEventId(this.selectedEventId);
        return newState;
    }
}