package use_case.search_event_by_name;

public interface SearchEventByNameOutputBoundary {
    /**
     * Prepares the success view with the found event.
     * @param outputData the output data containing the event
     */
    void prepareSuccessView(SearchEventByNameOutputData outputData);

    /**
     * Prepares the failure view when no event is found.
     * @param errorMessage the error message to display
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the Dashboard View.
     */
    void switchToDashboardView();
}