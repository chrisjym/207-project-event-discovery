package entity;

import java.util.List;
import java.util.Optional;

public interface EventRepository {
    List<Event> findAllEvents();
    Optional<Event> findById(String id);
    List<Event> findByName(String query);
}
