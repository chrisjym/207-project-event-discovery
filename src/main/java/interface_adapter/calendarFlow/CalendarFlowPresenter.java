package interface_adapter.calendarFlow;

import java.util.ArrayList;

import interface_adapter.ViewManagerModel;
import use_case.calendarFlow.CalendarFlowOutputBoundary;
import use_case.calendarFlow.CalendarFlowOutputData;

public class CalendarFlowPresenter implements CalendarFlowOutputBoundary {
    private final CalendarFlowViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public CalendarFlowPresenter(CalendarFlowViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(CalendarFlowOutputData outputData) {
        final CalendarFlowState state = new CalendarFlowState();
        state.setEventList(outputData.getEvents());
        state.setDate(outputData.getSelectedDate());
        state.setErrorMessage(null);

        viewModel.setState(state);
        viewModel.firePropertyChange();

        viewManagerModel.setState(viewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final CalendarFlowState state = viewModel.getState();
        state.setEventList(new ArrayList<>());
        state.setErrorMessage(errorMessage);

        viewManagerModel.setState(viewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void switchToDashboardView() {
        viewManagerModel.setState("display local events");
        viewManagerModel.firePropertyChange();
    }
}
