package use_case.save_event;

import entity.Event;

public class SaveEventInputData {
    private final Event event;

    public SaveEventInputData(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return this.event;
    }

}
