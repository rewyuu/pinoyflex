package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditCustomerForm extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private JTextField textFieldPhone;
    private JTextField textFieldName;
    private JTextField textFieldBirthdate;
    private JTextField textFieldEmail;
    private JTextField textFieldGender;
    private JComboBox<String> statusComboBox;
    private int customerId;
    private ManageCustomers parentFrame;

    public EditCustomerForm(int customerId, String currentUsername, String currentPassword, String currentPhone, String currentName, String currentBirthdate, String currentEmail, String currentGender, String currentStatus, ManageCustomers parentFrame) {
        this.customerId = customerId;
        this.parentFrame = parentFrame;
        setTitle("Edit Customer");
        setBounds(100, 100, 450, 400);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(42, 42, 42));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setBounds(42, 45, 80, 20);
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
        contentPanel.add(lblUsername);

        textFieldUsername = new JTextField();
        textFieldUsername.setBounds(132, 45, 200, 25);
        textFieldUsername.setText(currentUsername);
        contentPanel.add(textFieldUsername);
        textFieldUsername.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setBounds(42, 79, 80, 20);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
        contentPanel.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(132, 79, 200, 25);
        passwordField.setText(currentPassword);
        contentPanel.add(passwordField);

        JLabel lblPhone = new JLabel("Phone #:");
        lblPhone.setForeground(Color.WHITE);
        lblPhone.setBounds(42, 120, 80, 20);
        lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 14));
        contentPanel.add(lblPhone);

        textFieldPhone = new JTextField();
        textFieldPhone.setBounds(132, 120, 200, 25);
        textFieldPhone.setColumns(10);
        textFieldPhone.setText(currentPhone);
        contentPanel.add(textFieldPhone);

        JLabel lblName = new JLabel("Name:");
        lblName.setForeground(Color.WHITE);
        lblName.setBounds(42, 155, 80, 20);
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        contentPanel.add(lblName);

        textFieldName = new JTextField();
        textFieldName.setBounds(132, 155, 200, 25);
        textFieldName.setColumns(10);
        textFieldName.setText(currentName);
        contentPanel.add(textFieldName);

        JLabel lblBirthdate = new JLabel("Birthdate:");
        lblBirthdate.setForeground(Color.WHITE);
        lblBirthdate.setBounds(42, 190, 80, 20);
        lblBirthdate.setFont(new Font("Tahoma", Font.PLAIN, 14));
        contentPanel.add(lblBirthdate);

        textFieldBirthdate = new JTextField();
        textFieldBirthdate.setBounds(132, 190, 200, 25);
        textFieldBirthdate.setColumns(10);
        textFieldBirthdate.setText(currentBirthdate);
        contentPanel.add(textFieldBirthdate);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setBounds(42, 227, 80, 20);
        lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
        contentPanel.add(lblEmail);

        textFieldEmail = new JTextField();
        textFieldEmail.setBounds(132, 227, 200, 25);
        textFieldEmail.setColumns(10);
        textFieldEmail.setText(currentEmail);
        contentPanel.add(textFieldEmail);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setForeground(Color.WHITE);
        lblGender.setBounds(42, 262, 80, 20);
        lblGender.setFont(new Font("Tahoma", Font.PLAIN, 14));
        contentPanel.add(lblGender);

        textFieldGender = new JTextField();
        textFieldGender.setBounds(132, 262, 200, 25);
        textFieldGender.setColumns(10);
        textFieldGender.setText(currentGender);
        contentPanel.add(textFieldGender);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setBounds(42, 297, 80, 20);
        lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 14));
        contentPanel.add(lblStatus);

        statusComboBox = new JComboBox<>();
        statusComboBox.setBounds(132, 297, 200, 25);
        statusComboBox.addItem("Active");
        statusComboBox.addItem("Disabled");
        statusComboBox.setSelectedItem(currentStatus);
        contentPanel.add(statusComboBox);

        JLabel lblEditCustomer = new JLabel("Edit Customer");
        lblEditCustomer.setForeground(Color.WHITE);
        lblEditCustomer.setBounds(132, 11, 175, 30);
        lblEditCustomer.setFont(new Font("Century Gothic", Font.BOLD, 24));
        contentPanel.add(lblEditCustomer);

        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(new Color(32, 32, 32));
        buttonPane.setBounds(0, 332, 436, 31);
        contentPanel.add(buttonPane);
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton okButton = new JButton("OK");
        okButton.setBackground(new Color(42, 42, 42));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newUsername = textFieldUsername.getText();
                String newPassword = new String(passwordField.getPassword());
                String newPhone = textFieldPhone.getText();
                String newName = textFieldName.getText();
                String newBirthdate = textFieldBirthdate.getText();
                String newEmail = textFieldEmail.getText();
                String newGender = textFieldGender.getText();
                String newStatus = (String) statusComboBox.getSelectedItem();
                updateCustomer(customerId, newUsername, newPassword, newPhone, newName, newBirthdate, newEmail, newGender, newStatus);
                dispose();
            }
        });
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(42, 42, 42));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);

        setLocationRelativeTo(parentFrame);
        setResizable(false);
    }

    private void updateCustomer(int customerId, String newUsername, String newPassword, String newPhone, String newName, String newBirthdate, String newEmail, String newGender, String newStatus) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("UPDATE Customers SET Username = ?, Password = ?, Phone = ?, Name = ?, Birthdate = ?, Email = ?, Gender = ?, Status = ? WHERE CustomerID = ?")) {
            pst.setString(1, newUsername);
            pst.setString(2, newPassword);
            pst.setString(3, newPhone);
            pst.setString(4, newName);
            pst.setString(5, newBirthdate);
            pst.setString(6, newEmail);
            pst.setString(7, newGender);
            pst.setString(8, newStatus);
            pst.setInt(9, customerId);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Customer updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                parentFrame.loadCustomers("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
