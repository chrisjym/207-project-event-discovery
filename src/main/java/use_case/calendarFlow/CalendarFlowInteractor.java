package use_case.calendarFlow;

import entity.Event;

import java.time.LocalDate;
import java.util.List;

public class CalendarFlowInteractor implements CalendarFlowInputBoundary {
    private final CalendarFlowDataAccessInterface calendarFlowDataAccess;
    private final CalendarFlowOutputBoundary presenter;

    CalendarFlowInteractor(CalendarFlowDataAccessInterface calendarFlowDataAccess,
                           CalendarFlowOutputBoundary calendarFlowOutputBoundary) {
        this.calendarFlowDataAccess = calendarFlowDataAccess;
        this.presenter = calendarFlowOutputBoundary;
    }

    @Override
    public void execute(CalendarFlowInputData inputData) {
        if (inputData.getSelectedDate() == null) {
            presenter.prepareFailView("Date cannot be null");
            return;
        }

        if (inputData.getUserLocation() == null) {
            presenter.prepareFailView("User location is required");
            return;
        }

        List<Event> events = calendarFlowDataAccess.getEventsByDate(
                inputData.getSelectedDate(),
                inputData.getUserLocation(),
                inputData.getRadiusKm()
        );

        LocalDate date = inputData.getSelectedDate();
        if (events == null || events.isEmpty()) {
            presenter.prepareFailView("No events found for " + date);
        } else {
            CalendarFlowOutputData outputData = new CalendarFlowOutputData(date, events);
            presenter.prepareSuccessView(outputData);
        }
    }

//    //to implement later
//    @Override
//    public void switchToDashboardView() {
//        presenter.switchToDashboardView();
//    }
}
