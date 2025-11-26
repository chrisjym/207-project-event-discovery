package interface_adapter.save_event;

import entity.Event;
import use_case.save_event.SaveEventInputBoundary;
import use_case.save_event.SaveEventInputData;

public class SaveEventController {
    private final SaveEventInputBoundary saveEventInteractor;

    public SaveEventController(SaveEventInputBoundary saveEventUseCase) {
        this.saveEventInteractor = saveEventUseCase;
    }

    public void saveEvent(Event event) {
        SaveEventInputData inputData = new SaveEventInputData(event);
        saveEventInteractor.execute(inputData);
    }

    public void switchToDashboardView() {
        saveEventInteractor.switchToDashboardView();
    }
}
