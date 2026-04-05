package com.cinema.view.abstracts;

import com.cinema.model.User;
import com.cinema.service.auth.UserSession;
import com.cinema.util.constants.TextConstants;
import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public abstract class AbstractTabularGroupView<K, V, Z> extends JPanel {
    protected final PanelActionListener<Z> actionListener;
    protected final JTabbedPane tabbedPane;

    public AbstractTabularGroupView(PanelActionListener<Z> actionListener) {
        setLayout(new BorderLayout());
        this.actionListener = actionListener;
        this.tabbedPane = new JTabbedPane();

        this.setupControlButtons();
        add(this.tabbedPane, BorderLayout.CENTER);
    }

    private void setupControlButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton refreshButton = new JButton(TextConstants.REFRESH_TXT);
        refreshButton.addActionListener(e -> actionListener.onRefreshRequested());
        buttonPanel.add(refreshButton);

        User user = UserSession.getInstance().getCurrentUser();

        if (user.getRole().getRoleId() == 1) {
            JButton addButton = new JButton(TextConstants.ADD_TXT);
            addButton.addActionListener(e -> actionListener.onEditRequested(null));
            buttonPanel.add(addButton);
        }

        add(buttonPanel, BorderLayout.NORTH);
    }

    public void setGroupedContent(HashMap<K, ArrayList<V>> groupedData, boolean canEdit) {
        tabbedPane.removeAll();

        List<K> sortedKeys = new ArrayList<>(groupedData.keySet());
        Collections.sort((List<Comparable>) sortedKeys);

        for (K key: sortedKeys) {
            String tabTitle = this.getTabTitle(key);
            List<V> items = groupedData.get(key);

            JScrollPane contentPanel = this.createGroupListPanel(key, items, canEdit);

            if (Objects.equals(tabTitle, LocalDate.now().toString())) {
                tabTitle = "Oggi";
            } else {
                String[] dates = tabTitle.split("-");
                tabTitle = dates[2] + "/" + dates[1] + "/" + dates[0];
            }

            tabbedPane.addTab(tabTitle, contentPanel);
        }

    }

    protected abstract String getTabTitle(K key);

    protected abstract JScrollPane createGroupListPanel(K key, List<V> items, boolean canEdit);
}
