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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;


public class ManageEvents extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField titleField;
    private JTextField dateField;
    private JTextField timeField;
    private JTable eventsTable;
    private JButton btnEdit;

    public void loadEvents() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("SELECT * FROM Events")) {
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new String[]{"Event ID", "Title", "Date", "Time"}, 0) {
            	@Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            

            while (rs.next()) {
                int eventId = rs.getInt("EventID");
                String title = rs.getString("Title");
                String date = rs.getString("Date");
                String time = rs.getString("Time");

                model.addRow(new Object[]{eventId, title, date, time});
            }

            eventsTable.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addEvent(String title, String date, String time) {
        if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide all details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date (yyyy-MM-dd).", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidTime(time)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid time (HH:mm:ss).", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("INSERT INTO Events (Title, Date, Time) VALUES (?, ?, ?)")) {
            pst.setString(1, title);
            pst.setString(2, date);
            pst.setString(3, time);
            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Event added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                titleField.setText("");
                dateField.setText("");
                timeField.setText("");
                loadEvents();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void deleteEvent(int eventId) {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this event?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement pst = connection.prepareStatement("DELETE FROM Events WHERE EventID = ?")) {
                pst.setInt(1, eventId);
                int rowsDeleted = pst.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Event deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadEvents();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isValidDate(String date) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        if (!Pattern.matches(regex, date)) return false;
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidTime(String time) {
        String regex = "\\d{2}:\\d{2}:\\d{2}";
        if (!Pattern.matches(regex, time)) return false;
        try {
            new SimpleDateFormat("HH:mm:ss").parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Create the frame.
     */
    public ManageEvents() {
        setTitle("Manage Events");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 933, 548);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(228, 228, 228));
        contentPane.setBorder(new CompoundBorder());

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel contentPane_1 = new JPanel();
        contentPane_1.setLayout(null);
        contentPane_1.setBorder(new CompoundBorder());
        contentPane_1.setBackground(new Color(42, 42, 42));
        contentPane_1.setBounds(0, 0, 917, 509);
        contentPane.add(contentPane_1);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(32, 32, 32));
        panel.setBounds(0, 0, 917, 100);
        contentPane_1.add(panel);

        JLabel lblManageEvents = new JLabel("Manage Events");
        lblManageEvents.setForeground(Color.WHITE);
        lblManageEvents.setFont(new Font("Century Gothic", Font.BOLD, 30));
        lblManageEvents.setBounds(335, 33, 321, 37);
        panel.add(lblManageEvents);

        JLabel lblTitle = new JLabel("Title");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Century Gothic", Font.BOLD, 23));
        lblTitle.setBounds(43, 178, 120, 37);
        contentPane_1.add(lblTitle);

        JLabel lblDate = new JLabel("Date (YYYY-MM-DD)");
        lblDate.setForeground(Color.WHITE);
        lblDate.setFont(new Font("Century Gothic", Font.BOLD, 14));
        lblDate.setBounds(43, 221, 147, 37);
        contentPane_1.add(lblDate);

        JLabel lblTime = new JLabel("Time (00:00:00)");
        lblTime.setForeground(Color.WHITE);
        lblTime.setFont(new Font("Century Gothic", Font.BOLD, 17));
        lblTime.setBounds(43, 264, 127, 37);
        contentPane_1.add(lblTime);

        titleField = new JTextField();
        titleField.setColumns(10);
        titleField.setBounds(198, 188, 140, 27);
        contentPane_1.add(titleField);

        dateField = new JTextField();
        dateField.setColumns(10);
        dateField.setBounds(198, 226, 140, 27);
        contentPane_1.add(dateField);

        timeField = new JTextField();
        timeField.setColumns(10);
        timeField.setBounds(198, 269, 140, 27);
        contentPane_1.add(timeField);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(425, 128, 456, 357);
        contentPane_1.add(scrollPane);

        eventsTable = new JTable();
        eventsTable.setModel(new DefaultTableModel(
                new Object[][]{
                    {null, null, null, null}
                },
                new String[]{
                    "Event ID", "Title", "Date", "Time"
                }
        ) {
            Class[] columnTypes = new Class[]{
                Integer.class, String.class, String.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            boolean[] columnEditables = new boolean[]{
                false, false, false, false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });

        scrollPane.setViewportView(eventsTable);

        btnEdit = new JButton("Edit");
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnEdit.setBackground(new Color(32, 32, 32));
        btnEdit.setBounds(287, 353, 107, 37);
        contentPane_1.add(btnEdit);

        JButton btnAddButton = new JButton("Add");
        btnAddButton.setForeground(Color.WHITE);
        btnAddButton.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnAddButton.setBackground(new Color(32, 32, 32));
        btnAddButton.setBounds(43, 353, 107, 37);
        contentPane_1.add(btnAddButton);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnDelete.setBackground(new Color(32, 32, 32));
        btnDelete.setBounds(160, 353, 107, 37);
        contentPane_1.add(btnDelete);

        JButton btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnBack.setBackground(new Color(32, 32, 32));
        btnBack.setBounds(36, 461, 107, 37);
        contentPane_1.add(btnBack);

        btnAddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String date = dateField.getText();
                String time = timeField.getText();
                addEvent(title, date, time);
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = eventsTable.getSelectedRow();
                if (selectedRow != -1) {
                    int eventId = (int) eventsTable.getValueAt(selectedRow, 0);
                    deleteEvent(eventId);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete");
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	AdminHomepage adminPage = new AdminHomepage();
                adminPage.main(new String[]{});
                dispose();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = eventsTable.getSelectedRow();
                if (selectedRow != -1) {
                    int eventId = (int) eventsTable.getValueAt(selectedRow, 0);
                    String title = (String) eventsTable.getValueAt(selectedRow, 1);
                    String date = (String) eventsTable.getValueAt(selectedRow, 2);
                    String time = (String) eventsTable.getValueAt(selectedRow, 3);
                    new EditEventsForm(eventId, title, date, time, ManageEvents.this).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to edit");
                }
            }
        });

        loadEvents();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ManageEvents frame = new ManageEvents();
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
