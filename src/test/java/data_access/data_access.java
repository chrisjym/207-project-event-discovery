package data_access;

import entity.Event;
import entity.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryEventDataAccessObjectTest {

    @Test
    void saveAndGetEvent() {
        EventDataAccessInterface eventDAO = new InMemoryEventDataAccessObject();

        Location loc = new Location(43.0, -79.0);
        Event event = new Event(
                "1",
                "Music Festival",
                "Outdoor live music event",
                "123 Queen St",
                loc,
                "Music",
                "2025-11-20T19:00"
        );

        eventDAO.save(event);
        Event retrieved = eventDAO.get("1");

        assertTrue(eventDAO.existsById("1"));
        assertNotNull(retrieved);
        assertEquals("Music Festival", retrieved.getName());
        assertEquals("123 Queen St", retrieved.getAddress());
        assertEquals(43.0, retrieved.getLocation().getLatitude(), 1e-6);
        assertEquals(-79.0, retrieved.getLocation().getLongitude(), 1e-6);
    }

    @Test
    void eventDoesNotExist() {
        EventDataAccessInterface eventDAO = new InMemoryEventDataAccessObject();

        boolean exists = eventDAO.existsById("999");
        Event retrieved = eventDAO.get("999");

        assertFalse(exists);
        assertNull(retrieved);
    }
}
