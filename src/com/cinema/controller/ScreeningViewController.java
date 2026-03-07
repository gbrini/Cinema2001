package com.cinema.controller;

import com.cinema.model.SeatEditor;
import com.cinema.model.User;
import com.cinema.service.SeatService;
import com.cinema.service.auth.UserSession;
import com.cinema.view.SeatMapPanel;

import javax.swing.*;
import java.util.ArrayList;

public class ScreeningViewController {
    private final User user;
    private final ArrayList<SeatEditor> seats;

    public ScreeningViewController(int screeningId) {
        this.user = UserSession.getInstance().getCurrentUser();
        this.seats = SeatService.getSeatsStatusByScreeningId(screeningId);
    }

    public JPanel getView() {
        SeatMapPanel seatMapPanel = new SeatMapPanel(this.seats, false, this.user);
        return seatMapPanel.getView();
    }
}
