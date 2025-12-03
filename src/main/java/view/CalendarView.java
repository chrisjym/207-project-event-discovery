package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import entity.Location;
import interface_adapter.ViewManagerModel;
import interface_adapter.ViewNames;
import interface_adapter.calendarFlow.CalendarFlowController;

/**
 * View for displaying a calendar with month navigation.
 * Note: This View class has coupling of 8 types, which is expected for GUI components
 * that coordinate multiple UI elements. This follows Clean Architecture principles
 */
public class CalendarView extends JPanel implements ActionListener {
    private final String viewName = ViewNames.CALENDAR;
    private final JLabel monthYearLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton previousMonthButton = new JButton("◀");
    private final JButton nextMonthButton = new JButton("▶");
    private final JPanel dayNamesPanel = new JPanel(new GridLayout(1, 7));
    private final JPanel dayPanel = new JPanel(new GridLayout(6, 7, 5, 5));
    private final JButton[] dayButtons = new JButton[Constants.NUMBER_OF_DAY_BUTTONS];
    private YearMonth currentYearMonth;
    private final String textFormat = "SegoeUI";

    private ViewManagerModel viewManagerModel;
    private CalendarFlowController calendarFlowController;
    private Location userLocation;
    private double searchRadiusKm = Constants.SEARCH_RADIUS_KM_DEFAULT;

    public CalendarView() {
        this.setLayout(new BorderLayout());

        monthYearLabel.setFont(new Font(textFormat, Font.BOLD, Constants.MONTH_YEAR_LABEL_TEXT_FONT_SIZE));
        previousMonthButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        changeMonth(-1);
                    }
                }
        );

        nextMonthButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        changeMonth(1);
                    }
                }
        );

        final JButton backButton = new JButton("← Back");
        backButton.addActionListener(evt -> {
            if (calendarFlowController != null) {
                calendarFlowController.switchToDashboardView();
            }
        });

        final JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);

        final JPanel monthNavPanel = new JPanel(new FlowLayout());
        monthNavPanel.add(previousMonthButton);
        monthNavPanel.add(monthYearLabel);
        monthNavPanel.add(nextMonthButton);

        topPanel.add(monthNavPanel, BorderLayout.CENTER);
        this.add(topPanel, BorderLayout.NORTH);

        final JPanel dayAndDayNamePanel = new JPanel(new BorderLayout());
        final String[] dayNames = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        for (String dayName : dayNames) {
            final JLabel dayLabel = new JLabel(dayName, SwingConstants.CENTER);
            dayLabel.setFont(new Font(textFormat, Font.BOLD, Constants.DAY_LABEL_TEXT_FONT_SIZE));
            dayNamesPanel.add(dayLabel);
        }

        for (int i = 0; i < Constants.NUMBER_OF_DAY_BUTTONS; i++) {
            dayButtons[i] = new JButton();
            dayButtons[i].setFont(new Font(textFormat, Font.PLAIN, Constants.DAY_BUTTONS_FONT_SIZE));
            dayPanel.add(dayButtons[i]);
        }

        dayAndDayNamePanel.add(dayNamesPanel, BorderLayout.NORTH);
        dayAndDayNamePanel.add(dayPanel, BorderLayout.CENTER);

        this.add(dayAndDayNamePanel, BorderLayout.CENTER);
        currentYearMonth = YearMonth.now();
        displayMonth(currentYearMonth);
    }

    private void changeMonth(int monthOffset) {
        currentYearMonth = currentYearMonth.plusMonths(monthOffset);
        displayMonth(currentYearMonth);
    }

    private void displayMonth(YearMonth yearMonth) {
        currentYearMonth = yearMonth;
        monthYearLabel.setText(yearMonth.getMonth() + " " + yearMonth.getYear());

        final LocalDate firstOfMonth = yearMonth.atDay(1);
        final int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;
        final int daysInMonth = yearMonth.lengthOfMonth();

        for (JButton button : dayButtons) {
            button.setText("");
            button.setEnabled(false);

            for (var listener : button.getActionListeners()) {
                button.removeActionListener(listener);
            }
        }

        for (int day = 1; day <= daysInMonth; day++) {
            final int dayToIndex = firstDayOfWeek + day - 1;
            dayButtons[dayToIndex].setText(String.valueOf(day));
            dayButtons[dayToIndex].setEnabled(true);

            final LocalDate date = yearMonth.atDay(day);
            dayButtons[dayToIndex].addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        getSelectedDay(date);
                    }
                }
            );

        }
    }

    public void setEventController(CalendarFlowController controller) {
        this.calendarFlowController = controller;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    public void setUserLocation(Location location) {
        this.userLocation = location;
    }

    public void setSearchRadiusKm(double kiloMeter) {
        this.searchRadiusKm = kiloMeter;
    }

    /**
     * Handles the selection of a specific date on the calendar.
     * Triggers a search for events occurring on the selected date within
     * the specified search radius from the user's location.
     * @param date the date selected by the user
     */
    public void getSelectedDay(LocalDate date) {
        if (calendarFlowController != null) {
            calendarFlowController.execute(date, userLocation, searchRadiusKm);
        }

    }

    public String getViewName() {
        return viewName;
    }

    /**
     * Handles action events triggered by button clicks.
     * Currently, logs the action command to the console for debugging purposes.
     * @param evt the action event triggered by a button click
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }
}

