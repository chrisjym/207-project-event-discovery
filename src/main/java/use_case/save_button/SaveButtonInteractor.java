package use_case.save_button;


public class SaveButtonInteractor implements SaveButtonInputBoundary{

    private SaveButtonOutputBoundary eventPresenter;

    public SaveButtonInteractor(SaveButtonOutputBoundary outputBoundary) {
        this.eventPresenter = outputBoundary;
    }

    @Override
    public void execute(SaveButtonInputData inputData) {

    }
}
