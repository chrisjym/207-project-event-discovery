package interface_adapter.update_location;

import interface_adapter.ViewModel;

/**
 * ViewModel for the Update Location feature.
 */
public class UpdateLocationViewModel extends ViewModel<UpdateLocationState> {

    public UpdateLocationViewModel() {
        super("update location");
        setState(new UpdateLocationState());
    }
}