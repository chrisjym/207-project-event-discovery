package use_case.save_event;

import use_case.login.LoginInputData;

public interface SaveEventInputBoundary {
    /**
     * Executes the save event use case
     * @param inputData the input data
     */
    void execute(SaveEventInputData inputData);

    /**
     * Executes switch to dashboard view use case
     */
    void switchToDashboardView();
}
