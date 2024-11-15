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
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;

public class CustomerJoinEvents extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private int customerId;

    public CustomerJoinEvents(int customerId) {
        this.customerId = customerId;
        initUI();
        loadEventData();
    }

    private void initUI() {
        setTitle("Events");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 933, 548);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(42, 42, 42));
        contentPane.setBorder(new CompoundBorder());
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 178, 850, 267);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        JButton btnJoinEvent = new JButton("Join Event");
        btnJoinEvent.setForeground(Color.WHITE);
        btnJoinEvent.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnJoinEvent.setBackground(new Color(32, 32, 32));
        btnJoinEvent.setBounds(730, 461, 150, 37);
        contentPane.add(btnJoinEvent);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(32, 32, 32));
        panel.setBounds(0, 1, 917, 100);
        contentPane.add(panel);
        
        JLabel lblJoinEvents = new JLabel("Join Events");
        lblJoinEvents.setForeground(Color.WHITE);
        lblJoinEvents.setFont(new Font("Century Gothic", Font.BOLD, 30));
        lblJoinEvents.setBounds(344, 32, 343, 37);
        panel.add(lblJoinEvents);
        
                JLabel lblEvents = new JLabel("Events");
                lblEvents.setForeground(Color.WHITE);
                lblEvents.setBounds(30, 119, 122, 48);
                contentPane.add(lblEvents);
                lblEvents.setFont(new Font("Century Gothic", Font.BOLD, 30));
                
                JButton btnBack = new JButton("Back");
                btnBack.setForeground(Color.WHITE);
                btnBack.setFont(new Font("Century Gothic", Font.BOLD, 15));
                btnBack.setBackground(new Color(32, 32, 32));
                btnBack.setBounds(19, 465, 103, 29);
                contentPane.add(btnBack);

        btnJoinEvent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int eventId = (int) table.getValueAt(selectedRow, 0);
                    joinEvent(eventId);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an event to join.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
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
    
    

    private void loadEventData() {
        DefaultTableModel model = new DefaultTableModel() {
        	@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        model.addColumn("EventID");
        model.addColumn("Title");
        model.addColumn("Date");
        model.addColumn("Time");

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("SELECT EventID, Title, Date, Time FROM events")) {

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("EventID"));
                    row.add(rs.getString("Title"));
                    row.add(rs.getDate("Date"));
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    row.add(sdf.format(rs.getTime("Time")));
                    model.addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setModel(model);
    }

    private void joinEvent(int eventId) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "INSERT INTO event_participants (CustomerID, EventID) VALUES (?, ?)")) {
            pst.setInt(1, customerId);
            pst.setInt(2, eventId);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Event joined successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to join event.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CustomerJoinEvents frame = new CustomerJoinEvents(0);
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
