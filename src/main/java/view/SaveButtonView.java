package view;

import entity.Event;
import interface_adapter.save_event.SaveEventController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SaveButtonView extends JPanel {

    private final String viewName = "save";
    private JButton saveButton = null;
    private Event event;
    private SaveEventController saveButtonController = null;
    private Timer resetTimer;


    public SaveButtonView() {
        Color base = new Color(59, 130, 246);

        saveButton = new JButton("Save Event");
        saveButton.setFont(new Font("SegoeUI", Font.BOLD, 14));
        saveButton.setForeground(new Color(59, 130, 246));
        saveButton.setBackground(Color.WHITE);
        saveButton.setBorder(BorderFactory.createLineBorder(base, 3));
        saveButton.setPreferredSize(new Dimension(160, 45));

        saveButton.setContentAreaFilled(false);
        saveButton.setOpaque(true);

        addActionListeners();

        add(saveButton);


    }

    private void addActionListeners() {
        Color base = new Color(59, 130, 246);
        Color pressedBackground = new Color(37, 99, 235);


        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveButton.setBackground(base);
                saveButton.setForeground(Color.WHITE);
                saveButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                saveButton.setBackground(Color.WHITE);
                saveButton.setForeground(base);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Visual feedback when pressed
                saveButton.setBackground(pressedBackground);
                saveButton.setBorder(BorderFactory.createLineBorder(pressedBackground, 2));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Return to hover state if still hovering
                if (saveButton.contains(e.getPoint())) {
                    saveButton.setBackground(base);
                    saveButton.setBorder(BorderFactory.createLineBorder(base, 2));
                } else {
                    saveButton.setBackground(Color.WHITE);
                    saveButton.setForeground(base);
                    saveButton.setBorder(BorderFactory.createLineBorder(base, 2));
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (saveButtonController != null && event != null) {
                    System.out.println("YIPPEE");
                    saveButtonController.saveEvent(event);
                    saveButton.setText("Saved");
                    saveButton.setEnabled(false);

                    if (resetTimer != null && resetTimer.isRunning()) {
                        resetTimer.stop();
                    }

                    resetTimer = new Timer(10000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            resetButtonState();
                        }
                    });
                    resetTimer.setRepeats(false); // Only run once
                    resetTimer.start();

                }
            }
        });
    }

    private void resetButtonState() {
        if (saveButton != null) {
            saveButton.setText("Save Event");
            saveButton.setEnabled(true);

            Color base = new Color(59, 130, 246);
            saveButton.setForeground(base);
            saveButton.setBackground(Color.WHITE);
            saveButton.setBorder(BorderFactory.createLineBorder(base, 3));
        }
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setSaveEventController(SaveEventController controller) {
        this.saveButtonController = controller;
    }

}