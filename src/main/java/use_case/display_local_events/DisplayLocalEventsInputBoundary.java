package use_case.display_local_events;

public interface DisplayLocalEventsInputBoundary {
    void execute(DisplayLocalEventsInputData inputData);

    /**
     * Executes switch to save event view use case
     */
    void switchToSaveView();
}
