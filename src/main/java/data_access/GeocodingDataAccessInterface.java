package data_access;

import entity.Location;
import java.util.Optional;

/**
 * Interface for geocoding services.
 *
 * CA NOTE:
 * interface lives in the data_access package but is used by use cases.
 * allows us to swap implementations (e.g., for testing or different APIs).
 */
public interface GeocodingDataAccessInterface {

    /**
     * Convert an address string to Location coordinates.
     * @param address the address to geocode
     * @return Optional containing Location if found, empty if not found
     */
    Optional<Location> geocodeAddress(String address);

    /**
     * Check if an address can be geocoded.
     * @param address the address to validate
     * @return true if the address is valid and can be geocoded
     */
    boolean isValidAddress(String address);
}