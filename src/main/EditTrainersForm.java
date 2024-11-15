package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;

public class EditTrainersForm extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField_TrainerName;
    private JTextField textField_Contact;
    private JTextField textField_Specialization;

    private int trainerId;
    private ManageTrainers manageTrainers;

    public EditTrainersForm(int trainerId, String trainerName, String contact, String specialization, ManageTrainers manageTrainers) {
        this.trainerId = trainerId;
        this.manageTrainers = manageTrainers;

        setTitle("Edit Trainer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(42, 42, 42));
        contentPane.setBorder(new CompoundBorder());

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblEditTrainer = new JLabel("Edit Trainer");
        lblEditTrainer.setForeground(Color.WHITE);
        lblEditTrainer.setFont(new Font("Century Gothic", Font.BOLD, 25));
        lblEditTrainer.setBounds(150, 20, 150, 30);
        contentPane.add(lblEditTrainer);

        JLabel lblTrainerName = new JLabel("Trainer Name");
        lblTrainerName.setForeground(Color.WHITE);
        lblTrainerName.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblTrainerName.setBounds(30, 70, 150, 30);
        contentPane.add(lblTrainerName);

        textField_TrainerName = new JTextField(trainerName);
        textField_TrainerName.setBounds(200, 75, 200, 30);
        contentPane.add(textField_TrainerName);
        textField_TrainerName.setColumns(10);

        JLabel lblContact = new JLabel("Contact #");
        lblContact.setForeground(Color.WHITE);
        lblContact.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblContact.setBounds(30, 110, 150, 30);
        contentPane.add(lblContact);

        textField_Contact = new JTextField(contact);
        textField_Contact.setBounds(200, 115, 200, 29);
        contentPane.add(textField_Contact);
        textField_Contact.setColumns(10);

        JLabel lblSpecialization = new JLabel("Specialization");
        lblSpecialization.setForeground(Color.WHITE);
        lblSpecialization.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblSpecialization.setBounds(30, 150, 150, 30);
        contentPane.add(lblSpecialization);

        textField_Specialization = new JTextField(specialization);
        textField_Specialization.setBounds(200, 155, 200, 30);
        contentPane.add(textField_Specialization);
        textField_Specialization.setColumns(10);
        
        JButton btnSave = new JButton("Save");
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnSave.setBackground(new Color(32, 32, 32));
        btnSave.setBounds(65, 213, 100, 37);
        contentPane.add(btnSave);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnCancel.setBackground(new Color(32, 32, 32));
        btnCancel.setBounds(233, 213, 107, 37);
        contentPane.add(btnCancel);
        
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String trainerName = textField_TrainerName.getText();
                String contact = textField_Contact.getText();
                String specialization = textField_Specialization.getText();
                updateTrainer(trainerId, trainerName, contact, specialization);
            }
        });
        
        setLocationRelativeTo(null);
        setResizable(false);
    }
        

    private void updateTrainer(int trainerId, String trainerName, String contact, String specialization) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("UPDATE Trainers SET TrainerName = ?, Contact = ?, Specialization = ? WHERE TrainerID = ?")) {
            pst.setString(1, trainerName);
            pst.setString(2, contact);
            pst.setString(3, specialization);
            pst.setInt(4, trainerId);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Trainer updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                manageTrainers.loadTrainers();
                dispose();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
