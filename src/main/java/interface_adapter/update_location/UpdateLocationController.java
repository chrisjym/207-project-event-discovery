package interface_adapter.update_location;

import use_case.update_location.UpdateLocationInputBoundary;
import use_case.update_location.UpdateLocationInputData;

/**
 * Controller for the Update Location Use Case.
 *
 * CLEAN ARCHITECTURE NOTE:
 * The Controller receives input from the View and converts it to
 * Use Case input data, then calls the Use Case.
 */
public class UpdateLocationController {

    private final UpdateLocationInputBoundary updateLocationInteractor;

    public UpdateLocationController(UpdateLocationInputBoundary updateLocationInteractor) {
        this.updateLocationInteractor = updateLocationInteractor;
    }

    /**
     * Execute the update location use case.
     * @param address the address entered by the user
     */
    public void execute(String address) {
        UpdateLocationInputData inputData = new UpdateLocationInputData(address);
        updateLocationInteractor.execute(inputData);
    }
}