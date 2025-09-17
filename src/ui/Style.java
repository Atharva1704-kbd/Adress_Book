/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class Style {
    // Gradient palette (matches the screenshots)
    public static final Color PRIMARY_START = new Color(106,116,247); // left
    public static final Color PRIMARY_END   = new Color(121,65,177);  // right

    public static final Color ACCENT = new Color(93,110,234);
    public static final Color CARD_BG = Color.white;
    public static final Color TEXT_MUTED = new Color(120,120,120);

    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font BRAND_FONT = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font H2_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SUB_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 16);

    public static void styleField(JComponent c){
        c.setFont(FIELD_FONT);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230,230,230)),
                BorderFactory.createEmptyBorder(6,10,6,10)
        ));
    }
}
