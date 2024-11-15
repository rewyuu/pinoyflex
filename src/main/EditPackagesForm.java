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

public class EditPackagesForm extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField packageNameField;
    private JTextField durationField;
    private JTextField priceField;
    private int packageId;
    private ManagePackages managePackages;

    public EditPackagesForm(ManagePackages managePackages, int packageId, String packageName, String duration, double price) {
        this.packageId = packageId;
        this.managePackages = managePackages;

        setTitle("Edit Package");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setForeground(Color.WHITE);
        contentPane.setBackground(new Color(42, 42, 42));
        contentPane.setBorder(new CompoundBorder());

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblEditPackage = new JLabel("Edit Package");
        lblEditPackage.setForeground(Color.WHITE);
        lblEditPackage.setFont(new Font("Century Gothic", Font.BOLD, 24));
        lblEditPackage.setBounds(126, 11, 174, 30);
        contentPane.add(lblEditPackage);

        packageNameField = new JTextField();
        packageNameField.setFont(new Font("Tahoma", Font.PLAIN, 11));
        packageNameField.setBounds(186, 65, 174, 29);
        contentPane.add(packageNameField);
        packageNameField.setColumns(10);
        packageNameField.setText(packageName);

        JLabel lblDuration = new JLabel("Duration");
        lblDuration.setForeground(Color.WHITE);
        lblDuration.setFont(new Font("Century Gothic", Font.BOLD, 23));
        lblDuration.setBounds(50, 105, 150, 22);
        contentPane.add(lblDuration);

        durationField = new JTextField();
        durationField.setFont(new Font("Tahoma", Font.PLAIN, 11));
        durationField.setBounds(186, 105, 174, 30);
        contentPane.add(durationField);
        durationField.setColumns(10);
        durationField.setText(duration);

        JLabel lblPrice = new JLabel("Price");
        lblPrice.setForeground(Color.WHITE);
        lblPrice.setFont(new Font("Century Gothic", Font.BOLD, 23));
        lblPrice.setBounds(50, 145, 150, 22);
        contentPane.add(lblPrice);

        priceField = new JTextField();
        priceField.setFont(new Font("Tahoma", Font.PLAIN, 11));
        priceField.setBounds(186, 145, 174, 30);
        contentPane.add(priceField);
        priceField.setColumns(10);
        priceField.setText(String.valueOf(price));

        JButton btnSave = new JButton("Save");
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(new Color(32, 32, 32));
        btnSave.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnSave.setBounds(64, 194, 100, 37);
        contentPane.add(btnSave);
        
        JLabel lblPackageName = new JLabel("Title");
        lblPackageName.setForeground(Color.WHITE);
        lblPackageName.setToolTipText("Package Name");
        lblPackageName.setFont(new Font("Century Gothic", Font.BOLD, 23));
        lblPackageName.setBounds(47, 57, 120, 37);
        contentPane.add(lblPackageName);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnCancel.setBackground(new Color(32, 32, 32));
        btnCancel.setBounds(232, 194, 107, 37);
        contentPane.add(btnCancel);
        
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String updatedPackageName = packageNameField.getText();
                String updatedDuration = durationField.getText();
                double updatedPrice = Double.parseDouble(priceField.getText());
                managePackages.updatePackage(packageId, updatedPackageName, updatedDuration, updatedPrice);
                dispose();
            }
        });
        
        setLocationRelativeTo(null);
        setResizable(false);
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
	