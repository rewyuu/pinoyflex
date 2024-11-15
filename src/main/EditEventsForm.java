package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;

public class EditEventsForm extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField titleField;
    private JTextField dateField;
    private JTextField timeField;
    private int eventId;
    private ManageEvents manageEvents;

    public EditEventsForm(int eventId, String title, String date, String time, ManageEvents manageEvents) {
        this.eventId = eventId;
        this.manageEvents = manageEvents; 

        setTitle("Edit Event");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(42, 42, 42));
        contentPane.setBorder(new CompoundBorder());

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("Title");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Century Gothic", Font.BOLD, 23));
        lblTitle.setBounds(68, 58, 120, 37);
        contentPane.add(lblTitle);

        JLabel lblDate = new JLabel("Date");
        lblDate.setForeground(Color.WHITE);
        lblDate.setFont(new Font("Century Gothic", Font.BOLD, 23));
        lblDate.setBounds(68, 105, 213, 37);
        contentPane.add(lblDate);

        JLabel lblTime = new JLabel("Time");
        lblTime.setForeground(Color.WHITE);
        lblTime.setFont(new Font("Century Gothic", Font.BOLD, 23));
        lblTime.setBounds(68, 152, 127, 37);
        contentPane.add(lblTime);

        titleField = new JTextField(title);
        titleField.setColumns(10);
        titleField.setBounds(223, 66, 140, 27);
        contentPane.add(titleField);

        dateField = new JTextField(date);
        dateField.setColumns(10);
        dateField.setBounds(223, 113, 140, 27);
        contentPane.add(dateField);

        timeField = new JTextField(time);
        timeField.setColumns(10);
        timeField.setBounds(223, 160, 140, 27);
        contentPane.add(timeField);

        JButton btnSave = new JButton("Save");
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnSave.setBackground(new Color(32, 32, 32));
        btnSave.setBounds(43, 200, 107, 37);
        contentPane.add(btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Century Gothic", Font.BOLD, 20));
        btnCancel.setBackground(new Color(32, 32, 32));
        btnCancel.setBounds(213, 200, 107, 37);
        contentPane.add(btnCancel);

        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JLabel lblEditEvent = new JLabel("Edit Event");
        lblEditEvent.setForeground(Color.WHITE);
        lblEditEvent.setFont(new Font("Century Gothic", Font.BOLD, 24));
        lblEditEvent.setBounds(148, 11, 133, 30);
        contentPane.add(lblEditEvent);

        setLocationRelativeTo(null);
        setResizable(false);

        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String date = dateField.getText();
                String time = timeField.getText();
                if (!isValidDate(date)) {
                    JOptionPane.showMessageDialog(EditEventsForm.this, "Please enter a valid date (yyyy-MM-dd).", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!isValidTime(time)) {
                    JOptionPane.showMessageDialog(EditEventsForm.this, "Please enter a valid time (HH:mm:ss).", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                updateEvent(eventId, title, date, time);
                manageEvents.loadEvents(); 
                dispose();
            }
        });
    }

    private boolean isValidDate(String date) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        if (!Pattern.matches(regex, date)) return false;
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidTime(String time) {
        String regex = "\\d{2}:\\d{2}:\\d{2}";
        if (!Pattern.matches(regex, time)) return false;
        try {
            new SimpleDateFormat("HH:mm:ss").parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void updateEvent(int eventId, String title, String date, String time) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("UPDATE Events SET Title = ?, Date = ?, Time = ? WHERE EventID = ?")) {
            pst.setString(1, title);
            pst.setString(2, date);
            pst.setString(3, time);
            pst.setInt(4, eventId);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Event updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                manageEvents.loadEvents();
                dispose(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
