package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.YearMonth;

import interface_adapter.calendarFlow.CalendarFlowController;
import interface_adapter.ViewManagerModel;
import entity.Location;


public class  CalendarView extends JPanel implements ActionListener {
    private final String viewName = "calendar view";
    private final JLabel monthYearLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton previousMonthButton = new JButton("◀");
    private final JButton nextMonthButton = new JButton("▶");
    private final JPanel dayNamesPanel = new JPanel(new GridLayout(1, 7));
    private final JPanel dayPanel = new JPanel(new GridLayout(6, 7, 5, 5));
    private final JButton[] dayButtons = new JButton[42];
    private YearMonth currentYearMonth; // Track which month in which year we're displaying
    private String textFormat = "SegoeUI";

    private CalendarFlowController calendarFlowController;
    private ViewManagerModel viewManagerModel;
    private Location userLocation;
    private double searchRadiusKm = 50.0; //default

    public void setEventController(CalendarFlowController controller) {
        this.calendarFlowController = controller;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }


    public void setUserLocation(Location location) {
        this.userLocation = location;
    }

    public void setSearchRadiusKm(double km) {
        this.searchRadiusKm = km;
    }

    public CalendarView(){
        this.setLayout(new BorderLayout());

        monthYearLabel.setFont(new Font(textFormat, Font.BOLD, 18));
        previousMonthButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {changeMonth(-1);}
        });

        nextMonthButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {changeMonth(1);}
                }
        );

        JButton backButton = new JButton("← Back");
        backButton.addActionListener(e -> {
            if(calendarFlowController != null) {
                calendarFlowController.switchToDashboardView();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);

        JPanel monthNavPanel = new JPanel(new FlowLayout());
        monthNavPanel.add(previousMonthButton);
        monthNavPanel.add(monthYearLabel);
        monthNavPanel.add(nextMonthButton);

        topPanel.add(monthNavPanel, BorderLayout.CENTER);
        this.add(topPanel, BorderLayout.NORTH);

        JPanel dayAndDayNamePanel = new JPanel(new BorderLayout());
        String[] dayNames = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        for (String dayName : dayNames){
            JLabel dayLabel = new JLabel(dayName, SwingConstants.CENTER);
            dayLabel.setFont(new Font(textFormat, Font.BOLD, 12));
            dayNamesPanel.add(dayLabel);
        }

        for(int i = 0; i < 42; i++){
            dayButtons[i] = new JButton();
            dayButtons[i].setFont(new Font(textFormat, Font.PLAIN, 12));
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

    private void displayMonth(YearMonth yearMonth){
        currentYearMonth = yearMonth;
        monthYearLabel.setText(yearMonth.getMonth() + " " + yearMonth.getYear());

        LocalDate firstOfMonth = yearMonth.atDay(1);
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;
        int daysInMonth = yearMonth.lengthOfMonth();

        //Defaulting all the button first
        for (JButton button : dayButtons) {
            button.setText("");
            button.setEnabled(false);

            for (var listener : button.getActionListeners()) {
                button.removeActionListener(listener);
            }
        }

        for(int day = 1; day <= daysInMonth; day++){
            int dayToIndex = firstDayOfWeek + day - 1;
            dayButtons[dayToIndex].setText(String.valueOf(day));
            dayButtons[dayToIndex].setEnabled(true);

            final LocalDate date = yearMonth.atDay(day);
            dayButtons[dayToIndex].addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        getSelectedDay(date);
                    }
            });

        }
    }

    public void getSelectedDay(LocalDate date){
        if (calendarFlowController != null){
            calendarFlowController.execute(date, userLocation, searchRadiusKm);
        }

    }

    public String getViewName(){
        return viewName;
    }

    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

}
