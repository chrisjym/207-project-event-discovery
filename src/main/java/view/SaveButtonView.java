package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import entity.Event;
import interface_adapter.save_event.SaveEventController;

/**
 * View for the save event button.
 */
public class SaveButtonView extends JPanel {

    private static final int BUTTON_WIDTH = 160;
    private static final int BUTTON_HEIGHT = 45;
    private static final int BORDER_WIDTH_NORMAL = 3;
    private static final int BORDER_WIDTH_HOVER = 2;
    private static final int FONT_SIZE = 14;
    private static final int RESET_DELAY = 10000;

    private static final Color BASE_COLOR = new Color(59, 130, 246);
    private static final Color PRESSED_BACKGROUND_COLOR = new Color(37, 99, 235);
    private static final Color WHITE_COLOR = Color.WHITE;

    private static final String FONT_NAME = "Segoe UI";
    private static final String SAVE_BUTTON_TEXT = "Save Event";
    private static final String SAVED_BUTTON_TEXT = "Saved";
    private static final String YIPPEE_MESSAGE = "YIPPEE";

    private final String viewName = "save";
    private JButton saveButton;
    private Event event;
    private SaveEventController saveButtonController;
    private Timer resetTimer;

    /**
     * Constructor for SaveButtonView.
     */
    public SaveButtonView() {
        initializeButton();
        addActionListeners();
        add(saveButton);
    }

    private void initializeButton() {
        saveButton = new JButton(SAVE_BUTTON_TEXT);
        saveButton.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE));
        saveButton.setForeground(BASE_COLOR);
        saveButton.setBackground(WHITE_COLOR);
        saveButton.setBorder(BorderFactory.createLineBorder(BASE_COLOR, BORDER_WIDTH_NORMAL));
        saveButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        saveButton.setContentAreaFilled(false);
        saveButton.setOpaque(true);
    }

    private void addActionListeners() {
        addMouseListeners();
        addActionListener();
    }

    private void addMouseListeners() {
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent mouseEvent) {
                handleMouseEntered();
            }

            @Override
            public void mouseExited(final MouseEvent mouseEvent) {
                handleMouseExited();
            }

            @Override
            public void mousePressed(final MouseEvent mouseEvent) {
                handleMousePressed();
            }

            @Override
            public void mouseReleased(final MouseEvent mouseEvent) {
                handleMouseReleased(mouseEvent);
            }
        });
    }

    private void handleMouseEntered() {
        saveButton.setBackground(BASE_COLOR);
        saveButton.setForeground(WHITE_COLOR);
        saveButton.setBorder(BorderFactory.createLineBorder(WHITE_COLOR, BORDER_WIDTH_HOVER));
    }

    private void handleMouseExited() {
        saveButton.setBackground(WHITE_COLOR);
        saveButton.setForeground(BASE_COLOR);
        saveButton.setBorder(BorderFactory.createLineBorder(BASE_COLOR, BORDER_WIDTH_NORMAL));
    }

    private void handleMousePressed() {
        saveButton.setBackground(PRESSED_BACKGROUND_COLOR);
        saveButton.setBorder(BorderFactory.createLineBorder(PRESSED_BACKGROUND_COLOR,
                BORDER_WIDTH_HOVER));
    }

    private void handleMouseReleased(final MouseEvent mouseEvent) {
        if (saveButton.contains(mouseEvent.getPoint())) {
            saveButton.setBackground(BASE_COLOR);
            saveButton.setBorder(BorderFactory.createLineBorder(BASE_COLOR, BORDER_WIDTH_HOVER));
        }
        else {
            saveButton.setBackground(WHITE_COLOR);
            saveButton.setForeground(BASE_COLOR);
            saveButton.setBorder(BorderFactory.createLineBorder(BASE_COLOR, BORDER_WIDTH_HOVER));
        }
    }

    private void addActionListener() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                handleSaveAction();
            }
        });
    }

    private void handleSaveAction() {
        if (saveButtonController != null && event != null) {
            saveButtonController.saveEvent(event);
            saveButton.setText(SAVED_BUTTON_TEXT);
            saveButton.setEnabled(false);
            startResetTimer();
        }
    }

    private void startResetTimer() {
        if (resetTimer != null && resetTimer.isRunning()) {
            resetTimer.stop();
        }

        resetTimer = new Timer(RESET_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                resetButtonState();
            }
        });
        resetTimer.setRepeats(false);
        resetTimer.start();
    }

    private void resetButtonState() {
        if (saveButton != null) {
            saveButton.setText(SAVE_BUTTON_TEXT);
            saveButton.setEnabled(true);
            saveButton.setForeground(BASE_COLOR);
            saveButton.setBackground(WHITE_COLOR);
            saveButton.setBorder(BorderFactory.createLineBorder(BASE_COLOR, BORDER_WIDTH_NORMAL));
        }
    }

    /**
     * Sets the event to be saved.
     *
     * @param newEvent the event to set
     */
    public void setEvent(final Event newEvent) {
        this.event = newEvent;
    }

    /**
     * Sets the save event controller.
     *
     * @param controller the controller to set
     */
    public void setSaveEventController(final SaveEventController controller) {
        this.saveButtonController = controller;
    }

    /**
     * Gets the view name.
     *
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }
}
