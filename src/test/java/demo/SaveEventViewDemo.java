package demo;

import data_access.FileSavedEventsDataAccessObject;
import entity.Event;
import entity.EventCategory;
import entity.Location;
import interface_adapter.ViewManagerModel;
import interface_adapter.save_event.SaveEventController;
import interface_adapter.save_event.SaveEventPresenter;
import interface_adapter.save_event.SaveEventViewModel;
import use_case.login.LoginUserDataAccessInterface;
import use_case.save_event.SaveEventInteractor;
import view.SaveButtonView;
import view.SaveEventsView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * Visual demo to test the SaveButtonView and SaveEventsView integration with file persistence
 * Run this to see the complete save event workflow with data persistence
 * WRITTEN BY GENAI for DEMO purposes only
 */
public class SaveEventViewDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main frame
            JFrame frame = new JFrame("Save Event Feature Demo (With Persistence!)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);

            // Create a mock user data access with a demo user
            MockUserDataAccess userDataAccess = new MockUserDataAccess();
            userDataAccess.setCurrentUsername("demo_user"); // Fallback user for demo

            // Create the file-based saved events DAO
            FileSavedEventsDataAccessObject savedEventsDAO = new FileSavedEventsDataAccessObject();

            // Set up the architecture components
            SaveEventViewModel saveEventViewModel = new SaveEventViewModel();
            ViewManagerModel viewManagerModel = new ViewManagerModel();
            SaveEventPresenter presenter = new SaveEventPresenter(saveEventViewModel, viewManagerModel);
            SaveEventInteractor interactor = new SaveEventInteractor(presenter, savedEventsDAO, userDataAccess);
            SaveEventController controller = new SaveEventController(interactor);

            // Create views
            SaveEventsView saveEventsView = new SaveEventsView(saveEventViewModel);
            saveEventsView.setSaveEventInteractor(interactor);
            saveEventsView.setSaveEventController(controller);

            // Create main panel with CardLayout for switching views
            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);

            // Create the event browsing panel
            JPanel browsePanel = createBrowsePanel(controller, cardLayout, mainPanel, saveEventsView);

            // Add panels to card layout
            mainPanel.add(browsePanel, "browse");
            mainPanel.add(saveEventsView, "saved");

            viewManagerModel.addPropertyChangeListener(evt -> {
                if ("state".equals(evt.getPropertyName())) {
                    cardLayout.show(mainPanel, "browse");
                }
            });

            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }

    private static JPanel createBrowsePanel(SaveEventController controller,
                                            CardLayout cardLayout,
                                            JPanel mainPanel,
                                            SaveEventsView saveEventsView) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(249, 250, 251));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Browse Events (demo_user)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(17, 24, 39));

        JButton viewSavedButton = new JButton("View My Saved Events →");
        viewSavedButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewSavedButton.setForeground(new Color(59, 130, 246));
        viewSavedButton.setBackground(Color.WHITE);
        viewSavedButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(59, 130, 246), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        viewSavedButton.setFocusPainted(false);
        viewSavedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        viewSavedButton.addActionListener(e -> {
            saveEventsView.refreshEventsList();
            cardLayout.show(mainPanel, "saved");
        });

        header.add(title, BorderLayout.WEST);
        header.add(viewSavedButton, BorderLayout.EAST);

        // Events container
        JPanel eventsContainer = new JPanel();
        eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));
        eventsContainer.setBackground(new Color(249, 250, 251));
        eventsContainer.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Add sample events
        eventsContainer.add(createEventCard(createSampleEvent1(), controller));
        eventsContainer.add(Box.createVerticalStrut(20));
//        eventsContainer.add(createEventCard(createSampleEvent2(), controller));
//        eventsContainer.add(Box.createVerticalStrut(20));
//        eventsContainer.add(createEventCard(createSampleEvent3(), controller));
//        eventsContainer.add(Box.createVerticalStrut(20));
//        eventsContainer.add(createEventCard(createSampleEvent4(), controller));
//        eventsContainer.add(Box.createVerticalStrut(20));
//        eventsContainer.add(createEventCard(createSampleEvent5(), controller));

        JScrollPane scrollPane = new JScrollPane(eventsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel createEventCard(Event event, SaveEventController controller) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        // Image placeholder
        JPanel imagePanel = createImagePanel(event.getCategory());
        imagePanel.setPreferredSize(new Dimension(140, 140));

        // Details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);

        // Category badge
        JLabel categoryLabel = new JLabel(event.getCategory().toString());
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        Color categoryColor = getCategoryColor(event.getCategory());
        categoryLabel.setForeground(categoryColor);
        categoryLabel.setOpaque(true);
        categoryLabel.setBackground(new Color(categoryColor.getRed(),
                categoryColor.getGreen(), categoryColor.getBlue(), 40));
        categoryLabel.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Event name
        JLabel nameLabel = new JLabel(event.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nameLabel.setForeground(new Color(17, 24, 39));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Description
        JLabel descLabel = new JLabel("<html>" + truncateText(event.getDescription(), 100) + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(107, 114, 128));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Date
        JLabel dateLabel = new JLabel("📅 " + formatDate(event.getStartTime()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateLabel.setForeground(new Color(107, 114, 128));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Location
        JLabel locationLabel = new JLabel("📍 " + event.getLocation().getAddress());
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        locationLabel.setForeground(new Color(107, 114, 128));
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        detailsPanel.add(categoryLabel);
        detailsPanel.add(Box.createVerticalStrut(10));
        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createVerticalStrut(8));
        detailsPanel.add(descLabel);
        detailsPanel.add(Box.createVerticalStrut(10));
        detailsPanel.add(dateLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(locationLabel);

        // Save button
        SaveButtonView saveButtonView = new SaveButtonView();
        saveButtonView.setSaveEventController(controller);
        saveButtonView.setEvent(event);
        saveButtonView.setOpaque(false);

        card.add(imagePanel, BorderLayout.WEST);
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(saveButtonView, BorderLayout.EAST);

        return card;
    }

    private static JPanel createImagePanel(EventCategory category) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color categoryColor = getCategoryColor(category);
                GradientPaint gradient = new GradientPaint(
                        0, 0, categoryColor.darker(),
                        getWidth(), getHeight(), categoryColor
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Draw icon
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 48));
                String icon = getIconForCategory(category);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(icon)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 5;
                g2.drawString(icon, x, y);
            }
        };
    }

    private static String getIconForCategory(EventCategory category) {
        switch (category) {
            case MUSIC: return "♪";
            case SPORTS: return "⚽";
            case ARTS_THEATRE: return "🎭";
            case FILM: return "🎬";
            default: return "★";
        }
    }

    private static Color getCategoryColor(EventCategory category) {
        switch (category) {
            case MUSIC:
                return new Color(147, 51, 234); // Purple
            case SPORTS:
                return new Color(59, 130, 246); // Blue
            case ARTS_THEATRE:
                return new Color(236, 72, 153); // Pink
            case FILM:
                return new Color(245, 158, 11); // Orange
            default:
                return new Color(107, 114, 128); // Gray
        }
    }

    private static String truncateText(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }

    private static String formatDate(LocalDateTime dateTime) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[dateTime.getMonthValue() - 1] + " " +
                dateTime.getDayOfMonth() + ", " +
                dateTime.getYear() + " at " +
                String.format("%02d:%02d", dateTime.getHour(), dateTime.getMinute());
    }

    // Sample Events
    private static Event createSampleEvent1() {
        Location location = new Location("Scotiabank Arena, 40 Bay St, Toronto, ON", 43.6435, -79.3791);
        return new Event(
                "event-1",
                "Toronto Raptors vs Los Angeles Lakers",
                "An epic showdown between two NBA powerhouses. Watch the Raptors take on LeBron James and the Lakers in what promises to be an unforgettable night of basketball action!",
                location.getAddress(),
                EventCategory.SPORTS,
                location,
                LocalDateTime.of(2025, 12, 15, 19, 30),
        "https://via.placeholder.com/500x700"
        );
    }

    // Mock User Data Access for demo purposes
    private static class MockUserDataAccess implements LoginUserDataAccessInterface {
        private String currentUsername;

        @Override
        public boolean existsByName(String identifier) {
            return "demo_user".equals(identifier);
        }

        @Override
        public void save(entity.User user) {
            // Not needed for demo
        }

        @Override
        public entity.User get(String username) {
            return null; // Not needed for demo
        }

        @Override
        public void setCurrentUsername(String name) {
            this.currentUsername = name;
        }

        @Override
        public String getCurrentUsername() {
            return currentUsername;
        }
    }
}