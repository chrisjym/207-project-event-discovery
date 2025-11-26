package use_case.save_button;

import use_case.save_button.SaveButtonInputData;

public interface SaveButtonInputBoundary {

    /**
     * Executes the save button use case
     * @param inputData the input data
     */
    void execute(SaveButtonInputData inputData);
}
