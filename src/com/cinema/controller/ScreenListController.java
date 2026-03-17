package com.cinema.controller;

import com.cinema.model.Screen;
import com.cinema.model.User;
import com.cinema.service.MovieService;
import com.cinema.service.ScreenService;
import com.cinema.service.auth.UserSession;
import com.cinema.util.DialogCloseObserver;
import com.cinema.view.ListScreenPanel;
import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ScreenListController extends BaseController implements PanelActionListener<Screen>, DialogCloseObserver {
    private final User user;
    private final ListScreenPanel view;

    public ScreenListController() {
        this.user = UserSession.getInstance().getCurrentUser();
        this.view = new ListScreenPanel(this, this.user.getRole().getRoleId());
        this.onRefreshRequested();
    }

    public ListScreenPanel getView() { return this.view; }

    @Override
    public void onRefreshRequested() {
        try {
            ArrayList<Screen> screens = ScreenService.getAllScreen();
            this.view.setContentList(screens);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void onEditRequested(Screen item) {
        Window ownerWindow = SwingUtilities.getWindowAncestor(this.view);
        Frame ownerFrame = (ownerWindow instanceof Frame) ? (Frame) ownerWindow : JOptionPane.getRootFrame();

        JDialog dialog = new JDialog(ownerFrame, (item == null ? "Add" : "Edit") + " screen", true);

        SeatMapController seatMapController = new SeatMapController(item);
        seatMapController.addObserver(this);

        dialog.setContentPane(seatMapController.getView());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(ownerFrame);
        dialog.setVisible(true);
    }

    @Override
    public void onDeleteRequested(Screen item) {

    }

    @Override
    public void onDialogClosed(boolean changedSaved) {
        String message = changedSaved ? "Screen saved successfully" : "Editing cancelled or failed";
        JOptionPane.showMessageDialog(this.view, message);

        if (changedSaved) {
            this.onRefreshRequested();
        }
    }
}
