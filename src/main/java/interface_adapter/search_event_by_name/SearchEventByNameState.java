package interface_adapter.search_event_by_name;

import entity.Event;

public class SearchEventByNameState {
    private Event event = null;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
