package demo;

import interface_adapter.login.LoginViewModel;
import use_case.search_event_by_name.SearchEventByNameDataAccessInterface;
import entity.Event;
import entity.Location;
import interface_adapter.ViewManagerModel;
import interface_adapter.search_event_by_name.SearchEventByNameController;
import interface_adapter.search_event_by_name.SearchEventByNamePresenter;
import interface_adapter.search_event_by_name.SearchEventByNameViewModel;
import use_case.search_event_by_name.SearchEventByNameInputBoundary;
import use_case.search_event_by_name.SearchEventByNameInteractor;
import use_case.search_event_by_name.SearchEventByNameOutputBoundary;
import view.SearchEventByNameView;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class NoDataAccessEventViewDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Event View Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new CardLayout());

        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final LoginViewModel loginViewModel = new LoginViewModel();

        // Create a simple login view for testing
        JPanel loginView = new JPanel();
        loginView.setBackground(Color.BLUE);
        loginView.add(new JLabel("Login View - Back button worked!"));

        // Create mock data access
        SearchEventByNameDataAccessInterface mockDataAccess = new SearchEventByNameDataAccessInterface() {
            @Override
            public List<Event> searchEventsByName(String keyword, Location location, double radiusKm) {
                return new ArrayList<>();
            }


            public Event getEventByName(String eventName) {
                return null;
            }


            public List<Event> searchEvents(String query) {
                return new ArrayList<>();
            }
        };

        // Create the search event view
        final SearchEventByNameViewModel viewModel = new SearchEventByNameViewModel();
        final SearchEventByNameOutputBoundary outputBoundary = new SearchEventByNamePresenter(viewModel, viewManagerModel);
        final SearchEventByNameInputBoundary inputBoundary = new SearchEventByNameInteractor(mockDataAccess, outputBoundary);
        final SearchEventByNameController eventController = new SearchEventByNameController(inputBoundary);

        SearchEventByNameView eventView = new SearchEventByNameView(viewModel);
        eventView.setEventController(eventController);

        // Add both views to the frame with CardLayout
        frame.add(eventView, "eventView");
        frame.add(loginView, "loginView");

        // Set up ViewManagerModel listener to switch views
        viewManagerModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("state".equals(evt.getPropertyName())) {
                    CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                    String newView = (String) evt.getNewValue();
                    cardLayout.show(frame.getContentPane(), newView);
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });

        // Set initial view
        viewManagerModel.setState("");
        viewManagerModel.firePropertyChange();

        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}