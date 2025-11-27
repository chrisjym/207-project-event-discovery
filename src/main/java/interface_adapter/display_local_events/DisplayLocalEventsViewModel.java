package interface_adapter.display_local_events;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the Display Local Events use case.
 * Stores data formatted specifically for UI display.
 * Contains no business logic and does not depend on use case or entity logic.
 */
public class DisplayLocalEventsViewModel {

    public DisplayLocalEventsViewModel()  {

    }

    /**
     * Represents a single event card to be displayed in the UI.
     * This class contains UI-friendly data only (mostly Strings),
     * instead of raw domain objects like LocalDateTime or Location.
     */
    public static class EventCard {
        private final String id;
        private final String name;
        private final String dateTime;
        private final String address;
        private final String category;
        private final String distanceText;
        private final String imageUrl;

        public EventCard(String id,
                         String name,
                         String dateTime,
                         String address,
                         String category,
                         String distanceText,
                         String imageUrl) {
            this.id = id;
            this.name = name;
            this.dateTime = dateTime;
            this.address = address;
            this.category = category;
            this.distanceText = distanceText;
            this.imageUrl = imageUrl;
        }


        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDateTime() {
            return dateTime;
        }

        public String getAddress() {
            return address;
        }

        public String getCategory() {
            return category;
        }

        public String getDistanceText() {
            return distanceText;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    private List<EventCard> eventCards = new ArrayList<>();

    private String message = "";

    private String error = "";


    public List<EventCard> getEventCards() {
        return eventCards;
    }

    public void setEventCards(List<EventCard> eventCards) {
        this.eventCards = eventCards != null ? eventCards : new ArrayList<>();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message != null ? message : "";
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error != null ? error : "";
    }


    /** Returns true if there are events to display. */
    public boolean hasEvents() {
        return !eventCards.isEmpty();
    }

    /** Returns true if there is an error message. */
    public boolean hasError() {
        return !error.isEmpty();
    }
}

