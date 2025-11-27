package app;

import javax.swing.JFrame;

public class Main {

    /**
     * Main gateway into application.
     * Running this file will begin the application
     * @param args input arguments to the main method
     *
     **/

    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addEventDescriptionView()
                .addEventDescriptionView()
                .addDisplayLocalEventsView()
                .addSaveEventView()
                .addEventSearchView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addLogoutUseCase()
                .addEventDescriptionUseCase()
                .addEventDescriptionUseCase()
                .addSaveEventUseCase()
                .addDisplayLocalEventsUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
