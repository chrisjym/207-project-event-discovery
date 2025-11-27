package interface_adapter.display_local_events;

import entity.Event;
import interface_adapter.ViewManagerModel;
import interface_adapter.save_event.SaveEventController;
import interface_adapter.save_event.SaveEventViewModel;
import use_case.display_local_events.DisplayLocalEventsOutputBoundary;
import use_case.display_local_events.DisplayLocalEventsOutputData;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisplayLocalEventsPresenter implements DisplayLocalEventsOutputBoundary {
    private final DisplayLocalEventsViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final SaveEventViewModel saveEventViewModel;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public DisplayLocalEventsPresenter(DisplayLocalEventsViewModel viewModel,
                                       ViewManagerModel viewManagerModel,
                                       SaveEventViewModel saveEventViewModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.saveEventViewModel = saveEventViewModel;
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

            DisplayLocalEventsViewModel.EventCard card = new DisplayLocalEventsViewModel.EventCard(
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


        viewManagerModel.setState(viewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void presentError(String errorMessage) {
        viewModel.setEventCards(List.of());
        viewModel.setMessage("");
        viewModel.setError(errorMessage != null ? errorMessage : "Unknown error");


        viewManagerModel.setState(viewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    public void switchToSaveView() {
        viewManagerModel.setState(saveEventViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

}