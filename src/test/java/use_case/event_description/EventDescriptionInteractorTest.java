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
}
