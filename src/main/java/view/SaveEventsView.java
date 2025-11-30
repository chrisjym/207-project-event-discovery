package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import entity.Event;
import interface_adapter.ViewManagerModel;
import interface_adapter.save_event.SaveEventController;
import interface_adapter.save_event.SaveEventViewModel;
import use_case.save_event.SaveEventInteractor;

/**
 * View for displaying saved events.
 * Shows all events the user has saved and allows them to remove events.
 * Automatically refreshes when the view becomes visible.
 */
public class SaveEventsView extends JPanel implements PropertyChangeListener {

    private static final int HEADER_PADDING = 15;
    private static final int HEADER_BUTTON_PADDING = 8;
    private static final int CONTAINER_PADDING = 20;
    private static final int SCROLL_UNIT = 16;
    private static final int HEADER_FONT_SIZE = 20;
    private static final int BUTTON_FONT_SIZE = 14;
    private static final int SMALL_BUTTON_FONT_SIZE = 12;
    private static final int EMPTY_ICON_SIZE = 48;
    private static final int EMPTY_TITLE_SIZE = 24;
    private static final int COUNT_FONT_SIZE = 14;
    private static final int CARD_FONT_SIZE = 16;
    private static final int BADGE_FONT_SIZE = 10;
    private static final int CARD_SPACING = 12;
    private static final int CARD_PADDING = 15;
    private static final int MAX_CARD_HEIGHT = 120;
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_ADDRESS_LENGTH = 60;

    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color HEADER_COLOR = new Color(25, 118, 210);
    private static final Color HEADER_HOVER = new Color(21, 101, 192);
    private static final Color PRIMARY_BLUE = new Color(59, 130, 246);
    private static final Color BADGE_BLUE = new Color(219, 234, 254);
    private static final Color CARD_BORDER = new Color(230, 230, 230);
    private static final Color CARD_HOVER = new Color(250, 250, 250);
    private static final Color ERROR_RED = new Color(220, 53, 69);
    private static final Color ERROR_BG = new Color(255, 240, 240);
    private static final Color TEXT_DARK = new Color(30, 30, 30);
    private static final Color TEXT_GRAY = new Color(100, 100, 100);
    private static final Color TEXT_LIGHT_GRAY = new Color(120, 120, 120);
    private static final Color TEXT_MEDIUM_GRAY = new Color(150, 150, 150);
    private static final Color TEXT_DARK_GRAY = new Color(50, 50, 50);

    private final String viewName = "save event";
    private final SaveEventViewModel viewModel;
    private ViewManagerModel viewManagerModel;
    private SaveEventInteractor saveEventInteractor;
    private SaveEventController saveEventController;

    private final JPanel eventsContainer = new JPanel();
    private final JButton backButton = new JButton("<- Back to Events");
    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");

    public SaveEventsView(SaveEventViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        add(createHeader(), BorderLayout.NORTH);

        eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));
        eventsContainer.setBackground(BACKGROUND_COLOR);
        eventsContainer.setBorder(new EmptyBorder(CONTAINER_PADDING, CONTAINER_PADDING,
                CONTAINER_PADDING, CONTAINER_PADDING));

        final JScrollPane scrollPane = new JScrollPane(eventsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                renderSavedEvents();
            }
        });

        renderSavedEvents();
    }

    private JPanel createHeader() {
        final JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setBorder(new EmptyBorder(HEADER_PADDING, CONTAINER_PADDING,
                HEADER_PADDING, CONTAINER_PADDING));

        backButton.setFont(new Font("Segoe UI", Font.PLAIN, BUTTON_FONT_SIZE));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(HEADER_COLOR);
        backButton.setBorder(BorderFactory.createEmptyBorder(HEADER_BUTTON_PADDING,
                HEADER_PADDING, HEADER_BUTTON_PADDING, HEADER_PADDING));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(true);
        backButton.setContentAreaFilled(false);

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setContentAreaFilled(true);
                backButton.setBackground(HEADER_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setContentAreaFilled(false);
            }
        });

        backButton.addActionListener(evt -> navigateBack());
        header.add(backButton, BorderLayout.WEST);

        final JLabel title = new JLabel("Your Saved Events");
        title.setFont(new Font("Segoe UI", Font.BOLD, HEADER_FONT_SIZE));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(title, BorderLayout.CENTER);

        final JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, SMALL_BUTTON_FONT_SIZE));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(HEADER_COLOR);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(HEADER_BUTTON_PADDING,
                HEADER_PADDING, HEADER_BUTTON_PADDING, HEADER_PADDING));
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setContentAreaFilled(false);
        refreshButton.addActionListener(evt -> renderSavedEvents());
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
     * Called on initial load, when view becomes visible, and when events change.
     */
    public void renderSavedEvents() {
        eventsContainer.removeAll();

        List<Event> savedEvents = null;
        if (saveEventInteractor != null) {
            savedEvents = saveEventInteractor.getSavedEvents();
            System.out.println("Loaded " + (savedEvents != null ? savedEvents.size() : 0)
                    + " saved events");
        }

        if (savedEvents == null || savedEvents.isEmpty()) {
            renderEmptyState();
        }
        else {
            eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));

            final JLabel countLabel = new JLabel("You have " + savedEvents.size() + " saved event"
                    + (savedEvents.size() == 1 ? "" : "s"));
            countLabel.setFont(new Font("Segoe UI", Font.PLAIN, COUNT_FONT_SIZE));
            countLabel.setForeground(TEXT_GRAY);
            countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            countLabel.setBorder(new EmptyBorder(0, 0, HEADER_PADDING, 0));
            eventsContainer.add(countLabel);

            for (Event event : savedEvents) {
                final JPanel card = createEventCard(event);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                eventsContainer.add(card);
                eventsContainer.add(Box.createVerticalStrut(CARD_SPACING));
            }

            eventsContainer.add(Box.createVerticalGlue());
        }

        eventsContainer.revalidate();
        eventsContainer.repaint();
    }

    private void renderEmptyState() {
        eventsContainer.setLayout(new GridBagLayout());

        final JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setBackground(BACKGROUND_COLOR);
        emptyPanel.setBorder(new EmptyBorder(MAX_NAME_LENGTH, MAX_NAME_LENGTH,
                MAX_NAME_LENGTH, MAX_NAME_LENGTH));

        final JLabel iconLabel = new JLabel("📅", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, EMPTY_ICON_SIZE));
        iconLabel.setForeground(TEXT_MEDIUM_GRAY);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel titleLabel = new JLabel("No Saved Events Yet");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, EMPTY_TITLE_SIZE));
        titleLabel.setForeground(TEXT_DARK_GRAY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel descLabel = new JLabel("<html><div style='text-align: center;'>"
                + "Events you save will appear here.<br>"
                + "Click 'Save Event' on any event to add it!</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, COUNT_FONT_SIZE));
        descLabel.setForeground(TEXT_LIGHT_GRAY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);

        final JButton browseButton = new JButton("Browse Events");
        browseButton.setFont(new Font("Segoe UI", Font.BOLD, BUTTON_FONT_SIZE));
        browseButton.setForeground(Color.WHITE);
        browseButton.setBackground(PRIMARY_BLUE);
        browseButton.setBorder(new EmptyBorder(CARD_SPACING, EMPTY_TITLE_SIZE,
                CARD_SPACING, EMPTY_TITLE_SIZE));
        browseButton.setFocusPainted(false);
        browseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        browseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        browseButton.addActionListener(evt -> navigateBack());

        emptyPanel.add(iconLabel);
        emptyPanel.add(Box.createVerticalStrut(CONTAINER_PADDING));
        emptyPanel.add(titleLabel);
        emptyPanel.add(Box.createVerticalStrut(CARD_SPACING));
        emptyPanel.add(descLabel);
        emptyPanel.add(Box.createVerticalStrut(EMPTY_TITLE_SIZE + 1));
        emptyPanel.add(browseButton);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        eventsContainer.add(emptyPanel, gbc);
    }

    private JPanel createEventCard(Event event) {
        final JPanel card = new JPanel(new BorderLayout(HEADER_PADDING, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER),
                new EmptyBorder(CARD_PADDING, CARD_PADDING, CARD_PADDING, CARD_PADDING)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, MAX_CARD_HEIGHT));

        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        final String eventName = truncateText(event.getName(), MAX_NAME_LENGTH);
        final JLabel nameLabel = new JLabel(eventName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, CARD_FONT_SIZE));
        nameLabel.setForeground(TEXT_DARK);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JLabel categoryLabel = new JLabel(event.getCategory().getDisplayName());
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, BADGE_FONT_SIZE));
        categoryLabel.setForeground(PRIMARY_BLUE);
        categoryLabel.setOpaque(true);
        categoryLabel.setBackground(BADGE_BLUE);
        categoryLabel.setBorder(new EmptyBorder(2, HEADER_BUTTON_PADDING,
                2, HEADER_BUTTON_PADDING));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final String address = event.getLocation() != null
                ? event.getLocation().getAddress() : "Location not available";
        final String truncatedAddress = truncateText(address, MAX_ADDRESS_LENGTH);
        final JLabel locationLabel = new JLabel(truncatedAddress);
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, SMALL_BUTTON_FONT_SIZE));
        locationLabel.setForeground(TEXT_GRAY);
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final String dateTimeStr = event.getStartTime() != null
                ? event.getStartTime().format(dateFormatter) : "Date not available";
        final JLabel dateLabel = new JLabel(dateTimeStr);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, SMALL_BUTTON_FONT_SIZE));
        dateLabel.setForeground(TEXT_GRAY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final int spacing = 5;
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(spacing));
        infoPanel.add(categoryLabel);
        infoPanel.add(Box.createVerticalStrut(HEADER_BUTTON_PADDING));
        infoPanel.add(locationLabel);
        infoPanel.add(Box.createVerticalStrut(spacing / 2));
        infoPanel.add(dateLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        final JButton removeButton = new JButton("Remove");
        removeButton.setFont(new Font("Segoe UI", Font.PLAIN, SMALL_BUTTON_FONT_SIZE));
        removeButton.setForeground(ERROR_RED);
        removeButton.setBackground(Color.WHITE);
        removeButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ERROR_RED),
                new EmptyBorder(HEADER_BUTTON_PADDING, CARD_SPACING,
                        HEADER_BUTTON_PADDING, CARD_SPACING)
        ));
        removeButton.setFocusPainted(false);
        removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        removeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                removeButton.setBackground(ERROR_BG);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                removeButton.setBackground(Color.WHITE);
            }
        });

        removeButton.addActionListener(evt -> handleRemoveEvent(event));

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(removeButton);
        card.add(buttonPanel, BorderLayout.EAST);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(CARD_HOVER);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_BLUE),
                        new EmptyBorder(CARD_PADDING, CARD_PADDING,
                                CARD_PADDING, CARD_PADDING)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(CARD_BORDER),
                        new EmptyBorder(CARD_PADDING, CARD_PADDING,
                                CARD_PADDING, CARD_PADDING)
                ));
            }
        });

        return card;
    }

    private void handleRemoveEvent(Event event) {
        final int confirm = JOptionPane.showConfirmDialog(this,
                "Remove \"" + event.getName() + "\" from saved events?",
                "Confirm Remove",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION && saveEventInteractor != null) {
            saveEventInteractor.unsaveEvent(event);
            renderSavedEvents();
            JOptionPane.showMessageDialog(this,
                    "Event removed from saved events.",
                    "Removed", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Truncate text to a maximum length, adding "..." if truncated.
     * @param text the text to truncate
     * @param maxLength the maximum length
     * @return the truncated text
     */
    private String truncateText(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxLength) {
            return text;
        }
        final int truncateLength = 3;
        return text.substring(0, maxLength - truncateLength) + "...";
    }

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
        renderSavedEvents();
    }

    public String getViewName() {
        return viewName;
    }
}