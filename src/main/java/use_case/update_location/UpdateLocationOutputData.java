package use_case.update_location;

import entity.Location;

/**
 * Output Data for the Update Location Use Case.
 */
public class UpdateLocationOutputData {

    private final String address;
    private final Location location;
    private final boolean success;

    public UpdateLocationOutputData(String address, Location location, boolean success) {
        this.address = address;
        this.location = location;
        this.success = success;
    }

    public String getAddress() {
        return address;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isSuccess() {
        return success;
    }
}