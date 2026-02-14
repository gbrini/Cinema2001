package com.cinema.view;

import com.cinema.util.ComboBoxMethods;
import com.cinema.util.TimeSlot;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

public class ComboBoxRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean hasCellFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, hasCellFocus);
        String text = "";

        if (value instanceof ComboBoxMethods) {
            text = ((ComboBoxMethods) value).toStringComboBox();
        } else if (value instanceof LocalTime) {
            text = TimeSlot.format((LocalTime) value);
        }

        setText(text);
        return this;
    }
}
