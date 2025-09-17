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
import java.util.Date;

public class EditContactForm extends JFrame {
    private final int contactId; private final ContactManager parent; private final String role;

    private final JTextField nameField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JDateChooser dobChooser = new JDateChooser();
    private final JTextArea addressArea = new JTextArea();

    public EditContactForm(int contactId, ContactManager parent, String role){
        this.contactId=contactId; this.parent=parent; this.role=role;

        setTitle("Edit "+role);
        setSize(560, 580); setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout()){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setPaint(new GradientPaint(0,0,Style.PRIMARY_START,getWidth(),getHeight(),Style.PRIMARY_END));
                g2.fillRect(0,0,getWidth(),getHeight()); g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(getWidth(),64));
        JLabel title = new JLabel("  Edit "+role);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.white);
        header.add(title, BorderLayout.WEST);

      /*  JButton toLogin = new JButton("Back to Login");
        toLogin.setBackground(Color.white); toLogin.setForeground(Style.PRIMARY_END);
        toLogin.setFocusPainted(false);
        toLogin.setBorder(BorderFactory.createEmptyBorder(6,10,6,10));
        header.add(toLogin, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        toLogin.addActionListener(e -> { dispose(); new LoginForm(); });  */

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
        JScrollPane sp = new JScrollPane(addressArea); sp.setBounds(xField,y,wField,90); form.add(sp); new TextPrompt("Address, city, state", addressArea);
        y+=110;

        GradientButton update = new GradientButton("Update"); update.setBounds(xField, y, 160, 44); form.add(update);
        JButton cancel = new JButton("Cancel"); cancel.setBounds(xField+180, y, 120, 44); form.add(cancel);

        loadRecord();

        update.addActionListener(e -> doUpdate());
        cancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void loadRecord(){
        String table = role.equalsIgnoreCase("Student")? "students":"teachers";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM "+table+" WHERE id=?")){
            ps.setInt(1, contactId); ResultSet rs = ps.executeQuery();
            if(rs.next()){
                nameField.setText(rs.getString("full_name"));
                phoneField.setText(rs.getString("phone_number"));
                emailField.setText(rs.getString("email_address"));
                Date d = rs.getDate("date_of_birth");
                if(d!=null) dobChooser.setDate(new java.util.Date(d.getTime()));
                addressArea.setText(rs.getString("address"));
            }
        }catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Load failed: "+ex.getMessage()); dispose(); }
    }

    private void doUpdate(){
        String name = nameField.getText().trim();
        String phone= phoneField.getText().trim();
        String email= emailField.getText().trim();
        java.util.Date d = dobChooser.getDate();
        java.sql.Date sqlDob = d==null? null : new java.sql.Date(d.getTime());
        String addr = addressArea.getText().trim();

        if(name.isEmpty()){ JOptionPane.showMessageDialog(this,"Name required."); return; }
        if(!phone.matches("^[0-9]{10}$")){ JOptionPane.showMessageDialog(this,"Phone must be exactly 10 digits."); return; }
        if(!email.isEmpty() && !email.matches("^\\S+@\\S+\\.\\S+$")){ JOptionPane.showMessageDialog(this,"Invalid email."); return; }

        String table = role.equalsIgnoreCase("Student")? "students":"teachers";
        String sql = "UPDATE "+table+" SET full_name=?, phone_number=?, email_address=?, date_of_birth=?, address=? WHERE id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,name); ps.setString(2,phone); ps.setString(3,email);
            if(sqlDob!=null) ps.setDate(4, sqlDob); else ps.setNull(4, Types.DATE);
            ps.setString(5, addr); ps.setInt(6, contactId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Updated.");
            parent.reloadWithFilter(); dispose();
        }catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Update failed: "+ex.getMessage()); }
    }
}

