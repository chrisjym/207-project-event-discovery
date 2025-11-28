package interface_adapter.update_location;

import use_case.update_location.UpdateLocationOutputBoundary;
import use_case.update_location.UpdateLocationOutputData;

/**
 * Presenter for the Update Location Use Case.
 *
 * CLEAN ARCHITECTURE NOTE:
 * The Presenter receives output from the Use Case and formats it
 * for the View by updating the ViewModel.
 */
public class UpdateLocationPresenter implements UpdateLocationOutputBoundary {

    private final UpdateLocationViewModel viewModel;

    public UpdateLocationPresenter(UpdateLocationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(UpdateLocationOutputData outputData) {
        UpdateLocationState state = viewModel.getState();
        state.setAddress(outputData.getAddress());
        state.setLocation(outputData.getLocation());
        state.setError(null);
        state.setSuccess(true);

        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        UpdateLocationState state = viewModel.getState();
        state.setError(errorMessage);
        state.setSuccess(false);

        viewModel.setState(state);
        viewModel.firePropertyChange();
    }
}