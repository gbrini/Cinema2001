package com.cinema.view.client;

import com.cinema.model.TicketRecord;
import com.cinema.util.constants.DimensionConstants;
import com.cinema.util.constants.ThemeConstants;
import com.cinema.view.abstracts.AbstractListPanel;
import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ListHistoryPanel extends AbstractListPanel<TicketRecord> {
    public ListHistoryPanel(PanelActionListener<TicketRecord> actionListener, int roleId) {
        super(actionListener, roleId);
    }

    @Override
    protected JPanel createItemRowPanel(TicketRecord item, int index) {
        JPanel ticketPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = DimensionConstants.STANDARD_INSETS;
        gbc.anchor = GridBagConstraints.WEST;

        Color backgroundColor = (index % 2 == 0) ? ThemeConstants.EVEN_ROW_BG : ThemeConstants.ODD_ROW_BG;
        ticketPanel.setBackground(backgroundColor);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel titleLabel = new JLabel(item.movie().getTitle());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        ticketPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        ticketPanel.add(new JLabel(item.screening().getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))), gbc);

        gbc.gridx = 2;
        String posto = item.seat().getSeatRow() + item.seat().getSeatNumber()
                + (item.seat().isVip() ? " · VIP" : "")
                + (item.seat().isHandicap() ? " · Handicap" : "");
        ticketPanel.add(new JLabel(posto), gbc);

        gbc.gridx = 3;
        ticketPanel.add(new JLabel(item.ticketType().getTypeName()), gbc);

        gbc.gridx = 4;
        ticketPanel.add(new JLabel(String.format("%.2f €", item.ticket().getFinalPrice())), gbc);

        gbc.gridx = 5;
        if (item.screening().getStartTime().minusHours(1).isAfter(LocalDateTime.now())) {
            JButton cancelBtn = new JButton("Cancella");
            cancelBtn.setForeground(Color.RED);
            cancelBtn.addActionListener(e -> actionListener.onDeleteRequested(item));
            ticketPanel.add(cancelBtn, gbc);
        }

        return ticketPanel;
    }
}
