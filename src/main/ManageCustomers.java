package main;

import java.awt.Color;
import java.util.regex.*;
import java.awt.Component;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class ManageCustomers extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField_Username;
    private JTextField textField_Name;
    private JPasswordField textField_Password;
    private JTextField searchField;
    private JTable customersTable;
    private JButton btnDisable;
    private JTextField textField_Phone;
    private JTextField textField_Birthdate;
    private JTextField textField_Email;
    private JTextField textField_Gender;
    private JRadioButton rdbtnID;
    private JRadioButton rdbtnName;
    private JRadioButton rdbtnStatus;
    private ButtonGroup searchGroup;
    private JButton btnEdit;
    private JButton btnBack;

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\d{11}$");
    }

    private boolean isValidBirthdate(String birthdate) {
        String birthdatePattern = "^\\d{2}/\\d{2}/\\d{4}$";
        return Pattern.matches(birthdatePattern, birthdate);
    }

    private boolean isValidEmail(String email) {
        return email.contains("@");
    }

    public void loadCustomers(String searchTerm) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "SELECT CustomerID, Username, Password, Phone, Name, Birthdate, Email, Gender, Status FROM Customers " +
                     "WHERE " + getSearchColumn() + " LIKE ?")) {
            String searchPattern = "%" + searchTerm + "%";
            pst.setString(1, searchPattern);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new String[]{"Customer ID", "Username", "Password", "Phone #", "Name", "Birthdate", "Email", "Gender", "Status"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            while (rs.next()) {
                int customerId = rs.getInt("CustomerID");
                String username = rs.getString("Username");
                String password = rs.getString("Password");
                String phone = rs.getString("Phone");
                String name = rs.getString("Name");
                String birthdate = rs.getString("Birthdate");
                String email = rs.getString("Email");
                String gender = rs.getString("Gender");
                String status = rs.getString("Status");

                model.addRow(new Object[]{customerId, username, password, phone, name, birthdate, email, gender, status});
            }

            customersTable.setModel(model);
            customersTable.getColumnModel().getColumn(2).setCellRenderer(new PasswordCellRenderer());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private String getSearchColumn() {
        if (rdbtnID.isSelected()) {
            return "CustomerID";
        } else if (rdbtnName.isSelected()) {
            return "Name";
        } else if (rdbtnStatus.isSelected()) {
            return "Status";
        } else {
            return "CustomerID"; 
        }
    }

    private void addCustomer(String username, String password, String phone, String name, String birthdate, String email, String gender) {
        if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || name.isEmpty() || birthdate.isEmpty() || email.isEmpty() || gender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide all details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidPhoneNumber(phone)) {
            JOptionPane.showMessageDialog(this, "Invalid phone number. It must be a numeric value and contain exactly 11 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidBirthdate(birthdate)) {
            JOptionPane.showMessageDialog(this, "Invalid birthdate format. It must be in the format DD/MM/YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email address. It must contain '@' symbol.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement("SELECT COUNT(*) FROM Customers WHERE Username = ?")) {
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) {
                JOptionPane.showMessageDialog(this, "Username already exists. Please choose another username.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("INSERT INTO Customers (Username, Password, Phone, Name, Birthdate, Email, Gender, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, phone);
            pst.setString(4, name);
            pst.setString(5, birthdate);
            pst.setString(6, email);
            pst.setString(7, gender);
            pst.setString(8, "Active");
            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Customer added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadCustomers("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void disableCustomer(int customerId) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement("SELECT Status FROM Customers WHERE CustomerID = ?")) {
            checkStmt.setInt(1, customerId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("Status");
                if (status.equals("Disabled")) {
                    JOptionPane.showMessageDialog(this, "Customer account is already disabled.", "Information", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to disable this customer?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement pst = connection.prepareStatement("UPDATE Customers SET Status = 'Disabled' WHERE CustomerID = ?")) {
                pst.setInt(1, customerId);
                int rowsUpdated = pst.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Customer disabled successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCustomers("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        textField_Name.setText("");
        textField_Password.setText("");
        textField_Username.setText("");
        textField_Phone.setText("");
        textField_Birthdate.setText("");
        textField_Email.setText("");
        textField_Gender.setText("");
    }

    /**
     * Create the frame.
     */
    public ManageCustomers() {
        setTitle("Manage Customers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 933, 548);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(228, 228, 228));
        contentPane.setBorder(new CompoundBorder());

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel contentPane_1 = new JPanel();
        contentPane_1.setBorder(new CompoundBorder());
        contentPane_1.setBackground(new Color(42, 42, 42));
        contentPane_1.setBounds(0, 0, 917, 509);
        contentPane.add(contentPane_1);
        contentPane_1.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 917, 100);
        panel.setLayout(null);
        panel.setBackground(new Color(32, 32, 32));
        contentPane_1.add(panel);

        JLabel lblManageCustomers = new JLabel("Manage Customers");
        lblManageCustomers.setForeground(Color.WHITE);
        lblManageCustomers.setFont(new Font("Century Gothic", Font.BOLD, 30));
        lblManageCustomers.setBounds(335, 33, 321, 37);
        panel.add(lblManageCustomers);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(298, 150, 619, 337);
        contentPane_1.add(scrollPane);

        customersTable = new JTable();
        scrollPane.setViewportView(customersTable);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setBounds(10, 148, 123, 27);
        lblUsername.setFont(new Font("Century Gothic", Font.BOLD, 15));
        contentPane_1.add(lblUsername);

        textField_Username = new JTextField();
        textField_Username.setBounds(135, 149, 153, 27);
        textField_Username.setFont(new Font("Century Gothic", Font.BOLD, 15));
        textField_Username.setColumns(10);
        contentPane_1.add(textField_Username);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setBounds(10, 188, 123, 27);
        lblPassword.setFont(new Font("Century Gothic", Font.BOLD, 15));
        contentPane_1.add(lblPassword);

        textField_Password = new JPasswordField();
        textField_Password.setBounds(135, 189, 153, 27);
        textField_Password.setFont(new Font("Century Gothic", Font.BOLD, 15));
        textField_Password.setColumns(10);
        contentPane_1.add(textField_Password);

        JLabel lblName = new JLabel("Name:");
        lblName.setForeground(Color.WHITE);
        lblName.setBounds(10, 230, 123, 27);
        lblName.setFont(new Font("Century Gothic", Font.BOLD, 15));
        contentPane_1.add(lblName);

        textField_Name = new JTextField();
        textField_Name.setBounds(135, 229, 153, 27);
        textField_Name.setFont(new Font("Century Gothic", Font.BOLD, 15));
        textField_Name.setColumns(10);
        contentPane_1.add(textField_Name);

        JLabel lblPhone = new JLabel("Phone #:");
        lblPhone.setForeground(Color.WHITE);
        lblPhone.setBounds(10, 270, 123, 27);
        lblPhone.setFont(new Font("Century Gothic", Font.BOLD, 15));
        contentPane_1.add(lblPhone);

        textField_Phone = new JTextField();
        textField_Phone.setBounds(135, 269, 153, 27);
        textField_Phone.setFont(new Font("Century Gothic", Font.BOLD, 15));
        textField_Phone.setColumns(10);
        contentPane_1.add(textField_Phone);

        JLabel lblBirthdate = new JLabel("Birthdate:");
        lblBirthdate.setForeground(Color.WHITE);
        lblBirthdate.setBounds(10, 310, 123, 27);
        lblBirthdate.setFont(new Font("Century Gothic", Font.BOLD, 15));
        contentPane_1.add(lblBirthdate);

        textField_Birthdate = new JTextField();
        textField_Birthdate.setBounds(135, 309, 153, 27);
        textField_Birthdate.setFont(new Font("Century Gothic", Font.BOLD, 15));
        textField_Birthdate.setColumns(10);
        contentPane_1.add(textField_Birthdate);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setBounds(10, 350, 123, 27);
        lblEmail.setFont(new Font("Century Gothic", Font.BOLD, 15));
        contentPane_1.add(lblEmail);

        textField_Email = new JTextField();
        textField_Email.setBounds(135, 349, 153, 27);
        textField_Email.setFont(new Font("Century Gothic", Font.BOLD, 15));
        textField_Email.setColumns(10);
        contentPane_1.add(textField_Email);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setForeground(Color.WHITE);
        lblGender.setBounds(10, 390, 123, 27);
        lblGender.setFont(new Font("Century Gothic", Font.BOLD, 15));
        contentPane_1.add(lblGender);

        textField_Gender = new JTextField();
        textField_Gender.setBounds(135, 389, 153, 27);
        textField_Gender.setFont(new Font("Century Gothic", Font.BOLD, 15));
        textField_Gender.setColumns(10);
        contentPane_1.add(textField_Gender);

        JButton btnAdd = new JButton("Add");
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBounds(10, 427, 83, 27);
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCustomer(
                    textField_Username.getText(),
                    new String(textField_Password.getPassword()),
                    textField_Phone.getText(),
                    textField_Name.getText(),
                    textField_Birthdate.getText(),
                    textField_Email.getText(),
                    textField_Gender.getText()
                );
            }
        });
        btnAdd.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnAdd.setBackground(new Color(32, 32, 32));
        contentPane_1.add(btnAdd);

        btnDisable = new JButton("Disable");
        btnDisable.setForeground(Color.WHITE);
        btnDisable.setBounds(104, 427, 95, 27);
        btnDisable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customersTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int customerId = (int) customersTable.getValueAt(selectedRow, 0);
                    disableCustomer(customerId);
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Please select a customer to disable.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnDisable.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnDisable.setBackground(new Color(32, 32, 32));
        contentPane_1.add(btnDisable);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setForeground(Color.WHITE);
        lblSearch.setBounds(348, 113, 94, 27);
        lblSearch.setFont(new Font("Century Gothic", Font.BOLD, 15));
        contentPane_1.add(lblSearch);

        searchField = new JTextField();
        searchField.setBounds(439, 114, 153, 30);
        searchField.setFont(new Font("Century Gothic", Font.BOLD, 15));
        contentPane_1.add(searchField);
        searchField.setColumns(10);

        rdbtnID = new JRadioButton("ID");
        rdbtnID.setForeground(Color.WHITE);
        rdbtnID.setBounds(608, 114, 51, 30);
        rdbtnID.setFont(new Font("Century Gothic", Font.BOLD, 15));
        rdbtnID.setBackground(new Color(42, 42, 42));
        contentPane_1.add(rdbtnID);

        rdbtnName = new JRadioButton("Name");
        rdbtnName.setForeground(Color.WHITE);
        rdbtnName.setBounds(668, 114, 100, 30);
        rdbtnName.setFont(new Font("Century Gothic", Font.BOLD, 15));
        rdbtnName.setBackground(new Color(42, 42, 42));
        contentPane_1.add(rdbtnName);

        rdbtnStatus = new JRadioButton("Status");
        rdbtnStatus.setForeground(Color.WHITE);
        rdbtnStatus.setBounds(768, 114, 100, 30);
        rdbtnStatus.setFont(new Font("Century Gothic", Font.BOLD, 15));
        rdbtnStatus.setBackground(new Color(42, 42, 42));
        contentPane_1.add(rdbtnStatus);

        searchGroup = new ButtonGroup();
        searchGroup.add(rdbtnID);
        searchGroup.add(rdbtnName);
        searchGroup.add(rdbtnStatus);
        rdbtnID.setSelected(true);
        
        btnEdit = new JButton("Edit");
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setBounds(209, 427, 83, 27);
        btnEdit.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnEdit.setBackground(new Color(32, 32, 32));
        contentPane_1.add(btnEdit);
        
        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customersTable.getSelectedRow();
                if (selectedRow != -1) {
                    int customerId = (int) customersTable.getValueAt(selectedRow, 0);
                    String currentUsername = (String) customersTable.getValueAt(selectedRow, 1);
                    String currentPassword = (String) customersTable.getValueAt(selectedRow, 2);
                    String currentPhone = (String) customersTable.getValueAt(selectedRow, 3);
                    String currentName = (String) customersTable.getValueAt(selectedRow, 4);
                    String currentBirthdate = (String) customersTable.getValueAt(selectedRow, 5);
                    String currentEmail = (String) customersTable.getValueAt(selectedRow, 6);
                    String currentGender = (String) customersTable.getValueAt(selectedRow, 7);
                    String currentStatus = (String) customersTable.getValueAt(selectedRow, 8);

                    EditCustomerForm editCustomerForm = new EditCustomerForm(customerId, currentUsername, currentPassword, currentPhone, currentName, currentBirthdate, currentEmail, currentGender, currentStatus, ManageCustomers.this);
                    editCustomerForm.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a customer to edit.");
                }
            }
        });

        
        btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnBack.setBackground(new Color(32, 32, 32));
        btnBack.setBounds(10, 472, 83, 27);
        contentPane_1.add(btnBack);
        
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminHomepage adminPage = new AdminHomepage();
                adminPage.main(new String[]{});
                dispose();
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                loadCustomers(searchField.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                loadCustomers(searchField.getText());
            }

            public void insertUpdate(DocumentEvent e) {
                loadCustomers(searchField.getText());
            }
        });

        loadCustomers("");
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ManageCustomers frame = new ManageCustomers();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                    frame.setResizable(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class PasswordCellRenderer extends DefaultTableCellRenderer {
        @Override
        protected void setValue(Object value) {
            if (value != null) {
                setText(new String(new char[value.toString().length()]).replace("\0", "*"));
            } else {
                setText("");
            }
        }
    }
}
