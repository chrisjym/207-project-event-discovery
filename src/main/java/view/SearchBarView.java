package view;

import interface_adapter.search.SearchController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SearchBarView extends JPanel {
    private JTextField searchField;
    private Color borderColor = new Color(220, 220, 220);
    private Color focusBorderColor = new Color(180, 180, 180);
    private Color backgroundColor = new Color(250, 250, 250);
    private boolean isFocused = false;
    private String searchDescription = null;
    private SearchController searchController = null;


    public SearchBarView(String text) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 60));
        setOpaque(false);
        this.searchDescription = text;
        add(initComponents(searchDescription));
    }

    private JPanel initComponents(String text) {
        JPanel container = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw subtle shadow
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(1, 3, getWidth() - 2, getHeight() - 3, 30, 30);

                // Draw background
                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 4, 30, 30);

                // Draw border
                g2.setColor(isFocused ? focusBorderColor : borderColor);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 4, 30, 30);

                g2.dispose();
            }
        };
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(8, 20, 8, 20));

        searchField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocused) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(170, 170, 170));
                    g2.setFont(getFont());
                    g2.drawString(text, 5, getHeight() / 2 + 6);
                    g2.dispose();
                }
            }
        };
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        searchField.setBorder(new EmptyBorder(8, 5, 8, 5));
        searchField.setOpaque(false);
        searchField.setForeground(new Color(100, 100, 100));

        container.add(searchField, BorderLayout.CENTER);
        return container;
    }

    public void setSearchController(SearchController controller) {
        this.searchController = controller;
    }



}
