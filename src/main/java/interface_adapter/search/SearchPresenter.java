package interface_adapter.search;

import interface_adapter.ViewManagerModel;
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
    }

    @Override
    public void prepareFailureView(String failMessage) {

    }

    @Override
    public void switchToEventSearchView(SearchOutputData outputData) {
        viewManagerModel.setState(searchEventByNameViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
