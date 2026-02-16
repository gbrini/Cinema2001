package com.cinema.controller;

import com.cinema.model.Screening;
import com.cinema.model.ScreeningRecord;
import com.cinema.model.User;
import com.cinema.service.MovieService;
import com.cinema.service.ScreenService;
import com.cinema.service.ScreeningService;
import com.cinema.service.auth.UserSession;
import com.cinema.util.DialogCloseObserver;
import com.cinema.util.Observable;
import com.cinema.util.TimeSlot;
import com.cinema.view.EditScreeningPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ScreeningController implements Observable<DialogCloseObserver> {
    private final User user;
    private final EditScreeningPanel view;
    private final ArrayList<DialogCloseObserver> observers = new ArrayList<>();

    public ScreeningController(Screening screening) {
        this.user = UserSession.getInstance().getCurrentUser();
        this.view = new EditScreeningPanel(screening, user);

        this.fillComboBox();
        this.attachListeners();
    }

    private void fillComboBox() {
        this.view.setAvailableMovies(MovieService.getAllMovies());
        this.view.setAvailableScreens(ScreenService.getAllScreen());
        this.view.setAvailableTimeSlots(TimeSlot.SLOTS);
    }

    private void attachListeners() {
        this.view.addSaveListener(_ -> this.save());
    }

    public EditScreeningPanel getView() { return this.view; }

    private void save() {
        ScreeningRecord screeningRecord = this.view.getScreeningData();

        boolean isOk = ScreeningService.validateAndSchedule(screeningRecord);

        this.closeDialog();
        this.notifyObservers(isOk);
    }

    private void closeDialog() {
        Window window = SwingUtilities.getWindowAncestor(this.view);

        if (window instanceof JDialog dialog) {
            dialog.dispose();
        }
    }

    @Override
    public void addObserver(DialogCloseObserver observer) { this.observers.add(observer); }

    @Override
    public void deleteObserver(DialogCloseObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(boolean changedSaved) {
        for (DialogCloseObserver observer: this.observers) {
            observer.onDialogClosed(changedSaved);
        }
    }
}
