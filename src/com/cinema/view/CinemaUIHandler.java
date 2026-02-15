package com.cinema.view;

import com.cinema.controller.MovieListController;
import com.cinema.controller.ScreenListController;
import com.cinema.controller.ScreeningListController;
import com.cinema.controller.auth.LoginController;
import com.cinema.model.User;
import com.cinema.util.constants.DimensionConstants;
import com.cinema.util.constants.TextConstants;

import javax.swing.*;
import java.awt.*;

public class CinemaUIHandler extends JFrame {

    public CinemaUIHandler(User user) {
        super(TextConstants.TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DimensionConstants.MAIN_FRAME_DIMENSION);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton refreshButton = new JButton("Logout");
        refreshButton.addActionListener(e -> LoginController.getInstance().logout(this));
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.NORTH);
        JTabbedPane mainTabbedPane = new JTabbedPane();

        switch (user.getRole().getRoleId()) {
            case 1:
                mainTabbedPane.addTab(TextConstants.SCREEN_PANEL, new ScreenListController().getView());
                mainTabbedPane.addTab(TextConstants.MOVIE_PANEL, new MovieListController().getView());
                //See screening situation
                break;
            case 2:
                mainTabbedPane.addTab(TextConstants.MOVIE_PANEL, new MovieListController().getView());
                //Add/edit screening
                mainTabbedPane.addTab(TextConstants.SCREENING_PANEL, new ScreeningListController().getView());
                //I can add a movie only if the start time is after the last movie in that screen + that movie_duration + 15 min...
                //See screening situation
                break;
            case 3:
                //List movie
                mainTabbedPane.addTab(TextConstants.MOVIE_PANEL, new MovieListController().getView());
                //Buy tickets
                //Purchase history (cancel only before X hours)
            default:
                break;
        }

        add(mainTabbedPane, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
