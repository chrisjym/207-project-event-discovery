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
import interface_adapter.calendarFlow.CalendarFlowViewModel;

import entity.Event;
import interface_adapter.calendarFlow.CalendarFlowState;


public class EventListByDateView extends JPanel implements PropertyChangeListener {
    private final String viewName = "event list by date";
    private CalendarFlowViewModel calendarFlowViewModel;

    private final JLabel titleLabel = new JLabel("Events", SwingConstants.CENTER);
    private final JLabel dateLabel = new JLabel("", SwingConstants.CENTER);
    private final JPanel eventsContainer = new JPanel();
    private final JScrollPane scrollPane;

    private final JLabel emptyStateLabel = new JLabel("No events found for this date.", SwingConstants.CENTER);
    private final JButton backButton = new JButton("← Back to DashBoard");
    private final String textFont = "SegoeUI";

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

    private JPanel buildTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(25, 118, 210));

        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(true);
        backButton.setOpaque(true);
        backButton.setBackground(Color.WHITE);
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(6, 12, 6, 12)
                )
        );
        backButton.setFont(new Font(textFont, Font.PLAIN, 12));
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
        centerPanel.add(Box.createVerticalStrut(5));//space between two subpanel
        centerPanel.add(dateLabel);
        topPanel.add(centerPanel, BorderLayout.CENTER);

        return topPanel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        CalendarFlowState state = (CalendarFlowState) evt.getNewValue();

        if (state.getDate() != null) {
            // Update date label
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);
            dateLabel.setText(state.getDate().format(formatter));
        }

        if (state.getEventList() != null && !state.getEventList().isEmpty()) {
            eventListsUI(state.getEventList());
        } else if (state.getErrorMessage() != null) {
            renderError(state.getErrorMessage());
        }
//        else {
//            noEventUI();
//        }
    }

//    private void noEventUI() {
//        eventsContainer.removeAll();
//        eventsContainer.setLayout(new BorderLayout());
//
//        emptyStateLabel.setFont(new Font(textFont, Font.PLAIN, 16));
//        emptyStateLabel.setForeground(new Color(120, 120, 120));
//
//        eventsContainer.add(emptyStateLabel, BorderLayout.CENTER);
//        eventsContainer.revalidate(); // re-run layout
//        eventsContainer.repaint(); // redraw the graphics
//
//    }

    private void renderError(String errorMessage) {
        eventsContainer.removeAll();
        eventsContainer.setLayout(new BorderLayout());

        JLabel errorLabel = new JLabel(errorMessage, SwingConstants.CENTER);
        errorLabel.setFont(errorLabel.getFont().deriveFont(Font.PLAIN, 16f));
//        errorLabel.setForeground(new Color(211, 47, 47));
        emptyStateLabel.setForeground(new Color(120, 120, 120));

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
            eventsContainer.add(Box.createVerticalStrut(10)); // Spacing between cards
        }

        eventsContainer.revalidate();
        eventsContainer.repaint();
    }

    private JPanel buildEventCard(Event event) {
        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
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

        //button that links to Joy's usecase
        JButton exploreButton = new JButton("→");
        exploreButton.setFont(new Font(textFont, Font.BOLD, 20));
        exploreButton.setFocusPainted(false);

        exploreButton.setOpaque(true);
        exploreButton.setContentAreaFilled(true);
        exploreButton.setBorderPainted(false);

        exploreButton.setBackground(new Color(25, 118, 210));
        exploreButton.setForeground(Color.WHITE);
        exploreButton.setBorder(new EmptyBorder(5, 20, 5, 20));
        exploreButton.setPreferredSize(new Dimension(60, 60));

        //Place to links
        exploreButton.addActionListener(e -> {
            // For now, just print to test
            System.out.println("Explore event: " + event.getName());
        });

        JPanel arrowWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        arrowWrapper.setOpaque(false);
        arrowWrapper.add(exploreButton);

        card.add(arrowWrapper, BorderLayout.EAST);
//        card.add(exploreButton, BorderLayout.EAST);

        return card;
    }

        public String getViewName() {
        return viewName;
    }

    /**
     * Sets the action for the back button
     * @param listener the action listener
     * implement by i.e. eventListView.setBackButtonAction(e -> viewManager.show("view name"));
     */
    public void setBackButtonAction(java.awt.event.ActionListener listener) {
        // Remove existing listeners
        for (var l : backButton.getActionListeners()) {
            backButton.removeActionListener(l);
        }
        backButton.addActionListener(listener);
    }

}
