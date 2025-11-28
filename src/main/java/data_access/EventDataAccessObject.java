package data_access;

import entity.Event;
import entity.EventCategory;
import entity.Location;
import okhttp3.*;
import org.json.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventDataAccessObject {

    private static final String API_KEY = "API_KEY"; // Replace with actual API key
    private static final String BASE_URL = "https://app.ticketmaster.com/discovery/v2";
    private static final String EVENTS_ENDPOINT = "/events.json";

    private final OkHttpClient client;

    public EventDataAccessObject() {
        this.client = new OkHttpClient();
    }

    /**
     * Get events near a location within a radius
     */
    public List<Event> getEventsByLocation(Location location, double radiusKm) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + EVENTS_ENDPOINT).newBuilder();
        urlBuilder.addQueryParameter("apikey", API_KEY);
        urlBuilder.addQueryParameter("latlong",
                String.format("%.6f,%.6f", location.getLatitude(), location.getLongitude()));
        urlBuilder.addQueryParameter("radius", String.valueOf((int) radiusKm));
        urlBuilder.addQueryParameter("unit", "km");
        urlBuilder.addQueryParameter("size", "50");
        urlBuilder.addQueryParameter("sort", "distance,asc");

        return fetchEvents(urlBuilder.build().toString());
    }

    /**
     * Get events filtered by category
     */
    public List<Event> getEventsByCategory(Location location, double radiusKm, EventCategory category) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + EVENTS_ENDPOINT).newBuilder();
        urlBuilder.addQueryParameter("apikey", API_KEY);
        urlBuilder.addQueryParameter("latlong",
                String.format("%.6f,%.6f", location.getLatitude(), location.getLongitude()));
        urlBuilder.addQueryParameter("radius", String.valueOf((int) radiusKm));
        urlBuilder.addQueryParameter("unit", "km");
        urlBuilder.addQueryParameter("size", "50");
        urlBuilder.addQueryParameter("sort", "distance,asc");

        String classificationName = mapCategoryToTicketmaster(category);
        if (classificationName != null) {
            urlBuilder.addQueryParameter("classificationName", classificationName);
        }

        return fetchEvents(urlBuilder.build().toString());
    }

    /**
     * Search events by name/keyword
     */
    public List<Event> searchEventsByName(String keyword, Location location, double radiusKm) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + EVENTS_ENDPOINT).newBuilder();
        urlBuilder.addQueryParameter("apikey", API_KEY);
        urlBuilder.addQueryParameter("keyword", keyword);
        urlBuilder.addQueryParameter("latlong",
                String.format("%.6f,%.6f", location.getLatitude(), location.getLongitude()));
        urlBuilder.addQueryParameter("radius", String.valueOf((int) radiusKm));
        urlBuilder.addQueryParameter("unit", "km");
        urlBuilder.addQueryParameter("size", "50");

        return fetchEvents(urlBuilder.build().toString());
    }

    /**
     * Get a specific event by ID
     */
    public Event getEventById(String eventId) {
        String url = BASE_URL + "/events/" + eventId + ".json?apikey=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return null;
            }

            String jsonResponse = response.body().string();
            JSONObject eventJson = new JSONObject(jsonResponse);

            return parseEvent(eventJson);

        } catch (IOException e) {
            return null;
        }
    }

    private List<Event> fetchEvents(String url) {
        List<Event> events = new ArrayList<>();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()){
            if (!response.isSuccessful()) {
                System.err.println("Failed to fetch events: " + response.code());
                return events;
            }

            String jsonResponse = response.body().string();
            JSONObject root = new JSONObject(jsonResponse);

            if (!root.has("_embedded")) {
                return events;
            }

            JSONObject embedded = root.getJSONObject("_embedded");
            if (!embedded.has("events")) {
                return events;
            }

            JSONArray eventsArray = embedded.getJSONArray("events");

            for (int i = 0; i < eventsArray.length(); i++) {
                JSONObject eventJson = eventsArray.getJSONObject(i);
                Event event = parseEvent(eventJson);
                if (event != null) {
                    events.add(event);
                }
            }

        } catch (IOException e) {
            System.err.println("Error fetching events: " + e.getMessage());
        }

        return events;
    }

    private Event parseEvent(JSONObject eventJson) {
        try {
            String id = eventJson.getString("id");
            String name = eventJson.getString("name");

            String description = "";
            if (eventJson.has("info")) {
                description = eventJson.getString("info");
            } else if (eventJson.has("description")) {
                description = eventJson.getString("description");
            } else if (eventJson.has("pleaseNote")) {
                description = eventJson.getString("pleaseNote");
            }

            EventCategory category = extractCategory(eventJson);
            Location location = extractLocation(eventJson);
            if (location == null) {
                return null;
            }

            LocalDateTime startTime = extractStartTime(eventJson);
            if (startTime == null) {
                return null;
            }

            String imageUrl = extractImageUrl(eventJson);

            return new Event(id, name, description, location.getAddress(), category, location, startTime, imageUrl);

        } catch (Exception e) {
            System.err.println("Error parsing event: " + e.getMessage());
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
        } catch (Exception e) {
            System.err.println("Error extracting image URL: " + e.getMessage());
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
        } catch (Exception e) {
            System.err.println("Error extracting category: " + e.getMessage());
        }

        return EventCategory.MISCELLANEOUS;
    }

    private Location extractLocation(JSONObject eventJson) {
        try {
            if (!eventJson.has("_embedded")) {
                return null;
            }

            JSONObject embedded = eventJson.getJSONObject("_embedded");
            if (!embedded.has("venues")) {
                return null;
            }

            JSONArray venues = embedded.getJSONArray("venues");
            if (venues.length() == 0) {
                return null;
            }

            JSONObject venue = venues.getJSONObject(0);

            StringBuilder addressBuilder = new StringBuilder();

            if (venue.has("name")) {
                addressBuilder.append(venue.getString("name"));
            }

            if (venue.has("address")) {
                JSONObject address = venue.getJSONObject("address");
                if (address.has("line1")) {
                    if (addressBuilder.length() > 0) {
                        addressBuilder.append(", ");
                    }
                    addressBuilder.append(address.getString("line1"));
                }
            }

            if (venue.has("city")) {
                JSONObject city = venue.getJSONObject("city");
                if (city.has("name")) {
                    if (addressBuilder.length() > 0) {
                        addressBuilder.append(", ");
                    }
                    addressBuilder.append(city.getString("name"));
                }
            }

            if (venue.has("state")) {
                JSONObject state = venue.getJSONObject("state");
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

            double latitude = locationJson.getDouble("latitude");
            double longitude = locationJson.getDouble("longitude");

            return new Location(address, latitude, longitude);

        } catch (Exception e) {
            System.err.println("Error extracting location: " + e.getMessage());
            return null;
        }
    }

    private LocalDateTime extractStartTime(JSONObject eventJson) {
        try {
            if (!eventJson.has("dates")) {
                return null;
            }

            JSONObject dates = eventJson.getJSONObject("dates");
            if (!dates.has("start")) {
                return null;
            }

            JSONObject start = dates.getJSONObject("start");

            if (start.has("dateTime")) {
                String dateTimeStr = start.getString("dateTime");
                dateTimeStr = dateTimeStr.replace("Z", "");
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                return LocalDateTime.parse(dateTimeStr, formatter);
            } else if (start.has("localDate")) {
                String localDate = start.getString("localDate");
                String localTime = "19:00:00";
                if (start.has("localTime")) {
                    localTime = start.getString("localTime");
                }
                return LocalDateTime.parse(localDate + "T" + localTime);
            }

            return null;

        } catch (Exception e) {
            System.err.println("Error extracting start time: " + e.getMessage());
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

        String normalized = ticketmasterName.toLowerCase().trim();

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
