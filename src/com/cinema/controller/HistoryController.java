package com.cinema.controller;

import com.cinema.model.Ticket;
import com.cinema.service.auth.UserSession;
import com.cinema.util.DialogCloseObserver;
import com.cinema.view.ListHistoryPanel;
import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;

public class HistoryController extends BaseController implements PanelActionListener<Ticket>, DialogCloseObserver {
    private final ListHistoryPanel view;

    public HistoryController() {
        view = new ListHistoryPanel(this, UserSession.getInstance().getCurrentUser().getRole().getRoleId());

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

    }

    @Override
    public void onEditRequested(Ticket item) {

    }

    @Override
    public void onDeleteRequested(Ticket item) {

    }
}
