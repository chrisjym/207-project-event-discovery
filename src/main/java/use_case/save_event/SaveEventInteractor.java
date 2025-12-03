package use_case.save_event;

import data_access.FileSavedEventsDataAccessObject;
import entity.Event;
import use_case.login.LoginUserDataAccessInterface;

import java.util.List;

public class SaveEventInteractor implements SaveEventInputBoundary {

    private SaveEventOutputBoundary eventPresenter;
    private SaveEventDataAccessInterface savedEventsDAO;
    private LoginUserDataAccessInterface userDataAccess;

    public SaveEventInteractor(SaveEventOutputBoundary outputBoundary,
                               SaveEventDataAccessInterface savedEventsDAO,
                               LoginUserDataAccessInterface userDataAccess) {
        this.eventPresenter = outputBoundary;
        this.savedEventsDAO = savedEventsDAO;
        this.userDataAccess = userDataAccess;
    }

    @Override
    public void execute(SaveEventInputData inputData) {
        if (inputData.getEvent() == null) {
            eventPresenter.prepareFailureView("No Event Found");
        } else {
            Event event = inputData.getEvent();
            String currentUsername = userDataAccess.getCurrentUsername();

            // FIXED: Changed from isSavedEvent to isEventSaved
            if (savedEventsDAO.isEventSaved(currentUsername, event.getId())) {
                eventPresenter.prepareFailureView("Event already saved");
                return;
            }

            savedEventsDAO.saveEvent(currentUsername, event);
            SaveEventOutputData outputData = new SaveEventOutputData(event);
            eventPresenter.prepareSuccessView(outputData);
        }
    }

    @Override
    public void switchToDashboardView() {
        eventPresenter.switchToDashboardView();
    }

    // FIXED: Changed from removeEvent to unsaveEvent
    public void removeEvent(Event event) {
        String currentUsername = userDataAccess.getCurrentUsername();
        if (currentUsername != null) {
            savedEventsDAO.unsaveEvent(currentUsername, event);
        }
    }

    public List<Event> getSavedEvents() {
        String username = userDataAccess.getCurrentUsername();
        if (username == null) return List.of();
        return savedEventsDAO.getSavedEvents(username);
    }

    public void unsaveEvent(Event event) {
        String username = userDataAccess.getCurrentUsername();
        if (username != null) {
            savedEventsDAO.unsaveEvent(username, event);
        }
    }

    public boolean isEventSaved(String eventId) {
        String username = userDataAccess.getCurrentUsername();
        if (username == null || eventId == null) return false;
        return savedEventsDAO.isEventSaved(username, eventId);
    }
}