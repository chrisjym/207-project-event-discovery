package use_case.event_description;

public class EventDescriptionOutputData {

    private final String name;
    private final String description;
    private final String address;
    private final String category;
    private final String dateTime;
    private final double distanceKm;

    public EventDescriptionOutputData(String name,
                                      String description,
                                      String address,
                                      String category,
                                      String dateTime,
                                      double distanceKm) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.category = category;
        this.dateTime = dateTime;
        this.distanceKm = distanceKm;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getCategory() {
        return category;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getDistanceKm() {
        return distanceKm;
    }
}
