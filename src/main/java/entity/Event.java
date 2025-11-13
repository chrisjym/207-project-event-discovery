package entity;

import java.time.LocalDateTime;

public class Event {
    private final String id;
    private final String name;
    private final String description;
    private final EventCategory category;
    private final Location location;
    private final LocalDateTime EventsTime;
    private final String imageUrl;

    public Event(String id, String name, String description, EventCategory category,
                 Location location, LocalDateTime startTime, String imageUrl) {

        // Validate and set ID
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty");
        }
        this.id = id.trim();

        // Validate and set name
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be null or empty");
        }
        this.name = name.trim();

        // Set description (handle null)
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }

        // Set category (handle null)
        if (category == null) {
            this.category = EventCategory.MISCELLANEOUS;
        } else {
            this.category = category;
        }

        // Set location (must not be null)
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        this.location = location;

        if ( imageUrl == null || imageUrl.trim().isEmpty()) {
            this.imageUrl = "";
        } else {
            this.imageUrl = imageUrl.trim();
        }

        // Set start time (must not be null)
        if (startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        this.EventsTime = startTime;
    }

    public String getImageUrl() {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return "https://via.placeholder.com/300x200/CCCCCC/969696?text=No+Event+Image";
        }
        return imageUrl;
    }


    // Core business method: Check if event is within specified radius
    public boolean isWithinRadius(Location userLocation, double radiusKm) {
        if (userLocation == null || radiusKm < 0) {
            return false;
        }
        double distance = this.location.calculateDistance(userLocation);
        return distance <= radiusKm;
    }

    // Core business method: Calculate distance to user location
    public double calculateDistanceTo(Location userLocation) {
        if (userLocation == null) {
            return Double.MAX_VALUE;
        }
        return this.location.calculateDistance(userLocation);
    }

    // Core business method: Filter by category
    public boolean isInCategory(EventCategory targetCategory) {
        return this.category == targetCategory;
    }

    // Getter methods
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public EventCategory getCategory() { return category; }
    public Location getLocation() { return location; }
    public LocalDateTime getStartTime() { return EventsTime; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Event{id='%s', name='%s', category=%s, image=%s}",
                id, name, category, getImageUrl());
    }
}
