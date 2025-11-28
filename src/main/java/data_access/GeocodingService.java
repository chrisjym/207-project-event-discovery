package data_access;

import entity.Location;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Service to convert addresses to coordinates (geocoding).
 * Uses the free Nominatim OpenStreetMap API.
 *
 * CLEAN ARCHITECTURE NOTE:
 * This is a Data Access / External Service adapter.
 * It implements an interface that the Use Case layer depends on.
 */
public class GeocodingService implements GeocodingDataAccessInterface {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    private final OkHttpClient client;

    public GeocodingService() {
        this.client = new OkHttpClient();
    }

    /**
     * Convert an address string to Location coordinates.
     *
     * @param address the address to geocode (e.g., "123 Main St, Toronto, ON")
     * @return Optional containing Location if found, empty if not found
     */
    @Override
    public Optional<Location> geocodeAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            String encodedAddress = URLEncoder.encode(address.trim(), StandardCharsets.UTF_8.toString());
            String url = NOMINATIM_URL + "?q=" + encodedAddress + "&format=json&limit=1";

            Request request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", "EventGateApp/1.0")  // Required by Nominatim
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.err.println("Geocoding failed: " + response.code());
                    return Optional.empty();
                }

                String jsonResponse = response.body().string();
                JSONArray results = new JSONArray(jsonResponse);

                if (results.length() == 0) {
                    System.err.println("No geocoding results for: " + address);
                    return Optional.empty();
                }

                JSONObject firstResult = results.getJSONObject(0);
                double latitude = firstResult.getDouble("lat");
                double longitude = firstResult.getDouble("lon");
                String displayName = firstResult.getString("display_name");

                // Use the display name from the API for a standardized address
                Location location = new Location(displayName, latitude, longitude);

                System.out.println("Geocoded '" + address + "' to: " + latitude + ", " + longitude);
                return Optional.of(location);

            }
        } catch (IOException e) {
            System.err.println("Geocoding error: " + e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Geocoding parse error: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Validate if an address can be geocoded.
     */
    @Override
    public boolean isValidAddress(String address) {
        return geocodeAddress(address).isPresent();
    }
}