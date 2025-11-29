package view;

import entity.Event;
import interface_adapter.ViewManagerModel;
import interface_adapter.save_event.SaveEventController;
import interface_adapter.save_event.SaveEventViewModel;
import use_case.save_event.SaveEventInteractor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * View for displaying saved events.
 * Shows all events the user has saved and allows them to remove events.
 */
public class SaveEventsView extends JPanel implements PropertyChangeListener {

    private final String viewName = "save event";
    private final SaveEventViewModel viewModel;
    private ViewManagerModel viewManagerModel;
    private SaveEventInteractor saveEventInteractor;
    private SaveEventController saveEventController;

    private final JPanel eventsContainer = new JPanel();
    private final JButton backButton = new JButton("← Back to Events");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");

    public SaveEventsView(SaveEventViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // Header
        add(createHeader(), BorderLayout.NORTH);

        // Events container with scroll
        eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));
        eventsContainer.setBackground(new Color(245, 247, 250));
        eventsContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(eventsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);  // No horizontal scroll
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  // Vertical only when needed
        add(scrollPane, BorderLayout.CENTER);

        // Initial render
        renderSavedEvents();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 118, 210));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Back button
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(25, 118, 210));
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(true);
        backButton.setContentAreaFilled(false);

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                backButton.setContentAreaFilled(true);
                backButton.setBackground(new Color(21, 101, 192));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                backButton.setContentAreaFilled(false);
            }
        });

        backButton.addActionListener(e -> navigateBack());
        header.add(backButton, BorderLayout.WEST);

        // Title
        JLabel title = new JLabel("Your Saved Events");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(title, BorderLayout.CENTER);

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(25, 118, 210));
        refreshButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setContentAreaFilled(false);
        refreshButton.addActionListener(e -> renderSavedEvents());
        header.add(refreshButton, BorderLayout.EAST);

        return header;
    }

    private void navigateBack() {
        if (viewManagerModel != null) {
            viewManagerModel.setState("display local events");
            viewManagerModel.firePropertyChange();
        }
    }

    /**
     * Render the saved events list.
     * Called on initial load and when events change.
     */
    public void renderSavedEvents() {
        eventsContainer.removeAll();

        List<Event> savedEvents = null;
        if (saveEventInteractor != null) {
            try {
                savedEvents = saveEventInteractor.getSavedEvents();
            } catch (Exception e) {
                System.err.println("Error getting saved events: " + e.getMessage());
            }
        }

        if (savedEvents == null || savedEvents.isEmpty()) {
            renderEmptyState();
        } else {
            // Reset to vertical BoxLayout for event cards
            eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));

            // Add count label
            JLabel countLabel = new JLabel("You have " + savedEvents.size() + " saved event" +
                    (savedEvents.size() == 1 ? "" : "s"));
            countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            countLabel.setForeground(new Color(100, 100, 100));
            countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            countLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
            eventsContainer.add(countLabel);

            // Add event cards vertically
            for (Event event : savedEvents) {
                JPanel card = createEventCard(event);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                eventsContainer.add(card);
                eventsContainer.add(Box.createVerticalStrut(12));
            }

            // Add glue at the end to push everything to the top
            eventsContainer.add(Box.createVerticalGlue());
        }

        eventsContainer.revalidate();
        eventsContainer.repaint();
    }

    private void renderEmptyState() {
        eventsContainer.setLayout(new GridBagLayout());

        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setBackground(new Color(245, 247, 250));
        emptyPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        // Title
        JLabel titleLabel = new JLabel("No Saved Events Yet");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 0, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Description
        JLabel descLabel = new JLabel("<html><div style='text-align: center;'>Events you save will appear here.<br>Click 'Save Event' on any event to add it!</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(120, 120, 120));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Back to events button
        JButton browseButton = new JButton("Browse Events");
        browseButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        browseButton.setForeground(Color.WHITE);
        browseButton.setBackground(new Color(0, 0, 0));
        browseButton.setBorder(new EmptyBorder(12, 24, 12, 24));
        browseButton.setFocusPainted(false);
        browseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        browseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        browseButton.addActionListener(e -> navigateBack());

        emptyPanel.add(Box.createVerticalStrut(20));
        emptyPanel.add(titleLabel);
        emptyPanel.add(Box.createVerticalStrut(12));
        emptyPanel.add(descLabel);
        emptyPanel.add(Box.createVerticalStrut(25));
        emptyPanel.add(browseButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        eventsContainer.add(emptyPanel, gbc);
    }

    private JPanel createEventCard(Event event) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Left side - Event info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        // Event name
        JLabel nameLabel = new JLabel(event.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(30, 30, 30));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Category badge
        JLabel categoryLabel = new JLabel(event.getCategory().getDisplayName());
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        categoryLabel.setForeground(new Color(59, 130, 246));
        categoryLabel.setOpaque(true);
        categoryLabel.setBackground(new Color(219, 234, 254));
        categoryLabel.setBorder(new EmptyBorder(2, 8, 2, 8));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Location
        String address = event.getLocation() != null ? event.getLocation().getAddress() : "Location not available";
        JLabel locationLabel = new JLabel("📍 " + address);
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        locationLabel.setForeground(new Color(100, 100, 100));
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Date/Time
        String dateTime = event.getStartTime() != null ?
                event.getStartTime().format(dateFormatter) : "Date not available";
        JLabel dateLabel = new JLabel("📅 " + dateTime);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(categoryLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(locationLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(dateLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // Right side - Remove button
        JButton removeButton = new JButton("Remove");
        removeButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        removeButton.setForeground(new Color(220, 53, 69));
        removeButton.setBackground(Color.WHITE);
        removeButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 53, 69)),
                new EmptyBorder(8, 12, 8, 12)
        ));
        removeButton.setFocusPainted(false);
        removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        removeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                removeButton.setBackground(new Color(255, 240, 240));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                removeButton.setBackground(Color.WHITE);
            }
        });

        removeButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Remove \"" + event.getName() + "\" from saved events?",
                    "Confirm Remove",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (saveEventInteractor != null) {
                    try {
                        saveEventInteractor.unsaveEvent(event);
                        renderSavedEvents();  // Refresh the list
                        JOptionPane.showMessageDialog(this,
                                "Event removed from saved events.",
                                "Removed", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                                "Could not remove event: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(removeButton);
        card.add(buttonPanel, BorderLayout.EAST);

        // Add hover effect to card
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(250, 250, 250));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(59, 130, 246)),
                        new EmptyBorder(15, 15, 15, 15)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 230, 230)),
                        new EmptyBorder(15, 15, 15, 15)
                ));
            }
        });

        return card;
    }

    // Setters for dependency injection
    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    public void setSaveEventInteractor(SaveEventInteractor interactor) {
        this.saveEventInteractor = interactor;
    }

    public void setSaveEventController(SaveEventController controller) {
        this.saveEventController = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Refresh when view model changes
        renderSavedEvents();
    }

    public String getViewName() {
        return viewName;
    }
}