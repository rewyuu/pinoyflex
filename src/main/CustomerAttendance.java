package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;

public class CustomerAttendance extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable attendanceTable;
    private int customerId;

    public CustomerAttendance(int customerId) {
        this.customerId = customerId;
        initUI();
    }

    private void initUI() {
        setTitle("Attendance");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 933, 548);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(42, 42, 42));
        contentPane.setBorder(new CompoundBorder());

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setForeground(Color.WHITE);
        panel.setLayout(null);
        panel.setBackground(new Color(32, 32, 32));
        panel.setBounds(0, 0, 917, 100);
        contentPane.add(panel);

        JLabel lblAttendance = new JLabel("Attendance");
        lblAttendance.setForeground(Color.WHITE);
        lblAttendance.setFont(new Font("Century Gothic", Font.BOLD, 30));
        lblAttendance.setBounds(344, 32, 343, 37);
        panel.add(lblAttendance);

        JButton btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnBack.setBackground(new Color(32, 32, 32));
        btnBack.setBounds(26, 450, 107, 37);
        contentPane.add(btnBack);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(425, 128, 456, 357);
        contentPane.add(scrollPane);

        attendanceTable = new JTable();
        attendanceTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Attendance Date", "Attendance Time"
                }
        ) {
            Class[] columnTypes = new Class[]{
                String.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            boolean[] columnEditables = new boolean[]{
                false, false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        scrollPane.setViewportView(attendanceTable);

        loadAttendanceRecords();

        JButton btnRecordAttendance = new JButton("Record Attendance");
        btnRecordAttendance.setForeground(Color.WHITE);
        btnRecordAttendance.setBackground(new Color(32, 32, 32));
        btnRecordAttendance.setFont(new Font("Century Gothic", Font.BOLD, 25));
        btnRecordAttendance.setBounds(43, 326, 284, 49);
        contentPane.add(btnRecordAttendance);

        btnRecordAttendance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recordAttendance();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomerHomepage homepage = new CustomerHomepage(customerId);
                homepage.setVisible(true);
                homepage.setLocationRelativeTo(null);
                homepage.setResizable(false);
                dispose();
            }
        });
    }

    private void loadAttendanceRecords() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "SELECT attendance_date, attendance_time FROM attendance_records WHERE customer_id = ?")) {
            pst.setInt(1, customerId);
            try (ResultSet rs = pst.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) attendanceTable.getModel();
                while (rs.next()) {
                    String date = rs.getString("attendance_date");
                    String time = rs.getString("attendance_time");
                    model.addRow(new Object[]{date, time});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void recordAttendance() {
        LocalDateTime now = LocalDateTime.now();
        String date = now.toLocalDate().toString();
        String time = now.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        System.out.println("CustomerID: " + customerId);
        System.out.println("Date: " + date);
        System.out.println("Time: " + time);

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkPst = connection.prepareStatement(
                     "SELECT COUNT(*) FROM customers WHERE CustomerID = ?")) {

            checkPst.setInt(1, customerId);
            try (ResultSet rs = checkPst.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    try (PreparedStatement pst = connection.prepareStatement(
                            "INSERT INTO attendance_records (customer_id, attendance_date, attendance_time) VALUES (?, ?, ?)")) {
                        pst.setInt(1, customerId);
                        pst.setString(2, date);
                        pst.setString(3, time);

                        int rowsInserted = pst.executeUpdate();
                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(CustomerAttendance.this,
                                    "Attendance recorded successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

                            DefaultTableModel model = (DefaultTableModel) attendanceTable.getModel();
                            model.addRow(new Object[]{date, time});
                        } else {
                            JOptionPane.showMessageDialog(CustomerAttendance.this,
                                    "Error recording attendance", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(CustomerAttendance.this,
                            "Customer ID not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(CustomerAttendance.this,
                    "Database error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CustomerAttendance frame = new CustomerAttendance(0); 
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                    frame.setResizable(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
