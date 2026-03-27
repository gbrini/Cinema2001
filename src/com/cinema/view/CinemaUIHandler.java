package com.cinema.view;

import com.cinema.controller.MovieListController;
import com.cinema.controller.ScreenListController;
import com.cinema.controller.ScreeningListController;
import com.cinema.controller.auth.LoginController;
import com.cinema.model.User;
import com.cinema.service.auth.UserSession;
import com.cinema.util.constants.DimensionConstants;
import com.cinema.util.constants.TextConstants;

import javax.swing.*;
import java.awt.*;

public class CinemaUIHandler extends JFrame {

    public CinemaUIHandler() {
        super(TextConstants.TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DimensionConstants.MAIN_FRAME_DIMENSION);

        User user = UserSession.getInstance().getCurrentUser();

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JLabel label = new JLabel(user.getFirstName() + " - " + user.getRole().getRoleName());
        infoPanel.add(label);

        JButton refreshButton = new JButton("Logout");
        refreshButton.addActionListener(e -> LoginController.getInstance().logout(this));
        infoPanel.add(refreshButton);

        add(infoPanel, BorderLayout.NORTH);
        JTabbedPane mainTabbedPane = new JTabbedPane();

        switch (user.getRole().getRoleId()) {
            case 1:
                mainTabbedPane.addTab(TextConstants.SCREEN_PANEL, new ScreenListController().getView());
                mainTabbedPane.addTab(TextConstants.MOVIE_PANEL, new MovieListController().getView());
                mainTabbedPane.addTab(TextConstants.SCREENING_PANEL, new ScreeningListController().getView());
                break;
//            case 2:
//                mainTabbedPane.addTab(TextConstants.MOVIE_PANEL, new MovieListController().getView());
//                mainTabbedPane.addTab(TextConstants.SCREENING_PANEL, new ScreeningListController().getView());
//                break;
            case 3:
                mainTabbedPane.addTab(TextConstants.MOVIE_PANEL, new MovieListController().getView());
                mainTabbedPane.addTab(TextConstants.SCREENING_PANEL, new ScreeningListController().getView());
                mainTabbedPane.addTab(TextConstants.HISTORY_PANEL, new ScreeningListController().getView());
            default:
                break;
        }

        add(mainTabbedPane, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
