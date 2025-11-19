package use_case.search_event_by_name;

import entity.User;
import entity.UserFactory;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupUserDataAccessInterface;
import entity.Event;
import java.util.List;

public class SearchEventByNameInteractor implements SearchEventByNameInputBoundary{

    private final SearchEventByNameDataAccessInterface eventDataAccess;
    private final SearchEventByNameOutputBoundary userPresenter;

    public SearchEventByNameInteractor(
            SearchEventByNameDataAccessInterface eventDataAccess,
            SearchEventByNameOutputBoundary searchEventByNameOutputBoundary) {
        this.eventDataAccess = eventDataAccess;
        this.userPresenter = searchEventByNameOutputBoundary;
    }

    @Override
    public void execute(SearchEventByNameInputData inputData) {
        if (inputData.getKeyword() == null || inputData.getKeyword().trim().isEmpty()) {
            userPresenter.prepareFailView("Search keyword cannot be empty");
            return;
        }

        if (inputData.getUserLocation() == null) {
            userPresenter.prepareFailView("User location is required");
            return;
        }

        // Search for events
        List<Event> events = eventDataAccess.searchEventsByName(
                inputData.getKeyword(),
                inputData.getUserLocation(),
                inputData.getRadiusKm()
        );

        if (events.isEmpty()) {
            userPresenter.prepareFailView("No events found for: " + inputData.getKeyword());
        } else {
            // Return the first matching event
            Event foundEvent = events.get(0);
            SearchEventByNameOutputData outputData = new SearchEventByNameOutputData(foundEvent, true);
            userPresenter.prepareSuccessView(outputData);
        }
    }

    @Override
    public void switchToDashboardView() {
        userPresenter.switchToDashboardView();
    }
}
