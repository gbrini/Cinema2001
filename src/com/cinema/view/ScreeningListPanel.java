package com.cinema.view;

import com.cinema.model.Screening;
import com.cinema.view.abstracts.AbstractListPanel;
import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;

public class ScreeningListPanel extends AbstractListPanel<Screening> {
    public ScreeningListPanel(PanelActionListener<Screening> actionListener, int roleId) {
        super(actionListener, roleId);
    }

    @Override
    protected JPanel createItemRowPanel(Screening item, int index) {
        return null;
    }
}
