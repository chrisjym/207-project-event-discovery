package use_case.save_event;

import data_access.FileSavedEventsDataAccessObject;
import entity.Event;
import entity.EventCategory;
import entity.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.login.LoginUserDataAccessInterface;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for SaveEventInteractor
 * Tests the business logic for saving events
 */
class SaveEventInteractorTest {

    private SaveEventInteractor interactor;
    private MockSaveEventPresenter mockPresenter;
    private MockFileSavedEventsDAO mockSavedEventsDAO;
    private MockLoginUserDataAccess mockUserDataAccess;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        mockPresenter = new MockSaveEventPresenter();
        mockSavedEventsDAO = new MockFileSavedEventsDAO();
        mockUserDataAccess = new MockLoginUserDataAccess();

        interactor = new SaveEventInteractor(
                mockPresenter,
                mockSavedEventsDAO,
                mockUserDataAccess
        );

        Location location = new Location("Scotiabank Arena, Toronto, ON", 43.6435, -79.3791);
        testEvent = new Event(
                "evt123",
                "Toronto Raptors vs Lakers",
                "Exciting NBA game",
                "Scotiabank Arena",
                EventCategory.SPORTS,
                location,
                LocalDateTime.of(2025, 11, 28, 19, 30),
                "https://example.com/image.jpg"
        );
    }

    @Test
    void testExecute_SuccessfulSave() {
        mockUserDataAccess.setCurrentUsername("testUser");
        SaveEventInputData inputData = new SaveEventInputData(testEvent);

        interactor.execute(inputData);

        assertTrue(mockPresenter.isSuccessViewPrepared(), "Success view should be prepared");
        assertFalse(mockPresenter.isFailureViewPrepared(), "Failure view should not be prepared");
        assertEquals(testEvent, mockPresenter.getOutputData().getEvent(), "Output event should match input");
        assertTrue(mockSavedEventsDAO.wasSaveCalled(), "Save should be called on DAO");
        assertEquals("testUser", mockSavedEventsDAO.getLastSavedUsername(), "Username should match");
        assertEquals(testEvent, mockSavedEventsDAO.getLastSavedEvent(), "Saved event should match");
    }

    @Test
    void testExecute_NullEvent() {
        SaveEventInputData inputData = new SaveEventInputData(null);

        interactor.execute(inputData);

        assertTrue(mockPresenter.isFailureViewPrepared(), "Failure view should be prepared");
        assertFalse(mockPresenter.isSuccessViewPrepared(), "Success view should not be prepared");
        assertEquals("No Event Found", mockPresenter.getErrorMessage());
        assertFalse(mockSavedEventsDAO.wasSaveCalled(), "Save should not be called for null event");
    }

    @Test
    void testExecute_EventAlreadySaved() {
        mockUserDataAccess.setCurrentUsername("testUser");
        mockSavedEventsDAO.addSavedEvent("testUser", testEvent); // Pre-save the event
        SaveEventInputData inputData = new SaveEventInputData(testEvent);

        interactor.execute(inputData);

        assertTrue(mockPresenter.isFailureViewPrepared(), "Failure view should be prepared");
        assertFalse(mockPresenter.isSuccessViewPrepared(), "Success view should not be prepared");
        assertEquals("Event already saved", mockPresenter.getErrorMessage());
    }

    @Test
    void testGetSavedEvents_WithValidUser() {
        mockUserDataAccess.setCurrentUsername("testUser");
        Event event1 = createTestEvent("evt1", "Event 1");
        Event event2 = createTestEvent("evt2", "Event 2");
        mockSavedEventsDAO.addSavedEvent("testUser", event1);
        mockSavedEventsDAO.addSavedEvent("testUser", event2);

        List<Event> savedEvents = interactor.getSavedEvents();

        assertNotNull(savedEvents);
        assertEquals(2, savedEvents.size());
        assertTrue(savedEvents.contains(event1));
        assertTrue(savedEvents.contains(event2));
    }

    @Test
    void testGetSavedEvents_NoCurrentUser() {
        // Arrange
        mockUserDataAccess.setCurrentUsername(null);

        // Act
        List<Event> savedEvents = interactor.getSavedEvents();

        // Assert
        assertNotNull(savedEvents);
        assertTrue(savedEvents.isEmpty(), "Should return empty list when no current user");
    }

    @Test
    void testGetSavedEvents_UserHasNoSavedEvents() {
        mockUserDataAccess.setCurrentUsername("userWithNoEvents");

        List<Event> savedEvents = interactor.getSavedEvents();

        assertNotNull(savedEvents);
        assertTrue(savedEvents.isEmpty(), "Should return empty list for user with no saved events");
    }

    @Test
    void testRemoveEvent_Success() {
        mockUserDataAccess.setCurrentUsername("testUser");
        mockSavedEventsDAO.addSavedEvent("testUser", testEvent);

        interactor.removeEvent(testEvent);

        assertTrue(mockSavedEventsDAO.wasRemoveCalled(), "Remove should be called on DAO");
        assertEquals("testUser", mockSavedEventsDAO.getLastRemovedUsername());
        assertEquals(testEvent, mockSavedEventsDAO.getLastRemovedEvent());
    }

    @Test
    void testRemoveEvent_NoCurrentUser() {
        mockUserDataAccess.setCurrentUsername(null);

        interactor.removeEvent(testEvent);

        assertFalse(mockSavedEventsDAO.wasRemoveCalled(), "Remove should not be called when no user");
    }

    @Test
    void testSwitchToDashboardView() {
        interactor.switchToDashboardView();

        assertTrue(mockPresenter.wasSwitchToDashboardCalled(), "Switch to dashboard should be called on presenter");
    }

    @Test
    void testExecute_MultipleEventsSameName() {
        mockUserDataAccess.setCurrentUsername("testUser");
        Event event1 = createTestEvent("evt1", "Same Name");
        Event event2 = createTestEvent("evt2", "Same Name");

        interactor.execute(new SaveEventInputData(event1));
        interactor.execute(new SaveEventInputData(event2));

        List<Event> savedEvents = interactor.getSavedEvents();
        assertEquals(2, savedEvents.size(), "Should save both events even with same name");
    }

    private Event createTestEvent(String id, String name) {
        Location location = new Location("Test Venue", 43.0, -79.0);
        return new Event(
                id,
                name,
                "Test description",
                "Test Address",
                EventCategory.MUSIC,
                location,
                LocalDateTime.now().plusDays(1),
                ""
        );
    }

    /**
     * Mock presenter for testing
     */
    private static class MockSaveEventPresenter implements SaveEventOutputBoundary {
        private boolean successViewPrepared = false;
        private boolean failureViewPrepared = false;
        private boolean switchToDashboardCalled = false;
        private SaveEventOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(SaveEventOutputData outputData) {
            this.successViewPrepared = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            this.failureViewPrepared = true;
            this.errorMessage = errorMessage;
        }

        @Override
        public void switchToDashboardView() {
            this.switchToDashboardCalled = true;
        }

        public boolean isSuccessViewPrepared() {
            return successViewPrepared;
        }

        public boolean isFailureViewPrepared() {
            return failureViewPrepared;
        }

        public boolean wasSwitchToDashboardCalled() {
            return switchToDashboardCalled;
        }

        public SaveEventOutputData getOutputData() {
            return outputData;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    /**
     * Mock FileSavedEventsDataAccessObject for testing
     */
    private static class MockFileSavedEventsDAO extends FileSavedEventsDataAccessObject {
        private boolean saveCalled = false;
        private boolean removeCalled = false;
        private String lastSavedUsername;
        private Event lastSavedEvent;
        private String lastRemovedUsername;
        private Event lastRemovedEvent;
        private java.util.Map<String, java.util.List<Event>> storage = new java.util.HashMap<>();

        @Override
        public void saveEvent(String username, Event event) {
            this.saveCalled = true;
            this.lastSavedUsername = username;
            this.lastSavedEvent = event;
            storage.computeIfAbsent(username, k -> new java.util.ArrayList<>()).add(event);
        }

        @Override
        public void removeEvent(String username, Event event) {
            this.removeCalled = true;
            this.lastRemovedUsername = username;
            this.lastRemovedEvent = event;
            List<Event> events = storage.get(username);
            if (events != null) {
                events.removeIf(e -> e.getId().equals(event.getId()));
            }
        }

        @Override
        public List<Event> getSavedEvents(String username) {
            return storage.getOrDefault(username, new java.util.ArrayList<>());
        }

        @Override
        public boolean isSavedEvent(String username, String id) {
            List<Event> events = storage.get(username);
            if (events == null) return false;
            return events.stream().anyMatch(e -> e.getId().equals(id));
        }

        public void addSavedEvent(String username, Event event) {
            storage.computeIfAbsent(username, k -> new java.util.ArrayList<>()).add(event);
        }

        public boolean wasSaveCalled() {
            return saveCalled;
        }

        public boolean wasRemoveCalled() {
            return removeCalled;
        }

        public String getLastSavedUsername() {
            return lastSavedUsername;
        }

        public Event getLastSavedEvent() {
            return lastSavedEvent;
        }

        public String getLastRemovedUsername() {
            return lastRemovedUsername;
        }

        public Event getLastRemovedEvent() {
            return lastRemovedEvent;
        }
    }

    /**
     * Mock LoginUserDataAccessInterface for testing
     */
    private static class MockLoginUserDataAccess implements LoginUserDataAccessInterface {
        private String currentUsername;

        public void setCurrentUsername(String username) {
            this.currentUsername = username;
        }

        @Override
        public String getCurrentUsername() {
            return currentUsername;
        }

        // Implement other required methods from interface
        @Override
        public boolean existsByName(String identifier) {
            return false;
        }

        @Override
        public void save(entity.User user) {
            // Not needed for this test
        }

        @Override
        public entity.User get(String username) {
            return null;
        }

    }
}