package interface_adapter.calendarFlow;

import interface_adapter.ViewModel;

public class CalendarFlowViewModel extends ViewModel<CalendarFlowState> {
    public CalendarFlowViewModel() {
        super("event list by date");
        setState(new CalendarFlowState());
    }
}
