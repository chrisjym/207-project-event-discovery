package use_case.calendarFlow;

import java.time.LocalDate;
import java.util.List;

import entity.Event;

public class CalendarFlowInteractor implements CalendarFlowInputBoundary {
    private final CalendarFlowDataAccessInterface calendarFlowDataAccess;
    private final CalendarFlowOutputBoundary presenter;

    public CalendarFlowInteractor(CalendarFlowDataAccessInterface calendarFlowDataAccess,
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

        try {
            final List<Event> events = calendarFlowDataAccess.getEventsByDate(
                    inputData.getSelectedDate(),
                    inputData.getUserLocation(),
                    inputData.getRadiusKm()
            );

            final LocalDate date = inputData.getSelectedDate();
            if (events == null || events.isEmpty()) {
                presenter.prepareFailView("No events found for " + date);
            }
            else {
                final CalendarFlowOutputData outputData = new CalendarFlowOutputData(date, events);
                presenter.prepareSuccessView(outputData);
            }
        }
        catch (Exception ex) {
            presenter.prepareFailView("Error fetching events: " + ex.getMessage());
        }
    }

    @Override
    public void switchToDashboardView() {
        presenter.switchToDashboardView();
    }
}
