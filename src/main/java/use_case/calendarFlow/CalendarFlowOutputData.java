package use_case.calendarFlow;

import java.time.LocalDate;
import java.util.List;

import entity.Event;

/**
 * Output Data for the Calendar Flow Use Case.
 * Contains the selected date and the events found for that date.
 */
public class CalendarFlowOutputData {
    private final LocalDate selectedDate;
    private final List<Event> events;
    private final boolean hasEvents;

    public CalendarFlowOutputData(LocalDate selectedDate, List<Event> events) {
        this.selectedDate = selectedDate;
        this.events = events;
        this.hasEvents = events != null && !events.isEmpty();
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public List<Event> getEvents() {
        return events;
    }

    public boolean getHasEvents() {
        return hasEvents;
    }

}
