package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.jetbrains.annotations.NotNull;

import entity.Event;
import interface_adapter.ViewManagerModel;
import interface_adapter.calendarFlow.CalendarFlowController;
import interface_adapter.calendarFlow.CalendarFlowState;
import interface_adapter.calendarFlow.CalendarFlowViewModel;

/**
 * View for displaying a list of events occurring on a specific date.
 * This view shows events retrieved from the calendar in a scrollable list format,
 * with each event displayed as a card showing its name, location, and time.
 * Users can click on individual events to view more details.
 */
@SuppressWarnings({"checkstyle:ClassDataAbstractionCoupling", "checkstyle:SuppressWarnings"})
public class EventListByDateView extends JPanel implements PropertyChangeListener {
    private final String viewName = "event list by date";
    private CalendarFlowViewModel calendarFlowViewModel;
    private CalendarFlowController calendarFlowController;
    private ViewManagerModel viewManagerModel;
    private EventSelectionListener eventSelectionListener;

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
        setBorder(new EmptyBorder(
                Constants.EMPTY_BORDER_SIZE,
                Constants.EMPTY_BORDER_SIZE,
                Constants.EMPTY_BORDER_SIZE,
                Constants.EMPTY_BORDER_SIZE));
        setBackground(new Color(Constants.BACKGROUND_COLOR_R,
                Constants.BACKGROUND_COLOR_G,
                Constants.BACKGROUND_COLOR_B));

        add(buildTopPanel(), BorderLayout.NORTH);

        eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));
        eventsContainer.setBorder(new EmptyBorder(Constants.CONTAINER_EMPTY_BORDER_SIZE,
                Constants.CONTAINER_EMPTY_BORDER_SIZE,
                Constants.CONTAINER_EMPTY_BORDER_SIZE,
                Constants.CONTAINER_EMPTY_BORDER_SIZE));
        eventsContainer.setBackground(new Color(Constants.BACKGROUND_COLOR_R,
                Constants.BACKGROUND_COLOR_G,
                Constants.BACKGROUND_COLOR_B));

        scrollPane = new JScrollPane(eventsContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(Constants.UNIT_INCREMENT_SCALE);
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
        final JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(Constants.TOP_PANEL_EMPTY_BORDER_SIZE,
                Constants.TOP_PANEL_EMPTY_BORDER_SIZE,
                Constants.TOP_PANEL_EMPTY_BORDER_SIZE,
                Constants.TOP_PANEL_EMPTY_BORDER_SIZE));
        topPanel.setBackground(new Color(Constants.TOP_PANEL_BACKGROUND_COLOR_R,
                Constants.TOP_PANEL_BACKGROUND_COLOR_G,
                Constants.TOP_PANEL_BACKGROUND_COLOR_B));

        backButton.setFocusPainted(false);
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.setContentAreaFilled(true);
        backButton.setBorderPainted(true);
        backButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(Constants.BACK_BUTTON_LINE_BOARDER_COLOR_R,
                                Constants.BACK_BUTTON_LINE_BOARDER_COLOR_G,
                                Constants.BACK_BUTTON_LINE_BOARDER_COLOR_B), 1),
                        new EmptyBorder(Constants.BACK_BUTTON_EMPTY_BOARDER_TOP,
                                Constants.BACK_BUTTON_EMPTY_BOARDER_LEFT,
                                Constants.BACK_BUTTON_EMPTY_BOARDER_BOTTOM,
                                Constants.BACK_BUTTON_EMPTY_BOARDER_RIGHT)
                )
        );
        backButton.setFont(new Font(textFont, Font.PLAIN, Constants.BACK_BUTTON_FONT_SIZE));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        backButton.addActionListener(event -> {
            if (calendarFlowController != null) {
                calendarFlowController.switchToDashboardView();
            }
        });

        topPanel.add(backButton, BorderLayout.WEST);

        final JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        titleLabel.setFont(new Font(textFont, Font.BOLD, Constants.CENTER_PANEL_TITLE_LABEL_FONT_SIZE));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dateLabel.setFont(new Font(textFont, Font.PLAIN, Constants.CENTER_PANEL_DATE_LABEL_FONT_SIZE));
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(Constants.CENTER_PANEL_BOX_VERTICAL_STRUC_HIGH));
        centerPanel.add(dateLabel);
        topPanel.add(centerPanel, BorderLayout.CENTER);

        return topPanel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final CalendarFlowState state = (CalendarFlowState) evt.getNewValue();

        if (state.getDate() != null) {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);
            dateLabel.setText(state.getDate().format(formatter));
        }

        if (state.getEventList() != null && !state.getEventList().isEmpty()) {
            eventListsUi(state.getEventList());
        }
        else if (state.getErrorMessage() != null) {
            renderError(state.getErrorMessage());
        }
    }

    private void renderError(String errorMessage) {
        eventsContainer.removeAll();
        eventsContainer.setLayout(new BorderLayout());

        final JLabel errorLabel = new JLabel(errorMessage, SwingConstants.CENTER);
        errorLabel.setFont(errorLabel.getFont().deriveFont(Font.PLAIN, Constants.ERROR_LABEL_TEXT_FONT_SIZE));
        errorLabel.setForeground(new Color(Constants.ERROR_LABEL_BACKGROUND_COLOR_R,
                Constants.ERROR_LABEL_BACKGROUND_COLOR_G,
                Constants.ERROR_LABEL_BACKGROUND_COLOR_B));

        eventsContainer.add(errorLabel, BorderLayout.CENTER);
        eventsContainer.revalidate();
        eventsContainer.repaint();
    }

    private void eventListsUi(List<Event> events) {
        eventsContainer.removeAll();
        eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));

        for (Event event : events) {
            final JPanel eventCard = buildEventCard(event);
            eventsContainer.add(eventCard);
            eventsContainer.add(Box.createVerticalStrut(Constants.EVENT_CONTAINER_BOX_VERTICAL_STRUC_HEIGHT));
        }

        eventsContainer.revalidate();
        eventsContainer.repaint();
    }

    private JPanel buildEventCard(Event event) {
        final JPanel card = createCardBasePanel();
        final JPanel infoPanel = createInfoPanel();
        final JLabel nameLabel = createNameLabel(event);
        final JLabel locationLabel = createLocationLabel(event);

        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
        final String timeText = event.getStartTime().format(timeFormatter);
        final JLabel timeLabel = new JLabel(timeText);
        timeLabel.setFont(new Font(textFont, Font.PLAIN, Constants.TIME_LABEL_TEXT_FONT_SIZE));
        timeLabel.setForeground(new Color(Constants.TIME_LABEL_BACKGROUND_COLOR_R,
                Constants.TIME_LABEL_BACKGROUND_COLOR_G,
                Constants.TIME_LABEL_BACKGROUND_COLOR_B));
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(Constants.INFO_PANEL_BOX_VERTICAL_STRUC_HEIGHT));
        infoPanel.add(locationLabel);
        infoPanel.add(Box.createVerticalStrut(Constants.INFO_PANEL_BOX_VERTICAL_STRUC_HEIGHT));
        infoPanel.add(timeLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // Button that navigates to event description view
        final JButton exploreButton = createExploreButton();
        exploreButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        // Add hover effect
        exploreButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                exploreButton.setBackground(new Color(Constants.EXPLORE_BUTTON_MOUSE_ENTER_COLOR_R,
                        Constants.EXPLORE_BUTTON_MOUSE_ENTER_COLOR_G,
                        Constants.EXPLORE_BUTTON_MOUSE_ENTER_COLOR_B));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                exploreButton.setBackground(new Color(Constants.EXPLORE_BUTTON_MOUSE_EXITED_COLOR_R,
                        Constants.EXPLORE_BUTTON_MOUSE_EXITED_COLOR_G,
                        Constants.EXPLORE_BUTTON_MOUSE_EXITED_COLOR_B));
            }
        });

        // Navigate to event description view when clicked
        exploreButton.addActionListener(eve -> {
            System.out.println("Explore event: " + event.getName());
            if (eventSelectionListener != null) {
                eventSelectionListener.onEventSelected(event);
            }
        });

        card.add(exploreButton, BorderLayout.EAST);

        return card;
    }

    @NotNull
    private JButton createExploreButton() {
        final JButton exploreButton = new JButton("->");
        exploreButton.setFont(new Font(textFont, Font.BOLD, Constants.EXPLORE_BUTTON_FONT_SIZE));
        exploreButton.setFocusPainted(false);
        exploreButton.setOpaque(true);
        exploreButton.setContentAreaFilled(true);
        exploreButton.setBorderPainted(false);
        exploreButton.setBackground(new Color(Constants.EXPLORE_BUTTON_BACKGROUND_COLOR_R,
                Constants.EXPLORE_BUTTON_BACKGROUND_COLOR_G,
                Constants.EXPLORE_BUTTON_BACKGROUND_COLOR_B));
        exploreButton.setForeground(Color.WHITE);
        exploreButton.setBorder(BorderFactory.createEmptyBorder(
                Constants.EXPLORE_BUTTON_EMPTY_BOARDER_TOP,
                Constants.EXPLORE_BUTTON_EMPTY_BOARDER_LEFT,
                Constants.EXPLORE_BUTTON_EMPTY_BOARDER_BOTTOM,
                Constants.EXPLORE_BUTTON_EMPTY_BOARDER_RIGHT));
        exploreButton.setPreferredSize(new Dimension(Constants.EXPLORE_BUTTON_PREFERRED_WIDTH,
                Constants.EXPLORE_BUTTON_PREFERRED_HEIGHT));
        exploreButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return exploreButton;
    }

    @NotNull
    private JLabel createLocationLabel(Event event) {
        final JLabel locationLabel = new JLabel(event.getLocation().getAddress());
        locationLabel.setFont(new Font(textFont, Font.PLAIN, Constants.LOCATION_LABEL_TEXT_FONT_SIZE));
        locationLabel.setForeground(new Color(Constants.LOCATION_LABEL_BACKGROUND_COLOR_R,
                Constants.LOCATION_LABEL_BACKGROUND_COLOR_G,
                Constants.LOCATION_LABEL_BACKGROUND_COLOR_B));
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return locationLabel;
    }

    @NotNull
    private static JLabel createNameLabel(Event event) {
        final JLabel nameLabel = new JLabel(event.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, Constants.NAMEL_LABEL_TEXT_FONT_SIZE));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return nameLabel;
    }

    @NotNull
    private static JPanel createCardBasePanel() {
        final JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Constants.CARD_BASE_PANEL_MAXIMUM_SIZE_HEIGHT));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(Constants.CARD_BASE_PANEL_LINE_BOARDER_COLOR_R,
                        Constants.CARD_BASE_PANEL_LINE_BOARDER_COLOR_G,
                        Constants.CARD_BASE_PANEL_LINE_BOARDER_COLOR_B), 1),
                new EmptyBorder(Constants.CARD_BASE_PANEL_EMPTY_BOARDER_TOP,
                        Constants.CARD_BASE_PANEL_EMPTY_BOARDER_LEFT,
                        Constants.CARD_BASE_PANEL_EMPTY_BOARDER_BOTTOM,
                        Constants.CARD_BASE_PANEL_EMPTY_BOARDER_RIGHT)
        ));
        card.setBackground(Color.WHITE);
        return card;
    }

    @NotNull
    private static JPanel createInfoPanel() {
        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        return infoPanel;
    }

    public String getViewName() {
        return viewName;
    }

    /**
     * Listener interface for handling event selection actions.
     */
    public interface EventSelectionListener {
        /**
         * Called when an event is selected by the user.
         * @param event event the event that was selected
         */
        void onEventSelected(Event event);
    }
}
