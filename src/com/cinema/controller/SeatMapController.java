package com.cinema.controller;

import com.cinema.model.Screen;
import com.cinema.model.Seat;
import com.cinema.model.SeatEditor;
import com.cinema.model.User;
import com.cinema.service.MovieService;
import com.cinema.service.ScreenService;
import com.cinema.service.SeatService;
import com.cinema.service.auth.UserSession;
import com.cinema.util.DialogCloseObserver;
import com.cinema.util.Observable;
import com.cinema.view.SeatComponent;
import com.cinema.view.admin.SeatMapEditorPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SeatMapController extends BaseController implements Observable<DialogCloseObserver> {
    private final User user;
    private final SeatMapEditorPanel view;
    private final Screen screen;
    private final List<DialogCloseObserver> observers = new ArrayList<>();

    public SeatMapController(Screen screen) {
        this.user = UserSession.getInstance().getCurrentUser();
        this.screen = screen;

        if (this.screen == null) {
            this.view = new SeatMapEditorPanel(0, this.loadInitialModels(0), "", this.user);
        } else {
            this.view = new SeatMapEditorPanel(this.screen.getScreenId(), this.loadInitialModels(this.screen.getScreenId()), this.screen.getScreenName(), this.user);
        }

        this.attachListeners();
    }

    private List<SeatEditor> loadInitialModels(int screenId) {
        ArrayList<Seat> seats = new ArrayList<>();

        try {
            seats = SeatService.getSeatsByScreenId(screenId);
        } catch (Exception e) {
            handleException(e);
        }

        if(seats.isEmpty()) {
            return this.generateDefaultLayout();
        }

        return seats.stream().map(this::mapEditorToModel).collect(Collectors.toList());
    }

    private List<SeatEditor> generateDefaultLayout() {
        List<SeatEditor> defaults = new ArrayList<>();

        char currentRow = 'A';
        for (int r = 0; r < 5; r++) {
            String rowName = String.valueOf(currentRow++);
            for (int c = 1; c < 10 + 1; c++) {
                defaults.add(new SeatEditor(rowName, c));
            }
        }

        return defaults;
    }

    private SeatEditor mapEditorToModel(Seat seat) {
        return new SeatEditor(
            seat.getSeatId(),
            seat.getSeatRow(),
            seat.getSeatNumber(),
            seat.isVip(),
            seat.isHandicap(),
            seat.isActive(),
            false,
            false
        );
    }

    private void attachListeners() {
        this.view.addSeatSelectionListener(e -> {
            SeatComponent seatComponent = (SeatComponent) e.getSource();

            seatComponent.getSeatEditor().setSelected(seatComponent.isSelected());

            seatComponent.updateAppearance();
        });

        this.view.addVipListener(e -> this.applyFlag(true, false));
        this.view.addHandicapListener(e -> this.applyFlag(false, true));
        this.view.addRemovedListener(e -> this.removeSeats());
        this.view.addClearListener(e -> this.clearSeats());
        this.view.addSaveListener(e -> this.saveLayout());
    }

    private void applyFlag(boolean isVip, boolean isHandicap) {
        for (SeatComponent seatComponent: this.view.getSeatComponents()) {
            SeatEditor seatEditor = seatComponent.getSeatEditor();

            if (seatEditor.isSelected()) {
                seatEditor.setVip(isVip);
                seatEditor.setHandicap(isHandicap);

                seatEditor.setSelected(false);
                seatComponent.setSelected(false);

                seatComponent.updateAppearance();
            }
        }
    }

    private void removeSeats() {
        for (SeatComponent seatComponent: this.view.getSeatComponents()) {
            SeatEditor seatEditor = seatComponent.getSeatEditor();

            if (seatEditor.isSelected()) {
                seatEditor.setActive(false);
                seatEditor.setVip(false);
                seatEditor.setHandicap(false);

                seatEditor.setSelected(false);
                seatComponent.setSelected(false);

                seatComponent.updateAppearance();
            }
        }
    }

    private void clearSeats() {
        for (SeatComponent seatComponent: this.view.getSeatComponents()) {
            SeatEditor seatEditor = seatComponent.getSeatEditor();

            if (seatEditor.isSelected()) {
                seatEditor.setActive(true);
                seatEditor.setVip(false);
                seatEditor.setHandicap(false);

                seatEditor.setSelected(false);
                seatComponent.setSelected(false);

                seatComponent.updateAppearance();
            }
        }
    }

    private void saveLayout() {
        List<SeatEditor> seatsEditorToSave = this.view.getSeatComponents().stream().map(SeatComponent::getSeatEditor).toList();
        ArrayList<Seat> seatsToAdd = new ArrayList<>();
        String screenName = this.view.getScreenName();
        int screenId = 0;

        if (this.screen == null) {
            Screen screen = new Screen(screenName, seatsEditorToSave.size(), false);

            try {
                screenId = ScreenService.addScreen(screen);
            } catch (Exception e) {
                handleException(e);
            }

            if (screenId < 1) {
                JOptionPane.showMessageDialog(this.view, "There was an error saving this screen.");
                return;
            }
        } else {
            screenId = this.screen.getScreenId();
        }

        for (SeatEditor seatEditor: seatsEditorToSave) {
            seatsToAdd.add(new Seat(
                    screenId,
                    seatEditor.getSeatRow(),
                    seatEditor.getSeatNumber(),
                    seatEditor.isVip(),
                    seatEditor.isHandicap(),
                    seatEditor.isActive()
            ));
        }

        try {
            boolean addedSeats = SeatService.upsertSeats(seatsToAdd);
            this.notifyObservers(addedSeats);
        } catch (Exception e) {
            handleException(e);
        }

        this.closeDialog();
    }

    public SeatMapEditorPanel getView() { return view; }

    private void closeDialog() {
        Window window = SwingUtilities.getWindowAncestor(this.view);

        if (window instanceof JDialog dialog) {
            dialog.dispose();
        }
    }

    @Override
    public void addObserver(DialogCloseObserver observer) { this.observers.add(observer); }

    @Override
    public void deleteObserver(DialogCloseObserver observer) { this.observers.remove(observer); }

    @Override
    public void notifyObservers(boolean changedSaved) {
        for (DialogCloseObserver observer: this.observers) {
            observer.onDialogClosed(changedSaved);
        }
    }
}
