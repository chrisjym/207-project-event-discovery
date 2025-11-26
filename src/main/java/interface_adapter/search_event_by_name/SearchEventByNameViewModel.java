package interface_adapter.search_event_by_name;

import entity.Event;
import interface_adapter.ViewModel;

public class SearchEventByNameViewModel extends ViewModel<SearchEventByNameState> {

    public SearchEventByNameViewModel() {
        super("event search");
        setState(new SearchEventByNameState());
    }

    public Event getEvent() {
        return getState().getEvent();
    }

}
