package com.cinema.view.admin;

import com.cinema.model.SeatEditor;
import com.cinema.model.User;
import com.cinema.util.constants.DimensionConstants;
import com.cinema.view.SeatComponent;
import com.cinema.view.SeatMapPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SeatMapEditorPanel extends JPanel {
    private final User user;
    private ArrayList<SeatComponent> seatComponents = new ArrayList<>();
    private final int screenId;

    private final JTextField screenNameField = new JTextField(10);
    private final JButton vipButton = new JButton("Imposta come VIP");
    private final JButton handicapButton = new JButton("Imposta come Handicap");
    private final JButton removeButton = new JButton("Imposta come Removed");
    private final JButton clearButton = new JButton("Cancella selezionati");
    private final JButton saveButton = new JButton("Salva Layout");

    public SeatMapEditorPanel(int screenId, List<SeatEditor> initialLayout, String screenName, User user) {
        this.user = user;
        this.screenId = screenId;
        screenNameField.setText(screenName);

        setLayout(new BorderLayout(10, 10));

        this.initializeLayout(initialLayout);

        add(this.createControlPanel(), BorderLayout.NORTH);
    }

    private void initializeLayout(List<SeatEditor> initialLayout) {
        SeatMapPanel seatMapPanel = new SeatMapPanel(new ArrayList<>(initialLayout), true, this.user, null);
        JPanel seatView = seatMapPanel.getView();
        this.seatComponents = seatMapPanel.getSeatComponents();
        add(seatView, BorderLayout.CENTER);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(this.createButtonPanel());
        panel.add(this.createLegendPanel());

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

        panel.add(new JLabel("Screen Name "));
        panel.add(screenNameField);
        panel.add(vipButton);
        panel.add(handicapButton);
        panel.add(removeButton);
        panel.add(clearButton);
        panel.add(saveButton);

        return panel;
    }

    private JPanel createLegendPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));

        panel.add(this.createLegendItem(Color.ORANGE, "Posto VIP"));
        panel.add(this.createLegendItem(Color.GREEN, "Posto Handicap"));
        panel.add(this.createLegendItem(Color.DARK_GRAY.brighter(), "Posto disabilitato"));
        panel.add(this.createLegendItem(new Color(0, 150, 255), "Posto selezionato"));

        return panel;
    }

    private JPanel createLegendItem(Color color, String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        JLabel colorBox = new JLabel();
        colorBox.setPreferredSize(DimensionConstants.LEGEND_ITEM_DIMENSION);
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        panel.add(colorBox);
        panel.add(new JLabel(text));

        return panel;
    }

    public ArrayList<SeatComponent> getSeatComponents() { return seatComponents; }

    public String getScreenName() { return screenNameField.getText(); }

    public void addVipListener(ActionListener listener) { vipButton.addActionListener(listener); }
    public void addHandicapListener(ActionListener listener) { handicapButton.addActionListener(listener); }
    public void addRemovedListener(ActionListener listener) { removeButton.addActionListener(listener); }
    public void addClearListener(ActionListener listener) { clearButton.addActionListener(listener); }
    public void addSaveListener(ActionListener listener) { saveButton.addActionListener(listener); }

    public void addSeatSelectionListener(ActionListener listener) {
        for (SeatComponent seatComponent: seatComponents) {
            seatComponent.addActionListener(listener);
        }
    }
}
