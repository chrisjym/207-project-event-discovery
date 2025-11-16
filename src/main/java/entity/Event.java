package entity;

public class Event {
    private final String id;
    private final String name;
    private final String description;
    private final String address;
    private final double latitude;
    private final double longitude;
    private final String category;
    private final String dateTime; // or LocalDateTime

    public Event(String id, String name, String description,
                 String address, double latitude, double longitude,
                 String category, String dateTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }
    public String getAddress() {
        return address;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getCategory() {
        return category;
    }
    public String getDateTime() {
        return dateTime;
    }
}
