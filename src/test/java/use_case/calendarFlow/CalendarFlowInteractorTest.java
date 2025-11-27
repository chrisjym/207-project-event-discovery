//package use_case.calendarFlow;
//
//import entity.Event;
//import entity.EventCategory;
//import entity.Location;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Test class for CalendarFlowInteractor.
// * Tests the business logic for fetching events by date.
// */
//public class CalendarFlowInteractorTest {
//
//    private CalendarFlowInteractor interactor;
//    private MockCalendarFlowDataAccess mockDataAccess;
//    private MockCalendarFlowPresenter mockPresenter;
//
//    @BeforeEach
//    public void setUp() {
//        mockDataAccess = new MockCalendarFlowDataAccess();
//        mockPresenter = new MockCalendarFlowPresenter();
//        interactor = new CalendarFlowInteractor(mockDataAccess, mockPresenter);
//    }
//
//    @Test
//    public void testSuccessfulEventRetrieval() {
//        // Setup test data
//        LocalDate date = LocalDate.of(2024, 11, 22);
//        Location location = new Location("Toronto, ON", 43.6532, -79.3832);
//        double radius = 50.0;
//
//        // Create input data
//        CalendarFlowInputData inputData = new CalendarFlowInputData(date, location, radius);
//
//        // Execute
//        interactor.execute(inputData);
//
//        // Verify success view was called
//        assertTrue(mockPresenter.isSuccessViewCalled());
//        assertFalse(mockPresenter.isFailViewCalled());
//
//        // Verify output data
//        assertNotNull(mockPresenter.getOutputData());
//        assertEquals(date, mockPresenter.getOutputData().getSelectedDate());
//        assertEquals(3, mockPresenter.getOutputData().getEvents().size());
//    }
//
//    @Test
//    public void testNoEventsFound() {
//        // Setup - data access returns empty list
//        mockDataAccess.setReturnEmptyList(true);
//
//        LocalDate date = LocalDate.of(2024, 12, 25);
//        Location location = new Location("Toronto, ON", 43.6532, -79.3832);
//        double radius = 50.0;
//
//        CalendarFlowInputData inputData = new CalendarFlowInputData(date, location, radius);
//
//        // Execute
//        interactor.execute(inputData);
//
//        // Verify fail view was called
//        assertFalse(mockPresenter.isSuccessViewCalled());
//        assertTrue(mockPresenter.isFailViewCalled());
//        assertTrue(mockPresenter.getErrorMessage().contains("No events found"));
//    }
//
//    @Test
//    public void testNullDate() {
//        // Create input with null date
//        Location location = new Location("Toronto, ON", 43.6532, -79.3832);
//        CalendarFlowInputData inputData = new CalendarFlowInputData(null, location, 50.0);
//
//        // Execute
//        interactor.execute(inputData);
//
//        // Verify fail view was called with appropriate error
//        assertTrue(mockPresenter.isFailViewCalled());
//        assertFalse(mockPresenter.isSuccessViewCalled());
//        assertEquals("Date cannot be null", mockPresenter.getErrorMessage());
//    }
//
//    @Test
//    public void testNullLocation() {
//        // Create input with null location
//        LocalDate date = LocalDate.of(2024, 11, 22);
//        CalendarFlowInputData inputData = new CalendarFlowInputData(date, null, 50.0);
//
//        // Execute
//        interactor.execute(inputData);
//
//        // Verify fail view was called with appropriate error
//        assertTrue(mockPresenter.isFailViewCalled());
//        assertFalse(mockPresenter.isSuccessViewCalled());
//        assertEquals("User location is required", mockPresenter.getErrorMessage());
//    }
//
//    @Test
//    public void testDataAccessException() {
//        // Setup - data access throws exception
//        mockDataAccess.setShouldThrowException(true);
//
//        LocalDate date = LocalDate.of(2024, 11, 22);
//        Location location = new Location("Toronto, ON", 43.6532, -79.3832);
//        CalendarFlowInputData inputData = new CalendarFlowInputData(date, location, 50.0);
//
//        // Execute
//        interactor.execute(inputData);
//
//        // Verify fail view was called
//        assertTrue(mockPresenter.isFailViewCalled());
//        assertFalse(mockPresenter.isSuccessViewCalled());
//        assertTrue(mockPresenter.getErrorMessage().contains("Error fetching events"));
//    }
//
//    @Test
//    public void testDifferentRadii() {
//        // Test with small radius
//        LocalDate date = LocalDate.of(2024, 11, 22);
//        Location location = new Location("Toronto, ON", 43.6532, -79.3832);
//
//        CalendarFlowInputData inputData1 = new CalendarFlowInputData(date, location, 10.0);
//        interactor.execute(inputData1);
//        assertTrue(mockPresenter.isSuccessViewCalled());
//
//        // Reset
//        mockPresenter.reset();
//
//        // Test with large radius
//        CalendarFlowInputData inputData2 = new CalendarFlowInputData(date, location, 100.0);
//        interactor.execute(inputData2);
//        assertTrue(mockPresenter.isSuccessViewCalled());
//    }
//
//    @Test
//    public void testMultipleExecutions() {
//        LocalDate date1 = LocalDate.of(2024, 11, 22);
//        LocalDate date2 = LocalDate.of(2024, 11, 23);
//        Location location = new Location("Toronto, ON", 43.6532, -79.3832);
//
//        // First execution
//        CalendarFlowInputData inputData1 = new CalendarFlowInputData(date1, location, 50.0);
//        interactor.execute(inputData1);
//        assertTrue(mockPresenter.isSuccessViewCalled());
//
//        // Reset presenter
//        mockPresenter.reset();
//
//        // Second execution
//        CalendarFlowInputData inputData2 = new CalendarFlowInputData(date2, location, 50.0);
//        interactor.execute(inputData2);
//        assertTrue(mockPresenter.isSuccessViewCalled());
//    }
//
//    /**
//     * Mock implementation of CalendarFlowDataAccessInterface for testing.
//     */
//    private static class MockCalendarFlowDataAccess implements CalendarFlowDataAccessInterface {
//        private boolean returnEmptyList = false;
//        private boolean shouldThrowException = false;
//
//        public void setReturnEmptyList(boolean returnEmptyList) {
//            this.returnEmptyList = returnEmptyList;
//        }
//
//        public void setShouldThrowException(boolean shouldThrowException) {
//            this.shouldThrowException = shouldThrowException;
//        }
//
//        @Override
//        public List<Event> getEventsByDate(LocalDate date, Location location, double radiusKm) {
//            if (shouldThrowException) {
//                throw new RuntimeException("API connection failed");
//            }
//
//            if (returnEmptyList) {
//                return new ArrayList<>();
//            }
//
//            // Return mock events
//            List<Event> events = new ArrayList<>();
//
//            Location loc1 = new Location("Scotiabank Arena, Toronto, ON", 43.6435, -79.3791);
//            Event event1 = new Event(
//                    "1",
//                    "Toronto Maple Leafs vs Boston Bruins",
//                    "Hockey game",
//                    EventCategory.SPORTS,
//                    loc1,
//                    LocalDateTime.of(2024, 11, 22, 19, 0),
//                    "https://example.com/image1.jpg"
//            );
//
//            Location loc2 = new Location("Rogers Centre, Toronto, ON", 43.6414, -79.3894);
//            Event event2 = new Event(
//                    "2",
//                    "Drake Concert",
//                    "Music concert",
//                    EventCategory.MUSIC,
//                    loc2,
//                    LocalDateTime.of(2024, 11, 22, 20, 30),
//                    "https://example.com/image2.jpg"
//            );
//
//            Location loc3 = new Location("Second City, Toronto, ON", 43.6426, -79.3871);
//            Event event3 = new Event(
//                    "3",
//                    "Comedy Night",
//                    "Comedy show",
//                    EventCategory.ARTS_THEATRE,
//                    loc3,
//                    LocalDateTime.of(2024, 11, 22, 21, 0),
//                    "https://example.com/image3.jpg"
//            );
//
//            events.add(event1);
//            events.add(event2);
//            events.add(event3);
//
//            return events;
//        }
//    }
//
//    /**
//     * Mock implementation of CalendarFlowOutputBoundary for testing.
//     */
//    private static class MockCalendarFlowPresenter implements CalendarFlowOutputBoundary {
//        private boolean successViewCalled = false;
//        private boolean failViewCalled = false;
//        private CalendarFlowOutputData outputData;
//        private String errorMessage;
//
//        @Override
//        public void prepareSuccessView(CalendarFlowOutputData outputData) {
//            this.successViewCalled = true;
//            this.outputData = outputData;
//        }
//
//        @Override
//        public void prepareFailView(String errorMessage) {
//            this.failViewCalled = true;
//            this.errorMessage = errorMessage;
//        }
//
//        public boolean isSuccessViewCalled() {
//            return successViewCalled;
//        }
//
//        public boolean isFailViewCalled() {
//            return failViewCalled;
//        }
//
//        public CalendarFlowOutputData getOutputData() {
//            return outputData;
//        }
//
//        public String getErrorMessage() {
//            return errorMessage;
//        }
//
//        public void reset() {
//            successViewCalled = false;
//            failViewCalled = false;
//            outputData = null;
//            errorMessage = null;
//        }
//    }
//}
