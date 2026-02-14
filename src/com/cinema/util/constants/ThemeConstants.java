package com.cinema.util.constants;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public final class ThemeConstants {
    public static final Color BG_BUTTON = UIManager.getColor("Button.background");
    public static final Color INACTIVE_BUTTON = Color.DARK_GRAY.brighter();
    public static final Border INACTIVE_BORDER = BorderFactory.createLineBorder(Color.BLACK, 2);
    public static final Border VIP_BORDER = BorderFactory.createLineBorder(Color.ORANGE, 2);
    public static final Border HANDICAP_BORDER = BorderFactory.createLineBorder(Color.GREEN.darker(), 2);
    public static final Color EVEN_ROW_BG = Color.WHITE;
    public static final Color ODD_ROW_BG = new Color(240, 240, 240);

}
