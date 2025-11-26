package use_case.display_local_events;

import data_access.TicketmasterEventRepositoryAdapter;
import entity.Event;
import entity.EventRepository;
import entity.Location;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Interactor - implements the core business logic for displaying local events
 * This is the heart of the Display Local Events functionality
 */
public class DisplayLocalEventsInteractor implements DisplayLocalEventsInputBoundary {
    private final EventRepository eventRepository;
    private final DisplayLocalEventsOutputBoundary outputBoundary;

    /**
     * Constructor for DisplayLocalEventsInteractor
     * @param eventRepository the repository for accessing event data
     * @param outputBoundary the output boundary for presenting results
     */
    public DisplayLocalEventsInteractor(EventRepository eventRepository,
                                        DisplayLocalEventsOutputBoundary outputBoundary) {
        this.eventRepository = eventRepository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(DisplayLocalEventsInputData inputData) {
        try {
            if (!inputData.isValid()) {
                outputBoundary.presentError("Invalid input: location and radius are required");
                return;
            }

            List<Event> allEvents;
            if (eventRepository instanceof TicketmasterEventRepositoryAdapter) {
                TicketmasterEventRepositoryAdapter adapter =
                        (TicketmasterEventRepositoryAdapter) eventRepository;
                allEvents = adapter.findEvents(
                        inputData.getUserLocation(),
                        inputData.getRadiusKm()
                );
            } else {
                allEvents = eventRepository.findAllEvents();
            }

            List<Event> localEvents = allEvents.stream()
                    .filter(event -> event.isWithinRadius(inputData.getUserLocation(), inputData.getRadiusKm()))
                    .collect(java.util.stream.Collectors.toList());

            // 4. Filter by category if specified (using entity business logic)
            List<Event> filteredEvents = localEvents;
            if (inputData.hasCategoryFilter()) {
                filteredEvents = localEvents.stream()
                        .filter(event -> event.isInCategory(inputData.getFilterCategory()))
                        .collect(Collectors.toList());
            }

            // 5. Sort events by distance
            List<Event> sortedEvents = filteredEvents.stream()
                    .sorted(Comparator.comparing(event ->
                            event.calculateDistanceTo(inputData.getUserLocation())))
                    .collect(Collectors.toList());

            // 6. Calculate distances for each event
            Map<String, Double> eventDistances = calculateEventDistances(sortedEvents, inputData.getUserLocation());

            // 7. Prepare success message
            String message = buildSuccessMessage(sortedEvents.size(), inputData);

            // 8. Create output data
            DisplayLocalEventsOutputData outputData = new DisplayLocalEventsOutputData(
                    sortedEvents,
                    message,
                    eventDistances
            );

            // 9. Present successful results
            outputBoundary.presentSuccess(outputData);

        } catch (Exception e) {
            // 10. Handle errors gracefully
            outputBoundary.presentError("Error displaying local events: " + e.getMessage());
        }
    }

    /**
     * Calculates distances for each event from the user's location
     */
    private Map<String, Double> calculateEventDistances(List<Event> events, Location userLocation) {
        Map<String, Double> distances = new HashMap<>();
        for (Event event : events) {
            double distance = event.calculateDistanceTo(userLocation);
            distances.put(event.getId(), distance);
        }
        return distances;
    }

    /**
     * Builds a user-friendly success message based on the results
     */
    private String buildSuccessMessage(int eventCount, DisplayLocalEventsInputData inputData) {
        if (inputData.hasCategoryFilter()) {
            return String.format("Found %d %s events within %.1f km, sorted by distance",
                    eventCount,
                    inputData.getFilterCategory().getDisplayName(),
                    inputData.getRadiusKm());
        } else {
            return String.format("Found %d events within %.1f km, sorted by distance",
                    eventCount, inputData.getRadiusKm());
        }
    }
}