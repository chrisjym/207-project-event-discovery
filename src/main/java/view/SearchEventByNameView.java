package view;

import javax.swing.*;

import interface_adapter.save_event.SaveEventController;
import interface_adapter.search_event_by_name.SearchEventByNameController;
import interface_adapter.search_event_by_name.SearchEventByNameViewModel;
import entity.Event;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.time.format.DateTimeFormatter;

public class SearchEventByNameView extends JPanel implements PropertyChangeListener {

    private final String viewName = "event search";
    private final SearchEventByNameViewModel searchEventByNameViewModel;
    private SearchEventByNameController eventController = null;

    // Components that need to be updated
    private JLabel categoryLabel;
    private JLabel nameLabel;
    private JLabel dateLabel;
    private JLabel venueLabel;
    private JTextArea descArea;
    private JPanel imagePanel;
    private SaveButtonView saveButtonView;
    private SaveEventController saveEventController;

    public SearchEventByNameView(SearchEventByNameViewModel searchEventByNameViewModel) {
        this.searchEventByNameViewModel = searchEventByNameViewModel;
        this.searchEventByNameViewModel.addPropertyChangeListener(this);
        this.setLayout(new BorderLayout());

        JPanel detailsPanel = createDetailsPanel();
        add(detailsPanel, BorderLayout.CENTER);

        imagePanel = createImagePanel();
        imagePanel = createImagePanel();
        add(imagePanel, BorderLayout.EAST);

        // Initial update with current state
        updateView();
    }

    private JPanel createDetailsPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setForeground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setPreferredSize(new Dimension(400, 700));

        JButton backButton = new JButton("← Back to Events");
        backButton.setFont(new Font("SegoeUI", Font.PLAIN, 13));
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(backButton);
        mainPanel.add(Box.createVerticalStrut(24));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (eventController != null) {
                    eventController.switchToDashboardView();
                }
            }
        });

        categoryLabel = new JLabel("Loading");
        categoryLabel.setFont(new Font("SegoeUI", Font.BOLD, 11));
        categoryLabel.setForeground(new Color(59, 130, 246));
        categoryLabel.setOpaque(true);
        categoryLabel.setBackground(new Color(219, 234, 254));
        categoryLabel.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(categoryLabel);
        mainPanel.add(Box.createVerticalStrut(12));

        nameLabel = new JLabel("<html>Loading Event...</html>");
        nameLabel.setFont(new Font("SegoeUI", Font.BOLD, 32));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(nameLabel);
        mainPanel.add(Box.createVerticalStrut(12));

        JPanel dateRow = createRow("Date:", "");
        dateLabel = (JLabel) dateRow.getComponent(1);
        mainPanel.add(dateRow);
        mainPanel.add(Box.createVerticalStrut(12));

        JPanel venueRow = createRow("Venue:", "");
        venueLabel = (JLabel) venueRow.getComponent(1);
        mainPanel.add(venueRow);
        mainPanel.add(Box.createVerticalStrut(12));

        JLabel descTitle = new JLabel("About this event");
        descTitle.setFont(new Font("SegoeUI", Font.BOLD, 16));
        descTitle.setForeground(new Color(20, 20, 20));
        descTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(descTitle);
        mainPanel.add(Box.createVerticalStrut(12));

        descArea = new JTextArea("Loading description...");
        descArea.setFont(new Font("SegoeUI", Font.PLAIN, 14));
        descArea.setForeground(new Color(80, 80, 80));
        descArea.setBackground(Color.WHITE);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBorder(null);
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(descArea);
        mainPanel.add(Box.createVerticalStrut(30));

        saveButtonView = new SaveButtonView();
        saveButtonView.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(saveButtonView);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane);

        return wrapper;
    }

    private JPanel createImagePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(500, 700));
        mainPanel.setBackground(new Color(30, 30, 30));

        JLabel placeholderLabel = new JLabel("Loading", SwingConstants.CENTER);
        placeholderLabel.setForeground(Color.WHITE);
        mainPanel.add(placeholderLabel, BorderLayout.CENTER);

        return mainPanel;
    }

    private void updateImagePanel(String imageUrl) {
        imagePanel.removeAll();

        try {
            URL url = new URL(imageUrl);
            ImageIcon icon = new ImageIcon(url);
            Image image = icon.getImage().getScaledInstance(500, 700, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel placeholderLabel = new JLabel("No Image Available", SwingConstants.CENTER);
            placeholderLabel.setForeground(Color.WHITE);
            imagePanel.add(placeholderLabel, BorderLayout.CENTER);
        }

        imagePanel.revalidate();
        imagePanel.repaint();
    }

    private JPanel createRow(String category, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(400, 30));

        JLabel title = new JLabel(category);
        title.setFont(new Font("SegoeUI", Font.BOLD, 14));
        title.setForeground(new Color(50, 50, 50));

        JLabel description = new JLabel(text);
        description.setFont(new Font("SegoeUI", Font.PLAIN, 14));
        description.setForeground(new Color(50, 50, 50));

        row.add(title);
        row.add(description);

        return row;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateView();
    }

    private void updateView() {
        Event event = searchEventByNameViewModel.getEvent();

        if (event != null) {
            categoryLabel.setText(event.getCategory().getDisplayName());

            nameLabel.setText("<html>" + event.getName().replace(" ", "<br/>") + "</html>");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            dateLabel.setText(event.getStartTime().format(formatter));


            String venueText = event.getLocation().getAddress();
            if (venueText.length() > 50) {
                venueText = venueText.substring(0, 47) + "...";
            }
            venueLabel.setText(venueText);

            String desc = event.getDescription();
            descArea.setText(desc.isEmpty() ? "No description available" : desc);

            updateImagePanel(event.getImageUrl());

            saveButtonView.setEvent(event);
            saveButtonView.setSaveEventController(saveEventController);

        }
    }

    public String getViewName() {
        return viewName;
    }



    public void setEventController(SearchEventByNameController controller) {
        this.eventController = controller;
    }

    public void setSaveButtonController(SaveEventController eventController) {
        this.saveButtonView.setSaveEventController(eventController);
        this.saveEventController = eventController;
    }

}

