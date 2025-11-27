package view;

import entity.Location;
import interface_adapter.ViewManagerModel;
import interface_adapter.search.SearchController;
import interface_adapter.display_local_events.DisplayLocalEventsController;
import interface_adapter.display_local_events.DisplayLocalEventsViewModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DisplayLocalEventsView extends JPanel implements PropertyChangeListener {

    private DisplayLocalEventsController controller;
    private final DisplayLocalEventsViewModel viewModel;
    private final String viewName = "display local events";

    private final JLabel appNameLabel = new JLabel("Dashboard");
    private final JComboBox<String> cityBox = new JComboBox<>(new String[]{"Toronto", "Montreal", "New York"});
    private final JButton searchButton = new JButton("Search");
    private final JComboBox<String> categoryBox = new JComboBox<>(new String[]{"ALL", "Music", "Sports", "Arts & Theatre", "Film"});
    private final JComboBox<String> sortBox = new JComboBox<>(new String[]{"Distance", "Date", "Name"});
    private SearchBarView searchBarView;
    private final JButton calendarButton = new JButton("Calendar");
    private final JButton logoutButton = new JButton("Logout");
    private final JButton savedEventsButton = new JButton("Saved Events");
    private final JPanel cardsContainer = new JPanel();
    private final JScrollPane cardsScrollPane;
    private final JLabel emptyStateLabel = new JLabel("Choose a location and click search to see local events.", SwingConstants.CENTER);
    private static final double DEFAULT_RADIUS_KM = 1000.0;
    private ViewManagerModel viewManagerModel;

    private boolean isUpdatingFromOtherView = false;

    public DisplayLocalEventsView(DisplayLocalEventsViewModel viewModel) {
        this.viewModel = viewModel;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(new Color(245, 247, 250));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildSideBar(), BorderLayout.WEST);

        cardsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        cardsContainer.setBackground(new Color(245, 247, 250));

        cardsScrollPane = new JScrollPane(cardsContainer);
        cardsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(cardsScrollPane, BorderLayout.CENTER);

        renderEmptyState();

        searchButton.addActionListener(e -> onSearch());

        sortBox.addActionListener(e -> renderEvents());
        calendarButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Calendar UI not implemented yet."));
        logoutButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Logout / Login UI not implemented yet."));
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    public String getViewName() {
        return viewName;
    }

    public void setController(DisplayLocalEventsController controller) {
        this.controller = controller;
    }

    /**
     * Enhanced propertyChange method to handle different types of property changes.
     * This method will be called when ANY view fires a property change event through the shared ViewModel.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();


        if (isUpdatingFromOtherView) {
            return;
        }

        try {
            isUpdatingFromOtherView = true;

            switch (propertyName) {
                case DisplayLocalEventsViewModel.PROPERTY_STATE:
                    renderEvents();
                    break;

                case DisplayLocalEventsViewModel.PROPERTY_EVENT_CARDS:
                    renderEvents();
                    break;

                case DisplayLocalEventsViewModel.PROPERTY_SEARCH_PARAMS:
                    handleSearchParamsUpdate();
                    break;

                case DisplayLocalEventsViewModel.PROPERTY_CATEGORY:
                    String newCategory = viewModel.getLastSearchCategory();
                    if (newCategory != null && !newCategory.isEmpty()) {
                        categoryBox.setSelectedItem(newCategory);
                    }
                    break;

                case DisplayLocalEventsViewModel.PROPERTY_LOCATION:
                    String newLocation = viewModel.getLastSearchLocation();
                    updateLocationUI(newLocation);
                    break;

                case DisplayLocalEventsViewModel.PROPERTY_ERROR:
                    renderEvents();
                    if (viewModel.hasError()) {
                        showErrorNotification(viewModel.getError());
                    }
                    break;

                case DisplayLocalEventsViewModel.PROPERTY_LOADING:
                    boolean isLoading = viewModel.isLoading();
                    updateLoadingState(isLoading);
                    break;

                case DisplayLocalEventsViewModel.PROPERTY_MESSAGE:
                    String message = viewModel.getMessage();
                    if (message != null && !message.isEmpty()) {
                        showInfoNotification(message);
                    }
                    break;

                default:
                    renderEvents();
                    break;
            }
        } finally {
            isUpdatingFromOtherView = false;
        }
    }

    /**
     * Handle search parameters update from other views
     */
    private void handleSearchParamsUpdate() {
        String location = viewModel.getLastSearchLocation();
        String category = viewModel.getLastSearchCategory();

        if (location != null && !location.isEmpty()) {
            updateLocationUI(location);
        }

        if (category != null && !category.isEmpty()) {
            categoryBox.setSelectedItem(category);
        }

    }

    /**
     * Update location UI based on location string
     */
    private void updateLocationUI(String location) {
        if (location == null) return;

        if (location.contains("Montreal")) {
            cityBox.setSelectedItem("Montreal");
        } else if (location.contains("New York")) {
            cityBox.setSelectedItem("New York");
        } else if (location.contains("Toronto")) {
            cityBox.setSelectedItem("Toronto");
        }
    }

    /**
     * Update UI to show loading state
     */
    private void updateLoadingState(boolean isLoading) {
        searchButton.setEnabled(!isLoading);
        categoryBox.setEnabled(!isLoading);
        sortBox.setEnabled(!isLoading);
        cityBox.setEnabled(!isLoading);

        if (isLoading) {
            cardsContainer.removeAll();
            cardsContainer.setLayout(new BorderLayout());
            JLabel loadingLabel = new JLabel("Loading events...", SwingConstants.CENTER);
            loadingLabel.setFont(loadingLabel.getFont().deriveFont(Font.PLAIN, 14f));
            loadingLabel.setForeground(new Color(100, 100, 100));
            cardsContainer.add(loadingLabel, BorderLayout.CENTER);
            cardsContainer.revalidate();
            cardsContainer.repaint();
        }
    }

    /**
     * Show error notification to user
     */
    private void showErrorNotification(String error) {
        System.err.println("Error in DisplayLocalEventsView: " + error);
        // You can replace this with a more sophisticated notification system
        // For example: show a toast notification or status bar message
    }

    /**
     * Show info notification to user
     */
    private void showInfoNotification(String message) {
        System.out.println("Info in DisplayLocalEventsView: " + message);
        // You can replace this with a more sophisticated notification system
    }

    /**
     * Public method to allow other views to trigger updates
     * Other views can call this to update this view
     */
    public void updateFromOtherView(String location, String category, double radius) {
        if (controller != null) {
            Location loc = parseLocation(location);
            controller.display(loc, radius, category);

            // Update the ViewModel which will trigger propertyChange
            viewModel.updateSearchParams(location, category, radius);
        }
    }

    /**
     * Helper method to parse location string into Location object
     */
    private Location parseLocation(String locationStr) {
        if (locationStr == null) {
            return getCurrentLocation();
        }

        if (locationStr.contains("Montreal")) {
            return new Location("Montreal, QC", 45.5019, -73.5674);
        } else if (locationStr.contains("New York")) {
            return new Location("New York, NY", 40.7128, -74.0060);
        } else {
            return new Location("Toronto, ON", 43.6532, -79.3832);
        }
    }

    /**
     * Method to connect this view with other views
     * Call this during app initialization
     */
    public void connectToOtherViews(PropertyChangeListener... otherViews) {
        for (PropertyChangeListener listener : otherViews) {
            viewModel.addPropertyChangeListener(listener);
        }
    }

    public void addViewListener(PropertyChangeListener listener) {
        this.viewModel.addPropertyChangeListener(listener);
    }

    public void updateFromLocation(Location location, double radiusKm) {
        if (controller != null && location != null) {
            String category = (String) categoryBox.getSelectedItem();
            controller.display(location, radiusKm, category);

            viewModel.updateSearchParams(
                    location.getAddress(),
                    category != null ? category : "ALL",
                    radiusKm
            );
        }
    }

    public void updateFromCategory(String category) {
        if (category != null) {
            categoryBox.setSelectedItem(category);
            onSearch();
        }
    }

    public Location getCurrentLocationForOthers() {
        return getCurrentLocation();
    }

    public DisplayLocalEventsViewModel getViewModel() {
        return this.viewModel;
    }

    private JPanel buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(new EmptyBorder(8, 12, 8, 12));
        topBar.setBackground(new Color(25, 118, 210));

        appNameLabel.setFont(appNameLabel.getFont().deriveFont(Font.BOLD, 18f));
        appNameLabel.setForeground(Color.WHITE);
        topBar.add(appNameLabel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        centerPanel.setOpaque(false);
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setForeground(Color.WHITE);
        centerPanel.add(locationLabel);
        cityBox.setPreferredSize(new Dimension(130, 24));
        centerPanel.add(cityBox);
        styleTopBarButton(searchButton);
        centerPanel.add(searchButton);
        topBar.add(centerPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setOpaque(false);
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setForeground(Color.WHITE);
        rightPanel.add(categoryLabel);
        categoryBox.setPreferredSize(new Dimension(140, 24));
        rightPanel.add(categoryBox);
        JLabel sortLabel = new JLabel("Sorting by:");
        sortLabel.setForeground(Color.WHITE);
        rightPanel.add(sortLabel);
        sortBox.setPreferredSize(new Dimension(120, 24));
        rightPanel.add(sortBox);
        JLabel nameSearchLabel = new JLabel("Search by name:");
        nameSearchLabel.setForeground(Color.WHITE);
        rightPanel.add(nameSearchLabel);
        Location defaultLocation = getCurrentLocation();
        searchBarView = new SearchBarView("Search events...", defaultLocation);
        searchBarView.setPreferredSize(new Dimension(200, 40));
        rightPanel.add(searchBarView);
        topBar.add(rightPanel, BorderLayout.EAST);

        return topBar;
    }

    private void styleTopBarButton(JButton button) {
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel buildSideBar() {
        JPanel sideBar = new JPanel();
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBorder(new EmptyBorder(10, 8, 10, 8));
        sideBar.setBackground(new Color(245, 247, 250));

        styleSideButton(calendarButton);
        styleSideButton(logoutButton);
        styleSideButton(savedEventsButton);

        sideBar.add(calendarButton);
        sideBar.add(Box.createVerticalStrut(10));
        sideBar.add(logoutButton);
        sideBar.add(Box.createVerticalStrut(10));
        sideBar.add(savedEventsButton);
        sideBar.add(Box.createVerticalGlue());

        return sideBar;
    }

    private void styleSideButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(140, 36));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void onSearch() {
        if (controller == null) {
            JOptionPane.showMessageDialog(this, "Controller not initialized.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedCity = (String) cityBox.getSelectedItem();
        if (selectedCity == null) {
            JOptionPane.showMessageDialog(this, "Please select a location.",
                    "Input required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Location userLoc;
        switch (selectedCity) {
            case "Montreal":
                userLoc = new Location("Montreal, QC", 45.5019, -73.5674);
                break;
            case "New York":
                userLoc = new Location("New York, NY", 40.7128, -74.0060);
                break;
            case "Toronto":
            default:
                userLoc = new Location("Toronto, ON", 43.6435, -79.3791);
                break;
        }

        String category = (String) categoryBox.getSelectedItem();

        // Set loading state
        viewModel.setLoading(true);

        controller.display(userLoc, DEFAULT_RADIUS_KM, category);

        viewModel.updateSearchParams(
                selectedCity,
                category != null ? category : "ALL",
                DEFAULT_RADIUS_KM
        );
    }

    private void renderEmptyState() {
        cardsContainer.removeAll();
        cardsContainer.setLayout(new BorderLayout());
        emptyStateLabel.setFont(emptyStateLabel.getFont().deriveFont(Font.PLAIN, 14f));
        emptyStateLabel.setForeground(new Color(120, 120, 120));
        cardsContainer.add(emptyStateLabel, BorderLayout.CENTER);
        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    public void renderEvents() {
        cardsContainer.removeAll();

        // Turn off loading state
        viewModel.setLoading(false);

        if (viewModel.hasError()) {
            cardsContainer.setLayout(new BorderLayout());
            JLabel errorLabel = new JLabel("Error: " + viewModel.getError(), SwingConstants.CENTER);
            errorLabel.setForeground(new Color(211, 47, 47));
            errorLabel.setFont(errorLabel.getFont().deriveFont(Font.BOLD, 13f));
            cardsContainer.add(errorLabel, BorderLayout.CENTER);
        } else if (!viewModel.hasEvents()) {
            renderEmptyState();
            return;
        } else {
            List<DisplayLocalEventsViewModel.EventCard> cards = new ArrayList<>(viewModel.getEventCards());
            String sortBy = (String) sortBox.getSelectedItem();

            if ("Name".equalsIgnoreCase(sortBy)) {
                cards.sort(Comparator.comparing(
                        DisplayLocalEventsViewModel.EventCard::getName,
                        String.CASE_INSENSITIVE_ORDER));
            } else if ("Date".equalsIgnoreCase(sortBy)) {
                cards.sort(Comparator.comparing(
                        DisplayLocalEventsViewModel.EventCard::getDateTime));
            } else {
                cards.sort(Comparator.comparingDouble(this::parseDistance));
            }

            cardsContainer.setLayout(new GridLayout(0, 2, 10, 10));
            for (DisplayLocalEventsViewModel.EventCard cardData : cards) {
                JPanel card = buildEventCard(cardData);
                cardsContainer.add(card);
            }
        }

        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    private Location getCurrentLocation() {
        String selectedCity = (String) cityBox.getSelectedItem();
        if (selectedCity == null) {
            return new Location("Toronto, ON", 43.6532, -79.3832);
        }
        switch (selectedCity) {
            case "Montreal":
                return new Location("Montreal, QC", 45.5019, -73.5674);
            case "New York":
                return new Location("New York, NY", 40.7128, -74.0060);
            case "Toronto":
            default:
                return new Location("Toronto, ON", 43.6532, -79.3832);
        }
    }

    private double parseDistance(DisplayLocalEventsViewModel.EventCard card) {
        String text = card.getDistanceText();
        if (text == null || text.isBlank()) {
            return Double.MAX_VALUE;
        }
        try {
            String firstToken = text.split("\\s+")[0];
            return Double.parseDouble(firstToken);
        } catch (Exception e) {
            return Double.MAX_VALUE;
        }
    }

    private JPanel buildEventCard(DisplayLocalEventsViewModel.EventCard cardData) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 4, 4, 4),
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1)
        ));
        card.setBackground(Color.WHITE);

        JLabel pictureLabel = new JLabel("", SwingConstants.CENTER);
        pictureLabel.setPreferredSize(new Dimension(200, 110));
        ImageIcon icon = loadImageIcon(cardData.getImageUrl(), 200, 110);
        if (icon != null) {
            pictureLabel.setIcon(icon);
        } else {
            pictureLabel.setText("No Image");
            pictureLabel.setForeground(new Color(150, 150, 150));
        }
        card.add(pictureLabel, BorderLayout.NORTH);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(cardData.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 13f));
        JLabel addressLabel = new JLabel(cardData.getAddress());
        addressLabel.setForeground(new Color(100, 100, 100));
        JLabel dateLabel = new JLabel(cardData.getDateTime());
        dateLabel.setForeground(new Color(120, 120, 120));
        JLabel distanceLabel = new JLabel(cardData.getDistanceText());
        distanceLabel.setForeground(new Color(33, 150, 243));

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(addressLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(dateLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(distanceLabel);

        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    private ImageIcon loadImageIcon(String imageUrl, int width, int height) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
        }
        try {
            BufferedImage img = ImageIO.read(new URL(imageUrl));
            if (img == null) {
                return null;
            }
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException e) {
            System.err.println("Failed to load image: " + e.getMessage());
            return null;
        }
    }

    public void setSearchBarController(SearchController controller) {
        this.searchBarView.setSearchController(controller);
    }
}