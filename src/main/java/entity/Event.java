package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Event {

    // Core fields
    private final String id;
    private final String name;
    private final String description;
    private final String address;
    private final EventCategory category;
    private final Location location;
    private final LocalDateTime startTime;
    private final String imageUrl;

    /**
     * Main constructor with strong typing.
     */
    public Event(String id,
                 String name,
                 String description,
                 String address,
                 EventCategory category,
                 Location location,
                 LocalDateTime startTime,
                 String imageUrl) {


        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be null or empty");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Event address cannot be null or empty");
        }
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }

        this.id = id.trim();
        this.name = name.trim();
        this.address = address.trim();
        this.description = (description == null) ? "" : description;

        this.category = (category == null) ? EventCategory.MISCELLANEOUS : category;
        this.location = location;
        this.startTime = startTime;

        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            this.imageUrl = "";
        } else {
            this.imageUrl = imageUrl.trim();
        }
    }

    /**
     * Convenience constructor matching your ORIGINAL signature:
     * (id, name, description, address, location, categoryString, dateTimeString).
     *
     * This lets your existing tests and code continue to compile.
     */
    public Event(String id,
                 String name,
                 String description,
                 String address,
                 Location location,
                 String category,
                 String dateTime) {

        this(
                id,
                name,
                description,
                address,
                parseCategory(category),
                location,
                parseDateTime(dateTime),
                ""  // no image URL provided in the old constructor
        );
    }

    // ---- Helper parsers for the convenience constructor ----

    private static EventCategory parseCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return EventCategory.MISCELLANEOUS;
        }
        try {
            // assumes enum constants like MUSIC, SPORTS, etc.
            return EventCategory.valueOf(category.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            // if the string doesn't match any enum constant, fall back
            return EventCategory.MISCELLANEOUS;
        }
    }

    private static LocalDateTime parseDateTime(String dateTime) {
        if (dateTime == null || dateTime.trim().isEmpty()) {
            return LocalDateTime.now();
        }
        try {
            // works with ISO-8601 strings like "2025-11-20T19:00"
            return LocalDateTime.parse(dateTime.trim());
        } catch (DateTimeParseException ex) {
            // fallback if parse fails
            return LocalDateTime.now();
        }
    }

    // ---- Getter methods ----

    public String getId()          { return id; }
    public String getName()        { return name; }
    public String getDescription() { return description; }
    public String getAddress()     { return address; }
    public EventCategory getCategory() { return category; }
    public Location getLocation()  { return location; }
    public LocalDateTime getStartTime() { return startTime; }

    public String getImageUrl() {
        if (imageUrl == null || imageUrl.isEmpty()) {
            // default placeholder image
            return "https://via.placeholder.com/300x200/CCCCCC/969696?text=No+Event+Image";
        }
        return imageUrl;
    }

    // Check if event is within specified radius of a user location
    public boolean isWithinRadius(Location userLocation, double radiusKm) {
        if (userLocation == null || radiusKm < 0) {
            return false;
        }
        double distance = this.location.calculateDistance(userLocation);
        return distance <= radiusKm;
    }

    // Distance from event to a user location
    public double calculateDistanceTo(Location userLocation) {
        if (userLocation == null) {
            return Double.MAX_VALUE;
        }
        return this.location.calculateDistance(userLocation);
    }

    // Filter by category
    public boolean isInCategory(EventCategory targetCategory) {
        return this.category == targetCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
                "Event{id='%s', name='%s', category=%s, image=%s}",
                id, name, category, getImageUrl()
        );
    }
}
