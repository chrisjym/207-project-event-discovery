package use_case.calendarFlow;

/**
 * InputBoundary for the Calendar Flow Use Case.
 * Defines the interface for executing the calendar flow use case
 */
public interface CalendarFlowInputBoundary {

    /**
     *  Executes the Calendar Flow use case using the provided input data.
     *  This includes validating the date, user location, and radius, and retrieving the corresponding list of events.
     * @param inputData the input data containing the selected date, user location, and search radius
     */
    void execute(CalendarFlowInputData inputData);

    /**
     * Switch to the Dashboard View.
     */
    void switchToDashboardView();
}
