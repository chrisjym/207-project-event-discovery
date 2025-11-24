package interface_adapter.calendarFlow;

import entity.Location;
import use_case.calendarFlow.CalendarFlowInputBoundary;
import use_case.calendarFlow.CalendarFlowInputData;
import java.time.LocalDate;

public class CalendarFlowController {
    private final CalendarFlowInputBoundary CalendarFlowInteractor;

    public CalendarFlowController(CalendarFlowInputBoundary interactor) {
        this.CalendarFlowInteractor = interactor;
    }

    /**
     * Execute the search event by name use case.
     * @param selectedDate the event date to search for
     * @param userLocation the user's location
     * @param radiusKm the search radius in kilometers
     */
    public void execute(LocalDate selectedDate, Location userLocation, double radiusKm){
        final CalendarFlowInputData inputData = new CalendarFlowInputData(selectedDate, userLocation, radiusKm);

        CalendarFlowInteractor.execute(inputData);
    }

//    //to implement later
//    public void switchToDashboardView() {
//        CalendarFlowInteractor.switchToDashboardView();
//    }
}
