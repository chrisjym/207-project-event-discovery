package interface_adapter.search;

import interface_adapter.ViewManagerModel;
import interface_adapter.search_event_by_name.SearchEventByNameState;
import interface_adapter.search_event_by_name.SearchEventByNameViewModel;
import use_case.search.SearchOutputBoundary;
import use_case.search.SearchOutputData;

public class SearchPresenter implements SearchOutputBoundary {
    private final SearchEventByNameViewModel searchEventByNameViewModel;
    private final ViewManagerModel viewManagerModel;

    public SearchPresenter(SearchEventByNameViewModel searchEventByNameViewModel, ViewManagerModel viewManagerModel) {
        this.searchEventByNameViewModel = searchEventByNameViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(SearchOutputData outputData) {
        SearchEventByNameState state = searchEventByNameViewModel.getState();
        state.setEvent(outputData.getEvent());  // ← Assumes SearchOutputData has getEvent()
        searchEventByNameViewModel.setState(state);
        searchEventByNameViewModel.firePropertyChange();

        viewManagerModel.setState(searchEventByNameViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailureView(String failMessage) {

    }

    @Override
    public void switchToEventSearchView(SearchOutputData outputData) {
    }
}
