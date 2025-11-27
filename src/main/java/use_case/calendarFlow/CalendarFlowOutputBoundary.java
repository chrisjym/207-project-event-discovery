package use_case.calendarFlow;

/**
 * OutputBoundary for the Calendar Flow Use Case
 * Defines the interface for presenting the results
 */
public interface CalendarFlowOutputBoundary {
    /**
     * Prepares the success view when events are found for the selected date.
     * @param outputData the output data containing the date and events
     */
    void prepareSuccessView(CalendarFlowOutputData outputData);

    /**
     * Prepares the view when no events are found for the selected date.
     * @param errorMessage the error message to display
     */
    void prepareFailView(String errorMessage);

//    //to implement later
//    /**
//     * Switch to the Dashboard View.
//     */
//    void switchToDashboardView();
}
