package interface_adapter.display_local_events;

import entity.Event;
import use_case.display_local_events.DisplayLocalEventsOutputBoundary;
import use_case.display_local_events.DisplayLocalEventsOutputData;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Presenter for the Display Local Events use case.
 * This class implements the OutputBoundary and is responsible for:
 * - Transforming raw use case output data (domain objects) into a UI-friendly ViewModel.
 * - Formatting dates, distances, and messages for display.
 */
public class DisplayLocalEventsPresenter implements DisplayLocalEventsOutputBoundary {

    private final DisplayLocalEventsViewModel viewModel;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public DisplayLocalEventsPresenter(DisplayLocalEventsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(DisplayLocalEventsOutputData outputData) {
        List<DisplayLocalEventsViewModel.EventCard> cards = new ArrayList<>();
        Map<String, Double> distances = outputData.getEventDistances();

        for (Event event : outputData.getEvents()) {
            String distanceText = "";
            if (distances != null && distances.containsKey(event.getId())) {
                double distance = distances.get(event.getId());
                distanceText = String.format("%.1f km", distance);
            }

            DisplayLocalEventsViewModel.EventCard card =
                    new DisplayLocalEventsViewModel.EventCard(
                            event.getId(),
                            event.getName(),
                            event.getStartTime().format(dateTimeFormatter),
                            event.getLocation().getAddress(),
                            event.getCategory().getDisplayName(),
                            distanceText,
                            event.getImageUrl()
                    );

            cards.add(card);
        }

        viewModel.setEventCards(cards);
        viewModel.setMessage(outputData.getMessage());
        viewModel.setError("");

    }

    @Override
    public void presentError(String errorMessage) {

        viewModel.setEventCards(List.of());
        viewModel.setMessage("");
        viewModel.setError(errorMessage != null ? errorMessage : "Unknown error");

    }
}
