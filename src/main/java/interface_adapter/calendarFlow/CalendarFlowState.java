package interface_adapter.calendarFlow;

import entity.Event;
import java.util.List;
import java.time.LocalDate;

public class CalendarFlowState {
    private List<Event> eventList = null;
    private LocalDate date = null;
    private String errorMessage = null;

    public CalendarFlowState() {
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Event> getEventList() {
        return eventList;
    }
    public LocalDate getDate() {
        return date;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
