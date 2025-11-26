package data_access;

import entity.Event;
import entity.Location;

import java.util.HashMap;
import java.util.Map;

public class InMemoryEventDataAccessObject implements EventDataAccessInterface {

    private final Map<String, Event> events = new HashMap<>();

    public InMemoryEventDataAccessObject() {
        // --- FAKE TESTING DATA ---
        events.put("1", new Event(
                "1",
                "Campus Concert",
                "Live music at UofT.",
                "123 College St",
                new Location(43.6629, -79.3957),
                "Music",
                "2025-11-20 19:00"
        ));

        events.put("2", new Event(
                "2",
                "Art Fair",
                "An outdoor art fair with local artists.",
                "456 King St",
                new Location(43.6532, -79.3832),
                "Art",
                "2025-12-05 10:00"
        ));
    }

    // TODO: DELETE IF NOT USING, OLD CODE
//    @Override
//    public Event getEventById(String id) {
//        return events.get(id);
//    }

    @Override
    public void save(Event event) {
        events.put(event.getId(), event);
    }

    @Override
    public Event get(String id) {
        return events.get(id);
    }

    @Override
    public boolean existsById(String id) {
        return events.containsKey(id);
    }
}
