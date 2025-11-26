package interface_adapter.search_event_by_name;

import interface_adapter.ViewManagerModel;
import use_case.search_event_by_name.SearchEventByNameOutputBoundary;
import use_case.search_event_by_name.SearchEventByNameOutputData;

public class SearchEventByNamePresenter implements SearchEventByNameOutputBoundary {

    private final SearchEventByNameViewModel searchEventByNameViewModel;
    private final ViewManagerModel viewManagerModel;

    public SearchEventByNamePresenter(SearchEventByNameViewModel searchEventByNameViewModel,
                                      ViewManagerModel viewManagerModel) {
        this.searchEventByNameViewModel = searchEventByNameViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(SearchEventByNameOutputData outputData) {
        SearchEventByNameState state = searchEventByNameViewModel.getState();
        state.setEvent(outputData.getEvent());
        state.setErrorMessage(null);
        searchEventByNameViewModel.setState(state);
        searchEventByNameViewModel.firePropertyChange();

        viewManagerModel.setState(searchEventByNameViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SearchEventByNameState state = searchEventByNameViewModel.getState();
        state.setEvent(null);
        state.setErrorMessage(errorMessage);
        searchEventByNameViewModel.setState(state);
        searchEventByNameViewModel.firePropertyChange();
    }

    @Override
    public void switchToDashboardView() {
        viewManagerModel.setState("search"); // or whatever the dashboard view name is
        viewManagerModel.firePropertyChange();
    }
}