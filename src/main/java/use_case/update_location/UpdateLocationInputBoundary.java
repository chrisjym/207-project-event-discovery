package use_case.update_location;

/**
 * Input Boundary for the Update Location Use Case.
 *
 * CLEAN ARCHITECTURE NOTE:
 * This interface defines what the Controller can ask the Use Case to do.
 * The Interactor implements this interface.
 */
public interface UpdateLocationInputBoundary {

    /**
     * Execute the update location use case.
     * @param inputData the input data containing the address
     */
    void execute(UpdateLocationInputData inputData);
}