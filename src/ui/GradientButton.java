/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GradientButton extends JButton {
    private final Color start, end;
    private final Color hoverStart, hoverEnd;
    private boolean hover=false;

    public GradientButton(String text){
        this(text, Style.PRIMARY_START, Style.PRIMARY_END);
    }
    public GradientButton(String text, Color start, Color end){
        super(text);
        this.start=start; this.end=end;
        this.hoverStart = start.brighter();
        this.hoverEnd   = end.brighter();
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.white);
        setFont(Style.BUTTON_FONT);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(10,16,10,16));
        addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){ hover=true; repaint(); }
            public void mouseExited (MouseEvent e){ hover=false; repaint(); }
        });
    }
    @Override protected void paintComponent(Graphics g){
        Graphics2D g2=(Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w=getWidth(), h=getHeight();
        g2.setPaint(new GradientPaint(0,0, hover?hoverStart:start, w,h, hover?hoverEnd:end));
        g2.fillRoundRect(0,0,w,h,12,12);
        super.paintComponent(g);
        g2.dispose();
    }
}


