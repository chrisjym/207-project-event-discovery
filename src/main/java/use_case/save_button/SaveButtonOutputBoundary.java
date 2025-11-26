package use_case.save_button;

public interface SaveButtonOutputBoundary {

    /**
     * Prepare success view for save button use case
     * @param outputData
     */
    void prepareSuccessView(SaveButtonOutputData outputData);

    /**
     * Prepare failure view for save button use case
     * @param errorMessage
     */
    void prepareFailureView(String errorMessage);
}
