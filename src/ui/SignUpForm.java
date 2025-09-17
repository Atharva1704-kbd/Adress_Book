/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignUpForm extends JFrame {
    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmField  = new JPasswordField();

    public SignUpForm(){
        setTitle("Address Book - Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 1000);
        setLocationRelativeTo(null);

        GradientPanel bg = new GradientPanel(Style.PRIMARY_START, Style.PRIMARY_END);
        bg.setLayout(new GridBagLayout());
        add(bg);

        RoundedPanel card = new RoundedPanel(20, Style.CARD_BG);
        card.setPreferredSize(new Dimension(480, 520));
        card.setLayout(null);

        JLabel brandIcon = new JLabel("\uD83D\uDCD6");
        brandIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        brandIcon.setBounds(40, 28, 40, 40); card.add(brandIcon);

        JLabel brand = new JLabel("Address Book");
        brand.setFont(Style.BRAND_FONT); brand.setForeground(Style.ACCENT);
        brand.setBounds(100, 28, 260, 38); card.add(brand);

        JLabel sub = new JLabel("Manage your contacts efficiently");
        sub.setFont(Style.SUB_FONT); sub.setForeground(Style.TEXT_MUTED);
        sub.setBounds(100, 64, 280, 22); card.add(sub);

        JLabel head = new JLabel("Sign Up");
        head.setFont(Style.H2_FONT); head.setBounds(200, 120, 200, 28); card.add(head);

        usernameField.setBounds(90, 170, 300, 42); Style.styleField(usernameField); new TextPrompt("Username", usernameField); card.add(usernameField);
        passwordField.setBounds(90, 225, 300, 42); Style.styleField(passwordField); new TextPrompt("Password", passwordField); card.add(passwordField);
        confirmField.setBounds(90, 280, 300, 42); Style.styleField(confirmField); new TextPrompt("Confirm Password", confirmField); card.add(confirmField);

        GradientButton signBtn = new GradientButton("Sign Up");
        signBtn.setBounds(90, 345, 300, 50); card.add(signBtn);

        JLabel note = new JLabel("Already have an account?");
        note.setForeground(Style.TEXT_MUTED);
        note.setBounds(120, 410, 180, 22); card.add(note);

        JLabel loginLink = new JLabel("<html><u>Login</u></html>");
        loginLink.setForeground(Style.ACCENT);
        loginLink.setBounds(300, 410, 70, 22); card.add(loginLink);

        

        bg.add(card);

        signBtn.addActionListener(e -> doSignUp());
        loginLink.addMouseListener(new java.awt.event.MouseAdapter(){ public void mouseClicked(java.awt.event.MouseEvent e){ dispose(); new LoginForm(); }});
        

        setVisible(true);
    }

    private void doSignUp(){
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword()).trim();
        String c = new String(confirmField.getPassword()).trim();
        if(u.isEmpty() || p.isEmpty() || c.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(!p.equals(c)){
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO users(username,password) VALUES(?,?)")){
            ps.setString(1,u); ps.setString(2,p); ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registered. Please log in.");
            dispose(); new LoginForm();
        }catch(SQLIntegrityConstraintViolationException ex){
            JOptionPane.showMessageDialog(this, "Username already exists.");
        }catch(Exception ex){
            ex.printStackTrace(); JOptionPane.showMessageDialog(this, "DB Error: "+ex.getMessage());
        }
    }
}
