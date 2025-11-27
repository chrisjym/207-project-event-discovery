package interface_adapter.calendarFlow;

import entity.Event;
import interface_adapter.ViewModel;
import java.time.LocalDate;
import java.util.List;

public class CalendarFlowViewModel extends ViewModel<CalendarFlowState> {
    public CalendarFlowViewModel() {
        super("event list by date");
        setState(new CalendarFlowState());
    }
}
