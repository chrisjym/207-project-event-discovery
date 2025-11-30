package view;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.awt.*;
import java.util.Locale;
import javax.swing.border.EmptyBorder;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import entity.Event;
import interface_adapter.ViewManagerModel;
import interface_adapter.calendarFlow.CalendarFlowViewModel;
import interface_adapter.calendarFlow.CalendarFlowController;
import interface_adapter.calendarFlow.CalendarFlowState;


public class EventListByDateView extends JPanel implements PropertyChangeListener {
    private final String viewName = "event list by date";
    private CalendarFlowViewModel calendarFlowViewModel;
    private CalendarFlowController calendarFlowController;
    private ViewManagerModel viewManagerModel;

    // Event selection listener for navigation to event details
    private EventSelectionListener eventSelectionListener;

    public interface EventSelectionListener {
        void onEventSelected(Event event);
    }

    private final JLabel titleLabel = new JLabel("Events", SwingConstants.CENTER);
    private final JLabel dateLabel = new JLabel("", SwingConstants.CENTER);
    private final JPanel eventsContainer = new JPanel();
    private final JScrollPane scrollPane;

    private final JLabel emptyStateLabel = new JLabel("No events found for this date.", SwingConstants.CENTER);
    private final JButton backButton = new JButton("<- Back to DashBoard");
    private final String textFont = "Segoe UI";

    public EventListByDateView(CalendarFlowViewModel calendarFlowViewModel) {
        this.calendarFlowViewModel = calendarFlowViewModel;
        this.calendarFlowViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(new Color(245, 247, 250));

        add(buildTopPanel(), BorderLayout.NORTH);

        eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));
        eventsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        eventsContainer.setBackground(new Color(245, 247, 250));

        scrollPane = new JScrollPane(eventsContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setController(CalendarFlowController controller) {
        this.calendarFlowController = controller;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    public void setEventSelectionListener(EventSelectionListener listener) {
        this.eventSelectionListener = listener;
    }

    private JPanel buildTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(25, 118, 210));

        backButton.setFocusPainted(false);
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.setContentAreaFilled(true);
        backButton.setBorderPainted(true);
        backButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        new EmptyBorder(8, 15, 8, 15)
                )
        );
        backButton.setFont(new Font(textFont, Font.PLAIN, 12));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            backButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        } catch (Exception e) {
            // Fallback - ignore
        }

        backButton.addActionListener(e -> {
            if (calendarFlowController != null) {
                calendarFlowController.switchToDashboardView();
            }
        });

        topPanel.add(backButton, BorderLayout.WEST);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        titleLabel.setFont(new Font(textFont, Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dateLabel.setFont(new Font(textFont, Font.PLAIN, 16));
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(dateLabel);
        topPanel.add(centerPanel, BorderLayout.CENTER);

        return topPanel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        CalendarFlowState state = (CalendarFlowState) evt.getNewValue();

        if (state.getDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);
            dateLabel.setText(state.getDate().format(formatter));
        }

        if (state.getEventList() != null && !state.getEventList().isEmpty()) {
            eventListsUI(state.getEventList());
        } else if (state.getErrorMessage() != null) {
            renderError(state.getErrorMessage());
        }
    }

    private void renderError(String errorMessage) {
        eventsContainer.removeAll();
        eventsContainer.setLayout(new BorderLayout());

        JLabel errorLabel = new JLabel(errorMessage, SwingConstants.CENTER);
        errorLabel.setFont(errorLabel.getFont().deriveFont(Font.PLAIN, 16f));
        errorLabel.setForeground(new Color(120, 120, 120));

        eventsContainer.add(errorLabel, BorderLayout.CENTER);
        eventsContainer.revalidate();
        eventsContainer.repaint();
    }

    private void eventListsUI(List<Event> events) {
        eventsContainer.removeAll();
        eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));

        for (Event event : events) {
            JPanel eventCard = buildEventCard(event);
            eventsContainer.add(eventCard);
            eventsContainer.add(Box.createVerticalStrut(10));
        }

        eventsContainer.revalidate();
        eventsContainer.repaint();
    }

    private JPanel buildEventCard(Event event) {
        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(12, 15, 12, 15)
        ));
        card.setBackground(Color.WHITE);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(event.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 15f));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel locationLabel = new JLabel(event.getLocation().getAddress());
        locationLabel.setFont(new Font(textFont, Font.PLAIN, 12));
        locationLabel.setForeground(new Color(100, 100, 100));
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
        String timeText = event.getStartTime().format(timeFormatter);
        JLabel timeLabel = new JLabel(timeText);
        timeLabel.setFont(new Font(textFont, Font.PLAIN, 12));
        timeLabel.setForeground(new Color(33, 150, 243));
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(locationLabel);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(timeLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // Button that navigates to event description view
        JButton exploreButton = new JButton("->");
        exploreButton.setFont(new Font(textFont, Font.BOLD, 20));
        exploreButton.setFocusPainted(false);
        exploreButton.setOpaque(true);
        exploreButton.setContentAreaFilled(true);
        exploreButton.setBorderPainted(false);
        exploreButton.setBackground(new Color(25, 118, 210));
        exploreButton.setForeground(Color.WHITE);
        exploreButton.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        exploreButton.setPreferredSize(new Dimension(60, 60));
        exploreButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            exploreButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        } catch (Exception e) {
            // Fallback - ignore
        }

        // Add hover effect
        exploreButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                exploreButton.setBackground(new Color(21, 101, 192));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                exploreButton.setBackground(new Color(25, 118, 210));
            }
        });

        // Navigate to event description view when clicked
        exploreButton.addActionListener(e -> {
            System.out.println("Explore event: " + event.getName());
            if (eventSelectionListener != null) {
                eventSelectionListener.onEventSelected(event);
            }
        });

        card.add(exploreButton, BorderLayout.EAST);

        return card;
    }

    public String getViewName() {
        return viewName;
    }
}