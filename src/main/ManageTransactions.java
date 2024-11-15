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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class ManageTransactions extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField_CustomerID;
    private JComboBox<String> comboBox_packages;
    private JTable transactionsTable;
    private JTable customersTable;
    private JTextField textField;
    private JRadioButton rdbtnCustomerId;
    private JRadioButton rdbtnPackageName;

    public ManageTransactions() {
        setTitle("Manage Transactions");
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

        JLabel lblManageTransactions = new JLabel("Manage Transactions");
        lblManageTransactions.setForeground(Color.WHITE);
        lblManageTransactions.setFont(new Font("Century Gothic", Font.BOLD, 30));
        lblManageTransactions.setBounds(335, 33, 321, 37);
        panel.add(lblManageTransactions);

        JLabel lblCustomerId = new JLabel("Customer ID");
        lblCustomerId.setForeground(Color.WHITE);
        lblCustomerId.setBounds(22, 296, 133, 37);
        lblCustomerId.setFont(new Font("Century Gothic", Font.BOLD, 20));
        contentPane_1.add(lblCustomerId);

        JLabel lblSelectedPackage = new JLabel("Select Package");
        lblSelectedPackage.setForeground(Color.WHITE);
        lblSelectedPackage.setBounds(22, 343, 184, 37);
        lblSelectedPackage.setFont(new Font("Century Gothic", Font.BOLD, 20));
        contentPane_1.add(lblSelectedPackage);

        textField_CustomerID = new JTextField();
        textField_CustomerID.setBounds(216, 300, 140, 27);
        textField_CustomerID.setFont(new Font("Century Gothic", Font.BOLD, 20));
        textField_CustomerID.setColumns(10);
        contentPane_1.add(textField_CustomerID);

        comboBox_packages = new JComboBox<>();
        comboBox_packages.setBackground(Color.WHITE);
        comboBox_packages.setBounds(216, 351, 140, 27);
        contentPane_1.add(comboBox_packages);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(366, 166, 541, 319);
        contentPane_1.add(scrollPane);

        transactionsTable = new JTable();
        transactionsTable.setModel(new DefaultTableModel(
            new Object[][] {
                { null, null, null, null, null, null }
            },
            new String[] {
                "Transaction ID", "Customer ID", "Date", "Package ID", "Package Name", "Price"
            }
        ) {
            Class[] columnTypes = new Class[] {
                Integer.class, Integer.class, String.class, Integer.class, String.class, Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            boolean[] columnEditables = new boolean[] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });

        scrollPane.setViewportView(transactionsTable);

        JScrollPane scrollPaneCustomers = new JScrollPane();
        scrollPaneCustomers.setBounds(22, 128, 310, 154); 
        contentPane_1.add(scrollPaneCustomers);

        customersTable = new JTable();
        customersTable.setModel(new DefaultTableModel(
            new Object[][] {
                { null, null, null, null }
            },
            new String[] {
               "User ID", "Username","Name", "Status"
            }
        ));
        scrollPaneCustomers.setViewportView(customersTable);

        JButton btnAddButton = new JButton("Add");
        btnAddButton.setBounds(62, 409, 93, 27);
        btnAddButton.setForeground(Color.WHITE);
        btnAddButton.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnAddButton.setBackground(new Color(32, 32, 32));
        contentPane_1.add(btnAddButton);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(216, 408, 94, 29);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnDelete.setBackground(new Color(32, 32, 32));
        contentPane_1.add(btnDelete);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(21, 460, 93, 27);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnBack.setBackground(new Color(32, 32, 32));
        contentPane_1.add(btnBack);
        
        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setForeground(Color.WHITE);
        lblSearch.setFont(new Font("Century Gothic", Font.BOLD, 15));
        lblSearch.setBounds(366, 125, 94, 27);
        contentPane_1.add(lblSearch);
        
        textField = new JTextField();
        textField.setFont(new Font("Century Gothic", Font.BOLD, 15));
        textField.setColumns(10);
        textField.setBounds(439, 126, 164, 26);
        contentPane_1.add(textField);
        
        JRadioButton rdbtnCustomerId = new JRadioButton("Customer ID");
        rdbtnCustomerId.setForeground(Color.WHITE);
        rdbtnCustomerId.setFont(new Font("Century Gothic", Font.BOLD, 15));
        rdbtnCustomerId.setBackground(new Color(42, 42, 42));
        rdbtnCustomerId.setBounds(609, 126, 125, 30);
        contentPane_1.add(rdbtnCustomerId);
        
        JRadioButton rdbtnPackageName = new JRadioButton("Package Name");
        rdbtnPackageName.setForeground(Color.WHITE);
        rdbtnPackageName.setFont(new Font("Century Gothic", Font.BOLD, 15));
        rdbtnPackageName.setBackground(new Color(42, 42, 42));
        rdbtnPackageName.setBounds(736, 126, 164, 30);
        contentPane_1.add(rdbtnPackageName);
        
        ButtonGroup searchGroup = new ButtonGroup();
        searchGroup.add(rdbtnCustomerId);
        searchGroup.add(rdbtnPackageName);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(775, 166, 100, 27);
        btnSearch.setForeground(Color.BLACK);
        btnSearch.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnSearch.setBackground(Color.WHITE);
        contentPane_1.add(btnSearch);

        btnAddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int customerId = Integer.parseInt(textField_CustomerID.getText());
                    String date = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                    String selected_package = (String) comboBox_packages.getSelectedItem();

                    if (customerId <= 0 || selected_package.isEmpty()) {
                        JOptionPane.showMessageDialog(contentPane_1, "Please provide all details.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    addTransaction(customerId, date, selected_package);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(contentPane_1, "Invalid input. Please check your entries.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = transactionsTable.getSelectedRow();
                if (selectedRow != -1) {
                    int transactionId = (int) transactionsTable.getValueAt(selectedRow, 0);
                    deleteTransaction(transactionId);
                } else {
                    JOptionPane.showMessageDialog(contentPane_1, "Please select a transaction to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminHomepage.main(new String[] {});
                dispose();
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchTransactions();
            }
        });

        loadTransactions();
        loadCustomers();
    }
    
    private void searchTransactions() {
        String searchText = textField.getText().trim();
        if (searchText.isEmpty()) {
            loadTransactions();
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query;
            if (rdbtnCustomerId.isSelected()) {
                query = "SELECT TransactionID, CustomerID, Date, selected_package FROM Transactions WHERE CustomerID = ?";
            } else {
                query = "SELECT t.TransactionID, t.CustomerID, t.Date, t.selected_package " +
                        "FROM Transactions t " +
                        "JOIN Packages p ON t.selected_package = p.PackageID " +
                        "WHERE p.PackageName LIKE ?";
            }

            try (PreparedStatement pst = connection.prepareStatement(query)) {
                if (rdbtnCustomerId.isSelected()) {
                    try {
                        pst.setInt(1, Integer.parseInt(searchText));
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid Customer ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    pst.setString(1, "%" + searchText + "%");
                }

                ResultSet rs = pst.executeQuery();
                DefaultTableModel model = (DefaultTableModel) transactionsTable.getModel();
                model.setRowCount(0); // Clear the table

                while (rs.next()) {
                    int transactionID = rs.getInt("TransactionID");
                    int customerID = rs.getInt("CustomerID");
                    String date = rs.getString("Date");
                    int selected_package = rs.getInt("selected_package");

                    try (PreparedStatement packagePst = connection.prepareStatement("SELECT PackageName, Price FROM Packages WHERE PackageID = ?")) {
                        packagePst.setInt(1, selected_package);
                        ResultSet packageRs = packagePst.executeQuery();

                        if (packageRs.next()) {
                            String packageName = packageRs.getString("PackageName");
                            double price = packageRs.getDouble("Price");

                            model.addRow(new Object[]{transactionID, customerID, date, selected_package, packageName, price});
                        } else {
                            System.err.println("Package ID not found: " + selected_package);
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Search failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    private void loadCustomers() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("SELECT CustomerID, Username, Name, Status FROM customers")) {

            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) customersTable.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                int userID = rs.getInt("CustomerID");
                String username = rs.getString("Username");
                String name = rs.getString("Name");
                String status = rs.getString("Status");

                model.addRow(new Object[]{userID, username, name, status});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load customers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTransaction(int customerId, String date, String selected_package) {
        if (date.isEmpty() || selected_package.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide all details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkCustomer = connection.prepareStatement("SELECT COUNT(*) FROM Customers WHERE CustomerID = ?");
             PreparedStatement checkSubscription = connection.prepareStatement("SELECT COUNT(*) FROM Transactions WHERE CustomerID = ?");
             PreparedStatement pst = connection.prepareStatement("INSERT INTO Transactions (CustomerID, Date, selected_package) VALUES (?, ?, ?)")) {

            checkCustomer.setInt(1, customerId);
            ResultSet rsCustomer = checkCustomer.executeQuery();
            if (rsCustomer.next() && rsCustomer.getInt(1) == 0) {
                JOptionPane.showMessageDialog(this, "Customer ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            checkSubscription.setInt(1, customerId);
            ResultSet rsSubscription = checkSubscription.executeQuery();
            if (rsSubscription.next() && rsSubscription.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Customer already has a subscription.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] parts = selected_package.split(" - ");
            int packageId = Integer.parseInt(parts[0].trim());

            pst.setInt(1, customerId);
            pst.setString(2, date);
            pst.setInt(3, packageId);
            int rowsInserted = pst.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Transaction added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadTransactions();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add transaction", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTransaction(int transactionId) {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this transaction?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement pst = connection.prepareStatement("DELETE FROM Transactions WHERE TransactionID = ?")) {
                pst.setInt(1, transactionId);
                int rowsDeleted = pst.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Transaction deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadTransactions(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete transaction", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void loadTransactions() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("SELECT PackageID, PackageName FROM Packages")) {

            ResultSet rs = pst.executeQuery();

            comboBox_packages.removeAllItems();
            while (rs.next()) {
                int packageId = rs.getInt("PackageID");
                String packageName = rs.getString("PackageName");
                comboBox_packages.addItem(packageId + " - " + packageName);
            }

            if (comboBox_packages.getItemCount() > 0) {
                comboBox_packages.setSelectedIndex(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("SELECT TransactionID, CustomerID, Date, selected_package FROM Transactions")) {

            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) transactionsTable.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                int transactionID = rs.getInt("TransactionID");
                int customerID = rs.getInt("CustomerID");
                String date = rs.getString("Date");
                int selected_package = rs.getInt("selected_package");
                
                try (PreparedStatement packagePst = connection.prepareStatement("SELECT PackageName, Price FROM Packages WHERE PackageID = ?")) {
                    packagePst.setInt(1, selected_package);
                    ResultSet packageRs = packagePst.executeQuery();

                    if (packageRs.next()) {
                        String packageName = packageRs.getString("PackageName");
                        double price = packageRs.getDouble("Price");

                        model.addRow(new Object[]{transactionID, customerID, date, selected_package, packageName, price});
                    } else {
                        System.err.println("Package ID not found: " + selected_package);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ManageTransactions frame = new ManageTransactions();
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
