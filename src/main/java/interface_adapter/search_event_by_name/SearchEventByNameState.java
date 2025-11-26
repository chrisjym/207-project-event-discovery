package interface_adapter.search_event_by_name;

import entity.Event;

public class SearchEventByNameState {
    private Event event = null;
    private String errorMessage;


    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
