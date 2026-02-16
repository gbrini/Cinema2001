package com.cinema.view.abstracts;

import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class AbstractListPanel<T> extends JPanel {
    private final int roleId;
    protected final PanelActionListener<T> actionListener;
    protected final JPanel listContentPanel;
    private final JScrollPane scrollPane;

    public AbstractListPanel(PanelActionListener<T> actionListener, int roleId) {
        this.roleId = roleId;
        this.actionListener = actionListener;
        setLayout(new BorderLayout());

        this.listContentPanel = new JPanel();
        this.listContentPanel.setLayout(new BoxLayout(this.listContentPanel, BoxLayout.Y_AXIS));

        this.scrollPane = new JScrollPane(this.listContentPanel);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.setupControlButtons();
        add(this.scrollPane, BorderLayout.CENTER);
    }

    private void setupControlButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> actionListener.onRefreshRequested());
        buttonPanel.add(refreshButton);

        if (this.roleId == 1 || this.roleId == 2) {
            JButton addButton = new JButton("Add");
            addButton.addActionListener(e -> actionListener.onEditRequested(null));
            buttonPanel.add(addButton);
        }

        add(buttonPanel, BorderLayout.NORTH);
    }

    public void setContentList(ArrayList<T> items) {
        this.listContentPanel.removeAll();

        for (int i = 0; i < items.size(); i++) {
            this.listContentPanel.add(this.createItemRowPanel(items.get(i), i));
        }

        this.listContentPanel.revalidate();
        this.listContentPanel.repaint();
        this.scrollPane.revalidate();
    }

    protected abstract JPanel createItemRowPanel(T item, int index);

    protected int getRoleId() { return this.roleId; }
}
