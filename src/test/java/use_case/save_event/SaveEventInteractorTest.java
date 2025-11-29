//Assisted by AI
package use_case.save_event;

import data_access.FileSavedEventsDataAccessObject;
import entity.Event;
import entity.EventCategory;
import entity.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.login.LoginUserDataAccessInterface;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for SaveEventInteractor.
 * Tests the save event use case functionality including:
 * - Successful event saving
 * - Duplicate event detection
 * - Null event handling
 * - Event removal
 * - Checking if event is saved
 */
class SaveEventInteractorTest {

    private SaveEventInteractor interactor;
    private TestSaveEventPresenter testPresenter;
    private TestSavedEventsDAO testDAO;
    private TestUserDataAccess testUserDataAccess;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        // Create test event
        Location location = new Location("Test Venue, Toronto", 43.6532, -79.3832);
        testEvent = new Event(
                "test-123",
                "Test Concert",
                "A great test concert",
                "Test Venue, Toronto",
                EventCategory.MUSIC,
                location,
                LocalDateTime.of(2025, 12, 15, 19, 0),
                "http://example.com/image.jpg"
        );

        testPresenter = new TestSaveEventPresenter();
        testDAO = new TestSavedEventsDAO();
        testUserDataAccess = new TestUserDataAccess();

        interactor = new SaveEventInteractor(testPresenter, testDAO, testUserDataAccess);
    }

    @Test
    void testExecute_successfulSave() {
        testUserDataAccess.setCurrentUsername("testuser");
        SaveEventInputData inputData = new SaveEventInputData(testEvent);

        interactor.execute(inputData);

        assertTrue(testPresenter.isSuccessCalled(), "Success view should be called");
        assertFalse(testPresenter.isFailureCalled(), "Failure view should not be called");
        assertEquals(testEvent, testPresenter.getOutputData().getEvent(), "Output should contain the saved event");
        assertEquals(1, testDAO.getSavedEventCount("testuser"), "Event should be saved once");
        assertTrue(testDAO.isEventSaved("testuser", testEvent.getId()), "Event should be marked as saved");
    }

    @Test
    void testExecute_nullEvent() {
        // Arrange
        testUserDataAccess.setCurrentUsername("testuser");
        SaveEventInputData inputData = new SaveEventInputData(null);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(testPresenter.isFailureCalled(), "Failure view should be called");
        assertFalse(testPresenter.isSuccessCalled(), "Success view should not be called");
        assertEquals("No Event Found", testPresenter.getErrorMessage(), "Error message should indicate no event");
        assertEquals(0, testDAO.getSavedEventCount("testuser"), "No events should be saved");
    }

    @Test
    void testExecute_duplicateEvent() {
        testUserDataAccess.setCurrentUsername("testuser");
        SaveEventInputData inputData = new SaveEventInputData(testEvent);

        interactor.execute(inputData);
        testPresenter.reset();

        interactor.execute(inputData);

        assertTrue(testPresenter.isFailureCalled(), "Failure view should be called for duplicate");
        assertFalse(testPresenter.isSuccessCalled(), "Success view should not be called for duplicate");
        assertEquals("Event already saved", testPresenter.getErrorMessage(),
                "Error message should indicate duplicate");
        assertEquals(1, testDAO.getSavedEventCount("testuser"),
                "Event should still only be saved once");
    }

    @Test
    void testGetSavedEvents() {
        testUserDataAccess.setCurrentUsername("testuser");
        SaveEventInputData inputData = new SaveEventInputData(testEvent);
        interactor.execute(inputData);

        List<Event> savedEvents = interactor.getSavedEvents();

        assertNotNull(savedEvents, "Saved events list should not be null");
        assertEquals(1, savedEvents.size(), "Should have one saved event");
        assertEquals(testEvent.getId(), savedEvents.get(0).getId(), "Saved event should match test event");
    }

    @Test
    void testGetSavedEvents_noUser() {
        testUserDataAccess.setCurrentUsername(null);

        List<Event> savedEvents = interactor.getSavedEvents();

        assertNotNull(savedEvents, "Saved events list should not be null");
        assertEquals(0, savedEvents.size(), "Should have no saved events when no user");
    }

    @Test
    void testUnsaveEvent() {
        testUserDataAccess.setCurrentUsername("testuser");
        SaveEventInputData inputData = new SaveEventInputData(testEvent);
        interactor.execute(inputData);

        interactor.unsaveEvent(testEvent);

        assertEquals(0, testDAO.getSavedEventCount("testuser"), "Event should be removed");
        assertFalse(testDAO.isEventSaved("testuser", testEvent.getId()), "Event should not be marked as saved");
    }

    @Test
    void testUnsaveEvent_noUser() {
        testUserDataAccess.setCurrentUsername("testuser");
        SaveEventInputData inputData = new SaveEventInputData(testEvent);
        interactor.execute(inputData);

        testUserDataAccess.setCurrentUsername(null);

        interactor.unsaveEvent(testEvent);

        assertEquals(1, testDAO.getSavedEventCount("testuser"), "Event should not be removed without user");
    }

    @Test
    void testIsEventSaved() {
        testUserDataAccess.setCurrentUsername("testuser");
        SaveEventInputData inputData = new SaveEventInputData(testEvent);
        interactor.execute(inputData);

        assertTrue(interactor.isEventSaved(testEvent.getId()), "Event should be marked as saved");
        assertFalse(interactor.isEventSaved("nonexistent-id"), "Non-existent event should not be marked as saved");
    }

    @Test
    void testIsEventSaved_nullEventId() {
        testUserDataAccess.setCurrentUsername("testuser");

        assertFalse(interactor.isEventSaved(null), "Null event ID should return false");
    }

    @Test
    void testIsEventSaved_noUser() {
        testUserDataAccess.setCurrentUsername(null);

        assertFalse(interactor.isEventSaved(testEvent.getId()), "Should return false when no user");
    }

    @Test
    void testSwitchToDashboardView() {
        interactor.switchToDashboardView();

        assertTrue(testPresenter.isSwitchToDashboardCalled(), "Switch to dashboard should be called");
    }

    @Test
    void testRemoveEvent() {
        testUserDataAccess.setCurrentUsername("testuser");
        SaveEventInputData inputData = new SaveEventInputData(testEvent);
        interactor.execute(inputData);

        interactor.removeEvent(testEvent);

        assertEquals(0, testDAO.getSavedEventCount("testuser"), "Event should be removed");
        assertFalse(testDAO.isEventSaved("testuser", testEvent.getId()), "Event should not be marked as saved");
    }

    @Test
    void testRemoveEvent_noUser() {
        testUserDataAccess.setCurrentUsername("testuser");
        SaveEventInputData inputData = new SaveEventInputData(testEvent);
        interactor.execute(inputData);

        testUserDataAccess.setCurrentUsername(null);

        interactor.removeEvent(testEvent);

        assertEquals(1, testDAO.getSavedEventCount("testuser"), "Event should not be removed without user");
    }

    @Test
    void testIsEventSaved_nullUsername() {
        testUserDataAccess.setCurrentUsername(null);

        assertFalse(interactor.isEventSaved("some-event-id"), "Should return false when username is null");
    }


    /**
     * Test implementation of SaveEventOutputBoundary.
     */
    private static class TestSaveEventPresenter implements SaveEventOutputBoundary {
        private boolean successCalled = false;
        private boolean failureCalled = false;
        private boolean switchToDashboardCalled = false;
        private SaveEventOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(SaveEventOutputData outputData) {
            this.successCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            this.failureCalled = true;
            this.errorMessage = errorMessage;
        }

        @Override
        public void switchToDashboardView() {
            this.switchToDashboardCalled = true;
        }

        public boolean isSuccessCalled() {
            return successCalled;
        }

        public boolean isFailureCalled() {
            return failureCalled;
        }

        public boolean isSwitchToDashboardCalled() {
            return switchToDashboardCalled;
        }

        public SaveEventOutputData getOutputData() {
            return outputData;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void reset() {
            successCalled = false;
            failureCalled = false;
            switchToDashboardCalled = false;
            outputData = null;
            errorMessage = null;
        }
    }

    /**
     * Test implementation of FileSavedEventsDataAccessObject.
     */
    private static class TestSavedEventsDAO extends FileSavedEventsDataAccessObject {
        private final List<Event> savedEvents = new ArrayList<>();
        private String currentUser = null;

        @Override
        public void saveEvent(String username, Event event) {
            currentUser = username;
            if (!isEventSaved(username, event.getId())) {
                savedEvents.add(event);
            }
        }

        @Override
        public List<Event> getSavedEvents(String username) {
            if (username == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(savedEvents);
        }

        @Override
        public void unsaveEvent(String username, Event event) {
            savedEvents.removeIf(e -> e.getId().equals(event.getId()));
        }

        @Override
        public boolean isEventSaved(String username, String eventId) {
            if (username == null || eventId == null) {
                return false;
            }
            return savedEvents.stream().anyMatch(e -> e.getId().equals(eventId));
        }

        public int getSavedEventCount(String username) {
            return getSavedEvents(username).size();
        }
    }

    /**
     * Test implementation of LoginUserDataAccessInterface.
     */
    private static class TestUserDataAccess implements LoginUserDataAccessInterface {
        private String currentUsername;

        @Override
        public boolean existsByName(String username) {
            return true;
        }

        @Override
        public void save(entity.User user) {
        }

        @Override
        public entity.User get(String username) {
            return null;
        }

        @Override
        public void setCurrentUsername(String name) {
            this.currentUsername = name;
        }

        @Override
        public String getCurrentUsername() {
            return currentUsername;
        }
    }
}