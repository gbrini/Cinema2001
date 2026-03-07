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

        int index = 0;
        for (ScreeningRecord screeningRecord: items) {
            listContent.add(this.createScreeningRow(screeningRecord, index++));
        }

        return new JScrollPane(listContent);
    }

    private JPanel createScreeningRow(ScreeningRecord screeningRecord, int index) {
        JPanel screeningPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = DimensionConstants.STANDARD_INSETS;

        Color backgroundColor = (index % 2 == 0) ? ThemeConstants.EVEN_ROW_BG : ThemeConstants.ODD_ROW_BG;
        screeningPanel.setBackground(backgroundColor);

        Screening screening = screeningRecord.screening();
        Movie movie = screeningRecord.movie();
        Screen screen = screeningRecord.screen();

        gbc.gridx = 0;
        gbc.gridy = 0;
        screeningPanel.add(new JLabel(screening.getScreenId() + " - " + screen.getScreenName() + " - " + screening.getStartTimeT()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        screeningPanel.add(new JLabel(movie.getTitle()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        screeningPanel.add(new JLabel(screeningRecord.tickets_sold() + "/" + screen.getCapacity()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton viewButton = new JButton("View");
        viewButton.addActionListener(e -> this.openDialogView(screening.getScreeningId()));
        screeningPanel.add(viewButton, gbc);

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
