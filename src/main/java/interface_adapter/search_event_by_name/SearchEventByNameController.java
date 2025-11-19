package interface_adapter.search_event_by_name;

import use_case.search_event_by_name.SearchEventByNameInputBoundary;
import use_case.signup.SignupInputBoundary;
import entity.Location;
import use_case.search_event_by_name.SearchEventByNameInputBoundary;
import use_case.search_event_by_name.SearchEventByNameInputData;

public class SearchEventByNameController {

    private final SearchEventByNameInputBoundary searchEventByNameInteractor;

    public SearchEventByNameController(SearchEventByNameInputBoundary searchEventByNameInteractor) {
        this.searchEventByNameInteractor = searchEventByNameInteractor;
    }

    /**
     * Execute the search event by name use case.
     * @param keyword the event name to search for
     * @param userLocation the user's location
     * @param radiusKm the search radius in kilometers
     */
    public void execute(String keyword, Location userLocation, double radiusKm) {
        SearchEventByNameInputData inputData = new SearchEventByNameInputData(keyword, userLocation, radiusKm);
        searchEventByNameInteractor.execute(inputData);
    }

    /**
     * Executes the "switch to DashboardView" Use Case.
     */
    public void switchToDashboardView() {
        searchEventByNameInteractor.switchToDashboardView();
    }
}
