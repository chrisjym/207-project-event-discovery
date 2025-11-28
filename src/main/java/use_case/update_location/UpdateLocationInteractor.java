package use_case.update_location;

import data_access.GeocodingDataAccessInterface;
import entity.Location;

import java.util.Optional;

/**
 * Interactor for the Update Location Use Case.
 *
 * CLEAN ARCHITECTURE NOTE:
 * This is the Use Case / Business Logic layer.
 * - It receives input via UpdateLocationInputBoundary
 * - It uses Data Access interfaces (not concrete implementations)
 * - It outputs via UpdateLocationOutputBoundary
 * - It contains the business rules for updating location
 */
public class UpdateLocationInteractor implements UpdateLocationInputBoundary {

    private final UpdateLocationUserDataAccessInterface userDataAccess;
    private final GeocodingDataAccessInterface geocodingService;
    private final UpdateLocationOutputBoundary presenter;

    public UpdateLocationInteractor(UpdateLocationUserDataAccessInterface userDataAccess,
                                    GeocodingDataAccessInterface geocodingService,
                                    UpdateLocationOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.geocodingService = geocodingService;
        this.presenter = presenter;
    }

    @Override
    public void execute(UpdateLocationInputData inputData) {
        String address = inputData.getAddress();

        // Validate input
        if (address == null || address.trim().isEmpty()) {
            presenter.prepareFailView("Please enter an address.");
            return;
        }

        // Check if user is logged in
        String username = userDataAccess.getCurrentUsername();
        if (username == null) {
            presenter.prepareFailView("You must be logged in to update your location.");
            return;
        }

        // Geocode the address
        Optional<Location> locationOptional = geocodingService.geocodeAddress(address.trim());

        if (locationOptional.isEmpty()) {
            presenter.prepareFailView("Could not find that address. Please try a more specific address.");
            return;
        }

        Location location = locationOptional.get();

        // Save to user account
        userDataAccess.updateUserLocation(username, address.trim(), location);

        // Prepare success response
        UpdateLocationOutputData outputData = new UpdateLocationOutputData(
                address.trim(),
                location,
                true
        );

        presenter.prepareSuccessView(outputData);
    }
}