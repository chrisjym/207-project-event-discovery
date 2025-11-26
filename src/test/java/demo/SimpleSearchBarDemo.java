package demo;


import entity.Location;
import view.SearchBarView;
import javax.swing.*;

public class SimpleSearchBarDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Search Bar Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 200);
        frame.setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Create and add your search bar
        SearchBarView searchBar = new SearchBarView("Hello!", new Location("Milwaukee, WI", 43.0389, -87.9065));
        mainPanel.add(searchBar);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}