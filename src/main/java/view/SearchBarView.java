package view;

import entity.Location;
import interface_adapter.search.SearchController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SearchBarView extends JPanel {
    private JTextField searchField;
    private Color borderColor = new Color(220, 220, 220);
    private Color focusBorderColor = new Color(180, 180, 180);
    private Color backgroundColor = new Color(250, 250, 250);
    private boolean isFocused = false;
    private String searchDescription = null;
    private SearchController searchController = null;
    private Location location = null;


    public SearchBarView(String text, Location userLocation) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 30));
        setOpaque(false);
        this.searchDescription = text;
        this.location = userLocation;
        add(initComponents(searchDescription));
    }

    private JPanel initComponents(String text) {
        JPanel container = new JPanel(new BorderLayout()) {
            // Written with the help of generative AI
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(1, 3, getWidth() - 2, getHeight() - 3, 30, 30);

                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 4, 30, 30);

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
        searchField.setBackground(new Color(0, 0, 0, 0));

        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                isFocused = false;
                container.repaint();
            }

            public void focusLost(FocusEvent e) {
                isFocused = false;
                container.repaint();
            }
        });

        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (searchController != null) {
                    String query = searchField.getText().trim();
                    if (!query.isEmpty()) {
                        // Execute search with "name" as search type: currently has a default Location
                        searchController.execute(query, "name", location);
                    }
                }
            }
        });

        container.add(searchField, BorderLayout.CENTER);
        return container;
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public void setSearchController(SearchController controller) {
        this.searchController = controller;
    }



}
