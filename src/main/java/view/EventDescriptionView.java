package view;

import interface_adapter.event_description.EventDescriptionViewModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EventDescriptionView extends JPanel implements PropertyChangeListener {

    private final EventDescriptionViewModel viewModel;

    private final JLabel titleLabel = new JLabel("", SwingConstants.CENTER);

    // value labels
    private final JLabel nameValueLabel = new JLabel();
    private final JLabel descriptionValueLabel = new JLabel();
    private final JLabel addressValueLabel = new JLabel();
    private final JLabel categoryValueLabel = new JLabel();
    private final JLabel dateTimeValueLabel = new JLabel();
    private final JLabel distanceValueLabel = new JLabel();

    public EventDescriptionView(EventDescriptionViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setBackground(Color.WHITE);
        setLayout(new BorderLayout(0, 24));
        setBorder(new EmptyBorder(24, 32, 24, 32));

        // title
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(20, 20, 20));
        add(titleLabel, BorderLayout.NORTH);

        // image area
        JPanel imagePanel = getJPanel();

        add(imagePanel, BorderLayout.CENTER);

        // details card
        JPanel detailsCard = new JPanel(new GridBagLayout());
        detailsCard.setBackground(Color.WHITE);
        detailsCard.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 228, 240)),
                new EmptyBorder(16, 24, 16, 24)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;
        addRow(detailsCard, gbc, row++, "Name:",        nameValueLabel);
        addRow(detailsCard, gbc, row++, "Description:", descriptionValueLabel);
        addRow(detailsCard, gbc, row++, "Address:",     addressValueLabel);
        addRow(detailsCard, gbc, row++, "Category:",    categoryValueLabel);
        addRow(detailsCard, gbc, row++, "Date / Time:", dateTimeValueLabel);
        addRow(detailsCard, gbc, row,   "Distance:",    distanceValueLabel);

        add(detailsCard, BorderLayout.SOUTH);

        // fake sample data for now
        viewModel.setEventDetails(
                "Live at the Park",
                "An outdoor evening concert featuring local bands and food trucks.",
                "123 Queen St W, Toronto, ON",
                "Music",
                "Nov 30, 2025 • 7:30 PM",
                "2.4 km away"
        );
    }

    @NotNull
    private static JPanel getJPanel() {
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(600, 220));
        imagePanel.setBackground(new Color(240, 244, 252)); // light blue-ish
        imagePanel.setBorder(new LineBorder(new Color(220, 228, 240)));

        JLabel imagePlaceholder = new JLabel("Event image", SwingConstants.CENTER);
        imagePlaceholder.setFont(new Font("SansSerif", Font.PLAIN, 14));
        imagePlaceholder.setForeground(new Color(130, 140, 160));
        imagePanel.setLayout(new BorderLayout());
        imagePanel.add(imagePlaceholder, BorderLayout.CENTER);
        return imagePanel;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row,
                        String labelText, JLabel valueLabel) {

        Color primaryBlue = new Color(0, 102, 204); // blue highlights

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(primaryBlue);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        panel.add(label, gbc);

        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        valueLabel.setForeground(new Color(40, 40, 40));

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        panel.add(valueLabel, gbc);
    }

    public String getViewName() {
        return EventDescriptionViewModel.VIEW_NAME;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // update UI from ViewModel
        titleLabel.setText(viewModel.getName());

        nameValueLabel.setText(viewModel.getName());
        descriptionValueLabel.setText(viewModel.getDescription());
        addressValueLabel.setText(viewModel.getAddress());
        categoryValueLabel.setText(viewModel.getCategory());
        dateTimeValueLabel.setText(viewModel.getDateTime());
        distanceValueLabel.setText(viewModel.getDistanceText());
    }
}
