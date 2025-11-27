package interface_adapter.save_event;

import entity.Event;
import java.util.ArrayList;
import java.util.List;

public class SaveEventState {
    private List<Event> savedEvents = new ArrayList<>();
    private String successMessage = "";
    private String errorMessage = "";
    private Event lastSavedEvent = null;

    public SaveEventState() {
    }

    public List<Event> getSavedEvents() {
        return new ArrayList<>(savedEvents);
    }

    public void setSavedEvents(List<Event> savedEvents) {
        this.savedEvents = new ArrayList<>(savedEvents);
    }

    public void addSavedEvent(Event event) {
        if (!savedEvents.contains(event)) {
            savedEvents.add(event);
        }
    }

    public void removeSavedEvent(Event event) {
        savedEvents.remove(event);
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Event getLastSavedEvent() {
        return lastSavedEvent;
    }

    public void setLastSavedEvent(Event lastSavedEvent) {
        this.lastSavedEvent = lastSavedEvent;
    }
}