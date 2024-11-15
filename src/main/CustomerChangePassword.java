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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.CompoundBorder;

public class CustomerChangePassword extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPasswordField passwordFieldOld;
    private JPasswordField passwordFieldNew;
    private JButton btnChangePassword;
    private int customerId;
    private String username;

    public CustomerChangePassword(int customerId, String username) {
        this.customerId = customerId;
        this.username = username;
        initUI();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CustomerChangePassword frame = new CustomerChangePassword(0, "test_user"); // Default ID and username for testing
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                    frame.setResizable(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initUI() {
        setTitle("Change Password");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(32, 32, 32));
        contentPane.setBorder(new CompoundBorder());
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblOldPassword = new JLabel("Old Password:");
        lblOldPassword.setForeground(new Color(255, 255, 255));
        lblOldPassword.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblOldPassword.setBounds(50, 93, 150, 20);
        contentPane.add(lblOldPassword);

        passwordFieldOld = new JPasswordField();
        passwordFieldOld.setForeground(new Color(255, 255, 255));
        passwordFieldOld.setBounds(199, 97, 150, 20);
        contentPane.add(passwordFieldOld);

        JLabel lblNewPassword = new JLabel("New Password:");
        lblNewPassword.setForeground(new Color(255, 255, 255));
        lblNewPassword.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblNewPassword.setBounds(50, 143, 150, 20);
        contentPane.add(lblNewPassword);

        passwordFieldNew = new JPasswordField();
        passwordFieldNew.setForeground(new Color(255, 255, 255));
        passwordFieldNew.setBounds(199, 147, 150, 20);
        contentPane.add(passwordFieldNew);

        JLabel lblUsername = new JLabel("Username: " + username);
        lblUsername.setForeground(new Color(255, 255, 255));
        lblUsername.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblUsername.setBounds(50, 31, 300, 20);
        contentPane.add(lblUsername);

        btnChangePassword = new JButton("Change Password");
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnChangePassword.setBackground(new Color(42, 42, 42));
        btnChangePassword.setBounds(46, 193, 180, 30);
        contentPane.add(btnChangePassword);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnCancel.setBackground(new Color(42, 42, 42));
        btnCancel.setBounds(248, 193, 133, 30);
        contentPane.add(btnCancel);
        
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnChangePassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String oldPassword = new String(passwordFieldOld.getPassword());
        String newPassword = new String(passwordFieldNew.getPassword());

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "UPDATE customers SET Password = ? WHERE CustomerID = ? AND Password = ?")) {
            pst.setString(1, newPassword);
            pst.setInt(2, customerId);
            pst.setString(3, oldPassword);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(CustomerChangePassword.this,
                        "Password changed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(CustomerChangePassword.this,
                        "Incorrect old password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(CustomerChangePassword.this,
                    "Error changing password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
