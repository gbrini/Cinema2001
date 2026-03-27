package com.cinema.view;

import com.cinema.model.Ticket;
import com.cinema.view.abstracts.AbstractListPanel;
import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;

public class ListHistoryPanel extends AbstractListPanel<Ticket> {
    public ListHistoryPanel(PanelActionListener<Ticket> actionListener, int roleId) {
        super(actionListener, roleId);
    }

    @Override
    protected JPanel createItemRowPanel(Ticket item, int index) {
        return null;
    }
}
