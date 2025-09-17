/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JFrame {
    public WelcomePage(){
        setTitle("Address Book - Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 1000);
        setLocationRelativeTo(null);

        GradientPanel bg = new GradientPanel(Style.PRIMARY_START, Style.PRIMARY_END);
        bg.setLayout(new GridBagLayout());
        add(bg);

        RoundedPanel card = new RoundedPanel(20, Style.CARD_BG);
        card.setPreferredSize(new Dimension(480, 340));
        card.setLayout(null);

        JLabel brandIcon = new JLabel("\uD83D\uDCD6"); // small notebook icon
        brandIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
        brandIcon.setBounds(42, 26, 40, 40);
        card.add(brandIcon);

        JLabel brand = new JLabel("Address Book");
        brand.setFont(Style.BRAND_FONT);
        brand.setForeground(Style.ACCENT);
        brand.setBounds(100, 26, 260, 38);
        card.add(brand);

        JLabel tagline = new JLabel("Manage your contacts efficiently");
        tagline.setFont(Style.SUB_FONT);
        tagline.setForeground(Style.TEXT_MUTED);
        tagline.setBounds(100, 62, 280, 22);
        card.add(tagline);

        JLabel h2 = new JLabel("Welcome");
        h2.setFont(Style.H2_FONT);
        h2.setBounds(200, 110, 200, 28);
        card.add(h2);

        GradientButton getStarted = new GradientButton("Get Started");
        getStarted.setBounds(90, 170, 300, 48);
        card.add(getStarted);

        JLabel hint = new JLabel("Already have an account?");
        hint.setForeground(Style.TEXT_MUTED);
        hint.setBounds(120, 235, 190, 22);
        card.add(hint);

        JLabel loginLink = new JLabel("<html><u>Login</u></html>");
        loginLink.setForeground(Style.ACCENT);
        loginLink.setBounds(300, 235, 80, 22);
        card.add(loginLink);

        bg.add(card);

        // actions
        getStarted.addActionListener(e -> { dispose(); new LoginForm(); });
        loginLink.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent e){ dispose(); new LoginForm(); }
        });

        setVisible(true);
    }
}

