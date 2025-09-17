/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    private final Color start, end;
    public GradientPanel(Color start, Color end){ this.start=start; this.end=end; setOpaque(true); }
    @Override protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w=getWidth(), h=getHeight();
        g2.setPaint(new GradientPaint(0,0,start,w,h,end));
        g2.fillRect(0,0,w,h);
        g2.dispose();
    }
}


