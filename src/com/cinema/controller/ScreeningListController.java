package com.cinema.controller;

import com.cinema.model.Screening;
import com.cinema.model.ScreeningRecord;
import com.cinema.model.User;
import com.cinema.service.MovieService;
import com.cinema.service.ScreenService;
import com.cinema.service.ScreeningService;
import com.cinema.service.auth.UserSession;
import com.cinema.util.DialogCloseObserver;
import com.cinema.util.TimeSlot;
import com.cinema.view.ListScreeningPanel;
import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ScreeningListController extends BaseController implements PanelActionListener<Screening>, DialogCloseObserver {
    private final User user;
    private final ListScreeningPanel view;

    public ScreeningListController() {
        this.user = UserSession.getInstance().getCurrentUser();
        this.view = new ListScreeningPanel(this, this.user);

        this.onRefreshRequested();
    }

    public ListScreeningPanel getView() { return this.view; }

    @Override
    public void onRefreshRequested() {
        Instant todayInstant = Instant.now();
        Instant nextWeekInstant = todayInstant.plus(7, ChronoUnit.DAYS);

        try {
            HashMap<LocalDate, ArrayList<ScreeningRecord>> screenings = ScreeningService.getScreeningByDateRange(Date.from(todayInstant), Date.from(nextWeekInstant));
            this.view.setGroupedContent(screenings, true);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void onEditRequested(Screening item) {
        Window ownerWindow = SwingUtilities.getWindowAncestor(this.view);
        Frame ownerFrame = (ownerWindow instanceof Frame) ? (Frame) ownerWindow : JOptionPane.getRootFrame();

        JDialog dialog = new JDialog(ownerFrame, (item == null ? "Add" : "Edit") + " screening", true);

        ScreeningController screeningController = new ScreeningController(item);
        screeningController.addObserver(this);

        dialog.setContentPane(screeningController.getView());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(ownerFrame);
        dialog.setVisible(true);
    }

    @Override
    public void onDeleteRequested(Screening item) {

    }

    @Override
    public void onDialogClosed(boolean changedSaved) {
        if (changedSaved) {
            this.onRefreshRequested();
        } else {
            JOptionPane.showMessageDialog(this.view, "There was an error saving the screening.");
        }
    }
}
