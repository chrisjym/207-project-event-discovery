package app;

import data_access.InMemoryEventDataAccessObject;
import data_access.EventDataAccessInterface;
import interface_adapter.event_description.EventDescriptionViewModel;
import interface_adapter.event_description.EventDescriptionPresenter;
import interface_adapter.event_description.EventDescriptionController;
import use_case.event_description.*;
import view.*;


import data_access.FileUserDataAccessObject;
import entity.Event;
import entity.Location;
import data_access.EventDataAccessObject;
import data_access.TicketmasterEventRepositoryAdapter;
import data_access.CalendarFlowDataAccessObject;
import data_access.SearchEventDataAccessObject;
import data_access.FileSavedEventsDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.calendarFlow.CalendarFlowViewModel;
import interface_adapter.calendarFlow.CalendarFlowPresenter;
import interface_adapter.calendarFlow.CalendarFlowController;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.search.SearchController;
import interface_adapter.search.SearchPresenter;
import interface_adapter.search_event_by_name.SearchEventByNameController;
import interface_adapter.search_event_by_name.SearchEventByNamePresenter;
import interface_adapter.search_event_by_name.SearchEventByNameViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.calendarFlow.CalendarFlowInputBoundary;
import use_case.calendarFlow.CalendarFlowInteractor;
import use_case.calendarFlow.CalendarFlowOutputBoundary;
import use_case.calendarFlow.CalendarFlowDataAccessInterface;
import interface_adapter.save_event.SaveEventController;
import interface_adapter.save_event.SaveEventPresenter;
import interface_adapter.save_event.SaveEventViewModel;

import interface_adapter.display_local_events.DisplayLocalEventsController;
import interface_adapter.display_local_events.DisplayLocalEventsPresenter;
import interface_adapter.display_local_events.DisplayLocalEventsViewModel;
import interface_adapter.ViewManagerModel;

import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.search.SearchInputBoundary;
import use_case.search.SearchInteractor;
import use_case.search.SearchOutputBoundary;
import use_case.search_event_by_name.SearchEventByNameInputBoundary;
import use_case.search_event_by_name.SearchEventByNameInteractor;
import use_case.search_event_by_name.SearchEventByNameOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.*;

import use_case.display_local_events.DisplayLocalEventsInputBoundary;
import use_case.display_local_events.DisplayLocalEventsInteractor;
import use_case.display_local_events.DisplayLocalEventsOutputBoundary;

import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;
import view.DisplayLocalEventsView;
import use_case.save_event.SaveEventInputBoundary;
import use_case.save_event.SaveEventInteractor;
import use_case.save_event.SaveEventOutputBoundary;

import javax.swing.*;
import java.awt.*;

/**
 * AppBuilder - Constructs and wires together all components of the application.
 *
 * CLEAN ARCHITECTURE NOTE:
 * This class is responsible for Dependency Injection and wiring.
 * It creates all the layers and connects them:
 *
 * 1. ENTITIES (innermost): Event, User, Location, etc.
 * 2. USE CASES: Interactors (business logic)
 * 3. INTERFACE ADAPTERS: Controllers, Presenters, ViewModels
 * 4. FRAMEWORKS & DRIVERS (outermost): Views, Data Access Objects
 *
 * The Dependency Rule: Dependencies point INWARD.
 * - Views depend on ViewModels and Controllers
 * - Controllers depend on Use Case Input Boundaries (interfaces)
 * - Presenters implement Use Case Output Boundaries (interfaces)
 * - Use Cases depend on Entity interfaces, not concrete implementations
 *
 * This builder ensures proper dependency injection so each layer
 * only knows about the layers inside it, never outside.
 */
public class AppBuilder {
    private EventDescriptionViewModel eventDescriptionViewModel;
    private EventDescriptionView eventDescriptionView;

    final EventDataAccessInterface eventDataAccessObject = new InMemoryEventDataAccessObject();
    final FileSavedEventsDataAccessObject savedEventsDAO = new FileSavedEventsDataAccessObject();

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    final FileUserDataAccessObject userDataAccessObject =
            new FileUserDataAccessObject("users.csv", userFactory);

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private SearchEventByNameView searchEventView;
    private SearchEventByNameViewModel searchEventViewModel;
    private SaveEventViewModel saveEventViewModel;
    private SaveEventsView saveEventsView;
    private SaveButtonView saveButtonView;
    private SaveEventInteractor saveEventInteractor;
    private SaveEventController saveEventController;

    private CalendarFlowViewModel calendarFlowViewModel;
    private CalendarView calendarView;
    private EventListByDateView eventListByDateView;

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

    public AppBuilder addCalendarViews() {
        calendarFlowViewModel = new CalendarFlowViewModel();
        eventListByDateView = new EventListByDateView(calendarFlowViewModel);
        calendarView = new CalendarView();
        cardPanel.add(calendarView, "calendar view");
        cardPanel.add(eventListByDateView, calendarFlowViewModel.getViewName());
        return this;
    }

    public AppBuilder addDisplayLocalEventsView() {
        displayLocalEventsViewModel = new DisplayLocalEventsViewModel();
        displayLocalEventsView = new DisplayLocalEventsView(displayLocalEventsViewModel);
        displayLocalEventsView.setViewManagerModel(viewManagerModel);
        cardPanel.add(displayLocalEventsView, displayLocalEventsView.getViewName());
        return this;
    }

    public AppBuilder addEventSearchView() {
        searchEventViewModel = new SearchEventByNameViewModel();
        searchEventView = new SearchEventByNameView(searchEventViewModel);
        cardPanel.add(searchEventView, searchEventView.getViewName());
        return this;
    }

    public AppBuilder addSaveEventView() {
        saveEventViewModel = new SaveEventViewModel();
        saveEventsView = new SaveEventsView(saveEventViewModel);
        cardPanel.add(saveEventsView, saveEventsView.getViewName());
        return this;
    }

    public AppBuilder addSaveEventUseCase() {
        final FileSavedEventsDataAccessObject savedEventsDAO = new FileSavedEventsDataAccessObject();

        final SaveEventOutputBoundary saveEventPresenter = new SaveEventPresenter(
                saveEventViewModel,
                viewManagerModel
        );

        this.saveEventInteractor = new SaveEventInteractor(
                saveEventPresenter,
                savedEventsDAO,
                userDataAccessObject
        );

        this.saveEventController = new SaveEventController(saveEventInteractor);

        if (saveEventsView != null) {
            saveEventsView.setSaveEventController(saveEventController);
            saveEventsView.setSaveEventInteractor(saveEventInteractor);
        }

        if (saveButtonView != null) {
            saveButtonView.setSaveEventController(saveEventController);
        }

        return this;
    }

    public AppBuilder addSearchEventByNameUseCase() {
        final SearchEventDataAccessObject searchDataAccess = new SearchEventDataAccessObject();

        final SearchEventByNameOutputBoundary presenter = new SearchEventByNamePresenter(
                searchEventViewModel,
                viewManagerModel
        );

        final SearchEventByNameInputBoundary interactor = new SearchEventByNameInteractor(
                searchDataAccess,
                presenter
        );

        final SearchEventByNameController controller = new SearchEventByNameController(interactor);
        searchEventView.setEventController(controller);
        searchEventView.setSaveButtonController(saveEventController);

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

    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    public AppBuilder addCalendarFlowUseCase() {
        CalendarFlowDataAccessInterface calendarGateway = new CalendarFlowDataAccessObject();
        CalendarFlowOutputBoundary calendarOutputBoundary =
                new CalendarFlowPresenter(calendarFlowViewModel, viewManagerModel);
        CalendarFlowInputBoundary calendarInteractor =
                new CalendarFlowInteractor(calendarGateway, calendarOutputBoundary);
        CalendarFlowController calendarController = new CalendarFlowController(calendarInteractor);
        calendarView.setEventController(calendarController);

        return this;
    }

    public AppBuilder addDisplayLocalEventsUseCase() {
        EventDataAccessObject dao = new EventDataAccessObject();

        final SearchEventDataAccessObject searchDataAccess = new SearchEventDataAccessObject();

        final SearchOutputBoundary searchPresenter = new SearchPresenter(
                searchEventViewModel,
                viewManagerModel
        );

        final SearchInputBoundary searchInteractor = new SearchInteractor(
                searchDataAccess,
                searchPresenter
        );

        final SearchController searchController = new SearchController(searchInteractor);

        Location defaultCenter = new Location("Toronto, ON", 43.6532, -79.3832);
        double defaultRadiusKm = 100.0;

        TicketmasterEventRepositoryAdapter eventRepository =
                new TicketmasterEventRepositoryAdapter(dao, defaultCenter, defaultRadiusKm);

        DisplayLocalEventsOutputBoundary outputBoundary =
                new DisplayLocalEventsPresenter(displayLocalEventsViewModel, viewManagerModel, saveEventViewModel);

        DisplayLocalEventsInputBoundary interactor =
                new DisplayLocalEventsInteractor(eventRepository, outputBoundary);

        DisplayLocalEventsController controller =
                new DisplayLocalEventsController(interactor);

        displayLocalEventsView.setController(controller);
        displayLocalEventsView.setSearchBarController(searchController);

        return this;
    }

    /**
     * Add Event Description View.
     *
     * CLEAN ARCHITECTURE NOTE:
     * The View is created with its ViewModel.
     * The View only knows about the ViewModel, not about Use Cases or Data Access.
     */
    public AppBuilder addEventDescriptionView() {
        eventDescriptionViewModel = new EventDescriptionViewModel();
        eventDescriptionView = new EventDescriptionView(eventDescriptionViewModel);
        eventDescriptionView.setViewManagerModel(viewManagerModel);
        cardPanel.add(eventDescriptionView, eventDescriptionView.getViewName());
        return this;
    }

    /**
     * Add Event Description Use Case and wire up event navigation.
     *
     * CLEAN ARCHITECTURE NOTE:
     * This method demonstrates the complete wiring of Clean Architecture:
     *
     * 1. Create the Use Case (Interactor) with its dependencies:
     *    - Data Access (injected via interface)
     *    - Output Boundary (Presenter implements this interface)
     *
     * 2. Create the Controller with the Use Case (via Input Boundary interface)
     *
     * 3. Wire the View to use the Controller
     *
     * 4. Set up navigation using an EventSelectionListener
     *    - This follows the Dependency Inversion Principle
     *    - The View depends on an abstraction (listener interface)
     *    - The concrete implementation is injected here
     */
    public AppBuilder addEventDescriptionUseCase() {
        DistanceCalculator distanceCalculator = new HaversineDistanceCalculator();

        EventDescriptionOutputBoundary presenter =
                new EventDescriptionPresenter(eventDescriptionViewModel);

        EventDescriptionInputBoundary interactor =
                new EventDescriptionInteractor(eventDataAccessObject, presenter, distanceCalculator);

        EventDescriptionController controller =
                new EventDescriptionController(interactor);

        // Wire the save event controller to the event description view
        if (saveEventController != null) {
            eventDescriptionView.setSaveEventController(saveEventController);
        }

        // Set up the event selection listener for navigation from DisplayLocalEventsView
        // This is where we connect the "click on event card" action to the navigation
        displayLocalEventsView.setEventSelectionListener(new DisplayLocalEventsView.EventSelectionListener() {
            @Override
            public void onEventSelected(Event event) {
                // Store the event in the view for display
                eventDescriptionView.displayEvent(event);

                // Calculate and set distance
                Location userLocation = displayLocalEventsView.getCurrentLocationForOthers();
                if (userLocation != null && event.getLocation() != null) {
                    double distance = event.calculateDistanceTo(userLocation);
                    eventDescriptionView.setDistanceText(String.format("%.1f km from your location", distance));
                } else {
                    eventDescriptionView.setDistanceText("Distance unavailable");
                }

                // Navigate to the event description view
                viewManagerModel.setState(eventDescriptionView.getViewName());
                viewManagerModel.firePropertyChange();
            }
        });

        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Ticketmaster Program");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}