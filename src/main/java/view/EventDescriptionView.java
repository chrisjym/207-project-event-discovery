package view;

import entity.Event;
import interface_adapter.ViewManagerModel;
import interface_adapter.event_description.EventDescriptionViewModel;
import interface_adapter.save_event.SaveEventController;
import use_case.save_event.SaveEventInteractor;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;

/**
 * View for displaying event details.
 *
 * Features:
 * - Shows event details (name, category, date, location, description)
 * - Displays event image
 * - Shows distance from user's location
 * - Save Event button that shows "Already Saved" if event is already saved
 * - Back navigation to events list
 */
public class EventDescriptionView extends JPanel implements PropertyChangeListener {

    private final EventDescriptionViewModel viewModel;
    private ViewManagerModel viewManagerModel;
    private SaveEventController saveEventController;
    private SaveEventInteractor saveEventInteractor;  // For checking if event is already saved
    private Event currentEvent;

    // UI Components
    private final JLabel titleLabel = new JLabel("", SwingConstants.LEFT);
    private final JLabel categoryLabel = new JLabel();
    private final JLabel dateTimeLabel = new JLabel();
    private final JLabel addressLabel = new JLabel();
    private final JTextArea descriptionArea = new JTextArea();
    private final JLabel distanceLabel = new JLabel();
    private final JLabel imageLabel = new JLabel();
    private final JButton backButton = new JButton("← Back to Events");
    private final JButton saveButton = new JButton("Save Event");

    private final DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");

    public EventDescriptionView(EventDescriptionViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header with back button
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main content - split into image and details
        JSplitPane mainContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainContent.setDividerLocation(500);
        mainContent.setDividerSize(0);
        mainContent.setBorder(null);

        // Left side - Image
        JPanel imagePanel = createImagePanel();
        mainContent.setLeftComponent(imagePanel);

        // Right side - Details
        JPanel detailsPanel = createDetailsPanel();
        JScrollPane detailsScroll = new JScrollPane(detailsPanel);
        detailsScroll.setBorder(null);
        detailsScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainContent.setRightComponent(detailsScroll);

        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 118, 210));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Style back button
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(25, 118, 210));
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(true);

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                backButton.setBackground(new Color(21, 101, 192));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                backButton.setBackground(new Color(25, 118, 210));
            }
        });

        backButton.addActionListener(e -> navigateBack());

        header.add(backButton, BorderLayout.WEST);

        JLabel headerTitle = new JLabel("Event Details");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(headerTitle, BorderLayout.CENTER);

        return header;
    }

    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.setPreferredSize(new Dimension(500, 600));

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setForeground(Color.WHITE);
        imageLabel.setText("Loading image...");

        panel.add(imageLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Category badge
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        categoryLabel.setForeground(new Color(59, 130, 246));
        categoryLabel.setOpaque(true);
        categoryLabel.setBackground(new Color(219, 234, 254));
        categoryLabel.setBorder(new EmptyBorder(5, 12, 5, 12));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(categoryLabel);
        panel.add(Box.createVerticalStrut(15));

        // Title
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(17, 24, 39));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        // Date/Time section
        panel.add(createInfoRow("📅 Date & Time", dateTimeLabel));
        panel.add(Box.createVerticalStrut(15));

        // Location section
        panel.add(createInfoRow("📍 Location", addressLabel));
        panel.add(Box.createVerticalStrut(15));

        // Distance section
        panel.add(createInfoRow("📏 Distance", distanceLabel));
        panel.add(Box.createVerticalStrut(25));

        // Description section
        JLabel descTitle = new JLabel("About This Event");
        descTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        descTitle.setForeground(new Color(17, 24, 39));
        descTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(descTitle);
        panel.add(Box.createVerticalStrut(10));

        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionArea.setForeground(new Color(75, 85, 99));
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(null);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionArea.setMaximumSize(new Dimension(400, 200));
        panel.add(descriptionArea);
        panel.add(Box.createVerticalStrut(30));

        // Save button
        styleSaveButton();
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveButton.addActionListener(e -> saveEvent());
        panel.add(saveButton);

        return panel;
    }

    private JPanel createInfoRow(String label, JLabel valueLabel) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLbl = new JLabel(label);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setForeground(new Color(107, 114, 128));
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        valueLabel.setForeground(new Color(17, 24, 39));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        row.add(titleLbl);
        row.add(Box.createVerticalStrut(5));
        row.add(valueLabel);

        return row;
    }

    private void styleSaveButton() {
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(59, 130, 246));
        saveButton.setBorder(new EmptyBorder(12, 30, 12, 30));
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setOpaque(true);

        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (saveButton.isEnabled()) {
                    saveButton.setBackground(new Color(37, 99, 235));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (saveButton.isEnabled()) {
                    saveButton.setBackground(new Color(59, 130, 246));
                }
            }
        });
    }

    /**
     * Display an event's details.
     * This method is called when navigating to this view with an event.
     */
    public void displayEvent(Event event) {
        this.currentEvent = event;

        if (event == null) {
            titleLabel.setText("Event Not Found");
            categoryLabel.setText("N/A");
            dateTimeLabel.setText("N/A");
            addressLabel.setText("N/A");
            distanceLabel.setText("N/A");
            descriptionArea.setText("The requested event could not be found.");
            resetSaveButton();
            return;
        }

        // Update all fields
        titleLabel.setText("<html><body style='width: 350px'>" + event.getName() + "</body></html>");
        categoryLabel.setText(event.getCategory().getDisplayName());
        dateTimeLabel.setText(event.getStartTime().format(displayFormatter));
        addressLabel.setText("<html><body style='width: 300px'>" + event.getLocation().getAddress() + "</body></html>");

        // Description
        String desc = event.getDescription();
        if (desc == null || desc.trim().isEmpty()) {
            descriptionArea.setText("No description available for this event.");
        } else {
            descriptionArea.setText(desc);
        }

        // Load image
        loadEventImage(event.getImageUrl());

        // Distance will be set separately via setDistanceText
        distanceLabel.setText("Calculating...");

        // Check if event is already saved and update button accordingly
        updateSaveButtonState();
    }

    /**
     * Check if the current event is already saved and update button state.
     */
    private void updateSaveButtonState() {
        if (currentEvent != null && saveEventInteractor != null) {
            boolean isAlreadySaved = saveEventInteractor.isEventSaved(currentEvent.getId());
            if (isAlreadySaved) {
                showAlreadySavedState();
            } else {
                resetSaveButton();
            }
        } else {
            resetSaveButton();
        }
    }

    /**
     * Show the button in "Already Saved" state.
     */
    private void showAlreadySavedState() {
        saveButton.setText("Saved");
        saveButton.setBackground(new Color(34, 197, 94));  // Green
        saveButton.setEnabled(false);
        saveButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Reset save button to default state.
     */
    private void resetSaveButton() {
        saveButton.setText("Save Event");
        saveButton.setBackground(new Color(59, 130, 246));  // Blue
        saveButton.setEnabled(true);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Set the distance text (called after distance calculation).
     */
    public void setDistanceText(String distanceText) {
        distanceLabel.setText(distanceText);
    }

    private void loadEventImage(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            imageLabel.setIcon(null);
            imageLabel.setText("No image available");
            return;
        }

        // Load image in background thread to avoid UI freeze
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() {
                try {
                    BufferedImage img = ImageIO.read(new URL(imageUrl));
                    if (img != null) {
                        Image scaled = img.getScaledInstance(480, 400, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaled);
                    }
                } catch (IOException e) {
                    System.err.println("Failed to load event image: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        imageLabel.setIcon(icon);
                        imageLabel.setText("");
                    } else {
                        imageLabel.setIcon(null);
                        imageLabel.setText("Failed to load image");
                    }
                } catch (Exception e) {
                    imageLabel.setText("Error loading image");
                }
            }
        }.execute();
    }

    private void navigateBack() {
        if (viewManagerModel != null) {
            viewManagerModel.setState("display local events");
            viewManagerModel.firePropertyChange();
        }
    }

    private void saveEvent() {
        if (currentEvent != null && saveEventController != null) {
            saveEventController.saveEvent(currentEvent);
            showAlreadySavedState();  // Update button to show saved state

            // Show confirmation dialog
            JOptionPane.showMessageDialog(this,
                    "Event saved! You can view it in your Saved Events.",
                    "Event Saved", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Unable to save event. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Setters for dependency injection
    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    public void setSaveEventController(SaveEventController saveEventController) {
        this.saveEventController = saveEventController;
    }

    public void setSaveEventInteractor(SaveEventInteractor saveEventInteractor) {
        this.saveEventInteractor = saveEventInteractor;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Update from ViewModel if needed
        if (viewModel.getErrorMessage() != null && !viewModel.getErrorMessage().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    viewModel.getErrorMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getViewName() {
        return EventDescriptionViewModel.VIEW_NAME;
    }
}