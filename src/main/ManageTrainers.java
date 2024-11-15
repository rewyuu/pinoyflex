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

public class ManageTrainers extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTable trainersTable;
    private JButton btnEdit;
    private JTextField textField_4;

    public void loadTrainers() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("SELECT * FROM Trainers")) {
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new String[]{"Trainer ID", "Trainer Name", "Contact", "Specialization"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; 
                }
            };

            while (rs.next()) {
                int trainerId = rs.getInt("TrainerID");
                String trainerName = rs.getString("TrainerName");
                String contact = rs.getString("Contact");
                String specialization = rs.getString("Specialization");

                model.addRow(new Object[]{trainerId, trainerName, contact, specialization});
            }

            trainersTable.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addTrainer(String trainerName, String contact, String specialization) {
        if (trainerName.isEmpty() || contact.isEmpty() || specialization.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide all details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!contact.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "Invalid contact number. It must be 11 digits long and contain only numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (trainerNameExists(trainerName)) {
            JOptionPane.showMessageDialog(this, "Trainer with this name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("INSERT INTO Trainers (TrainerName, Contact, Specialization) VALUES (?, ?, ?)")) {
            pst.setString(1, trainerName);
            pst.setString(2, contact);
            pst.setString(3, specialization);
            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Trainer added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                textField_1.setText("");
                textField_2.setText("");
                textField_3.setText("");
                loadTrainers();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean trainerNameExists(String trainerName) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("SELECT COUNT(*) AS count FROM Trainers WHERE TrainerName = ?")) {
            pst.setString(1, trainerName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void deleteTrainer(int trainerId) {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this trainer?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement pst = connection.prepareStatement("DELETE FROM Trainers WHERE TrainerID = ?")) {
                pst.setInt(1, trainerId);
                int rowsDeleted = pst.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Trainer deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadTrainers();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ManageTrainers() {
        setTitle("Manage Trainers");
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

        JLabel lblManageTrainers = new JLabel("Manage Trainers");
        lblManageTrainers.setForeground(Color.WHITE);
        lblManageTrainers.setFont(new Font("Century Gothic", Font.BOLD, 30));
        lblManageTrainers.setBounds(335, 33, 321, 37);
        panel.add(lblManageTrainers);

        JLabel lblTrainerName = new JLabel("Trainer Name");
        lblTrainerName.setForeground(Color.WHITE);
        lblTrainerName.setFont(new Font("Century Gothic", Font.BOLD, 23));
        lblTrainerName.setBounds(36, 190, 213, 37);
        contentPane_1.add(lblTrainerName);

        JLabel lblContact = new JLabel("Contact #");
        lblContact.setForeground(Color.WHITE);
        lblContact.setFont(new Font("Century Gothic", Font.BOLD, 23));
        lblContact.setBounds(36, 233, 117, 37);
        contentPane_1.add(lblContact);

        JLabel lblSpecialization = new JLabel("Specialization");
        lblSpecialization.setForeground(Color.WHITE);
        lblSpecialization.setFont(new Font("Century Gothic", Font.BOLD, 23));
        lblSpecialization.setBounds(36, 276, 172, 37);
        contentPane_1.add(lblSpecialization);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(232, 200, 140, 27);
        contentPane_1.add(textField_1);

        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(232, 238, 140, 27);
        contentPane_1.add(textField_2);

        textField_3 = new JTextField();
        textField_3.setColumns(10);
        textField_3.setBounds(232, 286, 140, 27);
        contentPane_1.add(textField_3);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(425, 128, 456, 357);
        contentPane_1.add(scrollPane);

        trainersTable = new JTable();
        trainersTable.setModel(new DefaultTableModel(
                new Object[][]{
                    {null, null, null, null}
                },
                new String[]{
                    "Trainer ID", "Trainer Name", "Contact", "Specialization"
                }
        ) {
            Class[] columnTypes = new Class[]{
                Integer.class, String.class, String.class, String.class
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

        scrollPane.setViewportView(trainersTable);

        btnEdit = new JButton("Edit");
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnEdit.setBackground(new Color(32, 32, 32));
        btnEdit.setBounds(280, 354, 107, 37);
        contentPane_1.add(btnEdit);

        JButton btnAddButton = new JButton("Add");
        btnAddButton.setForeground(Color.WHITE);
        btnAddButton.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnAddButton.setBackground(new Color(32, 32, 32));
        btnAddButton.setBounds(36, 354, 107, 37);
        contentPane_1.add(btnAddButton);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnDelete.setBackground(new Color(32, 32, 32));
        btnDelete.setBounds(153, 354, 107, 37);
        contentPane_1.add(btnDelete);

        JButton btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnBack.setBackground(new Color(32, 32, 32));
        btnBack.setBounds(36, 461, 107, 37);
        contentPane_1.add(btnBack);

        btnAddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String trainerName = textField_1.getText();
                String contact = textField_2.getText();
                String specialization = textField_3.getText();
                addTrainer(trainerName, contact, specialization);
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = trainersTable.getSelectedRow();
                if (selectedRow != -1) {
                    int trainerId = (int) trainersTable.getValueAt(selectedRow, 0);
                    deleteTrainer(trainerId);
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Please select a trainer to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = trainersTable.getSelectedRow();
                if (selectedRow != -1) {
                    int trainerId = (int) trainersTable.getValueAt(selectedRow, 0);
                    String trainerName = (String) trainersTable.getValueAt(selectedRow, 1);
                    String contact = (String) trainersTable.getValueAt(selectedRow, 2);
                    String specialization = (String) trainersTable.getValueAt(selectedRow, 3);
                    EditTrainersForm editForm = new EditTrainersForm(trainerId, trainerName, contact, specialization, ManageTrainers.this);
                    editForm.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Please select a trainer to edit.", "Error", JOptionPane.ERROR_MESSAGE);
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

        loadTrainers();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ManageTrainers frame = new ManageTrainers();
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
