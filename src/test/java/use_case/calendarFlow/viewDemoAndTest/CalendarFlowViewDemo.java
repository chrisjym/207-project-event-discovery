package use_case.calendarFlow.viewDemoAndTest;

import view.CalendarView;
import javax.swing.*;

public class CalendarFlowViewDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Calendar Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            CalendarView calendarView = new CalendarView();
            frame.add(calendarView);
            frame.pack();
            frame.setLocationRelativeTo(null);  // Center on screen
            frame.setVisible(true);
        });
    }
}
