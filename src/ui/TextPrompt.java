/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;

public class TextPrompt extends JLabel implements FocusListener, DocumentListener, ComponentListener {
    private final JTextComponent comp;
    public TextPrompt(String text, JTextComponent comp){
        this.comp=comp;
        setText(text);
        setForeground(new Color(160,160,160));
        comp.setLayout(null);
        comp.add(this);
        comp.addFocusListener(this);
        comp.getDocument().addDocumentListener(this);
        comp.addComponentListener(this);
        setFont(comp.getFont());
        reposition();
        check();
    }
    private void reposition(){
        SwingUtilities.invokeLater(() -> {
            Insets i = comp.getInsets();
            setBounds(i.left+8, i.top, comp.getWidth()-i.left-i.right-8, comp.getHeight()-i.top-i.bottom);
        });
    }
    private void check(){ setVisible(comp.getText().isEmpty() && !comp.hasFocus()); }
    public void focusGained(FocusEvent e){ check(); }
    public void focusLost  (FocusEvent e){ check(); }
    public void insertUpdate(DocumentEvent e){ check(); }
    public void removeUpdate(DocumentEvent e){ check(); }
    public void changedUpdate(DocumentEvent e){ check(); }
    public void componentResized(ComponentEvent e){ reposition(); }
    public void componentMoved(ComponentEvent e){}
    public void componentShown(ComponentEvent e){ reposition(); }
    public void componentHidden(ComponentEvent e){}
}

