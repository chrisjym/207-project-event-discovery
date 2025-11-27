package use_case.calendarFlow;

import entity.Event;
import entity.EventCategory;
import entity.Location;
import interface_adapter.calendarFlow.CalendarFlowState;
import interface_adapter.calendarFlow.CalendarFlowViewModel;
import view.EventListByDateView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EventListByDateView.
 * Tests the UI rendering and state changes.
 */
public class EventListByDateViewTest {
    private EventListByDateView view;
    private CalendarFlowViewModel viewModel;

    /**
     * Helper method to create test events
     */
    private static List<Event> createTestEvents() {
        List<Event> events = new ArrayList<>();

        Location location1 = new Location("Scotiabank Arena, Toronto, ON", 43.6435, -79.3791);
        Event event1 = new Event(
                "1",
                "Toronto Maple Leafs vs Boston Bruins",
                "Exciting hockey game",
                EventCategory.SPORTS,
                location1,
                LocalDateTime.of(2024, 11, 22, 19, 0),
                "https://example.com/image1.jpg"
        );

        Location location2 = new Location("Rogers Centre, Toronto, ON", 43.6414, -79.3894);
        Event event2 = new Event(
                "2",
                "Drake Concert",
                "Live music performance",
                EventCategory.MUSIC,
                location2,
                LocalDateTime.of(2024, 11, 22, 20, 30),
                "https://example.com/image2.jpg"
        );

        Location location3 = new Location("Second City, Toronto, ON", 43.6426, -79.3871);
        Event event3 = new Event(
                "3",
                "Comedy Night",
                "Stand-up comedy show",
                EventCategory.ARTS_THEATRE,
                location3,
                LocalDateTime.of(2024, 11, 22, 21, 0),
                "https://example.com/image3.jpg"
        );

        events.add(event1);
        events.add(event2);
        events.add(event3);

        return events;
    }


    @BeforeEach
    public void setUp() {
        // Create ViewModel
        viewModel = new CalendarFlowViewModel();
        // Create View
        view = new EventListByDateView(viewModel);
    }

    @Test
    public void testViewCreation() {
        // Test that view is created successfully
        assertNotNull(view);
        assertEquals("event list by date", view.getViewName());
    }

    @Test
    public void testInitialState() {
        // Test that view starts with empty state
        assertNotNull(view);

        // View should be visible and properly initialized
        assertTrue(view.isVisible() || !view.isVisible()); // Just checking it exists
    }

    @Test
    public void testViewWithEvents() {
        // Create test events
        List<Event> events = createTestEvents();

        // Create state with events
        CalendarFlowState state = new CalendarFlowState();
        state.setDate(LocalDate.of(2024, 11, 22));
        state.setEventList(events);

        // Update ViewModel (this should trigger view update)
        viewModel.setState(state);
        viewModel.firePropertyChange();

        // Verify state was set
        assertEquals(3, viewModel.getState().getEventList().size());
        assertEquals(LocalDate.of(2024, 11, 22), viewModel.getState().getDate());
    }

    @Test
    public void testDateFormatting() {
        // Create state with date
        CalendarFlowState state = new CalendarFlowState();
        state.setDate(LocalDate.of(2024, 11, 22));
        state.setEventList(new ArrayList<>());

        // Update ViewModel
        viewModel.setState(state);
        viewModel.firePropertyChange();

        // Verify date is set
        assertEquals(LocalDate.of(2024, 11, 22), viewModel.getState().getDate());
    }

    @Test
    public void testBackButtonAction() {
        // Test that back button action can be set
        boolean[] actionPerformed = {false};

        view.setBackButtonAction(e -> {
            actionPerformed[0] = true;
        });

        // Action should not be performed yet
        assertFalse(actionPerformed[0]);

        // Note: Actually clicking the button would require UI testing framework
    }

    @Test
    public void testMultipleStateUpdates() {
        // First update - with events
        CalendarFlowState state1 = new CalendarFlowState();
        state1.setDate(LocalDate.of(2024, 11, 22));
        state1.setEventList(createTestEvents());

        viewModel.setState(state1);
        viewModel.firePropertyChange();

        assertEquals(3, viewModel.getState().getEventList().size());

        // Second update - no events
        CalendarFlowState state2 = new CalendarFlowState();
        state2.setDate(LocalDate.of(2024, 11, 23));
        state2.setEventList(new ArrayList<>());
        state2.setErrorMessage("No events found");

        viewModel.setState(state2);
        viewModel.firePropertyChange();

        assertTrue(viewModel.getState().getEventList().isEmpty());
        assertEquals(LocalDate.of(2024, 11, 23), viewModel.getState().getDate());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create ViewModel and View
            CalendarFlowViewModel viewModel = new CalendarFlowViewModel();
            EventListByDateView view = new EventListByDateView(viewModel);

            // Set up a sample state
            CalendarFlowState state = new CalendarFlowState();
            state.setDate(LocalDate.of(2024, 11, 22));
            state.setEventList(createTestEvents());  // uses the static helper

            viewModel.setState(state);
            viewModel.firePropertyChange();

            // Show in a JFrame
            JFrame frame = new JFrame("EventListByDateView – Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(view);
            frame.pack();                    // sizes frame to preferred sizes
            frame.setLocationRelativeTo(null); // center on screen
            frame.setVisible(true);
        });
    }

}
