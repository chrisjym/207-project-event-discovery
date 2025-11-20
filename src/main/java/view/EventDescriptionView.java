package view;

import interface_adapter.event_description.EventDescriptionController;
import interface_adapter.event_description.EventDescriptionViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EventDescriptionView extends JPanel implements PropertyChangeListener {

    private final EventDescriptionViewModel viewModel;

    private EventDescriptionController controller;

    private final JTextField eventIdField = new JTextField(10);
    private final JTextField latField = new JTextField(10);
    private final JTextField lonField = new JTextField(10);

    private final JLabel nameLabel = new JLabel();
    private final JLabel descriptionLabel = new JLabel();
    private final JLabel addressLabel = new JLabel();
    private final JLabel categoryLabel = new JLabel();
    private final JLabel dateTimeLabel = new JLabel();
    private final JLabel distanceLabel = new JLabel();
    private final JLabel errorLabel = new JLabel();

    public EventDescriptionView(EventDescriptionViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.add(new JLabel("Event ID:"));
        inputPanel.add(eventIdField);
        inputPanel.add(new JLabel("Your Latitude:"));
        inputPanel.add(latField);
        inputPanel.add(new JLabel("Your Longitude:"));
        inputPanel.add(lonField);

        JButton showButton = new JButton("Show Event");
        showButton.addActionListener(e -> onShowEvent());
        inputPanel.add(showButton);

        add(inputPanel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        detailsPanel.add(nameLabel);
        detailsPanel.add(descriptionLabel);
        detailsPanel.add(addressLabel);
        detailsPanel.add(categoryLabel);
        detailsPanel.add(dateTimeLabel);
        detailsPanel.add(distanceLabel);

        add(detailsPanel, BorderLayout.CENTER);

        // if error
        errorLabel.setForeground(Color.RED);
        add(errorLabel, BorderLayout.SOUTH);
    }

    public String getViewName() {
        return EventDescriptionViewModel.VIEW_NAME;
    }

    public void setController(EventDescriptionController controller) {
        this.controller = controller;
    }

    private void onShowEvent() {
        if (controller == null) return;

        String eventId = eventIdField.getText().trim();
        try {
            double lat = Double.parseDouble(latField.getText().trim());
            double lon = Double.parseDouble(lonField.getText().trim());
            controller.showEvent(eventId, lat, lon);
        } catch (NumberFormatException e) {
            errorLabel.setText("Please enter valid numbers for latitude and longitude.");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        nameLabel.setText("Name: " + viewModel.getName());
        descriptionLabel.setText("Description: " + viewModel.getDescription());
        addressLabel.setText("Address: " + viewModel.getAddress());
        categoryLabel.setText("Category: " + viewModel.getCategory());
        dateTimeLabel.setText("Date/Time: " + viewModel.getDateTime());
        distanceLabel.setText("Distance: " + viewModel.getDistanceText());
        errorLabel.setText(viewModel.getErrorMessage());
    }
}
