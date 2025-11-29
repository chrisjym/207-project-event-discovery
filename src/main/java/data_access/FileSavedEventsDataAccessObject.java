package data_access;

import entity.Event;
import entity.Location;
import entity.EventCategory;
import use_case.save_event.SaveEventDataAccessInterface;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * DAO for saved events with FILE PERSISTENCE.
 * Events are saved to a CSV file and persist across app restarts.
 *
 * CSV Format: username,eventId,eventName,category,dateTime,address,latitude,longitude,imageUrl,description
 */
public class FileSavedEventsDataAccessObject implements SaveEventDataAccessInterface {

    private static final String FILENAME = "saved_events.csv";
    private static final String HEADER = "username,eventId,eventName,category,dateTime,address,latitude,longitude,imageUrl,description";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final File csvFile;
    private final Map<String, List<Event>> savedEventsByUser = new HashMap<>();

    public FileSavedEventsDataAccessObject() {
        this.csvFile = new File(FILENAME);
        loadFromFile();
    }

    /**
     * Load saved events from file.
     */
    private void loadFromFile() {
        if (!csvFile.exists() || csvFile.length() == 0) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String header = reader.readLine(); // Skip header

            String row;
            while ((row = reader.readLine()) != null) {
                try {
                    // Parse CSV with proper handling of commas in quoted strings
                    String[] col = parseCSVLine(row);

                    if (col.length < 10) continue;

                    String username = col[0].trim();
                    String eventId = col[1].trim();
                    String eventName = col[2].trim();
                    String categoryStr = col[3].trim();
                    String dateTimeStr = col[4].trim();
                    String address = col[5].trim();
                    String latStr = col[6].trim();
                    String lonStr = col[7].trim();
                    String imageUrl = col[8].trim();
                    String description = col[9].trim();

                    // Parse location
                    double latitude = latStr.isEmpty() ? 0 : Double.parseDouble(latStr);
                    double longitude = lonStr.isEmpty() ? 0 : Double.parseDouble(lonStr);
                    Location location = new Location(address, latitude, longitude);

                    // Parse category using the fromString method
                    EventCategory category = EventCategory.fromString(categoryStr);

                    // Parse date/time
                    LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DATE_FORMATTER);

                    // Create event - matching Event constructor:
                    // Event(id, name, description, address, category, location, startTime, imageUrl)
                    Event event = new Event(eventId, eventName, description, address, category, location, dateTime, imageUrl);

                    // Add to user's list
                    savedEventsByUser.computeIfAbsent(username, k -> new ArrayList<>()).add(event);

                } catch (Exception e) {
                    System.err.println("Error parsing saved event row: " + e.getMessage());
                }
            }

            System.out.println("Loaded saved events for " + savedEventsByUser.size() + " users");

        } catch (IOException ex) {
            System.err.println("Error reading saved events file: " + ex.getMessage());
        }
    }

    /**
     * Parse a CSV line handling quoted strings with commas.
     */
    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());

        return result.toArray(new String[0]);
    }

    /**
     * Save all events to file.
     */
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write(HEADER);
            writer.newLine();

            for (Map.Entry<String, List<Event>> entry : savedEventsByUser.entrySet()) {
                String username = entry.getKey();
                for (Event event : entry.getValue()) {
                    StringBuilder line = new StringBuilder();
                    line.append(username).append(",");
                    line.append(event.getId()).append(",");
                    line.append("\"").append(escapeCSV(event.getName())).append("\",");
                    line.append(event.getCategory().name()).append(",");
                    line.append(event.getStartTime().format(DATE_FORMATTER)).append(",");
                    line.append("\"").append(escapeCSV(event.getLocation().getAddress())).append("\",");
                    line.append(event.getLocation().getLatitude()).append(",");
                    line.append(event.getLocation().getLongitude()).append(",");
                    line.append("\"").append(escapeCSV(event.getImageUrl() != null ? event.getImageUrl() : "")).append("\",");
                    line.append("\"").append(escapeCSV(event.getDescription() != null ? event.getDescription() : "")).append("\"");

                    writer.write(line.toString());
                    writer.newLine();
                }
            }

            System.out.println("Saved events to file");

        } catch (IOException ex) {
            System.err.println("Error saving events file: " + ex.getMessage());
        }
    }

    /**
     * Escape special characters for CSV.
     */
    private String escapeCSV(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"").replace("\n", " ").replace("\r", "");
    }

    @Override
    public void saveEvent(String username, Event event) {
        if (username == null || event == null) return;

        List<Event> userEvents = savedEventsByUser.computeIfAbsent(username, k -> new ArrayList<>());

        // Don't add duplicates
        boolean alreadySaved = userEvents.stream()
                .anyMatch(e -> e.getId().equals(event.getId()));

        if (!alreadySaved) {
            userEvents.add(event);
            saveToFile();  // Persist to file
            System.out.println("Saved event: " + event.getName() + " for user: " + username);
        } else {
            System.out.println("Event already saved: " + event.getName());
        }
    }

    @Override
    public List<Event> getSavedEvents(String username) {
        if (username == null) return new ArrayList<>();
        return new ArrayList<>(savedEventsByUser.getOrDefault(username, new ArrayList<>()));
    }

    @Override
    public void unsaveEvent(String username, Event event) {
        if (username == null || event == null) return;

        List<Event> userEvents = savedEventsByUser.get(username);
        if (userEvents != null) {
            userEvents.removeIf(e -> e.getId().equals(event.getId()));
            saveToFile();  // Persist to file
            System.out.println("Removed event: " + event.getName() + " for user: " + username);
        }
    }

    @Override
    public boolean isEventSaved(String username, String eventId) {
        if (username == null || eventId == null) return false;

        List<Event> userEvents = savedEventsByUser.get(username);
        if (userEvents == null) return false;

        return userEvents.stream().anyMatch(e -> e.getId().equals(eventId));
    }
}