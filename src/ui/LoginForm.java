/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginForm extends JFrame {
    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public LoginForm(){
        setTitle("Address Book - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 1000);
        setLocationRelativeTo(null);

        GradientPanel bg = new GradientPanel(Style.PRIMARY_START, Style.PRIMARY_END);
        bg.setLayout(new GridBagLayout());
        add(bg);

        RoundedPanel card = new RoundedPanel(20, Style.CARD_BG);
        card.setPreferredSize(new Dimension(460, 460));
        card.setLayout(null);

        // Brand header
        JLabel brandIcon = new JLabel("\uD83D\uDCD6");
        brandIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        brandIcon.setBounds(40, 28, 40, 40); card.add(brandIcon);

        JLabel brand = new JLabel("Address Book");
        brand.setFont(Style.BRAND_FONT); brand.setForeground(Style.ACCENT);
        brand.setBounds(100, 28, 260, 38); card.add(brand);

        JLabel sub = new JLabel("Manage your contacts efficiently");
        sub.setFont(Style.SUB_FONT); sub.setForeground(Style.TEXT_MUTED);
        sub.setBounds(100, 64, 280, 22); card.add(sub);

        JLabel head = new JLabel("Login");
        head.setFont(Style.H2_FONT); head.setBounds(200, 115, 200, 28); card.add(head);

        // fields
        usernameField.setBounds(80, 165, 300, 42); Style.styleField(usernameField); new TextPrompt("Username", usernameField); card.add(usernameField);
        passwordField.setBounds(80, 220, 300, 42); Style.styleField(passwordField); new TextPrompt("Password", passwordField); card.add(passwordField);

        // generous spacing above/below button
        GradientButton loginBtn = new GradientButton("Login");
        loginBtn.setBounds(80, 285, 300, 50); card.add(loginBtn);

        // row link with spacing under the button
        JLabel note = new JLabel("Don't have an account?");
        note.setForeground(Style.TEXT_MUTED);
        note.setBounds(110, 350, 170, 22); card.add(note);

        JLabel signLink = new JLabel("<html><u>Sign Up</u></html>");
        signLink.setForeground(Style.ACCENT);
        signLink.setBounds(280, 350, 70, 22); card.add(signLink);

        // Back to Welcome (if needed)
        JButton back = new JButton("← Back");
        back.setBounds(18, 18, 80, 26);
        back.setFocusPainted(false);
        back.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
        back.setBackground(new Color(245,245,245));
        card.add(back);

        bg.add(card);

        // actions
        back.addActionListener(e -> { dispose(); new WelcomePage(); });
        signLink.addMouseListener(new java.awt.event.MouseAdapter(){ public void mouseClicked(java.awt.event.MouseEvent e){ dispose(); new SignUpForm(); }});
        loginBtn.addActionListener(e -> doLogin());

        setVisible(true);
    }

    private void doLogin(){
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword()).trim();
        if(u.isEmpty() || p.isEmpty()){
            JOptionPane.showMessageDialog(this, "Enter username & password.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id FROM users WHERE username=? AND password=?")){
            ps.setString(1,u); ps.setString(2,p);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int userId = rs.getInt("id");
                dispose();
                new ContactManager(userId);
            }else{
                JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB Error: "+ex.getMessage());
        }
    }
}






