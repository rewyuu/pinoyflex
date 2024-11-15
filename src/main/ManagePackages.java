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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;

public class ManagePackages extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField packageNameField;
    private JTextField durationField;
    private JTextField priceField;
    private JTable packagesTable;
    private JButton btnEdit;

    public void loadPackages() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("SELECT * FROM Packages")) {
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new String[]{"Package ID", "Package Name", "Duration", "Price"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; 
                }
            };

            while (rs.next()) {
                int packageId = rs.getInt("PackageID");
                String packageName = rs.getString("PackageName");
                String duration = rs.getString("Duration");
                double price = rs.getDouble("Price");

                model.addRow(new Object[]{packageId, packageName, duration, price});
            }

            packagesTable.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPackage(String packageName, String duration, String price) {
        if (packageName.isEmpty() || duration.isEmpty() || price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide all details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int durationMonths;
        try {
            durationMonths = Integer.parseInt(duration);
            if (durationMonths <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid duration. Please enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double packagePrice;
        try {
            packagePrice = Double.parseDouble(price);
            if (packagePrice <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid price. Please enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement("SELECT COUNT(*) FROM Packages WHERE PackageName = ?")) {
            checkStmt.setString(1, packageName);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) {
                JOptionPane.showMessageDialog(this, "Package name already exists. Please choose another name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("INSERT INTO Packages (PackageName, Duration, Price) VALUES (?, ?, ?)")) {
            pst.setString(1, packageName);
            pst.setString(2, String.valueOf(durationMonths));
            pst.setDouble(3, packagePrice);
            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Package added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                packageNameField.setText("");
                durationField.setText("");
                priceField.setText("");
                loadPackages();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deletePackage(int packageId) {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this package?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement pst = connection.prepareStatement("DELETE FROM Packages WHERE PackageID = ?")) {
                pst.setInt(1, packageId);
                int rowsDeleted = pst.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Package deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadPackages();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ManagePackages() {
        setTitle("Manage Packages");
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

        JLabel lblManagePackages = new JLabel("Manage Packages");
        lblManagePackages.setForeground(Color.WHITE);
        lblManagePackages.setFont(new Font("Century Gothic", Font.BOLD, 30));
        lblManagePackages.setBounds(335, 33, 321, 37);
        panel.add(lblManagePackages);

        JLabel lblPackageName = new JLabel("Package Name");
        lblPackageName.setForeground(Color.WHITE);
        lblPackageName.setFont(new Font("Century Gothic", Font.BOLD, 23));
        lblPackageName.setBounds(43, 189, 213, 37);
        contentPane_1.add(lblPackageName);

        JLabel lblDuration = new JLabel("Duration (months)");
        lblDuration.setForeground(Color.WHITE);
        lblDuration.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblDuration.setBounds(43, 232, 184, 37);
        contentPane_1.add(lblDuration);

        JLabel lblPrice = new JLabel("Price");
        lblPrice.setForeground(Color.WHITE);
        lblPrice.setFont(new Font("Century Gothic", Font.BOLD, 23));
        lblPrice.setBounds(43, 275, 79, 37);
        contentPane_1.add(lblPrice);

        packageNameField = new JTextField();
        packageNameField.setColumns(10);
        packageNameField.setBounds(239, 199, 140, 27);
        contentPane_1.add(packageNameField);

        durationField = new JTextField();
        durationField.setColumns(10);
        durationField.setBounds(239, 237, 140, 27);
        contentPane_1.add(durationField);

        priceField = new JTextField();
        priceField.setColumns(10);
        priceField.setBounds(239, 285, 140, 27);
        contentPane_1.add(priceField);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(425, 128, 456, 357);
        contentPane_1.add(scrollPane);

        packagesTable = new JTable();
        packagesTable.setModel(new DefaultTableModel(
                new Object[][]{
                    {null, null, null, null}
                },
                new String[]{
                    "Package ID", "Package Name", "Duration", "Price"
                }
        ) {
            Class[] columnTypes = new Class[]{
                Integer.class, String.class, String.class, Double.class
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

        scrollPane.setViewportView(packagesTable);

        btnEdit = new JButton("Edit");
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnEdit.setBackground(new Color(32, 32, 32));
        btnEdit.setBounds(287, 356, 107, 37);
        contentPane_1.add(btnEdit);

        JButton btnAddButton = new JButton("Add");
        btnAddButton.setForeground(Color.WHITE);
        btnAddButton.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnAddButton.setBackground(new Color(32, 32, 32));
        btnAddButton.setBounds(43, 356, 107, 37);
        contentPane_1.add(btnAddButton);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnDelete.setBackground(new Color(32, 32, 32));
        btnDelete.setBounds(160, 356, 107, 37);
        contentPane_1.add(btnDelete);

        JButton btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnBack.setBackground(new Color(32, 32, 32));
        btnBack.setBounds(36, 461, 107, 37);
        contentPane_1.add(btnBack);

        btnAddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String packageName = packageNameField.getText();
                String duration = durationField.getText();
                String price = priceField.getText();
                addPackage(packageName, duration, price);
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = packagesTable.getSelectedRow();
                if (selectedRow != -1) {
                    int packageId = (int) packagesTable.getValueAt(selectedRow, 0);
                    deletePackage(packageId);
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Please select a package to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = packagesTable.getSelectedRow();
                if (selectedRow != -1) {
                    int packageId = (int) packagesTable.getValueAt(selectedRow, 0);
                    String packageName = (String) packagesTable.getValueAt(selectedRow, 1);
                    String duration = (String) packagesTable.getValueAt(selectedRow, 2);
                    double price = (double) packagesTable.getValueAt(selectedRow, 3);
                    new EditPackagesForm(ManagePackages.this, packageId, packageName, duration, price).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Please select a package to edit.", "Error", JOptionPane.ERROR_MESSAGE);
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

        loadPackages();
    }

    public void updatePackage(int packageId, String packageName, String duration, double price) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("UPDATE Packages SET PackageName = ?, Duration = ?, Price = ? WHERE PackageID = ?")) {
            pst.setString(1, packageName);
            pst.setString(2, duration);
            pst.setDouble(3, price);
            pst.setInt(4, packageId);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Package updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPackages();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ManagePackages frame = new ManagePackages();
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
