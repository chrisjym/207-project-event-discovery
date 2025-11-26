package interface_adapter.save_event;

import interface_adapter.ViewManagerModel;
import use_case.save_event.SaveEventOutputBoundary;
import use_case.save_event.SaveEventOutputData;

public class SaveEventPresenter implements SaveEventOutputBoundary {

    private final SaveEventViewModel saveEventViewModel;
    private final ViewManagerModel viewManagerModel;

    public SaveEventPresenter(SaveEventViewModel saveEventViewModel, ViewManagerModel viewManagerModel) {
        this.saveEventViewModel = saveEventViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(SaveEventOutputData outputData) {
        SaveEventState currentState = saveEventViewModel.getState();
        currentState.setEvent(outputData.getEvent());
        saveEventViewModel.setState(currentState);
        saveEventViewModel.firePropertyChange("event");
    }

    @Override
    public void prepareFailureView(String errorMessage) {
        SaveEventState currentState = saveEventViewModel.getState();
        currentState.setEvent(null);
        saveEventViewModel.setState(currentState);
        saveEventViewModel.firePropertyChange("error");
    }

    @Override
    public void switchToDashboardView() {
        viewManagerModel.setState("search"); // or whatever the dashboard view name is
        viewManagerModel.firePropertyChange();
    }


}
