package main;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenerateReports extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableMembers;
    private JTable tableCoaches;
    private JTable tableSales;
    private JLabel lblTotalSales;
    private JTextField textField;
    private JRadioButton rdbtnMembers;
    private JRadioButton rdbtnTrainers;
    private JRadioButton rdbtnTransactions;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GenerateReports frame = new GenerateReports();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                    frame.setResizable(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GenerateReports() {
        setTitle("Reports");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 933, 548);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(42, 42, 42));
        contentPane.setBorder(new CompoundBorder());
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblListMembers = new JLabel("List of Members");
        lblListMembers.setForeground(Color.WHITE);
        lblListMembers.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblListMembers.setHorizontalAlignment(SwingConstants.CENTER);
        lblListMembers.setBounds(33, 168, 200, 30);
        contentPane.add(lblListMembers);

        JScrollPane scrollPaneMembers = new JScrollPane();
        scrollPaneMembers.setBounds(33, 208, 200, 200);
        contentPane.add(scrollPaneMembers);

        tableMembers = new JTable();
        scrollPaneMembers.setViewportView(tableMembers);

        JLabel lblListCoaches = new JLabel("List of Trainers");
        lblListCoaches.setForeground(Color.WHITE);
        lblListCoaches.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblListCoaches.setHorizontalAlignment(SwingConstants.CENTER);
        lblListCoaches.setBounds(305, 168, 200, 30);
        contentPane.add(lblListCoaches);

        JScrollPane scrollPaneCoaches = new JScrollPane();
        scrollPaneCoaches.setBounds(257, 208, 284, 200);
        contentPane.add(scrollPaneCoaches);

        tableCoaches = new JTable();
        scrollPaneCoaches.setViewportView(tableCoaches);

        JLabel lblListSales = new JLabel("List of Transactions");
        lblListSales.setForeground(Color.WHITE);
        lblListSales.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblListSales.setHorizontalAlignment(SwingConstants.CENTER);
        lblListSales.setBounds(641, 168, 200, 30);
        contentPane.add(lblListSales);

        JScrollPane scrollPaneSales = new JScrollPane();
        scrollPaneSales.setBounds(572, 208, 324, 200);
        contentPane.add(scrollPaneSales);

        tableSales = new JTable();
        scrollPaneSales.setViewportView(tableSales);

        lblTotalSales = new JLabel("Total Sales: P" + getTotalSales());
        lblTotalSales.setForeground(Color.WHITE);
        lblTotalSales.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblTotalSales.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalSales.setBounds(513, 409, 300, 30);
        contentPane.add(lblTotalSales);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(32, 32, 32));
        panel.setBounds(0, 0, 917, 100);
        contentPane.add(panel);

        JLabel lblReports = new JLabel("Reports");
        lblReports.setForeground(Color.WHITE);
        lblReports.setFont(new Font("Century Gothic", Font.BOLD, 30));
        lblReports.setBounds(414, 33, 139, 37);
        panel.add(lblReports);

        JButton btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnBack.setBackground(new Color(32, 32, 32));
        btnBack.setBounds(33, 460, 107, 37);
        contentPane.add(btnBack);

        JButton btnGeneratePDF = new JButton("Generate PDF");
        btnGeneratePDF.setForeground(Color.WHITE);
        btnGeneratePDF.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnGeneratePDF.setBackground(new Color(32, 32, 32));
        btnGeneratePDF.setBounds(665, 461, 200, 37);
        contentPane.add(btnGeneratePDF);

        JLabel lblSearch = new JLabel("Search");
        lblSearch.setForeground(Color.WHITE);
        lblSearch.setFont(new Font("Century Gothic", Font.BOLD, 23));
        lblSearch.setBounds(33, 120, 140, 37);
        contentPane.add(lblSearch);

        textField = new JTextField();
        textField.setColumns(10);
        textField.setBounds(188, 130, 140, 27);
        contentPane.add(textField);

        ButtonGroup group = new ButtonGroup();

        rdbtnMembers = new JRadioButton("Members");
        rdbtnMembers.setSelected(true);
        rdbtnMembers.setForeground(Color.WHITE);
        rdbtnMembers.setFont(new Font("Century Gothic", Font.BOLD, 15));
        rdbtnMembers.setBackground(new Color(42, 42, 42));
        rdbtnMembers.setBounds(344, 131, 95, 30);
        contentPane.add(rdbtnMembers);
        group.add(rdbtnMembers);

        rdbtnTrainers = new JRadioButton("Trainers");
        rdbtnTrainers.setForeground(Color.WHITE);
        rdbtnTrainers.setFont(new Font("Century Gothic", Font.BOLD, 15));
        rdbtnTrainers.setBackground(new Color(42, 42, 42));
        rdbtnTrainers.setBounds(441, 131, 95, 30);
        contentPane.add(rdbtnTrainers);
        group.add(rdbtnTrainers);

        rdbtnTransactions = new JRadioButton("Transactions");
        rdbtnTransactions.setForeground(Color.WHITE);
        rdbtnTransactions.setFont(new Font("Century Gothic", Font.BOLD, 15));
        rdbtnTransactions.setBackground(new Color(42, 42, 42));
        rdbtnTransactions.setBounds(538, 132, 128, 30);
        contentPane.add(rdbtnTransactions);
        group.add(rdbtnTransactions);

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminHomepage adminPage = new AdminHomepage();
                adminPage.main(new String[]{});
                dispose();
            }
        });

        btnGeneratePDF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    generatePDFReports();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error generating PDF: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        updateMembersTable();
        updateCoachesTable();
        updateSalesTable();
    }

    private double getTotalSales() {
        double totalSales = 0;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("SELECT SUM(P.Price) FROM transactions T JOIN packages P ON T.selected_package = P.PackageID")) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                totalSales = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalSales;
    }

    private void updateMembersTable() {
        String searchText = textField.getText().trim();
        String sql = "SELECT Username, Phone, Status FROM customers";
        if (!searchText.isEmpty()) {
            sql += " WHERE Username LIKE '%" + searchText + "%' OR Phone LIKE '%" + searchText + "%'";
        }
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            tableMembers.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCoachesTable() {
        String searchText = textField.getText().trim();
        String sql = "SELECT TrainerName, Contact, Specialization FROM trainers";
        if (!searchText.isEmpty()) {
            sql += " WHERE TrainerName LIKE '%" + searchText + "%' OR Contact LIKE '%" + searchText + "%'";
        }
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            tableCoaches.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateSalesTable() {
        String searchText = textField.getText().trim();
        String sql = "SELECT T.TransactionID, T.Date, T.selected_package, P.Price " +
                "FROM transactions T " +
                "JOIN packages P ON T.selected_package = P.PackageID";
        if (!searchText.isEmpty()) {
            sql += " WHERE T.TransactionID LIKE '%" + searchText + "%' OR T.Date LIKE '%" + searchText + "%'";
        }
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            tableSales.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        java.sql.ResultSetMetaData metaData = rs.getMetaData();
        int columns = metaData.getColumnCount();
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int column = 1; column <= columns; column++) {
            model.addColumn(metaData.getColumnLabel(column));
        }

        while (rs.next()) {
            Object[] row = new Object[columns];
            for (int i = 1; i <= columns; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }

        return model;
    }

    private void performSearch() {
        if (rdbtnMembers.isSelected()) {
            updateMembersTable();
        } else if (rdbtnTrainers.isSelected()) {
            updateCoachesTable();
        } else if (rdbtnTransactions.isSelected()) {
            updateSalesTable();
        }
        lblTotalSales.setText("Total Sales: P" + getTotalSales());
    }

    private void generatePDFReports() throws IOException {
        try {
            File pdfFile = new File("Reports.pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
            fileOutputStream.write(generatePDFContent().getBytes());
            fileOutputStream.close();

            JOptionPane.showMessageDialog(null, "PDF report generated successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generating PDF: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generatePDFContent() {
        StringBuilder contentBuilder = new StringBuilder();

        contentBuilder.append("List of Members:\n");
        contentBuilder.append(tableToString(tableMembers));
        contentBuilder.append("\n\n");

        contentBuilder.append("List of Trainers:\n");
        contentBuilder.append(tableToString(tableCoaches));
        contentBuilder.append("\n\n");

        contentBuilder.append("List of Transactions:\n");
        contentBuilder.append(tableToString(tableSales));

        return contentBuilder.toString();
    }

    private String tableToString(JTable table) {
        StringBuilder tableBuilder = new StringBuilder();

        for (int i = 0; i < table.getColumnCount(); i++) {
            tableBuilder.append(table.getColumnName(i)).append("\t");
        }
        tableBuilder.append("\n");

        for (int row = 0; row < table.getRowCount(); row++) {
            for (int col = 0; col < table.getColumnCount(); col++) {
                tableBuilder.append(table.getValueAt(row, col)).append("\t");
            }
            tableBuilder.append("\n");
        }

        return tableBuilder.toString();
    }
}
