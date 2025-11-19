package use_case.search_event_by_name;

public interface SearchEventByNameInputBoundary {

    void execute(SearchEventByNameInputData inputData);

    /**
     * Executes the switch to login view use case.
     */
    void switchToDashboardView();
}
