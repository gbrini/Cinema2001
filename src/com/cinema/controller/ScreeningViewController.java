package com.cinema.controller;

import com.cinema.model.ScreeningRecord;
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
    private final ScreeningRecord screeningRecord;

    public ScreeningViewController(ScreeningRecord screeningRecord) {
        this.user = UserSession.getInstance().getCurrentUser();
        this.screeningRecord = screeningRecord;
        this.seats = SeatService.getSeatsStatusByScreeningId(screeningRecord.screening().getScreeningId());
    }

    public JPanel getView() {
        SeatMapPanel seatMapPanel = new SeatMapPanel(this.seats, this.user.getRole().getRoleId() != 1, this.user, this.screeningRecord);
        return seatMapPanel.getView();
    }
}
