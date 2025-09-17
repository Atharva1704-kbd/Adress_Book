/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import db.DBConnection;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddContactForm extends JFrame {
    private final int userId; private final ContactManager parent; private final String role;

    private final JTextField nameField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JDateChooser dobChooser = new JDateChooser();
    private final JTextArea addressArea = new JTextArea();

    public AddContactForm(int userId, ContactManager parent, String role){
        this.userId=userId; this.parent=parent; this.role=role;

        setTitle("Add "+role);
        setSize(560, 560); setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // top gradient header with actions
        JPanel header = new JPanel(new BorderLayout()){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setPaint(new GradientPaint(0,0,Style.PRIMARY_START,getWidth(),getHeight(),Style.PRIMARY_END));
                g2.fillRect(0,0,getWidth(),getHeight()); g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(getWidth(),64));
        JLabel title = new JLabel("  Add "+role);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.white);
        header.add(title, BorderLayout.WEST);

       /* JButton toLogin = new JButton("Back to Login");
        toLogin.setBackground(Color.white); toLogin.setForeground(Style.PRIMARY_END);
        toLogin.setFocusPainted(false);
        toLogin.setBorder(BorderFactory.createEmptyBorder(6,10,6,10));
        header.add(toLogin, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        toLogin.addActionListener(e -> { dispose(); new LoginForm(); }); */

        JPanel form = new JPanel(null);
        form.setBorder(BorderFactory.createEmptyBorder(12,18,12,18));
        add(form, BorderLayout.CENTER);

        int xLabel=20, wLabel=130, xField=160, wField=340, y=20, gap=55;

        JLabel l1=new JLabel("Full Name:"); l1.setBounds(xLabel,y,wLabel,28); form.add(l1);
        nameField.setBounds(xField,y,wField,34); Style.styleField(nameField); new TextPrompt("Full name", nameField); form.add(nameField); y+=gap;

        JLabel l2=new JLabel("Phone (10 digits):"); l2.setBounds(xLabel,y,wLabel,28); form.add(l2);
        phoneField.setBounds(xField,y,wField,34); Style.styleField(phoneField); new TextPrompt("9876543210", phoneField); form.add(phoneField); y+=gap;

        JLabel l3=new JLabel("Email:"); l3.setBounds(xLabel,y,wLabel,28); form.add(l3);
        emailField.setBounds(xField,y,wField,34); Style.styleField(emailField); new TextPrompt("you@domain.com", emailField); form.add(emailField); y+=gap;

        JLabel l4=new JLabel("DOB:"); l4.setBounds(xLabel,y,wLabel,28); form.add(l4);
        dobChooser.setDateFormatString("yyyy-MM-dd"); dobChooser.setBounds(xField,y,wField,34); form.add(dobChooser); y+=gap;

        JLabel l5=new JLabel("Address:"); l5.setBounds(xLabel,y,wLabel,28); form.add(l5);
        JScrollPane sp = new JScrollPane(addressArea); sp.setBounds(xField,y,wField,80); form.add(sp); new TextPrompt("Address, city, state", addressArea);

        GradientButton save = new GradientButton("Save");
        save.setBounds(xField, y+100, 160, 44);
        JButton cancel = new JButton("Cancel"); cancel.setBounds(xField+180, y+100, 120, 44);
        form.add(save); form.add(cancel);

        save.addActionListener(e -> saveContact());
        cancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void saveContact(){
        String name = nameField.getText().trim();
        String phone= phoneField.getText().trim();
        String email= emailField.getText().trim();
        java.util.Date d = dobChooser.getDate();
        java.sql.Date sqlDob = d==null? null : new java.sql.Date(d.getTime());
        String addr = addressArea.getText().trim();

        if(name.isEmpty()){ JOptionPane.showMessageDialog(this,"Name required."); return; }
        if(!phone.matches("^[0-9]{10}$")){ JOptionPane.showMessageDialog(this,"Phone must be exactly 10 digits."); return; }
        if(!email.isEmpty() && !email.matches("^\\S+@\\S+\\.\\S+$")){ JOptionPane.showMessageDialog(this,"Invalid email."); return; }

        String table = role.equalsIgnoreCase("Student")? "students" : "teachers";
        String sql = "INSERT INTO "+table+" (user_id, full_name, phone_number, email_address, date_of_birth, address) VALUES (?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, userId); ps.setString(2, name); ps.setString(3, phone); ps.setString(4, email);
            if(sqlDob!=null) ps.setDate(5, sqlDob); else ps.setNull(5, Types.DATE);
            ps.setString(6, addr);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, role+" added.");
            parent.reloadWithFilter(); dispose();
        }catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Save failed: "+ex.getMessage()); }
    }
}




