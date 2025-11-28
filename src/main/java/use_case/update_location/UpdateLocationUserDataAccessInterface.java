package use_case.update_location;

import entity.Location;

/**
 * Data Access Interface for the Update Location Use Case.
 */
public interface UpdateLocationUserDataAccessInterface {

    /**
     * Get the current logged-in username.
     */
    String getCurrentUsername();

    /**
     * Update the user's location.
     * @param username the username
     * @param address the address string
     * @param location the location coordinates
     */
    void updateUserLocation(String username, String address, Location location);

    /**
     * Get the current user's location.
     * @return the location or null if not set
     */
    Location getCurrentUserLocation();

    /**
     * Get the current user's address string.
     * @return the address or empty string if not set
     */
    String getCurrentUserAddress();
}