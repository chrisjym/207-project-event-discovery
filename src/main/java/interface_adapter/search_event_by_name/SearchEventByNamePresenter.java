package interface_adapter.search_event_by_name;

import interface_adapter.ViewManagerModel;
// Change to dashboardViewModel
import interface_adapter.login.LoginViewModel;
import view.ViewManager;
import use_case.search_event_by_name.SearchEventByNameOutputBoundary;

public class SearchEventByNamePresenter implements SearchEventByNameOutputBoundary {
    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    public SearchEventByNamePresenter(ViewManagerModel viewManagerModel, LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        // Change to dashboardViewModel
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void switchToDashboardView() {
        viewManagerModel.setState(loginViewModel.getViewName());
        // Need to change this to the dashboardViewModel when finished
        viewManagerModel.firePropertyChange();
    }
}
