package interface_adapter.calendarFlow;

import java.time.LocalDate;

import entity.Location;
import use_case.calendarFlow.CalendarFlowInputBoundary;
import use_case.calendarFlow.CalendarFlowInputData;

public class CalendarFlowController {
    private final CalendarFlowInputBoundary calendarFlowInteractor;

    public CalendarFlowController(CalendarFlowInputBoundary interactor) {
        this.calendarFlowInteractor = interactor;
    }

    /**
     * Execute the search event by name use case.
     * @param selectedDate the event date to search for
     * @param userLocation the user's location
     * @param radiusKm the search radius in kilometers
     */
    public void execute(LocalDate selectedDate, Location userLocation, double radiusKm) {
        final CalendarFlowInputData inputData = new CalendarFlowInputData(selectedDate, userLocation, radiusKm);

        calendarFlowInteractor.execute(inputData);
    }

    /**
     * Handles the request to switch to the dashboard view.
     * This method delegates the view change logic to the calendar flow interactor
     */
    public void switchToDashboardView() {
        calendarFlowInteractor.switchToDashboardView();
    }
}
