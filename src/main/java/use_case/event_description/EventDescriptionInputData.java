package use_case.event_description;

public class EventDescriptionInputData {

    private final String eventId;
    private final double userLatitude;
    private final double userLongitude;

    public EventDescriptionInputData(String eventId,
                                     double userLatitude,
                                     double userLongitude) {
        this.eventId = eventId;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
    }

    public String getEventId() {
        return eventId;
    }

    public double getUserLatitude() {
        return userLatitude;
    }

    public double getUserLongitude() {
        return userLongitude;
    }
}
