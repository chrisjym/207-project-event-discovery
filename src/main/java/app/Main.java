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
                .addEventDescriptionView()       // NEW
                .addSaveEventView()
                .addDisplayLocalEventsView()
                .addEventSearchView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addLogoutUseCase()
                .addCalendarFlowUseCase()
                .addSaveEventUseCase()
                .addEventDescriptionUseCase()    // NEW
                .addDisplayLocalEventsUseCase()
                .addSearchEventByNameUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
