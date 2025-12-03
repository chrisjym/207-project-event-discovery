package data_access;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Event;
import entity.EventCategory;
import entity.Location;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import use_case.calendarFlow.CalendarFlowDataAccessInterface;

public class CalendarFlowDataAccessObject implements CalendarFlowDataAccessInterface {
    private static final String BASE_URL = "https://app.ticketmaster.com/discovery/v2";
    private static final String EVENTS_ENDPOINT = "/events.json";
    private static final String API_KEY = "oL2pW4zAlAZvhBAhPNi5mNYvS7OsBM9J";
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Search events by date.
     * @param date the selected specific date
     * @param location the user's location
     * @param radiusKm the search radius in kilometers
     */
    @Override
    public List<Event> getEventsByDate(LocalDate date, Location location, double radiusKm) {
        final HttpUrl.Builder urlBuilder = HttpUrl
                .parse(BASE_URL + EVENTS_ENDPOINT)
                .newBuilder()
                .addQueryParameter("apikey", API_KEY);

        final ZonedDateTime startZdt = date.atStartOfDay(ZoneOffset.UTC);
        final ZonedDateTime endZdt = date.atTime(23, 59, 59).atZone(ZoneOffset.UTC);
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

        urlBuilder.addQueryParameter("startDateTime", formatter.format(startZdt));
        urlBuilder.addQueryParameter("endDateTime", formatter.format(endZdt));
        urlBuilder.addQueryParameter("latlong",
                String.format("%.6f,%.6f", location.getLatitude(), location.getLongitude()));
        urlBuilder.addQueryParameter("radius", String.valueOf((int) radiusKm));
        urlBuilder.addQueryParameter("unit", "km");
        urlBuilder.addQueryParameter("size", "50");

        final String url = urlBuilder.build().toString();
        return fetchEvents(url);
    }

    private List<Event> fetchEvents(String url) {
        final List<Event> events = new ArrayList<>();

        final Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Failed to fetch events: " + response.code());
                return events;
            }

            final String jsonResponse = response.body().string();
            final JSONObject root = new JSONObject(jsonResponse);

            if (!root.has("_embedded")) {
                return events;
            }

            final JSONObject embedded = root.getJSONObject("_embedded");
            if (!embedded.has("events")) {
                return events;
            }

            JSONArray eventsArray = embedded.getJSONArray("events");

            for (int i = 0; i < eventsArray.length(); i++) {
                final JSONObject eventJson = eventsArray.getJSONObject(i);
                Event event = parseEvent(eventJson);
                if (event != null) {
                    events.add(event);
                }
            }

        }
        catch (IOException evt) {
            System.err.println("Error fetching events: " + evt.getMessage());
        }

        return events;
    }

    private Event parseEvent(JSONObject eventJson) {
        try {
            final String id = eventJson.getString("id");
            final String name = eventJson.getString("name");

            String description = "";
            if (eventJson.has("info")) {
                description = eventJson.getString("info");
            }
            else if (eventJson.has("description")) {
                description = eventJson.getString("description");
            }
            else if (eventJson.has("pleaseNote")) {
                description = eventJson.getString("pleaseNote");
            }

            final EventCategory category = extractCategory(eventJson);
            final Location location = extractLocation(eventJson);
            if (location == null) {
                return null;
            }

            final LocalDateTime startTime = extractStartTime(eventJson);
            if (startTime == null) {
                return null;
            }

            final String imageUrl = extractImageUrl(eventJson);

            return new Event(id, name, description, location.getAddress(), category, location, startTime, imageUrl);

        }
        catch (Exception evt) {
            System.err.println("Error parsing event: " + evt.getMessage());
            return null;
        }
    }

    private String extractImageUrl(JSONObject eventJson) {
        try {
            if (eventJson.has("images")) {
                JSONArray images = eventJson.getJSONArray("images");
                if (images.length() > 0) {
                    JSONObject firstImage = images.getJSONObject(3);
                    if (firstImage.has("url")) {
                        return firstImage.getString("url");
                    }
                }
            }
        }
        catch (Exception evt) {
            System.err.println("Error extracting image URL: " + evt.getMessage());
        }
        return "";
    }

    private EventCategory extractCategory(JSONObject eventJson) {
        try {
            if (eventJson.has("classifications")) {
                JSONArray classifications = eventJson.getJSONArray("classifications");
                if (classifications.length() > 0) {
                    JSONObject classification = classifications.getJSONObject(0);

                    if (classification.has("segment")) {
                        JSONObject segment = classification.getJSONObject("segment");
                        if (segment.has("name")) {
                            String segmentName = segment.getString("name");
                            EventCategory category = mapTicketmasterToCategory(segmentName);
                            if (category != EventCategory.MISCELLANEOUS) {
                                return category;
                            }
                        }
                    }

                    if (classification.has("genre")) {
                        JSONObject genre = classification.getJSONObject("genre");
                        if (genre.has("name")) {
                            String genreName = genre.getString("name");
                            EventCategory category = mapTicketmasterToCategory(genreName);
                            if (category != EventCategory.MISCELLANEOUS) {
                                return category;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception evt) {
            System.err.println("Error extracting category: " + evt.getMessage());
        }

        return EventCategory.MISCELLANEOUS;
    }

    private Location extractLocation(JSONObject eventJson) {
        try {
            if (!eventJson.has("_embedded")) {
                return null;
            }

            final JSONObject embedded = eventJson.getJSONObject("_embedded");
            if (!embedded.has("venues")) {
                return null;
            }

            final JSONArray venues = embedded.getJSONArray("venues");
            if (venues.length() == 0) {
                return null;
            }

            final JSONObject venue = venues.getJSONObject(0);

            final StringBuilder addressBuilder = new StringBuilder();

            if (venue.has("name")) {
                addressBuilder.append(venue.getString("name"));
            }

            if (venue.has("address")) {
                final JSONObject address = venue.getJSONObject("address");
                if (address.has("line1")) {
                    if (addressBuilder.length() > 0) {
                        addressBuilder.append(", ");
                    }
                    addressBuilder.append(address.getString("line1"));
                }
            }

            if (venue.has("city")) {
                final JSONObject city = venue.getJSONObject("city");
                if (city.has("name")) {
                    if (addressBuilder.length() > 0) {
                        addressBuilder.append(", ");
                    }
                    addressBuilder.append(city.getString("name"));
                }
            }

            if (venue.has("state")) {
                final JSONObject state = venue.getJSONObject("state");
                if (state.has("stateCode")) {
                    if (addressBuilder.length() > 0) {
                        addressBuilder.append(", ");
                    }
                    addressBuilder.append(state.getString("stateCode"));
                }
            }

            String address = addressBuilder.toString();
            if (address.trim().isEmpty()) {
                address = "Address not available";
            }

            if (!venue.has("location")) {
                return null;
            }

            JSONObject locationJson = venue.getJSONObject("location");
            if (!locationJson.has("latitude") || !locationJson.has("longitude")) {
                return null;
            }

            final double latitude = locationJson.getDouble("latitude");
            final double longitude = locationJson.getDouble("longitude");

            return new Location(address, latitude, longitude);

        }
        catch (Exception evt) {
            System.err.println("Error extracting location: " + evt.getMessage());
            return null;
        }
    }

    private LocalDateTime extractStartTime(JSONObject eventJson) {
        try {
            if (!eventJson.has("dates")) {
                return null;
            }

            final JSONObject dates = eventJson.getJSONObject("dates");
            if (!dates.has("start")) {
                return null;
            }

            final JSONObject start = dates.getJSONObject("start");

            if (start.has("dateTime")) {
                String dateTimeStr = start.getString("dateTime");
                dateTimeStr = dateTimeStr.replace("Z", "");
                final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                return LocalDateTime.parse(dateTimeStr, formatter);
            }
            else if (start.has("localDate")) {
                final String localDate = start.getString("localDate");
                String localTime = "19:00:00";
                if (start.has("localTime")) {
                    localTime = start.getString("localTime");
                }
                return LocalDateTime.parse(localDate + "T" + localTime);
            }

            return null;

        }
        catch (Exception evt) {
            System.err.println("Error extracting start time: " + evt.getMessage());
            return null;
        }
    }

    private String mapCategoryToTicketmaster(EventCategory category) {
        switch (category) {
            case SPORTS:
                return "Sports";
            case MUSIC:
                return "Music";
            case ARTS_THEATRE:
                return "Arts & Theatre";
            case FILM:
                return "Film";
            case MISCELLANEOUS:
                return null;
            default:
                return null;
        }
    }

    private EventCategory mapTicketmasterToCategory(String ticketmasterName) {
        if (ticketmasterName == null) {
            return EventCategory.MISCELLANEOUS;
        }

        final String normalized = ticketmasterName.toLowerCase().trim();

        if (normalized.contains("music") || normalized.contains("concert")) {
            return EventCategory.MUSIC;
        }

        if (normalized.contains("sports") || normalized.contains("basketball") ||
                normalized.contains("football") || normalized.contains("hockey") ||
                normalized.contains("soccer") || normalized.contains("baseball")) {
            return EventCategory.SPORTS;
        }

        if (normalized.contains("arts") || normalized.contains("theatre") ||
                normalized.contains("theater") || normalized.contains("dance") ||
                normalized.contains("opera") || normalized.contains("ballet")) {
            return EventCategory.ARTS_THEATRE;
        }

        if (normalized.contains("film") || normalized.contains("movie") ||
                normalized.contains("cinema")) {
            return EventCategory.FILM;
        }

        return EventCategory.MISCELLANEOUS;
    }
}
