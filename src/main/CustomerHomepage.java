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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;

public class CustomerHomepage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblCustomerDashboard;
    private JButton btnLogOut;
    private JButton btnAttendance;
    private JButton btnJoinEvents;
    private JButton btnChangePassword;
    private JLabel lblSubscriptionInfo;
    private JLabel lblDurationInfo;
    private JList<String> listJoinedEvents;
    private int customerId;
    private String username;

    public CustomerHomepage(int customerId) {
        this.customerId = customerId;
        this.username = fetchUsername(customerId);
        initUI();
        startTimer();
    }

    private String fetchUsername(int customerId) {
        String username = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "SELECT Username FROM customers WHERE CustomerID = ?")) {
            pst.setInt(1, customerId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    username = rs.getString("Username");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return username;
    }

    private String[] fetchSubscriptionInfo(int customerId) {
        String subscriptionInfo = "No subscription found.";
        String durationInfo = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "SELECT p.PackageName, p.Duration, t.Date FROM transactions t " +
                     "JOIN packages p ON t.selected_package = p.PackageID " +
                     "WHERE t.CustomerID = ? ORDER BY t.Date DESC LIMIT 1")) {
            pst.setInt(1, customerId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String packageName = rs.getString("PackageName");
                    int duration = rs.getInt("Duration");
                    String dateStr = rs.getString("Date");
                    LocalDate startDate = LocalDate.parse(dateStr, formatter);
                    LocalDateTime startDateTime = startDate.atStartOfDay();
                    LocalDateTime endDateTime = startDateTime.plusMonths(duration);
                    LocalDateTime currentDateTime = LocalDateTime.now();

                    if (currentDateTime.isBefore(endDateTime)) {
                        Period period = Period.between(currentDateTime.toLocalDate(), endDateTime.toLocalDate());
                        Duration totalDuration = Duration.between(currentDateTime, endDateTime);

                        long days = totalDuration.toDays() % 30;
                        long hours = totalDuration.toHours() % 24;
                        long minutes = totalDuration.toMinutes() % 60;
                        long seconds = totalDuration.getSeconds() % 60;

                        subscriptionInfo = String.format("Package: %s", packageName);
                        durationInfo = String.format("Remaining Duration: %d years, %d months, %d days, %d hours, %d minutes, %d seconds",
                                period.getYears(), period.getMonths(), days, hours, minutes, seconds);
                    } else {
                        subscriptionInfo = "Subscription expired.";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new String[]{subscriptionInfo, durationInfo};
    }

    private List<String> fetchJoinedEvents(int customerId) {
        List<String> joinedEvents = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "SELECT e.Title, e.Date, e.Time FROM events e INNER JOIN event_participants ep ON e.EventID = ep.EventID WHERE ep.CustomerID = ?")) {
            pst.setInt(1, customerId);
            try (ResultSet rs = pst.executeQuery()) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
                while (rs.next()) {
                    String eventName = rs.getString("Title");
                    LocalDate eventDate = rs.getDate("Date").toLocalDate();
                    String eventTime = rs.getTime("Time").toLocalTime().format(timeFormatter);
                    String eventInfo = String.format("%s  -  %s  -  %s", eventName, eventDate.format(dateFormatter), eventTime);
                    joinedEvents.add(eventInfo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return joinedEvents;
    }

    private void startTimer() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] subscriptionInfo = fetchSubscriptionInfo(customerId);
                lblSubscriptionInfo.setText(subscriptionInfo[0]);
                lblDurationInfo.setText(subscriptionInfo[1]);
            }
        });
        timer.start();
    }

    private void initUI() {
        setTitle("Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 933, 548);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(42, 42, 42));
        contentPane.setBorder(new CompoundBorder());
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(32, 32, 32));
        panel.setBounds(0, 0, 917, 100);
        contentPane.add(panel);

        btnLogOut = new JButton("Log Out");
        btnLogOut.setForeground(Color.WHITE);
        btnLogOut.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnLogOut.setBackground(new Color(32, 32, 32));
        btnLogOut.setBounds(766, 52, 122, 37);
        panel.add(btnLogOut);

        lblCustomerDashboard = new JLabel("Customer Dashboard");
        lblCustomerDashboard.setForeground(Color.WHITE);
        lblCustomerDashboard.setFont(new Font("Century Gothic", Font.BOLD, 30));
        lblCustomerDashboard.setBounds(344, 32, 343, 37);
        panel.add(lblCustomerDashboard);

        JLabel lblCustomerId = new JLabel("Customer ID: " + customerId);
        lblCustomerId.setForeground(Color.WHITE);
        lblCustomerId.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblCustomerId.setBounds(30, 110, 300, 30);
        contentPane.add(lblCustomerId);

        btnAttendance = new JButton("Attendance");
        btnAttendance.setForeground(Color.WHITE);
        btnAttendance.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnAttendance.setBackground(new Color(32, 32, 32));
        btnAttendance.setBounds(30, 150, 150, 37);
        contentPane.add(btnAttendance);

        btnJoinEvents = new JButton("Join Events");
        btnJoinEvents.setForeground(Color.WHITE);
        btnJoinEvents.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnJoinEvents.setBackground(new Color(32, 32, 32));
        btnJoinEvents.setBounds(200, 150, 150, 37);
        contentPane.add(btnJoinEvents);

        btnChangePassword = new JButton("Change Password");
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnChangePassword.setBackground(new Color(32, 32, 32));
        btnChangePassword.setBounds(370, 150, 200, 37);
        contentPane.add(btnChangePassword);

        String[] subscriptionInfo = fetchSubscriptionInfo(customerId);
        lblSubscriptionInfo = new JLabel(subscriptionInfo[0]);
        lblSubscriptionInfo.setForeground(Color.WHITE);
        lblSubscriptionInfo.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblSubscriptionInfo.setBounds(30, 220, 600, 48);
        contentPane.add(lblSubscriptionInfo);

        lblDurationInfo = new JLabel(subscriptionInfo[1]);
        lblDurationInfo.setForeground(Color.WHITE);
        lblDurationInfo.setFont(new Font("Century Gothic", Font.BOLD, 15));
        lblDurationInfo.setBounds(30, 260, 600, 30);
        contentPane.add(lblDurationInfo);

        List<String> joinedEvents = fetchJoinedEvents(customerId);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String event : joinedEvents) {
            listModel.addElement(event);
        }

        listJoinedEvents = new JList<>(listModel);
        listJoinedEvents.setForeground(Color.WHITE);
        listJoinedEvents.setFont(new Font("Century Gothic", Font.BOLD, 16));
        listJoinedEvents.setBackground(new Color(42, 42, 42));

        JScrollPane scrollPane = new JScrollPane(listJoinedEvents);
        scrollPane.setBounds(30, 340, 400, 150);
        contentPane.add(scrollPane);

        JLabel lblJoinedEvents = new JLabel("Joined Events");
        lblJoinedEvents.setForeground(Color.WHITE);
        lblJoinedEvents.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblJoinedEvents.setBounds(30, 299, 300, 30);
        contentPane.add(lblJoinedEvents);

        btnJoinEvents.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomerJoinEvents joinEventsPage = new CustomerJoinEvents(customerId);
                joinEventsPage.setVisible(true);
                joinEventsPage.setLocationRelativeTo(null);
                joinEventsPage.setResizable(false);
                dispose();
            }
        });

        btnLogOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Logout successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                Login loginPage = new Login();
                loginPage.main(new String[]{});
                dispose();
            }
        });

        btnAttendance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomerAttendance attendancePage = new CustomerAttendance(customerId);
                attendancePage.setVisible(true);
                attendancePage.setLocationRelativeTo(null);
                attendancePage.setResizable(false);
                dispose();
            }
        });

        btnChangePassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomerChangePassword changepasswordPage = new CustomerChangePassword(customerId, username);
                changepasswordPage.setVisible(true);
                changepasswordPage.setLocationRelativeTo(null);
                changepasswordPage.setResizable(false);
            }
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CustomerHomepage frame = new CustomerHomepage(0); 
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
