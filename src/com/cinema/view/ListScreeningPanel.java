package com.cinema.view;

import com.cinema.controller.ScreeningViewController;
import com.cinema.model.*;
import com.cinema.util.constants.DimensionConstants;
import com.cinema.util.constants.ThemeConstants;
import com.cinema.view.abstracts.AbstractTabularGroupView;
import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ListScreeningPanel extends AbstractTabularGroupView<LocalDate, ScreeningRecord, Screening> {
    private final User user;

    public ListScreeningPanel(PanelActionListener<Screening> actionListener, User user) {
        super(actionListener);
        this.user = user;
    }

    @Override
    protected String getTabTitle(LocalDate key) {
        return key.toString();
    }

    @Override
    protected JScrollPane createGroupListPanel(LocalDate key, List<ScreeningRecord> items, boolean canEdit) {
        JPanel listContent = new JPanel();
        listContent.setLayout(new BoxLayout(listContent, BoxLayout.Y_AXIS));

        LinkedHashMap<String, List<ScreeningRecord>> grouped = new LinkedHashMap<>();
        for (ScreeningRecord sr : items) {
            grouped.computeIfAbsent(sr.movie().getTitle(), k -> new ArrayList<>()).add(sr);
        }

        int index = 0;
        for (List<ScreeningRecord> group : grouped.values()) {
            listContent.add(this.createGroupedScreeningRow(group, index++));
        }

        return new JScrollPane(listContent);
    }

    private JPanel createGroupedScreeningRow(List<ScreeningRecord> group, int index) {
        JPanel screeningPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = DimensionConstants.STANDARD_INSETS;

        Color backgroundColor = (index % 2 == 0) ? ThemeConstants.EVEN_ROW_BG : ThemeConstants.ODD_ROW_BG;
        screeningPanel.setBackground(backgroundColor);

        ScreeningRecord first = group.get(0);
        Movie movie = first.movie();
        Screen screen = first.screen();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = group.size();
        screeningPanel.add(new JLabel(movie.getTitle()), gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        for (int i = 0; i < group.size(); i++) {
            ScreeningRecord sr = group.get(i);
            gbc.gridx = i;
            JButton timeButton = new JButton(sr.screening().getStartTimeT().toString());
            timeButton.addActionListener(e -> this.openDialogView(sr.screening().getScreeningId()));
            screeningPanel.add(timeButton, gbc);
        }

        return screeningPanel;
    }

    private void openDialogView(int screeningId) {
        Window ownerWindow = SwingUtilities.getWindowAncestor(this);
        Frame ownerFrame = (ownerWindow instanceof Frame) ? (Frame) ownerWindow : JOptionPane.getRootFrame();

        JDialog dialog = new JDialog(ownerFrame, "View screening", true);
        ScreeningViewController screeningViewController = new ScreeningViewController(screeningId);

        dialog.setContentPane(screeningViewController.getView());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(ownerFrame);
        dialog.setVisible(true);
    }
}
