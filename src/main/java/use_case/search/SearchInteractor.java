package use_case.search;

import entity.Event;
import entity.Location;
import use_case.search_event_by_name.SearchEventByNameDataAccessInterface;
import java.util.List;

public class SearchInteractor implements SearchInputBoundary {

    private final SearchEventByNameDataAccessInterface eventDataAccess;
    private final SearchOutputBoundary searchPresenter;
    private final double RADIUS_KM = 50.0;


    public SearchInteractor(SearchEventByNameDataAccessInterface eventDataAccess,
                            SearchOutputBoundary searchPresenter) {
        this.eventDataAccess = eventDataAccess;
        this.searchPresenter = searchPresenter;
    }

    @Override
    public void execute(SearchInputData inputData) {
        if (inputData.getQuery() == null || inputData.getQuery().trim().isEmpty()) {
            searchPresenter.prepareFailureView("Search query cannot be empty");
            return;
        }

        Location userLocation = inputData.getLocation();

        // Search for events
        List<Event> events = eventDataAccess.searchEventsByName(
                inputData.getQuery(),
                userLocation,
                RADIUS_KM
        );

        if (events.isEmpty()) {
            searchPresenter.prepareFailureView("No events found for: " + inputData.getQuery());
        } else {
            Event firstEvent = events.get(0);
            SearchOutputData outputData = new SearchOutputData(
                    events,
                    inputData.getQuery(),
                    true,
                    "Found " + events.size() + " events"
            );
            searchPresenter.prepareSuccessView(outputData);
        }
    }
}