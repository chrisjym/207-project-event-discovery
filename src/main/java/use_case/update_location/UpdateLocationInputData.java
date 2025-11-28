package use_case.update_location;

/**
 * Input Data for the Update Location Use Case.
 */
public class UpdateLocationInputData {

    private final String address;

    public UpdateLocationInputData(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}