package interface_adapter.save_event;

import interface_adapter.ViewModel;
import interface_adapter.signup.SignupState;

public class SaveEventViewModel extends ViewModel<SaveEventState> {
    public SaveEventViewModel() {
        super("save event");
        setState(new SaveEventState());
    }
}
