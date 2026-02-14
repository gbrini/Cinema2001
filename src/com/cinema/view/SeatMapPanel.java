package com.cinema.view;

import com.cinema.model.SeatEditor;
import com.cinema.model.User;
import com.cinema.service.SeatService;
import com.cinema.util.constants.DimensionConstants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class SeatMapPanel extends JPanel {
    private final ArrayList<SeatEditor> seats;
    private final ArrayList<SeatComponent> seatComponents = new ArrayList<>();
    private boolean canEdit = false;

    public SeatMapPanel(int screeningId, boolean canEdit, User user) {
        this.seats = SeatService.getSeatsStatusByScreeningId(screeningId, user);
        this.canEdit = canEdit;
    }

    public SeatMapPanel(ArrayList<SeatEditor> seats, boolean canEdit, User user) {
        this.seats = seats;
        this.canEdit = canEdit;
    }

    public JPanel getView() {
        if (this.seats == null || this.seats.isEmpty()) {
            return new JPanel();
        }

        int maxRow = 0;
        int maxCol = 0;

        String maxRowChar = this.seats.stream().map(SeatEditor::getSeatRow).max(String::compareTo).orElse("A");
        maxRow = maxRowChar.charAt(0) - 'A' + 1;

        maxCol = this.seats.stream().map(SeatEditor::getSeatNumber).max(Integer::compareTo).orElse(1);

        JPanel seatGridPanel = new JPanel();
        seatGridPanel.setLayout(new GridLayout(maxRow, maxCol, 5, 5));

        Map<String, SeatEditor> seatMap = this.seats.stream().collect(Collectors.toMap(SeatEditor::toString, m -> m));

        JPanel listContent = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = DimensionConstants.STANDARD_INSETS;
        constraints.weighty = 1.0;

        char currentRow = 'A';
        for (int r = 0; r < maxRow; r++) {
            String rowName = String.valueOf(currentRow++);
            constraints.gridy = r;
            listContent.add(this.createRowLabel(rowName), constraints);
            for (int c = 1; c < maxCol + 1; c++) {

                String key = rowName + c;
                SeatEditor seatEditor = seatMap.get(key);

                if (seatEditor != null) {
                    SeatComponent seatComponent = new SeatComponent(seatEditor, this.canEdit);
                    seatComponents.add(seatComponent);
                    seatGridPanel.add(seatComponent);
                }
            }
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(listContent, BorderLayout.WEST);
        wrapper.add(seatGridPanel, BorderLayout.CENTER);

        return wrapper;
    }

    private JLabel createRowLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, 18));
        return label;
    }

    public ArrayList<SeatComponent> getSeatComponents() {
        return this.seatComponents;
    }
}
