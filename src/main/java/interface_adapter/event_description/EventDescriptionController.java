package interface_adapter.event_description;

import use_case.event_description.EventDescriptionInputBoundary;
import use_case.event_description.EventDescriptionInputData;

public class EventDescriptionController {

    private final EventDescriptionInputBoundary interactor;

    public EventDescriptionController(EventDescriptionInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void showEvent(String eventId, double userLat, double userLon) {
        EventDescriptionInputData inputData =
                new EventDescriptionInputData(eventId, userLat, userLon);
        interactor.execute(inputData);
    }
}
