package data_access;

import entity.Event;
import entity.EventCategory;
import entity.Location;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileSavedEventsDataAccessObject {
    private static final String SAVED_EVENTS_DIR = "events_saved";

    public FileSavedEventsDataAccessObject() {
        // This is to create the file directory with the name "events_saved", this wasn't done with a csv but a json
        File directory = new File(SAVED_EVENTS_DIR);
        // Make sure the directory doesn't exist before the directory is created
        if (directory.exists()) {
            return;
        } else {
            directory.mkdir();
        }
    }

    public void saveEvent(String username, Event event) {
        List<Event> savedEvents = getSavedEvents(username);
        for (int i = 0; i < savedEvents.size(); i++) {
            if (savedEvents.get(i).getId().equals(event.getId())) {
                return;
            }
        }
        savedEvents.add(event);
        writeEventsToFile(username, savedEvents);
    }

    public void removeEvent(String username, Event event) {
        List<Event> savedEvents = getSavedEvents(username);
        for (int i = 0; i < savedEvents.size(); i++) {
            if (savedEvents.get(i).getId().equals(event.getId())) {
                savedEvents.remove(i);
            }
        }
        writeEventsToFile(username, savedEvents);
    }

    public List<Event> getSavedEvents(String username) {
        File file = new File(SAVED_EVENTS_DIR, username + "_events.json");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
            JSONArray jsonArray = new JSONArray(content);

            List<Event> events = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject eventJson = jsonArray.getJSONObject(i);
                Event event = parseEventFromJson(eventJson);
                if (event != null) {
                    events.add(event);
                }
            }
            return events;

        } catch (IOException e) {
            System.err.println("Error reading saved events for user " + username + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean isSavedEvent(String username, String id) {
        List<Event> savedEvents = getSavedEvents(username);
        for (int i = 0; i < savedEvents.size(); i++) {
            if (savedEvents.get(i).getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Write events to file for a specific user.
     */
    private void writeEventsToFile(String username, List<Event> events) {
        File file = new File(SAVED_EVENTS_DIR, username + "_events.json");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            JSONArray jsonArray = new JSONArray();

            for (Event event : events) {
                JSONObject eventJson = convertEventToJson(event);
                jsonArray.put(eventJson);
            }

            writer.write(jsonArray.toString(4));

        } catch (IOException e) {
            System.err.println("Error writing saved events for user " + username + ": " + e.getMessage());
        }
    }
    /**
     * Convert an Event object to JSON.
     */
    private JSONObject convertEventToJson(Event event) {
        JSONObject json = new JSONObject();
        json.put("id", event.getId());
        json.put("name", event.getName());
        json.put("description", event.getDescription());
        json.put("category", event.getCategory().name());
        json.put("imageUrl", event.getImageUrl());
        json.put("startTime", event.getStartTime());

        // Location
        JSONObject locationJson = new JSONObject();
        locationJson.put("address", event.getLocation().getAddress());
        locationJson.put("latitude", event.getLocation().getLatitude());
        locationJson.put("longitude", event.getLocation().getLongitude());
        json.put("location", locationJson);

        return json;
    }

    /**
     * Parse an Event object from JSON.
     */
    private Event parseEventFromJson(JSONObject json) {
        try {
            String id = json.getString("id");
            String name = json.getString("name");
            String description = json.getString("description");
            EventCategory category = EventCategory.valueOf(json.getString("category"));
            String imageUrl = json.getString("imageUrl");
            LocalDateTime startTime = LocalDateTime.parse(json.getString("startTime"));

            JSONObject locationJson = json.getJSONObject("location");
            Location location = new Location(
                    locationJson.getString("address"),
                    locationJson.getDouble("latitude"),
                    locationJson.getDouble("longitude")
            );

            return new Event(id, name, description, location.getAddress(), category, location, startTime, imageUrl);

        } catch (Exception e) {
            System.err.println("Error parsing event from JSON: " + e.getMessage());
            return null;
        }
    }

}
