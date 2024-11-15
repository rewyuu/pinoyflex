package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.SystemColor;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField userField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                    frame.setResizable(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 458, 626);
        contentPane = new JPanel();
        contentPane.setForeground(Color.BLACK);
        contentPane.setBackground(new Color(42, 42, 42));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel_1 = new JLabel("Gym System");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setFont(new Font("Century Gothic", Font.BOLD, 25));
        lblNewLabel_1.setBounds(149, 139, 171, 45);
        contentPane.add(lblNewLabel_1);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblUsername.setBounds(46, 224, 120, 31);
        contentPane.add(lblUsername);

        userField = new JTextField();
        userField.setFont(new Font("Century Gothic", Font.BOLD, 20));
        userField.setForeground(Color.BLACK);
        userField.setBounds(170, 225, 231, 30);
        contentPane.add(userField);
        userField.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblPassword.setBounds(46, 291, 108, 45);
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Century Gothic", Font.BOLD, 20));
        passwordField.setForeground(Color.BLACK);
        passwordField.setBounds(170, 297, 231, 31);
        contentPane.add(passwordField);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setForeground(new Color(42, 42, 42));
        comboBox.setBackground(Color.WHITE);
        comboBox.setFont(new Font("Century Gothic", Font.BOLD, 15));
        comboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Customer", "Admin"}));
        comboBox.setBounds(40, 383, 361, 30);
        contentPane.add(comboBox);

        JButton btnLogin = new JButton("Login");
        btnLogin.setForeground(new Color(42, 42, 42));
        btnLogin.setBackground(Color.WHITE);
        btnLogin.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnLogin.setBounds(85, 441, 104, 35);
        contentPane.add(btnLogin);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setBounds(0, 522, 442, 65);
        contentPane.add(panel);

        JButton btnClear = new JButton("Clear");
        btnClear.setForeground(new Color(42, 42, 42));
        btnClear.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnClear.setBackground(Color.WHITE);
        btnClear.setBounds(256, 441, 104, 35);
        contentPane.add(btnClear);

        JCheckBox chkShowPassword = new JCheckBox("Show Password");
        chkShowPassword.setForeground(Color.WHITE);
        chkShowPassword.setBackground(new Color(42, 42, 42));
        chkShowPassword.setFont(new Font("Century Gothic", Font.BOLD, 15));
        chkShowPassword.setBounds(170, 335, 231, 20);
        contentPane.add(chkShowPassword);
        
        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.BLACK);
        panel_1.setBounds(0, 0, 442, 117);
        contentPane.add(panel_1);
        panel_1.setLayout(null);
        
        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setBounds(10, 11, 422, 95);
        panel_1.add(lblNewLabel);
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\Rey Benedict Rico\\eclipse-workspace\\PinoyFlexGymSystem\\images\\pinoyflex_logo.jpg"));

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) comboBox.getSelectedItem();
                login(username, password, role);
            }
        });

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userField.setText("");
                passwordField.setText("");
            }
        });

        chkShowPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (chkShowPassword.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('*');
                }
            }
        });
    }

    private void login(String username, String password, String role) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String dbUrl = "jdbc:mysql://localhost:3306/pinoyflexsystem";
        String dbUsername = "root";
        String dbPassword = "";

        try {
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

            String query;
            if ("Admin".equals(role)) {
                query = "SELECT * FROM admin WHERE username = ? AND password = ?";
            } else {
                query = "SELECT * FROM customers WHERE Username = ? AND Password = ?";
            }

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                if ("Customer".equals(role)) {
                    String status = rs.getString("Status");
                    if ("Disabled".equalsIgnoreCase(status)) {
                        JOptionPane.showMessageDialog(this, "Your account is disabled. Please contact support.", "Account Disabled", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                JOptionPane.showMessageDialog(this, "Login Successful!");
                this.dispose();
                if ("Admin".equals(role)) {
                    AdminHomepage adminHome = new AdminHomepage();
                    adminHome.setVisible(true);
                    adminHome.setLocationRelativeTo(null);
                } else {
                    int customerId = rs.getInt("CustomerID");
                    CustomerHomepage customerHome = new CustomerHomepage(customerId);
                    customerHome.setVisible(true);
                    customerHome.setLocationRelativeTo(null);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
