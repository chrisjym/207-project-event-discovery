package interface_adapter.event_description;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EventDescriptionViewModel {
    public static final String VIEW_NAME = "event description";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private String name = "";
    private String description = "";
    private String address = "";
    private String category = "";
    private String dateTime = "";
    private String distanceText = "";
    private String errorMessage = "";

    public String getViewName() {
        return VIEW_NAME;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getAddress() { return address; }
    public String getCategory() { return category; }
    public String getDateTime() { return dateTime; }
    public String getDistanceText() { return distanceText; }
    public String getErrorMessage() { return errorMessage; }

    public void setEventDetails(String name, String description, String address,
                                String category, String dateTime, String distanceText) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.category = category;
        this.dateTime = dateTime;
        this.distanceText = distanceText;
        this.errorMessage = "";
        firePropertyChange();
    }

    public void setError(String errorMessage) {
        this.errorMessage = errorMessage;
        firePropertyChange();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    private void firePropertyChange() {
        support.firePropertyChange("eventDescription", null, null);
    }
}
