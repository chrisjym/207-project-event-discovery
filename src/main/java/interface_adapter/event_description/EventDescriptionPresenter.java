package interface_adapter.event_description;

import use_case.event_description.EventDescriptionOutputBoundary;
import use_case.event_description.EventDescriptionOutputData;

public class EventDescriptionPresenter implements EventDescriptionOutputBoundary {

    private final EventDescriptionViewModel viewModel;

    public EventDescriptionPresenter(EventDescriptionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(EventDescriptionOutputData outputData) {
        String distanceText = String.format("%.2f km away", outputData.getDistanceKm());

        viewModel.setEventDetails(
                outputData.getName(),
                outputData.getDescription(),
                outputData.getAddress(),
                outputData.getCategory(),
                outputData.getDateTime(),
                distanceText
        );
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setError(errorMessage);
    }
}
