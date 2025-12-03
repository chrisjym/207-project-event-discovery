package interface_adapter.save_event;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewNames;
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
        currentState.addSavedEvent(outputData.getEvent());
        saveEventViewModel.setState(currentState);
        saveEventViewModel.firePropertyChange("event");
    }

    @Override
    public void prepareFailureView(String errorMessage) {
        SaveEventState currentState = saveEventViewModel.getState();
        currentState.addSavedEvent(null);
        saveEventViewModel.setState(currentState);
        saveEventViewModel.firePropertyChange("error");
    }

    @Override
    public void switchToDashboardView() {
        viewManagerModel.setState(ViewNames.DISPLAY_LOCAL_EVENTS);
        viewManagerModel.firePropertyChange();
    }

}
