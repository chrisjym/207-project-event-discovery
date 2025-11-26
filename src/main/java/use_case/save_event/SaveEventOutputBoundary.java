package use_case.save_event;

public interface SaveEventOutputBoundary {


    /**
     * Prepares the success view for the Save event use case
     * @param outputData
     */
    void prepareSuccessView(SaveEventOutputData outputData);

    /**
     * Prepares the failure view for the Save event use case
     * @param errorMessage
     */
    void prepareFailureView(String errorMessage);

    /**
     * Switches to dashboard view for the use case
     */
    void switchToDashboardView();

}
