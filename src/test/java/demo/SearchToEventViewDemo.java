package demo;

import data_access.EventDataAccessObject;
import entity.Event;
import entity.Location;
import interface_adapter.ViewManagerModel;
import interface_adapter.search.SearchController;
import interface_adapter.search.SearchPresenter;
import interface_adapter.search_event_by_name.SearchEventByNameController;
import interface_adapter.search_event_by_name.SearchEventByNamePresenter;
import interface_adapter.search_event_by_name.SearchEventByNameViewModel;
import use_case.search.SearchInputBoundary;
import use_case.search.SearchInteractor;
import use_case.search.SearchOutputBoundary;
import use_case.search_event_by_name.SearchEventByNameDataAccessInterface;
import use_case.search_event_by_name.SearchEventByNameInputBoundary;
import use_case.search_event_by_name.SearchEventByNameInteractor;
import use_case.search_event_by_name.SearchEventByNameOutputBoundary;
import view.SearchBarView;
import view.SearchEventByNameView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Test application that demonstrates search bar -> event view flow
 */
public class SearchToEventViewDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Event Search Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 750);

            // Creates the cardLayout
            CardLayout cardLayout = new CardLayout();
            JPanel cardPanel = new JPanel(cardLayout);
            ViewManagerModel viewManagerModel = new ViewManagerModel();
            ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
            SearchEventByNameDataAccessInterface dataAccess = new SearchEventByNameDataAccessObject();
            SearchEventByNameViewModel searchEventViewModel = new SearchEventByNameViewModel();
            SearchEventByNameView eventView = new SearchEventByNameView(searchEventViewModel);

            SearchOutputBoundary searchPresenter = new SearchPresenter(
                    searchEventViewModel,
                    viewManagerModel
            );

            SearchInputBoundary searchInteractor = new SearchInteractor(
                    dataAccess,
                    searchPresenter
            );

            SearchController searchController = new SearchController(searchInteractor);

            SearchEventByNameOutputBoundary eventPresenter = new SearchEventByNamePresenter(
                    searchEventViewModel,
                    viewManagerModel
            );

            SearchEventByNameInputBoundary eventInteractor = new SearchEventByNameInteractor(
                    dataAccess,
                    eventPresenter
            );

            SearchEventByNameController eventController = new SearchEventByNameController(eventInteractor);
            eventView.setEventController(eventController);

            JPanel searchPanel = createSearchView(searchController);

            cardPanel.add(searchPanel, "search");
            cardPanel.add(eventView, eventView.getViewName());

            viewManagerModel.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals("state")) {
                        String viewName = (String) evt.getNewValue();
                        cardLayout.show(cardPanel, viewName);
                    }
                }
            });

            viewManagerModel.setState("search");
            viewManagerModel.firePropertyChange();

            frame.add(cardPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            System.out.println("Application started! YIPPEE");
        });
    }

    /**
     * Creates a simple search view with a search bar
     */
    private static JPanel createSearchView(SearchController searchController) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Event Search", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));

        // Search bar with FIXED dimensions
        SearchBarView searchBar = new SearchBarView("Search for events...", new Location("Milwaukee, WI", 43.0389, -87.9065));
        searchBar.setSearchController(searchController);
        searchBar.setMinimumSize(new Dimension(600, 60));
        searchBar.setMaximumSize(new Dimension(600, 60));
        searchBar.setPreferredSize(new Dimension(600, 60));

        // Center panel - use FlowLayout to respect fixed sizes
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(Color.WHITE);

        JPanel verticalWrapper = new JPanel();
        verticalWrapper.setLayout(new BoxLayout(verticalWrapper, BoxLayout.Y_AXIS));
        verticalWrapper.setBackground(Color.WHITE);
        verticalWrapper.add(searchBar);
        verticalWrapper.add(Box.createVerticalStrut(10));

        centerPanel.add(verticalWrapper);

        // Add components to main panel
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Implementation of the data access interface that uses EventDataAccessObject
     */
    private static class SearchEventByNameDataAccessObject implements SearchEventByNameDataAccessInterface {
        private final EventDataAccessObject eventDAO;

        public SearchEventByNameDataAccessObject() {
            this.eventDAO = new EventDataAccessObject();
        }

        @Override
        public List<Event> searchEventsByName(String keyword, Location location, double radiusKm) {
            return eventDAO.searchEventsByName(keyword, location, radiusKm);
        }
    }
}