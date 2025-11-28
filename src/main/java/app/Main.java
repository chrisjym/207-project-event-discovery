package app;

import javax.swing.*;

public class Main {

    /**
     * Main gateway into application
     * Running this file will begin the application
     * @param args input arguments to the main method

     **/
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addCalendarViews()
                .addEventDescriptionView()
                .addDisplayLocalEventsView()
                .addSaveEventView()
                .addEventSearchView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addLogoutUseCase()
                .addCalendarFlowUseCase()
                .addEventDescriptionUseCase()
                .addSaveEventUseCase()
                .addDisplayLocalEventsUseCase()
                .addSearchEventByNameUseCase()
                .addUpdateLocationUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
