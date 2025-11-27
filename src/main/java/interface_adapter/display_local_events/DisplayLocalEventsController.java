package interface_adapter.display_local_events;

import entity.EventCategory;
import entity.Location;
import use_case.display_local_events.DisplayLocalEventsInputBoundary;
import use_case.display_local_events.DisplayLocalEventsInputData;

/**
 * Controller for the Display Local Events use case.
 * This class receives raw input from the UI layer (such as the user's location,
 * search radius, and selected category), constructs an InputData object, and
 * delegates execution to the use case interactor via the InputBoundary.
 */
public class DisplayLocalEventsController {

    private final DisplayLocalEventsInputBoundary interactor;

    public DisplayLocalEventsController(DisplayLocalEventsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Entry point for the UI to trigger the Display Local Events use case.
     *
     * @param userLocation  the user's current location (already constructed).
     * @param radiusKm      the search radius in kilometers.
     * @param categoryOrNull the selected category as a String.
     *                       - If null, empty, or "ALL" (case-insensitive), no category filter is applied.
     *                       - Otherwise, it will be converted to an EventCategory via EventCategory.fromString().
     */
    public void display(Location userLocation, double radiusKm, String categoryOrNull) {
        EventCategory category = null;

        if (categoryOrNull != null && !categoryOrNull.isBlank()
                && !categoryOrNull.equalsIgnoreCase("ALL")) {
            category = EventCategory.fromString(categoryOrNull);
        }

        DisplayLocalEventsInputData inputData =
                new DisplayLocalEventsInputData(userLocation, radiusKm, category);

        interactor.execute(inputData);
    }

    public void switchToSaveView() {
        interactor.switchToSaveView();
    }
}
