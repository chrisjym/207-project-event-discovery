package view;

import entity.Event;
import entity.EventCategory;
import interface_adapter.save_event.SaveEventController;
import interface_adapter.save_event.SaveEventViewModel;
import use_case.save_event.SaveEventInteractor;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class SaveEventsView extends JPanel implements PropertyChangeListener {
    private String viewName = "save events";
    private SaveEventViewModel saveEventsViewModel = null;
    private List<Event> savedEvents = new ArrayList<>();
    private JPanel eventsContainer;
    private SaveEventController saveEventController = null;
    private SaveEventInteractor saveEventInteractor = null;

    public SaveEventsView(SaveEventViewModel saveEventsViewModel, SaveEventInteractor saveEventInteractor) {
        this.saveEventsViewModel = saveEventsViewModel;
        this.saveEventInteractor = saveEventInteractor;
        saveEventsViewModel.addPropertyChangeListener(this);

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(249, 250, 251));

        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);

        eventsContainer = new JPanel();
        eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));
        eventsContainer.setBackground(new Color(249, 250, 251));
        eventsContainer.setBorder(new EmptyBorder(20, 40, 20, 40));

        JScrollPane scrollPane = new JScrollPane(eventsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
                new EmptyBorder(20, 40, 20, 40)
        ));

        JLabel title = new JLabel("My Saved Events");

        title.setFont(new Font("SegoeUI", Font.BOLD, 24));
        title.setForeground(new Color(17, 24, 39));
        title.setOpaque(false);

        JButton backButton = new JButton("← Back to Events");
        backButton.setFont(new Font("SegoeUI", Font.PLAIN, 13));
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveEventController.switchToDashboardView();
            }
        });

        header.add(title, BorderLayout.WEST);
        header.add(backButton, BorderLayout.EAST);
        return header;

    }

    public void refreshEventsList() {
        eventsContainer.removeAll();

        List<Event> savedEvents = saveEventInteractor.getSavedEvents();


        if (savedEvents.isEmpty()) {
            JPanel emptyPanel = new JPanel(new FlowLayout());
            emptyPanel.setBackground(new Color(249, 250, 251));

            JLabel emptyLabel = new JLabel("No saved events yet. Start exploring and save events you're interested in!");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            emptyLabel.setForeground(new Color(107, 114, 128));

            emptyPanel.add(emptyLabel);
            eventsContainer.add(emptyPanel);
        } else {
            for (Event event : savedEvents) {
                JPanel eventCard = createEventCard(event);
                eventsContainer.add(eventCard);
                eventsContainer.add(Box.createVerticalStrut(15));
            }
        }

        eventsContainer.revalidate();
        eventsContainer.repaint();
    }

    private JPanel createEventCard(Event event) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color categoryColor = getCategoryColor(event.getCategory());
                GradientPaint gradient = new GradientPaint(
                        0, 0, categoryColor.darker(),
                        getWidth(), getHeight(), categoryColor
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 36));
                FontMetrics fm = g2.getFontMetrics();
                String icon = "Event";
                int x = (getWidth() - fm.stringWidth(icon)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 4;
                g2.drawString(icon, x, y);
            }
        };
        imagePanel.setPreferredSize(new Dimension(100, 100));
        imagePanel.setOpaque(false);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);

        Color categoryColor = getCategoryColor(event.getCategory());
        JLabel categoryLabel = new JLabel(event.getCategory().toString());
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        categoryLabel.setForeground(categoryColor);
        categoryLabel.setOpaque(true);
        categoryLabel.setBackground(new Color(categoryColor.getRed(),
                categoryColor.getGreen(),
                categoryColor.getBlue(), 40));
        categoryLabel.setBorder(new EmptyBorder(4, 10, 4, 10));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel(event.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(new Color(17, 24, 39));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateLabel = new JLabel("Date: " + event.getStartTime().toString());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(107, 114, 128));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Location
        JLabel locationLabel = new JLabel("Venue: " + event.getLocation().getAddress());
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        locationLabel.setForeground(new Color(107, 114, 128));
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        detailsPanel.add(categoryLabel);
        detailsPanel.add(Box.createVerticalStrut(8));
        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createVerticalStrut(6));
        detailsPanel.add(dateLabel);
        detailsPanel.add(Box.createVerticalStrut(4));
        detailsPanel.add(locationLabel);

        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setOpaque(false);


        JButton removeButton = createActionButton("Remove", new Color(239, 68, 68));
        removeButton.setBorder(new CompoundBorder(
                new LineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(5, 20, 5, 20)
        ));

        removeButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                    "Remove \"" + event.getName() + "\" from saved events?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                savedEvents.remove(event);
                refreshEventsList();
            }
        });

        actionsPanel.add(Box.createVerticalStrut(35));
        actionsPanel.add(removeButton);

        card.add(imagePanel, BorderLayout.WEST);
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(actionsPanel, BorderLayout.EAST);

        return card;
    }

    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(color);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private Color getCategoryColor(EventCategory category) {
        switch (category) {
            case MUSIC:
                return new Color(147, 51, 234); // Purple
            case SPORTS:
                return new Color(59, 130, 246); // Blue
            case ARTS_THEATRE:
                return new Color(236, 72, 153); // Pink
            case MISCELLANEOUS:
            default:
                return new Color(107, 114, 128); // Gray
        }
    }

    public void addSavedEvent(Event event) {
        if (!savedEvents.contains(event)) {
            savedEvents.add(event);
            refreshEventsList();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("event".equals(evt.getPropertyName())) {
            refreshEventsList();
        } else if ("error".equals(evt.getPropertyName())) {
            System.out.println("Event was null, floundering...");
        }
    }

    public void setSaveEventController(SaveEventController saveEventController) {
        this.saveEventController = saveEventController;
    }

}
