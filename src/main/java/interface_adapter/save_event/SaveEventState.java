package interface_adapter.save_event;

import entity.Event;

public class SaveEventState {
    private Event event = null;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
