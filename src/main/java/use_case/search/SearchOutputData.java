package use_case.search;

import entity.Event;
import java.util.List;

public class SearchOutputData {
    private final List<Event> events;
    private final String query;
    private final boolean success;
    private final String message;

    public SearchOutputData(List<Event> events, String query, boolean success, String message) {
        this.events = events;
        this.query = query;
        this.success = success;
        this.message = message;
    }

    public List<Event> getEvents() { return events; }
    public String getQuery() { return query; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Event getEvent() {
        return events != null && !events.isEmpty() ? events.get(0) : null;
    }
}
