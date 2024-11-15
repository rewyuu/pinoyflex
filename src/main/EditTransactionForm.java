package main;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditTransactionForm extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField textField_TransactionID;
    private JTextField textField_Date;
    private JTextField textField_Amount;
    private JTextField textField_SelectedPackage;

    public EditTransactionForm(int transactionId, int customerId, String date, double amount, String selectedPackage, JFrame parent) {
        setTitle("Edit Transaction");
        setBounds(100, 100, 450, 350);  
        getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(42, 42, 42));
        panel.setBounds(0, 0, 434, 311); 
        getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblTransactionId = new JLabel("Transaction ID:");
        lblTransactionId.setForeground(Color.WHITE);
        lblTransactionId.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblTransactionId.setBounds(10, 73, 156, 26);
        panel.add(lblTransactionId);

        textField_TransactionID = new JTextField();
        textField_TransactionID.setEditable(false);
        textField_TransactionID.setBounds(176, 73, 215, 32);
        panel.add(textField_TransactionID);
        textField_TransactionID.setColumns(10);

        JLabel lblDate = new JLabel("Date:");
        lblDate.setForeground(Color.WHITE);
        lblDate.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblDate.setBounds(10, 116, 127, 26);
        panel.add(lblDate);

        textField_Date = new JTextField();
        textField_Date.setColumns(10);
        textField_Date.setBounds(176, 117, 215, 31);
        panel.add(textField_Date);

        JLabel lblAmount = new JLabel("Amount:");
        lblAmount.setForeground(Color.WHITE);
        lblAmount.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblAmount.setBounds(10, 158, 127, 26);
        panel.add(lblAmount);

        textField_Amount = new JTextField();
        textField_Amount.setColumns(10);
        textField_Amount.setBounds(176, 159, 215, 26);
        panel.add(textField_Amount);

        JLabel lblSelectedPackage = new JLabel("Selected Package:");  
        lblSelectedPackage.setForeground(Color.WHITE);
        lblSelectedPackage.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblSelectedPackage.setBounds(10, 200, 200, 26);
        panel.add(lblSelectedPackage);

        textField_SelectedPackage = new JTextField();  
        textField_SelectedPackage.setColumns(10);
        textField_SelectedPackage.setBounds(220, 200, 171, 26);
        panel.add(textField_SelectedPackage);

        textField_TransactionID.setText(String.valueOf(transactionId));
        textField_Date.setText(date);
        textField_Amount.setText(String.valueOf(amount));
        textField_SelectedPackage.setText(selectedPackage);

        JButton btnSave = new JButton("Save");
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnSave.setBackground(new Color(32, 32, 32));
        btnSave.setBounds(68, 250, 100, 37);
        panel.add(btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnCancel.setBackground(new Color(32, 32, 32));
        btnCancel.setBounds(236, 250, 107, 37);
        panel.add(btnCancel);

        JLabel lblEditTransactions = new JLabel("Edit Transactions");
        lblEditTransactions.setForeground(Color.WHITE);
        lblEditTransactions.setFont(new Font("Century Gothic", Font.BOLD, 25));
        lblEditTransactions.setBounds(106, 11, 222, 30);
        panel.add(lblEditTransactions);

        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement pst = connection.prepareStatement(
                             "UPDATE Transactions SET Date = ?, Amount = ?, selected_package = ? WHERE TransactionID = ?")) {
                    pst.setString(1, textField_Date.getText());
                    pst.setDouble(2, Double.parseDouble(textField_Amount.getText()));
                    pst.setString(3, textField_SelectedPackage.getText());
                    pst.setInt(4, Integer.parseInt(textField_TransactionID.getText()));
                    int rowsUpdated = pst.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(EditTransactionForm.this, "Transaction updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        ((ManageTransactions) parent).loadTransactions();
                        dispose();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(EditTransactionForm.this, "Error updating transaction", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setModal(true);
    }
}
