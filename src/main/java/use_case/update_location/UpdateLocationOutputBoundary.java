package use_case.update_location;

/**
 * Output Boundary for the Update Location Use Case.
 *
 * CLEAN ARCHITECTURE NOTE:
 * This interface defines how the Use Case communicates results back.
 * The Presenter implements this interface.
 */
public interface UpdateLocationOutputBoundary {

    /**
     * Prepare the success view when location is updated.
     * @param outputData the output data with location info
     */
    void prepareSuccessView(UpdateLocationOutputData outputData);

    /**
     * Prepare the failure view when location update fails.
     * @param errorMessage the error message
     */
    void prepareFailView(String errorMessage);
}