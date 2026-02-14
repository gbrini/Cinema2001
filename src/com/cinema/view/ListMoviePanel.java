package com.cinema.view;

import com.cinema.model.Movie;
import com.cinema.util.constants.DimensionConstants;
import com.cinema.util.constants.ThemeConstants;
import com.cinema.view.abstracts.AbstractListPanel;
import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ListMoviePanel extends AbstractListPanel<Movie> {
    public ListMoviePanel(PanelActionListener<Movie> actionListener, String roleName) {
        super(actionListener, roleName);
    }

    @Override
    protected JPanel createItemRowPanel(Movie movie, int index) {
        JPanel moviePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = DimensionConstants.STANDARD_INSETS;

        Color backgroundColor = (index % 2 == 0) ? ThemeConstants.EVEN_ROW_BG : ThemeConstants.ODD_ROW_BG;

        moviePanel.setBackground(backgroundColor);

        gbc.gridx = 0;
        gbc.gridy = 0;
        moviePanel.add(new JLabel(movie.getTitle() + " (" + movie.getReleaseDate().toString() + ")"), gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 1;
        moviePanel.add(new JLabel(movie.getRating() + " | " + String.valueOf(movie.getDurationMinutes()) + " min" + " | " + movie.getGenre()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        moviePanel.add(new JLabel("Diretto da: " + movie.getDirector()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;

        JTextArea descriptionArea = new JTextArea(movie.getDescription());
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(true);
        descriptionArea.setBackground(new Color(0 ,0 ,0, 0));

        moviePanel.add(descriptionArea, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        controlPanel.setBackground(backgroundColor);

        if (Objects.equals(this.getRoleName(), "USER")) {
            JButton editButton = new JButton("Buy tickets");
            editButton.addActionListener(e -> this.actionListener.onEditRequested(movie));
            controlPanel.add(editButton);
        } else {
            JButton editButton = new JButton("Edit");
            editButton.addActionListener(e -> this.actionListener.onEditRequested(movie));
            controlPanel.add(editButton);
            JButton removeButton = new JButton("Remove");
            //removeButton.addActionListener(e -> this.actionListener.onRefreshRequested());
            controlPanel.add(removeButton);
        }

        moviePanel.add(controlPanel, gbc);

        return moviePanel;
    }
}
