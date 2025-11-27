package interface_adapter.display_local_events;

import interface_adapter.ViewModel;
import java.time.LocalDate;
import java.util.List;


public class DisplayLocalEventsViewModel extends ViewModel<DisplayLocalEventsState> {

    public static final String VIEW_NAME = "display local events";


    public static final String PROPERTY_STATE = "state";
    public static final String PROPERTY_EVENT_CARDS = "eventCards";
    public static final String PROPERTY_SEARCH_PARAMS = "searchParamsUpdated";
    public static final String PROPERTY_CATEGORY = "categoryChanged";
    public static final String PROPERTY_LOCATION = "locationChanged";
    public static final String PROPERTY_ERROR = "error";
    public static final String PROPERTY_LOADING = "loading";
    public static final String PROPERTY_MESSAGE = "message";

    private LocalDate selectedDateFromCalendar;

    public DisplayLocalEventsViewModel() {
        super(VIEW_NAME);
        this.setState(new DisplayLocalEventsState());
    }

    public static class EventCard {
        private final String id;
        private final String name;
        private final String dateTime;
        private final String address;
        private final String category;
        private final String distanceText;
        private final String imageUrl;

        public EventCard(String id, String name, String dateTime, String address,
                         String category, String distanceText, String imageUrl) {
            this.id = id;
            this.name = name;
            this.dateTime = dateTime;
            this.address = address;
            this.category = category;
            this.distanceText = distanceText;
            this.imageUrl = imageUrl;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDateTime() { return dateTime; }
        public String getAddress() { return address; }
        public String getCategory() { return category; }
        public String getDistanceText() { return distanceText; }
        public String getImageUrl() { return imageUrl; }
    }

    public List<EventCard> getEventCards() {
        return this.getState().getEventCards();
    }

    public void setEventCards(List<EventCard> eventCards) {
        this.getState().setEventCards(eventCards);
        this.firePropertyChange(PROPERTY_EVENT_CARDS);
        this.firePropertyChange(PROPERTY_STATE);
    }

    public String getMessage() {
        return this.getState().getMessage();
    }

    public void setMessage(String message) {
        this.getState().setMessage(message);
        this.firePropertyChange(PROPERTY_MESSAGE);
        this.firePropertyChange(PROPERTY_STATE);
    }

    public String getError() {
        return this.getState().getError();
    }

    public void setError(String error) {
        this.getState().setError(error);
        this.firePropertyChange(PROPERTY_ERROR);
        this.firePropertyChange(PROPERTY_STATE);
    }

    public boolean hasEvents() {
        return this.getState().hasEvents();
    }

    public boolean hasError() {
        return this.getState().hasError();
    }


    public boolean isLoading() {
        return this.getState().isLoading();
    }

    public void setLoading(boolean loading) {
        this.getState().setLoading(loading);
        this.firePropertyChange(PROPERTY_LOADING);
    }

    public void updateSearchParams(String location, String category, double radius) {
        String oldLocation = this.getState().getLastSearchLocation();
        String oldCategory = this.getState().getLastSearchCategory();

        this.getState().setLastSearchLocation(location);
        this.getState().setLastSearchCategory(category);
        this.getState().setLastSearchRadius(radius);

        if (!oldLocation.equals(location)) {
            this.firePropertyChange(PROPERTY_LOCATION);
        }
        if (!oldCategory.equals(category)) {
            this.firePropertyChange(PROPERTY_CATEGORY);
        }

        this.firePropertyChange(PROPERTY_SEARCH_PARAMS);
    }

    public String getLastSearchLocation() {
        return this.getState().getLastSearchLocation();
    }

    public String getLastSearchCategory() {
        return this.getState().getLastSearchCategory();
    }

    public double getLastSearchRadius() {
        return this.getState().getLastSearchRadius();
    }

    public LocalDate getSelectedDateFromCalendar() {
        return selectedDateFromCalendar;
    }

    public void setSelectedDateFromCalendar(LocalDate date) {
        this.selectedDateFromCalendar = date;
        this.firePropertyChange("selectedDate");
    }

    public void clearEvents() {
        this.getState().setEventCards(List.of());
        this.getState().setError("");
        this.getState().setMessage("");
        this.firePropertyChange(PROPERTY_STATE);
    }

    public void refresh() {
        this.firePropertyChange(PROPERTY_STATE);
    }
}