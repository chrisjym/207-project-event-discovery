package use_case.event_description;

public interface EventDescriptionOutputBoundary {
    void prepareSuccessView(EventDescriptionOutputData outputData);
    void prepareFailView(String errorMessage);
}
