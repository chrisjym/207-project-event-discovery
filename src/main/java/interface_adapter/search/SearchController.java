package interface_adapter.search;

import entity.Location;
import use_case.search.SearchInputBoundary;
import use_case.search.SearchInputData;

public class SearchController {

    private final SearchInputBoundary searchUseCaseInteractor;

    public SearchController(SearchInputBoundary searchUseCaseInteractor) {
        this.searchUseCaseInteractor = searchUseCaseInteractor;
    }

    public void execute(String query, String searchType) {
        Location defaultLocation = new Location("Toronto, ON", 43.6532, -79.3832);
        execute(query, searchType, defaultLocation);
    }

    public void execute(String query, String searchType, Location location) {
        SearchInputData inputData = new SearchInputData(query, searchType, location);
        searchUseCaseInteractor.execute(inputData);
    }



}
