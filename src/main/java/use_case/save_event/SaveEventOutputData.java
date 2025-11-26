package use_case.save_event;

import entity.Event;

public class SaveEventOutputData {
    private final Event event;

    public SaveEventOutputData(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
