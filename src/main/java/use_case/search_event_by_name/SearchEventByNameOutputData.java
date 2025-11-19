package use_case.search_event_by_name;

import entity.Event;

public class SearchEventByNameOutputData {
    private final Event event;
    private final boolean success;

    public SearchEventByNameOutputData(Event event, boolean success) {
        this.event = event;
        this.success = success;
    }

    public Event getEvent() {
        return event;
    }

    public boolean isSuccess() {
        return success;
    }
}