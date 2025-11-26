package use_case.save_button;

import entity.Event;

public class SaveButtonInputData {
    private final Event event;

    public SaveButtonInputData(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return this.event;
    }
}
