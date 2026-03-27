package com.cinema.controller;

import com.cinema.model.Ticket;
import com.cinema.model.TicketRecord;
import com.cinema.model.User;
import com.cinema.service.TicketService;
import com.cinema.service.auth.UserSession;
import com.cinema.util.DialogCloseObserver;
import com.cinema.view.ListHistoryPanel;
import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;
import java.util.ArrayList;

public class HistoryController extends BaseController implements PanelActionListener<TicketRecord>, DialogCloseObserver {
    private final User user;
    private final ListHistoryPanel view;

    public HistoryController() {
        user = UserSession.getInstance().getCurrentUser();
        view = new ListHistoryPanel(this, user.getRole().getRoleId());

        this.onRefreshRequested();
    }

    public ListHistoryPanel getView() {
        return view;
    }

    @Override
    public void onDialogClosed(boolean changedSaved) {

    }

    @Override
    public void onRefreshRequested() {
        ArrayList<TicketRecord> items = TicketService.getTicketsByUser(user.getUserId());
        this.view.setContentList(items);
    }

    @Override
    public void onEditRequested(Ticket item) {

    }

    @Override
    public void onDeleteRequested(Ticket item) {

    }
}
