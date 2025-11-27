package interface_adapter.display_local_events;

import java.util.ArrayList;
import java.util.List;

/**
 * 简化的 State 类 - 只存储必要的数据
 */
public class DisplayLocalEventsState {
    private List<DisplayLocalEventsViewModel.EventCard> eventCards = new ArrayList<>();
    private String message = "";
    private String error = "";

    // 搜索参数
    private String lastSearchLocation = "";
    private String lastSearchCategory = "";
    private double lastSearchRadius = 0.0;

    // Getters and Setters
    public List<DisplayLocalEventsViewModel.EventCard> getEventCards() {
        return eventCards;
    }

    public void setEventCards(List<DisplayLocalEventsViewModel.EventCard> cards) {
        this.eventCards = (cards != null ? cards : new ArrayList<>());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = (msg != null ? msg : "");
    }

    public String getError() {
        return error;
    }

    public void setError(String err) {
        this.error = (err != null ? err : "");
    }

    public boolean hasEvents() {
        return !eventCards.isEmpty();
    }

    public boolean hasError() {
        return !error.isEmpty();
    }

    public String getLastSearchLocation() {
        return lastSearchLocation;
    }

    public void setLastSearchLocation(String location) {
        this.lastSearchLocation = (location != null ? location : "");
    }

    public String getLastSearchCategory() {
        return lastSearchCategory;
    }

    public void setLastSearchCategory(String category) {
        this.lastSearchCategory = (category != null ? category : "");
    }

    public double getLastSearchRadius() {
        return lastSearchRadius;
    }

    public void setLastSearchRadius(double radius) {
        this.lastSearchRadius = radius;
    }
}