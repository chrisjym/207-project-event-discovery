package use_case.event_description;

import data_access.EventDataAccessInterface;
import entity.Event;
import entity.Location;

public class EventDescriptionInteractor implements EventDescriptionInputBoundary {

    private final EventDataAccessInterface eventDataAccessObject;
    private final EventDescriptionOutputBoundary presenter;
    private final DistanceCalculator distanceCalculator;

    public EventDescriptionInteractor(EventDataAccessInterface eventDataAccessObject,
                                      EventDescriptionOutputBoundary presenter,
                                      DistanceCalculator distanceCalculator) {
        this.eventDataAccessObject = eventDataAccessObject;
        this.presenter = presenter;
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public void execute(EventDescriptionInputData inputData) {
        Event event = eventDataAccessObject.get(inputData.getEventId());

        if (event == null) {
            presenter.prepareFailView("Event not found.");
            return;
        }

        Location eventLocation = event.getLocation();

        double distanceKm = distanceCalculator.distanceKm(
                inputData.getUserLatitude(),
                inputData.getUserLongitude(),
                eventLocation.getLatitude(),
                eventLocation.getLongitude()
        );

        EventDescriptionOutputData outputData = new EventDescriptionOutputData(
                event.getName(),
                event.getDescription(),
                event.getAddress(),
                event.getCategory(),
                event.getDateTime(),
                distanceKm
        );

        presenter.prepareSuccessView(outputData);
    }
}

