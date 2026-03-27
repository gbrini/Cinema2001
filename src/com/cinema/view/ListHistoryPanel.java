package com.cinema.view;

import com.cinema.model.Ticket;
import com.cinema.model.TicketRecord;
import com.cinema.util.constants.DimensionConstants;
import com.cinema.util.constants.ThemeConstants;
import com.cinema.view.abstracts.AbstractListPanel;
import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;
import java.awt.*;

public class ListHistoryPanel extends AbstractListPanel<TicketRecord> {
    public ListHistoryPanel(PanelActionListener<TicketRecord> actionListener, int roleId) {
        super(actionListener, roleId);
    }

    @Override
    protected JPanel createItemRowPanel(TicketRecord item, int index) {
        JPanel ticketPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = DimensionConstants.STANDARD_INSETS;

        Color backgroundColor = (index % 2 == 0) ? ThemeConstants.EVEN_ROW_BG : ThemeConstants.ODD_ROW_BG;

        ticketPanel.setBackground(backgroundColor);

        gbc.gridx = 0;
        gbc.gridy = 0;
        ticketPanel.add(new JLabel(String.valueOf(item.ticket().getTicketId())), gbc);

        return ticketPanel;
    }
}
