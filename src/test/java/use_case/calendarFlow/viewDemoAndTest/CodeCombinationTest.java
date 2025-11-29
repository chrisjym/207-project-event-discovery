//Assisted by AI
package use_case.calendarFlow.viewDemoAndTest;
// ============================================
// SimpleCalendarAPITest.java
// Simple test: Click date → See REAL events from API
// With YOUR mock location and radius
// ============================================

import use_case.calendarFlow.CalendarFlowDataAccessInterface;
import use_case.calendarFlow.CalendarFlowInputBoundary;
import use_case.calendarFlow.CalendarFlowInteractor;
import use_case.calendarFlow.CalendarFlowOutputBoundary;
import view.CalendarView;
import view.EventListByDateView;
import interface_adapter.calendarFlow.*;
import interface_adapter.ViewManagerModel;
import data_access.CalendarFlowDataAccessObject;
import entity.Location;

import javax.swing.*;
import java.awt.*;

/**
 * SIMPLE TEST to see real API results
 *
 * What you'll see:
 * 1. Calendar view opens
 * 2. Click any date
 * 3. Real API call with YOUR location and radius
 * 4. Event list shows REAL events from Ticketmaster
 *
 * Mock Data:
 * - Location: Toronto, ON (43.6532, -79.3832)
 * - Radius: 100 km
 *
 * You can change these below!
 */
public class CodeCombinationTest {

    // ============================================
    // 🎯 CHANGE THESE TO TEST DIFFERENT LOCATIONS
    // ============================================
    private static final String TEST_LOCATION_NAME = "Toronto, ON";
    private static final double TEST_LATITUDE = 43.6532;
    private static final double TEST_LONGITUDE = -79.3832;
    private static final double TEST_RADIUS_KM = 100.0;

    // Other test locations you can try:
    // Montreal: 45.5019, -73.5674
    // New York: 40.7128, -74.0060
    // Los Angeles: 34.0522, -118.2437
    // Chicago: 41.8781, -87.6298

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║     SIMPLE CALENDAR API TEST                           ║");
        System.out.println("║     Real Ticketmaster Events                           ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        System.out.println("📍 Test Configuration:");
        System.out.println("   Location: " + TEST_LOCATION_NAME);
        System.out.println("   Coordinates: (" + TEST_LATITUDE + ", " + TEST_LONGITUDE + ")");
        System.out.println("   Search Radius: " + TEST_RADIUS_KM + " km");
        System.out.println();
        System.out.println("⚠️  Make sure API key is set in CalendarFlowDataAccessObject.java");
        System.out.println("   Line: private static final String API_KEY = \"YOUR_KEY_HERE\";\n");

        SwingUtilities.invokeLater(() -> {
            // Create test location
            Location testLocation = new Location(TEST_LOCATION_NAME, TEST_LATITUDE, TEST_LONGITUDE);

            // Setup view switching
            CardLayout cardLayout = new CardLayout();
            JPanel cardPanel = new JPanel(cardLayout);

            ViewManagerModel viewManagerModel = new ViewManagerModel();
            viewManagerModel.addPropertyChangeListener(evt -> {
                if (evt.getPropertyName().equals("state")) {
                    String viewName = (String) evt.getNewValue();
                    cardLayout.show(cardPanel, viewName);
                    System.out.println("→ Switched to: " + viewName);
                }
            });

            // Create ViewModel
            CalendarFlowViewModel viewModel = new CalendarFlowViewModel();

            // Create Views
            CalendarView calendarView = new CalendarView();
            EventListByDateView eventListView = new EventListByDateView(viewModel);

            // Create Use Case with REAL API
            CalendarFlowDataAccessInterface dataAccess = new CalendarFlowDataAccessObject();
            CalendarFlowOutputBoundary presenter = new CalendarFlowPresenter(viewModel, viewManagerModel);
            CalendarFlowInputBoundary interactor = new CalendarFlowInteractor(dataAccess, presenter);
            CalendarFlowController controller = new CalendarFlowController(interactor);

            // Wire up views with TEST location and radius
            calendarView.setUserLocation(testLocation);
            calendarView.setSearchRadiusKm(TEST_RADIUS_KM);
            calendarView.setEventController(controller);

            eventListView.setController(controller);

            // Add views to panel
            cardPanel.add(calendarView, "calendar view");
            cardPanel.add(eventListView, "event list by date");

            // Create window
            JFrame frame = new JFrame("Calendar API Test - " + TEST_LOCATION_NAME);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(cardPanel);
            frame.setSize(900, 700);
            frame.setLocationRelativeTo(null);

            // Start with calendar view
            cardLayout.show(cardPanel, "calendar view");
            frame.setVisible(true);

            // Instructions
            System.out.println("✅ Application started!\n");
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║  HOW TO USE                                            ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println("1. You see the calendar");
            System.out.println("2. Click ANY date");
            System.out.println("3. Watch console for API call details");
            System.out.println("4. Wait 1-2 seconds for API response");
            System.out.println("5. See REAL events in the list!");
            System.out.println();
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║  WHAT YOU'LL SEE IN THE EVENT LIST                    ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println("• Event name (e.g., 'Toronto Raptors vs Lakers')");
            System.out.println("• Venue address (e.g., 'Scotiabank Arena, 40 Bay St')");
            System.out.println("• Event time (e.g., '7:30 PM')");
            System.out.println("• Category indicator (Sports/Music/Arts/Film)");
            System.out.println();
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║  WATCH CONSOLE FOR                                     ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println("• Selected date");
            System.out.println("• API request details");
            System.out.println("• Number of events found");
            System.out.println("• Event details");
            System.out.println();
        });
    }
}

// ============================================
// DetailedCalendarAPITest.java
// Same as above but with DETAILED console output
// Shows exactly what API returns
// ============================================


