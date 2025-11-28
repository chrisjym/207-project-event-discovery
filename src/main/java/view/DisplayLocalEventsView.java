package view;

import entity.Event;
import entity.Location;
import interface_adapter.ViewManagerModel;
import interface_adapter.search.SearchController;
import interface_adapter.display_local_events.DisplayLocalEventsController;
import interface_adapter.display_local_events.DisplayLocalEventsViewModel;
import interface_adapter.update_location.UpdateLocationController;
import interface_adapter.update_location.UpdateLocationViewModel;
import interface_adapter.update_location.UpdateLocationState;

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

/**
 * View for displaying local events with user location support.
 * Improved layout with two-row top bar.
 */
public class DisplayLocalEventsView extends JPanel implements PropertyChangeListener {

    private DisplayLocalEventsController controller;
    private final DisplayLocalEventsViewModel viewModel;
    private final String viewName = "display local events";

    // Location components
    private UpdateLocationController updateLocationController;
    private UpdateLocationViewModel updateLocationViewModel;
    private JTextField addressField;
    private JButton setLocationButton;
    private JLabel currentLocationLabel;
    private JButton searchEventsButton;

    private final JLabel appNameLabel = new JLabel("Event Gate");
    private final JComboBox<String> categoryBox = new JComboBox<>(new String[]{"ALL", "Music", "Sports", "Arts & Theatre", "Film"});
    private final JComboBox<String> sortBox = new JComboBox<>(new String[]{"Distance", "Date", "Name"});
    private SearchBarView searchBarView;
    private final JButton calendarButton = new JButton("Calendar");
    private final JButton logoutButton = new JButton("Logout");
    private final JButton savedEventsButton = new JButton("Saved Events");
    private final JPanel cardsContainer = new JPanel();
    private final JScrollPane cardsScrollPane;
    private final JLabel emptyStateLabel = new JLabel("Set your location and click 'Search Events' to see local events.", SwingConstants.CENTER);
    private static final double DEFAULT_RADIUS_KM = 50.0;
    private ViewManagerModel viewManagerModel;

    // User's saved location
    private Location userLocation = null;

    // Event selection listener for navigation
    private EventSelectionListener eventSelectionListener;

    public interface EventSelectionListener {
        void onEventSelected(Event event);
    }

    public DisplayLocalEventsView(DisplayLocalEventsViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setBackground(new Color(245, 247, 250));

        // Two-row top bar
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildSideBar(), BorderLayout.WEST);

        cardsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        cardsContainer.setBackground(new Color(245, 247, 250));

        cardsScrollPane = new JScrollPane(cardsContainer);
        cardsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(cardsScrollPane, BorderLayout.CENTER);

        renderEmptyState();

        calendarButton.addActionListener(e -> navigateToCalendar());
        logoutButton.addActionListener(e -> handleLogout());
        savedEventsButton.addActionListener(e -> navigateToSavedEvents());
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

    public void setUpdateLocationController(UpdateLocationController controller) {
        this.updateLocationController = controller;
    }

    public void setUpdateLocationViewModel(UpdateLocationViewModel viewModel) {
        this.updateLocationViewModel = viewModel;
        this.updateLocationViewModel.addPropertyChangeListener(this);
    }

    public void setEventSelectionListener(EventSelectionListener listener) {
        this.eventSelectionListener = listener;
    }

    public void setUserLocation(Location location, String address) {
        this.userLocation = location;
        if (location != null && currentLocationLabel != null) {
            // Shorten the address for display
            String displayAddress = shortenAddress(address);
            currentLocationLabel.setText("📍 " + displayAddress);
            currentLocationLabel.setForeground(new Color(34, 197, 94));
        }
    }

    /**
     * Shorten a long address for display.
     */
    private String shortenAddress(String address) {
        if (address == null) return "Location set";
        if (address.length() <= 40) return address;

        // Try to get just the important parts
        String[] parts = address.split(",");
        if (parts.length >= 2) {
            return parts[0].trim() + ", " + parts[1].trim();
        }
        return address.substring(0, 37) + "...";
    }

    public Location getCurrentLocationForOthers() {
        return userLocation;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            if (updateLocationViewModel != null && evt.getSource() == updateLocationViewModel) {
                handleLocationUpdate();
            } else {
                renderEvents();
            }
        }
    }

    private void handleLocationUpdate() {
        UpdateLocationState state = updateLocationViewModel.getState();
        if (state.isSuccess()) {
            userLocation = state.getLocation();
            String displayAddress = shortenAddress(state.getAddress());
            currentLocationLabel.setText("📍 " + displayAddress);
            currentLocationLabel.setForeground(new Color(34, 197, 94));
            addressField.setText("");

            // Show success briefly
            JOptionPane.showMessageDialog(this,
                    "Location saved!\nYou can now search for events near you.",
                    "Location Set", JOptionPane.INFORMATION_MESSAGE);
        } else if (state.getError() != null) {
            JOptionPane.showMessageDialog(this,
                    state.getError(),
                    "Location Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Build a two-row top bar for better layout.
     */
    private JPanel buildTopBar() {
        JPanel topBarContainer = new JPanel();
        topBarContainer.setLayout(new BoxLayout(topBarContainer, BoxLayout.Y_AXIS));

        // === ROW 1: App name + Location input ===
        JPanel row1 = new JPanel(new BorderLayout());
        row1.setBorder(new EmptyBorder(10, 15, 5, 15));
        row1.setBackground(new Color(25, 118, 210));

        // Left: App name
        appNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        appNameLabel.setForeground(Color.WHITE);
        row1.add(appNameLabel, BorderLayout.WEST);

        // Center: Location input panel
        JPanel locationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        locationPanel.setOpaque(false);

        JLabel locationLabel = new JLabel("📍 Your Location:");
        locationLabel.setForeground(Color.WHITE);
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        locationPanel.add(locationLabel);

        addressField = new JTextField(25);
        addressField.setPreferredSize(new Dimension(250, 30));
        addressField.setToolTipText("Enter any address, intersection, or landmark (e.g., 'Bloor and Spadina', 'CN Tower', '123 Main St Toronto')");
        addressField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        // Add placeholder text behavior
        addressField.setText("Enter address, intersection, or landmark...");
        addressField.setForeground(Color.GRAY);
        addressField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (addressField.getText().equals("Enter address, intersection, or landmark...")) {
                    addressField.setText("");
                    addressField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (addressField.getText().isEmpty()) {
                    addressField.setText("Enter address, intersection, or landmark...");
                    addressField.setForeground(Color.GRAY);
                }
            }
        });
        locationPanel.add(addressField);

        setLocationButton = new JButton("Set Location");
        setLocationButton.addActionListener(e -> onSetLocation());
        styleButton(setLocationButton, new Color(76, 175, 80), Color.WHITE);
        locationPanel.add(setLocationButton);

        row1.add(locationPanel, BorderLayout.CENTER);

        // Right: Current location display
        currentLocationLabel = new JLabel("No location set");
        currentLocationLabel.setForeground(new Color(255, 200, 200));
        currentLocationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        row1.add(currentLocationLabel, BorderLayout.EAST);

        // === ROW 2: Filters + Search ===
        JPanel row2 = new JPanel(new BorderLayout());
        row2.setBorder(new EmptyBorder(5, 15, 10, 15));
        row2.setBackground(new Color(30, 136, 229)); // Slightly lighter blue

        // Left: Search events button
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);

        searchEventsButton = new JButton("🔍 Search Events Near Me");
        searchEventsButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchEventsButton.addActionListener(e -> onSearch());
        styleButton(searchEventsButton, Color.WHITE, new Color(25, 118, 210));
        searchEventsButton.setPreferredSize(new Dimension(200, 35));
        searchPanel.add(searchEventsButton);

        row2.add(searchPanel, BorderLayout.WEST);

        // Center: Category and Sort filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        filterPanel.setOpaque(false);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setForeground(Color.WHITE);
        filterPanel.add(categoryLabel);
        categoryBox.setPreferredSize(new Dimension(140, 28));
        filterPanel.add(categoryBox);

        JLabel sortLabel = new JLabel("Sort by:");
        sortLabel.setForeground(Color.WHITE);
        filterPanel.add(sortLabel);
        sortBox.setPreferredSize(new Dimension(100, 28));
        sortBox.addActionListener(e -> renderEvents());
        filterPanel.add(sortBox);

        row2.add(filterPanel, BorderLayout.CENTER);

        // Right: Search by name
        JPanel nameSearchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        nameSearchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Search by name:");
        searchLabel.setForeground(Color.WHITE);
        nameSearchPanel.add(searchLabel);

        Location defaultLoc = userLocation != null ? userLocation : new Location("Toronto", 43.6532, -79.3832);
        searchBarView = new SearchBarView("Event name...", defaultLoc);
        searchBarView.setPreferredSize(new Dimension(180, 32));
        nameSearchPanel.add(searchBarView);

        row2.add(nameSearchPanel, BorderLayout.EAST);

        topBarContainer.add(row1);
        topBarContainer.add(row2);

        return topBarContainer;
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private JPanel buildSideBar() {
        JPanel sideBar = new JPanel();
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBorder(new EmptyBorder(15, 10, 15, 10));
        sideBar.setBackground(new Color(250, 250, 250));
        sideBar.setPreferredSize(new Dimension(130, 0));

        styleSideButton(calendarButton, "📅 Calendar");
        styleSideButton(savedEventsButton, "❤ Saved");
        styleSideButton(logoutButton, "🚪 Logout");

        sideBar.add(calendarButton);
        sideBar.add(Box.createVerticalStrut(8));
        sideBar.add(savedEventsButton);
        sideBar.add(Box.createVerticalStrut(8));
        sideBar.add(logoutButton);
        sideBar.add(Box.createVerticalGlue());

        // Add help text at bottom
        JLabel helpLabel = new JLabel("<html><center><small>Tip: Set your location first, then search!</small></center></html>");
        helpLabel.setForeground(new Color(150, 150, 150));
        helpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sideBar.add(helpLabel);

        return sideBar;
    }

    private void styleSideButton(JButton button, String text) {
        button.setText(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(120, 40));
        button.setPreferredSize(new Dimension(120, 40));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(240, 240, 240));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });
    }

    private void onSetLocation() {
        String address = addressField.getText().trim();

        // Check for placeholder text
        if (address.isEmpty() || address.equals("Enter address, intersection, or landmark...")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your location.\n\nYou can enter:\n• A full address (123 Main St, Toronto)\n• An intersection (Bloor and Spadina)\n• A landmark (CN Tower)\n• Just a city (Toronto, ON)",
                    "Address Required", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (updateLocationController != null) {
            setLocationButton.setEnabled(false);
            setLocationButton.setText("Finding...");

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    updateLocationController.execute(address);
                    return null;
                }

                @Override
                protected void done() {
                    setLocationButton.setEnabled(true);
                    setLocationButton.setText("Set Location");
                }
            };
            worker.execute();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Location service not available. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSearch() {
        if (controller == null) {
            JOptionPane.showMessageDialog(this, "Search not available.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userLocation == null) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "You haven't set your location yet.\n\nWould you like to use Toronto as your default location?",
                    "No Location Set", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                userLocation = new Location("Toronto, ON", 43.6532, -79.3832);
                currentLocationLabel.setText("📍 Toronto, ON (default)");
                currentLocationLabel.setForeground(new Color(255, 193, 7)); // Yellow/warning
            } else {
                return;
            }
        }

        String category = (String) categoryBox.getSelectedItem();

        // Show loading state
        searchEventsButton.setText("Searching...");
        searchEventsButton.setEnabled(false);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                controller.display(userLocation, DEFAULT_RADIUS_KM, category);
                return null;
            }

            @Override
            protected void done() {
                searchEventsButton.setText("🔍 Search Events Near Me");
                searchEventsButton.setEnabled(true);
            }
        };
        worker.execute();
    }

    private void renderEmptyState() {
        cardsContainer.removeAll();
        cardsContainer.setLayout(new BorderLayout());

        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setBackground(new Color(245, 247, 250));
        emptyPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JLabel iconLabel = new JLabel("🎫", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Find Events Near You");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><center>Enter your location above and click<br>'Search Events Near Me' to discover local events!</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emptyPanel.add(Box.createVerticalGlue());
        emptyPanel.add(iconLabel);
        emptyPanel.add(Box.createVerticalStrut(20));
        emptyPanel.add(titleLabel);
        emptyPanel.add(Box.createVerticalStrut(10));
        emptyPanel.add(descLabel);
        emptyPanel.add(Box.createVerticalGlue());

        cardsContainer.add(emptyPanel, BorderLayout.CENTER);
        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    public void renderEvents() {
        cardsContainer.removeAll();

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

            cardsContainer.setLayout(new GridLayout(0, 2, 12, 12));
            for (DisplayLocalEventsViewModel.EventCard cardData : cards) {
                JPanel card = buildEventCard(cardData);
                cardsContainer.add(card);
            }
        }

        cardsContainer.revalidate();
        cardsContainer.repaint();
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
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onEventCardClicked(cardData.getId());
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(248, 250, 252));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(4, 4, 4, 4),
                        BorderFactory.createLineBorder(new Color(25, 118, 210), 2)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(4, 4, 4, 4),
                        BorderFactory.createLineBorder(new Color(230, 230, 230), 1)
                ));
            }
        });

        JLabel pictureLabel = new JLabel("", SwingConstants.CENTER);
        pictureLabel.setPreferredSize(new Dimension(200, 120));
        pictureLabel.setBackground(new Color(240, 240, 240));
        pictureLabel.setOpaque(true);
        ImageIcon icon = loadImageIcon(cardData.getImageUrl(), 200, 120);
        if (icon != null) {
            pictureLabel.setIcon(icon);
        } else {
            pictureLabel.setText("🎫");
            pictureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
            pictureLabel.setForeground(new Color(180, 180, 180));
        }
        card.add(pictureLabel, BorderLayout.NORTH);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(new EmptyBorder(10, 12, 10, 12));
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("<html><b>" + cardData.getName() + "</b></html>");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel addressLabel = new JLabel(cardData.getAddress());
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        addressLabel.setForeground(new Color(100, 100, 100));

        JLabel dateLabel = new JLabel("📅 " + cardData.getDateTime());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dateLabel.setForeground(new Color(120, 120, 120));

        JLabel distanceLabel = new JLabel("📍 " + cardData.getDistanceText() + " away");
        distanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        distanceLabel.setForeground(new Color(25, 118, 210));

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(addressLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(dateLabel);
        textPanel.add(Box.createVerticalStrut(6));
        textPanel.add(distanceLabel);

        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    private void onEventCardClicked(String eventId) {
        List<Event> events = viewModel.getEvents();

        Event selectedEvent = null;
        if (events != null) {
            for (Event event : events) {
                if (event.getId().equals(eventId)) {
                    selectedEvent = event;
                    break;
                }
            }
        }

        if (selectedEvent != null && eventSelectionListener != null) {
            eventSelectionListener.onEventSelected(selectedEvent);
        }
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
            return null;
        }
    }

    public void setSearchBarController(SearchController controller) {
        if (searchBarView != null) {
            this.searchBarView.setSearchController(controller);
        }
    }

    private void navigateToCalendar() {
        if (viewManagerModel != null) {
            viewManagerModel.setState("calendar view");
            viewManagerModel.firePropertyChange();
        }
    }

    private void navigateToSavedEvents() {
        if (viewManagerModel != null) {
            viewManagerModel.setState("save event");
            viewManagerModel.firePropertyChange();
        }
    }

    private void handleLogout() {
        userLocation = null;
        if (currentLocationLabel != null) {
            currentLocationLabel.setText("No location set");
            currentLocationLabel.setForeground(new Color(255, 200, 200));
        }
        if (viewManagerModel != null) {
            viewManagerModel.setState("log in");
            viewManagerModel.firePropertyChange();
        }
    }

    public DisplayLocalEventsViewModel getViewModel() {
        return this.viewModel;
    }
}