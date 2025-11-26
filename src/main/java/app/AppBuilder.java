package app;

import data_access.InMemoryEventDataAccessObject;
import data_access.EventDataAccessInterface;
import interface_adapter.event_description.EventDescriptionViewModel;
import interface_adapter.event_description.EventDescriptionPresenter;
import interface_adapter.event_description.EventDescriptionController;
import use_case.event_description.*;
import view.EventDescriptionView;


import data_access.FileUserDataAccessObject;
import data_access.EventDataAccessObject;
import entity.Location;

import data_access.TicketmasterEventRepositoryAdapter;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;

import interface_adapter.displaylocalevents.DisplayLocalEventsController;
import interface_adapter.displaylocalevents.DisplayLocalEventsPresenter;
import interface_adapter.displaylocalevents.DisplayLocalEventsViewModel;

import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;

import use_case.display_local_events.DisplayLocalEventsInputBoundary;
import use_case.display_local_events.DisplayLocalEventsInteractor;
import use_case.display_local_events.DisplayLocalEventsOutputBoundary;

import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;
import view.DisplayLocalEventsView;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {

    private static final String TICKETMASTER_API_KEY = "YOUR_API_KEY_HERE";

    private EventDescriptionViewModel eventDescriptionViewModel;
    private EventDescriptionView eventDescriptionView;

    // temporary Event DAO (for now, in-memory)
    final EventDataAccessInterface eventDataAccessObject = new InMemoryEventDataAccessObject();

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final FileUserDataAccessObject userDataAccessObject =
            new FileUserDataAccessObject("users.csv", userFactory);

    // DAO version using a shared external database
    // final DBUserDataAccessObject userDataAccessObject =
    //        new DBUserDataAccessObject(userFactory);

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;

    private DisplayLocalEventsViewModel displayLocalEventsViewModel;
    private DisplayLocalEventsView displayLocalEventsView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public AppBuilder addDisplayLocalEventsView() {
        displayLocalEventsViewModel = new DisplayLocalEventsViewModel();
        displayLocalEventsView = new DisplayLocalEventsView(displayLocalEventsViewModel);
        cardPanel.add(displayLocalEventsView, displayLocalEventsView.getViewName());
        return this;
    }


    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel, signupViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
                loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);
        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);
        return this;
    }


    public AppBuilder addDisplayLocalEventsUseCase() {
        EventDataAccessObject dao = new EventDataAccessObject();

        Location defaultCenter = new Location("Toronto, ON", 43.6532, -79.3832);
        double defaultRadiusKm = 50.0;

        TicketmasterEventRepositoryAdapter eventRepository =
                new TicketmasterEventRepositoryAdapter(dao, defaultCenter, defaultRadiusKm);

        DisplayLocalEventsOutputBoundary outputBoundary =
                new DisplayLocalEventsPresenter(displayLocalEventsViewModel);

        DisplayLocalEventsInputBoundary interactor =
                new DisplayLocalEventsInteractor(eventRepository, outputBoundary);

        DisplayLocalEventsController controller =
                new DisplayLocalEventsController(interactor);


        displayLocalEventsView.setController(controller);

        return this;
    }


    public AppBuilder addEventDescriptionView() {
        eventDescriptionViewModel = new EventDescriptionViewModel();
        eventDescriptionView = new EventDescriptionView(eventDescriptionViewModel);
        cardPanel.add(eventDescriptionView, eventDescriptionView.getViewName());
        return this;
    }

    public AppBuilder addEventDescriptionUseCase() {
        DistanceCalculator distanceCalculator = new HaversineDistanceCalculator();

        EventDescriptionOutputBoundary presenter =
                new EventDescriptionPresenter(eventDescriptionViewModel);

        EventDescriptionInputBoundary interactor =
                new EventDescriptionInteractor(eventDataAccessObject, presenter, distanceCalculator);

        EventDescriptionController controller =
                new EventDescriptionController(interactor);

//        eventDescriptionView.setController(controller);
        return this;
    }


    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        // TODO: FOR DEBUGGING PURPOSES ONLY
        //viewManagerModel.setState(eventDescriptionView.getViewName());

        // TODO: KEEP CODE BELOW
        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }


}
