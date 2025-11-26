package use_case.display_local_events;

public interface DisplayLocalEventsOutputBoundary {
    void presentSuccess(DisplayLocalEventsOutputData outputData);

    void presentError(String errorMessage);
}
