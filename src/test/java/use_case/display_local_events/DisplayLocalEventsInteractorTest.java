package use_case.display_local_events;

import entity.Event;
import entity.EventCategory;
import entity.EventRepository;
import entity.Location;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * A self-contained test class that does NOT require JUnit.
 */
public class DisplayLocalEventsInteractorTest {

    /** In-memory fake repository */
    static class InMemoryEventRepository implements EventRepository {
        private final List<Event> events;

        InMemoryEventRepository(List<Event> events) {
            this.events = events;
        }

        @Override
        public List<Event> findAllEvents() {
            return events;
        }

        @Override
        public Optional<Event> findById(String id) {
            return events.stream().filter(e -> e.getId().equals(id)).findFirst();
        }

        @Override
        public List<Event> findByName(String query) {
            return events.stream()
                    .filter(e -> e.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
        }
    }

    /** Fake presenter to capture output */
    static class TestOutputBoundary implements DisplayLocalEventsOutputBoundary {
        DisplayLocalEventsOutputData successData;
        String errorMessage;
        boolean successCalled = false;
        boolean errorCalled = false;

        @Override
        public void presentSuccess(DisplayLocalEventsOutputData outputData) {
            this.successCalled = true;
            this.successData = outputData;
        }

        @Override
        public void presentError(String errorMessage) {
            this.errorCalled = true;
            this.errorMessage = errorMessage;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Running DisplayLocalEventsInteractorSelfTest ===");

        testInvalidInput();
        testFilteringAndSorting();

        System.out.println("=== All tests completed ===");
    }

    private static void testInvalidInput() {
        System.out.println("Test 1: invalid input");

        DisplayLocalEventsInputData input =
                new DisplayLocalEventsInputData(null, -10, null);

        InMemoryEventRepository repo = new InMemoryEventRepository(List.of());
        TestOutputBoundary presenter = new TestOutputBoundary();
        DisplayLocalEventsInteractor interactor =
                new DisplayLocalEventsInteractor(repo, presenter);

        interactor.execute(input);

        if (presenter.errorCalled && !presenter.successCalled) {
            System.out.println("PASS: Error reported successfully.");
        } else {
            System.out.println("FAIL: Expected error but got success.");
        }
    }

    private static void testFilteringAndSorting() {
        System.out.println("Test 2: filtering by distance + category");

        Location userLoc = new Location("User", 43.6532, -79.3832);

        Location near = new Location("Near", 43.6533, -79.3833);
        Location far = new Location("Far", 40.0, -74.0);

        Event nearMusic = new Event(
                "1", "Near Music", "desc",
                EventCategory.MUSIC,
                near,
                LocalDateTime.now(),
                ""
        );

        Event farSports = new Event(
                "2", "Far Sports", "desc",
                EventCategory.SPORTS,
                far,
                LocalDateTime.now(),
                ""
        );

        InMemoryEventRepository repo =
                new InMemoryEventRepository(List.of(nearMusic, farSports));

        TestOutputBoundary presenter = new TestOutputBoundary();
        DisplayLocalEventsInteractor interactor =
                new DisplayLocalEventsInteractor(repo, presenter);

        DisplayLocalEventsInputData input =
                new DisplayLocalEventsInputData(userLoc, 5.0, EventCategory.MUSIC);

        interactor.execute(input);

        if (!presenter.successCalled) {
            System.out.println("FAIL: Expected success.");
            return;
        }

        DisplayLocalEventsOutputData out = presenter.successData;

        if (out.getTotalEvents() == 1 && out.getEvents().get(0).getId().equals("1")) {
            System.out.println("PASS: Correct event filtered.");
        } else {
            System.out.println("FAIL: Incorrect filtering result.");
        }
    }
}