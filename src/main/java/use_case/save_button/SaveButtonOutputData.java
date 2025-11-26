package use_case.save_button;

import entity.Event;

public class SaveButtonOutputData {
    private final Event event;

    public SaveButtonOutputData(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
