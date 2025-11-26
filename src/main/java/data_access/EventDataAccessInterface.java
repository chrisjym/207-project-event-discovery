package data_access;

import entity.Event;

//TODO: DELETE IF NOT USING, OLD CODE
//public interface EventDataAccessInterface {
//    /**
//     * Return Event with this id, or null if it does not exist
//     **/
//    Event getEventById(String id);
//}

public interface EventDataAccessInterface {
    void save(Event event);
    Event get(String id);
    boolean existsById(String id);
}

