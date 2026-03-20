package com.cinema.view;

import com.cinema.model.SeatEditor;
import com.cinema.util.constants.DimensionConstants;
import com.cinema.util.constants.ThemeConstants;

import javax.swing.*;
import java.awt.*;

public class SeatComponent extends JToggleButton {
    private final SeatEditor seatEditor;

    private boolean canEdit = false;

    public SeatComponent(SeatEditor seatEditor, boolean canEdit) {
        this.seatEditor = seatEditor;
        this.canEdit = canEdit;
        //setText(this.seatEditor.toString());
        setPreferredSize(DimensionConstants.SEAT_DIMENSION);
        setFocusPainted(false);
        this.updateAppearance();
    }

    public SeatEditor getSeatEditor() { return seatEditor; }

    public void updateAppearance() {
        setFont(UIManager.getFont("Button.font"));
        setBorder(UIManager.getBorder("Button.border"));

        Color fillColor = ThemeConstants.BG_BUTTON;
        String tooltip = this.seatEditor.toString();

        if (!this.seatEditor.isActive()) {
            fillColor = ThemeConstants.INACTIVE_BUTTON;
            setBorder(ThemeConstants.INACTIVE_BORDER);
            tooltip += " (Inactive)";
        } else if (this.seatEditor.isVip()) {
            setBorder(ThemeConstants.VIP_BORDER);
            tooltip += " (VIP)";
        } else if (this.seatEditor.isHandicap()) {
            setBorder(ThemeConstants.HANDICAP_BORDER);
            tooltip += " (Handicap)";
        }

        if(this.seatEditor.isTaken()) {
            setText("X");
            setEnabled(false);
        } else if (!this.canEdit) {
            setEnabled(false);
        }

//        if(this.seatEditor.isSelected()) {
//            fillColor = Color.YELLOW;
//            setBorder(BorderFactory.createLineBorder(Color.RED, 4));
//        }

        setBackground(fillColor);
        setToolTipText(tooltip);
        repaint();
    }
}
