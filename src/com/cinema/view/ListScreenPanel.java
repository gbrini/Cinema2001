package com.cinema.view;

import com.cinema.model.Screen;
import com.cinema.util.constants.ThemeConstants;
import com.cinema.view.abstracts.AbstractListPanel;
import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;
import java.awt.*;

public class ListScreenPanel extends AbstractListPanel<Screen> {

    public ListScreenPanel(PanelActionListener<Screen> actionListener, String roleName) {
        super(actionListener, roleName);
    }

    @Override
    protected JPanel createItemRowPanel(Screen item, int index) {
        JPanel screenPanel = new JPanel(new BorderLayout(10, 0));
        Color backgroundColor = (index % 2 == 0) ? ThemeConstants.EVEN_ROW_BG : ThemeConstants.ODD_ROW_BG;

        screenPanel.setBackground(backgroundColor);
        screenPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel infoLabel = new JLabel(item.getScreenName() + " (" + item.getcapacity() + ") seats");
        screenPanel.add(infoLabel, BorderLayout.WEST);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        controlPanel.setBackground(backgroundColor);

        JButton editButton = new JButton("Edit Map");
        editButton.addActionListener(e -> this.actionListener.onEditRequested(item));

        controlPanel.add(editButton);

        screenPanel.add(controlPanel);

        return screenPanel;
    }
}
