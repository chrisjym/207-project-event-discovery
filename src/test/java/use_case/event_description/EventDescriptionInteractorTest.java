package use_case.event_description;

import data_access.InMemoryEventDataAccessObject;
import entity.Event;
import entity.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventDescriptionInteractorTest {

    private static class TestPresenter implements EventDescriptionOutputBoundary {
        EventDescriptionOutputData lastOutput;
        String lastError;

        @Override
        public void prepareSuccessView(EventDescriptionOutputData outputData) {
            lastOutput = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            lastError = errorMessage;
        }
    }

    @Test
    void successPath_eventFoundAndDistanceComputed() {
        // arrange
        InMemoryEventDataAccessObject eventDAO = new InMemoryEventDataAccessObject();
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

        TestPresenter presenter = new TestPresenter();
        DistanceCalculator distanceCalculator = new HaversineDistanceCalculator();

        EventDescriptionInputBoundary interactor =
                new EventDescriptionInteractor(eventDAO, presenter, distanceCalculator);

        EventDescriptionInputData input =
                new EventDescriptionInputData("1", 43.1, -79.0);

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);

        assertEquals("Music Festival", presenter.lastOutput.getName());
        assertEquals("123 Queen St", presenter.lastOutput.getAddress());
        assertTrue(presenter.lastOutput.getDistanceKm() > 0);
    }

    @Test
    void eventNotFound_callsFailure() {
        InMemoryEventDataAccessObject eventDAO = new InMemoryEventDataAccessObject();
        TestPresenter presenter = new TestPresenter();
        DistanceCalculator distanceCalculator = new HaversineDistanceCalculator();

        EventDescriptionInputBoundary interactor =
                new EventDescriptionInteractor(eventDAO, presenter, distanceCalculator);

        EventDescriptionInputData input =
                new EventDescriptionInputData("999", 43.0, -79.0);

        interactor.execute(input);

        assertNull(presenter.lastOutput);
        assertEquals("Event not found.", presenter.lastError);
    }

    // ============ Additional tests for 100% coverage ============

    @Test
    void testEventDescriptionInputData_allGetters() {
        // Test all getters in EventDescriptionInputData
        EventDescriptionInputData inputData =
                new EventDescriptionInputData("event-123", 45.5, -75.5);

        assertEquals("event-123", inputData.getEventId());
        assertEquals(45.5, inputData.getUserLatitude(), 0.001);
        assertEquals(-75.5, inputData.getUserLongitude(), 0.001);
    }

    @Test
    void testEventDescriptionOutputData_allGetters() {
        // Test all getters in EventDescriptionOutputData
        EventDescriptionOutputData outputData = new EventDescriptionOutputData(
                "Concert Name",
                "A great concert",
                "456 Main St",
                "MUSIC",
                "2025-12-25 20:00",
                15.5
        );

        assertEquals("Concert Name", outputData.getName());
        assertEquals("A great concert", outputData.getDescription());
        assertEquals("456 Main St", outputData.getAddress());
        assertEquals("MUSIC", outputData.getCategory());
        assertEquals("2025-12-25 20:00", outputData.getDateTime());
        assertEquals(15.5, outputData.getDistanceKm(), 0.001);
    }

    @Test
    void testHaversineDistanceCalculator_sameLocation() {
        // Test distance calculator with same coordinates (should be 0)
        HaversineDistanceCalculator calculator = new HaversineDistanceCalculator();

        double distance = calculator.distanceKm(43.0, -79.0, 43.0, -79.0);

        assertEquals(0.0, distance, 0.001);
    }

    @Test
    void testHaversineDistanceCalculator_knownDistance() {
        // Test distance calculator with known coordinates
        // Toronto to Montreal is approximately 500 km
        HaversineDistanceCalculator calculator = new HaversineDistanceCalculator();

        // Toronto: 43.6532, -79.3832
        // Montreal: 45.5017, -73.5673
        double distance = calculator.distanceKm(43.6532, -79.3832, 45.5017, -73.5673);

        // Should be approximately 500-550 km
        assertTrue(distance > 400 && distance < 600,
                "Distance between Toronto and Montreal should be ~500km, was: " + distance);
    }

    @Test
    void testHaversineDistanceCalculator_antipodal() {
        // Test with very far apart locations
        HaversineDistanceCalculator calculator = new HaversineDistanceCalculator();

        // North pole to South pole (approximately 20,000 km)
        double distance = calculator.distanceKm(90.0, 0.0, -90.0, 0.0);

        assertTrue(distance > 19000 && distance < 21000,
                "Pole to pole distance should be ~20,000km, was: " + distance);
    }

    @Test
    void successPath_verifyAllOutputFields() {
        // Test that all output fields are correctly populated
        InMemoryEventDataAccessObject eventDAO = new InMemoryEventDataAccessObject();
        Location loc = new Location("789 Test Ave, Toronto", 43.65, -79.38);
        Event event = new Event(
                "test-id",
                "Test Event Name",
                "Test Description Here",
                "789 Test Ave, Toronto",
                loc,
                "Sports",
                "2025-06-15T14:30"
        );
        eventDAO.save(event);

        TestPresenter presenter = new TestPresenter();
        DistanceCalculator distanceCalculator = new HaversineDistanceCalculator();

        EventDescriptionInteractor interactor =
                new EventDescriptionInteractor(eventDAO, presenter, distanceCalculator);

        EventDescriptionInputData input =
                new EventDescriptionInputData("test-id", 43.65, -79.38);

        interactor.execute(input);

        assertNotNull(presenter.lastOutput);
        assertEquals("Test Event Name", presenter.lastOutput.getName());
        assertEquals("Test Description Here", presenter.lastOutput.getDescription());
        assertEquals("789 Test Ave, Toronto", presenter.lastOutput.getAddress());
        assertEquals("Sports", presenter.lastOutput.getCategory());
        assertEquals("2025-06-15 14:30", presenter.lastOutput.getDateTime());
        // Distance should be 0 or very small since user is at event location
        assertTrue(presenter.lastOutput.getDistanceKm() < 1.0);
    }
}