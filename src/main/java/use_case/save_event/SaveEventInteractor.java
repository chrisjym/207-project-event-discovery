package use_case.save_event;

import data_access.FileSavedEventsDataAccessObject;
import data_access.FileUserDataAccessObject;
import entity.Event;
import use_case.login.LoginUserDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class SaveEventInteractor implements SaveEventInputBoundary {

    private SaveEventOutputBoundary eventPresenter;
    private FileSavedEventsDataAccessObject savedEventsDAO;
    private LoginUserDataAccessInterface userDataAccess;

    public SaveEventInteractor(SaveEventOutputBoundary outputBoundary,
                               FileSavedEventsDataAccessObject savedEventsDAO,
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

            if (savedEventsDAO.isSavedEvent(currentUsername, event.getId())) {
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

    public List<Event> getSavedEvents() {
        String currentUsername = userDataAccess.getCurrentUsername();
        if (currentUsername == null) {
            return List.of();
        }
        return savedEventsDAO.getSavedEvents(currentUsername);
    }

    public void removeEvent(Event event) {
        String currentUsername = userDataAccess.getCurrentUsername();
        if (currentUsername != null) {
            savedEventsDAO.removeEvent(currentUsername, event);
        }
    }
}
