/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Vector;

public class ContactManager extends JFrame {
    private final int userId;

    private final DefaultTableModel studentModel = new DefaultTableModel(new String[]{"S.No","ID","Name","Phone","Email","DOB","Address"},0);
    private final DefaultTableModel teacherModel = new DefaultTableModel(new String[]{"S.No","ID","Name","Phone","Email","DOB","Address"},0);

    private final JTable studentTable = new JTable(studentModel);
    private final JTable teacherTable = new JTable(teacherModel);

    private final JTextField searchName = new JTextField();
    private final JTextField searchPhone = new JTextField();
    private final JComboBox<String> filterRole = new JComboBox<>(new String[]{"All","Student","Teacher"});

    public ContactManager(int userId){
        this.userId=userId;
        setTitle("Address Book - Contacts");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Gradient header bar
        JPanel header = new JPanel(new BorderLayout()){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setPaint(new GradientPaint(0,0,Style.PRIMARY_START,getWidth(),getHeight(),Style.PRIMARY_END));
                g2.fillRect(0,0,getWidth(),getHeight()); g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(getWidth(),70));
        JLabel title = new JLabel("  Address Book — My Contacts");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.white);
        header.add(title, BorderLayout.WEST);

        JButton back = new JButton("Back to Login");
        back.setFocusPainted(false);
        back.setBorder(BorderFactory.createEmptyBorder(8,14,8,14));
        back.setBackground(new Color(245,245,245));
        back.setForeground(Style.PRIMARY_END);
        back.addActionListener(e -> { dispose(); new LoginForm(); });
        
        
        
       


        // Search panel (card look)
        RoundedPanel searchCard = new RoundedPanel(12, Color.white);
        searchCard.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 12));
        searchCard.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        searchCard.add(new JLabel("Name:")); searchName.setColumns(16); Style.styleField(searchName); searchCard.add(searchName);
        searchCard.add(new JLabel("Phone:")); searchPhone.setColumns(12); Style.styleField(searchPhone); searchCard.add(searchPhone);
        searchCard.add(new JLabel("Role:")); searchCard.add(filterRole);
        JButton searchBtn = new JButton("Search"); JButton clearBtn = new JButton("Clear");
        searchCard.add(searchBtn); searchCard.add(clearBtn);
        searchCard.add(back);
        searchBtn.addActionListener(e -> reloadWithFilter());
        clearBtn.addActionListener(e -> { searchName.setText(""); searchPhone.setText(""); filterRole.setSelectedIndex(0); loadAll(); });

        JPanel searchWrap = new JPanel(new BorderLayout());
        searchWrap.setBorder(BorderFactory.createEmptyBorder(12,12,0,12));
        searchWrap.add(searchCard, BorderLayout.CENTER);
        add(searchWrap, BorderLayout.BEFORE_FIRST_LINE);

        // Tables
        styleTable(studentTable); styleTable(teacherTable);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Students", buildTab(studentTable, "Student"));
        tabs.addTab("Teachers", buildTab(teacherTable, "Teacher"));
        add(tabs, BorderLayout.CENTER);

        loadAll();
        setVisible(true);
    }

    private void styleTable(JTable t){
        t.setFont(Style.FIELD_FONT);
        t.setRowHeight(26);
        if (t.getColumnModel().getColumnCount()>1){ // hide DB id in col 1
            t.getColumnModel().getColumn(1).setMinWidth(0);
            t.getColumnModel().getColumn(1).setMaxWidth(0);
            t.getColumnModel().getColumn(1).setPreferredWidth(0);
        }
    }

    private JPanel buildTab(JTable table, String role){
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton add = new JButton("Add "+role);
        JButton edit = new JButton("Edit "+role);
        JButton del = new JButton("Delete "+role);
        JButton export = new JButton("Export CSV");

        add.setBackground(new Color(76,175,80)); add.setForeground(Color.white);
        edit.setBackground(new Color(255,193,7));
        del.setBackground(new Color(244,67,54)); del.setForeground(Color.white);
        export.setBackground(new Color(96,125,139)); export.setForeground(Color.white);

        bar.add(add); bar.add(edit); bar.add(del); bar.add(Box.createHorizontalStrut(20)); bar.add(export);
        p.add(bar, BorderLayout.SOUTH);

        add.addActionListener(e -> new AddContactForm(userId, this, role));
        edit.addActionListener(e -> {
            int r = table.getSelectedRow(); if (r==-1){ JOptionPane.showMessageDialog(this,"Select a row."); return; }
            int id = Integer.parseInt(table.getModel().getValueAt(r,1).toString());
            new EditContactForm(id, this, role);
        });
        del.addActionListener(e -> {
            int r = table.getSelectedRow(); if (r==-1){ JOptionPane.showMessageDialog(this,"Select a row."); return; }
            int id = Integer.parseInt(table.getModel().getValueAt(r,1).toString());
            String tname = role.equalsIgnoreCase("Student") ? "students" : "teachers";
            if (JOptionPane.showConfirmDialog(this, "Delete selected "+role+"?", "Confirm", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                try (Connection c = DBConnection.getConnection();
                     PreparedStatement ps = c.prepareStatement("DELETE FROM "+tname+" WHERE id=?")){
                    ps.setInt(1,id); ps.executeUpdate(); reloadWithFilter();
                }catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Delete failed: "+ex.getMessage()); }
            }
        });
        export.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Save CSV - "+role);
            fc.setSelectedFile(new File((role.equals("Student")?"students":"teachers")+"_user"+userId+".csv"));
            if (fc.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
                exportCSV(role.equals("Student")?studentModel:teacherModel, fc.getSelectedFile());
            }
        });

        return p;
    }

    private void exportCSV(DefaultTableModel model, File file){
        try(PrintWriter pw = new PrintWriter(file)){
            pw.println("S.No,Name,Phone,Email,DOB,Address");
            for(int i=0;i<model.getRowCount();i++){
                String sNo = model.getValueAt(i,0).toString();
                String name = safe(model.getValueAt(i,2));
                String phone= safe(model.getValueAt(i,3));
                String email= safe(model.getValueAt(i,4));
                String dob  = safe(model.getValueAt(i,5));
                String addr = safe(model.getValueAt(i,6));
                pw.println(csv(sNo)+","+csv(name)+","+csv(phone)+","+csv(email)+","+csv(dob)+","+csv(addr));
            }
            JOptionPane.showMessageDialog(this, "Saved: "+file.getAbsolutePath());
        }catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Export failed: "+ex.getMessage()); }
    }
    private String safe(Object o){ return o==null? "": o.toString(); }
    private String csv(String s){ if(s==null) return ""; if(s.contains(",")||s.contains("\"")||s.contains("\n")){ s=s.replace("\"","\"\""); return "\""+s+"\""; } return s; }

    private void loadAll(){ loadStudents("",""); loadTeachers("",""); }
    void reloadWithFilter(){
        String name = searchName.getText().trim();
        String phone = searchPhone.getText().trim();
        String role  = filterRole.getSelectedItem().toString();
        if("All".equals(role)){ loadStudents(name,phone); loadTeachers(name,phone); }
        else if("Student".equals(role)){ loadStudents(name,phone); teacherModel.setRowCount(0); }
        else { loadTeachers(name,phone); studentModel.setRowCount(0); }
    }

    private void loadStudents(String name, String phone){
        studentModel.setRowCount(0);
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM students WHERE user_id=? AND full_name LIKE ? AND phone_number LIKE ? ORDER BY id")){
            ps.setInt(1,userId); ps.setString(2,"%"+name+"%"); ps.setString(3,"%"+phone+"%");
            ResultSet rs = ps.executeQuery();
            int sn=1;
            while(rs.next()){
                Vector<Object> row = new Vector<>();
                row.add(sn++); row.add(rs.getInt("id"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("phone_number"));
                row.add(rs.getString("email_address"));
                Date d = rs.getDate("date_of_birth");
                row.add(d==null? "": d.toString());
                row.add(rs.getString("address"));
                studentModel.addRow(row);
            }
        }catch(Exception ex){ ex.printStackTrace(); }
    }

    private void loadTeachers(String name, String phone){
        teacherModel.setRowCount(0);
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM teachers WHERE user_id=? AND full_name LIKE ? AND phone_number LIKE ? ORDER BY id")){
            ps.setInt(1,userId); ps.setString(2,"%"+name+"%"); ps.setString(3,"%"+phone+"%");
            ResultSet rs = ps.executeQuery();
            int sn=1;
            while(rs.next()){
                Vector<Object> row = new Vector<>();
                row.add(sn++); row.add(rs.getInt("id"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("phone_number"));
                row.add(rs.getString("email_address"));
                Date d = rs.getDate("date_of_birth");
                row.add(d==null? "": d.toString());
                row.add(rs.getString("address"));
                teacherModel.addRow(row);
            }
        }catch(Exception ex){ ex.printStackTrace(); }
    }
}





