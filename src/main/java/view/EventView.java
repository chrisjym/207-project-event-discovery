package view;

import javax.swing.*;
import entity.Event;
import interface_adapter.search_event_by_name.SearchEventByNameController;
import interface_adapter.signup.SignupController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.net.URL;

public class EventView extends JPanel {

    private final String viewName = "event search";
    private final Event event;
    private SearchEventByNameController eventController = null;


    public EventView(Event event, SearchEventByNameController eventController) {
        this.event = event;
        this.eventController = eventController;

        this.setLayout(new BorderLayout());

        JPanel detailsPanel = createDetailsPanel(event);
        add(detailsPanel, BorderLayout.WEST);

        JPanel imagePanel = createImagePanel(event);
        add(imagePanel, BorderLayout.EAST);

    }

    private JPanel createDetailsPanel(Event event) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setForeground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Back
        JButton backButton = new JButton("← Back to Events");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                eventController.switchToDashboardView();

            }
        });

        // Category
        JLabel categoryLabel = new JLabel(event.getCategory().getDisplayName());
        categoryLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        categoryLabel.setForeground(new Color(59, 130, 246));
        categoryLabel.setOpaque(true);
        categoryLabel.setBackground(new Color(219, 234, 254));
        categoryLabel.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(categoryLabel);
        mainPanel.add(Box.createVerticalStrut(12));


        // Name
        JLabel nameLabel = new JLabel(event.getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(nameLabel);
        mainPanel.add(Box.createVerticalStrut(12));


        // Date
        JLabel dateLabel = new JLabel(event.getDate());
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(dateLabel);
        mainPanel.add(Box.createVerticalStrut(12));


        JLabel descTitle = new JLabel("About this event");
        descTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        descTitle.setForeground(new Color(20, 20, 20));
        descTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(descTitle);
        mainPanel.add(Box.createVerticalStrut(12));


        JTextArea descArea = new JTextArea(event.getDescription());
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descArea.setForeground(new Color(80, 80, 80));
        descArea.setBackground(Color.WHITE);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBorder(null);
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(descArea);
        mainPanel.add(Box.createVerticalStrut(30));

        JButton saveButton = new JButton("Save Event");
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveButton.setFocusPainted(false);
        saveButton.setBackground(Color.WHITE);
        saveButton.setForeground(new Color(59, 130, 246));
        saveButton.setBorder(BorderFactory.createLineBorder(new Color(59, 130, 246), 2));
        saveButton.setPreferredSize(new Dimension(140, 45));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(saveButton);


        // Im not sure what this does, and will have to do more research (just copied it from another source)
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane);

        return wrapper;
    }

    private JPanel createImagePanel(Event event) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(500, 700));
        mainPanel.setBackground(new Color(30, 30, 30));
        // Get image from TicketMaster API (Event Entity)

        try {
            URL imageUrl = new URL(event.getImageURL());
            ImageIcon icon = new ImageIcon(imageUrl);
            Image image = icon.getImage().getScaledInstance(500, 700, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            mainPanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            // Fallback if image doesn't load
            JLabel placeholderLabel = new JLabel("No Image :(");
            mainPanel.add(placeholderLabel, BorderLayout.CENTER);
        }
        return mainPanel;
    }

    public String getViewName() {
        return viewName;
    }
}
