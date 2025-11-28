package interface_adapter.update_location;

import entity.Location;

/**
 * State for the Update Location ViewModel.
 */
public class UpdateLocationState {

    private String address = "";
    private Location location = null;
    private String error = null;
    private boolean success = false;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean hasLocation() {
        return location != null;
    }
}