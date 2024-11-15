package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.*;


public class SplashScreen extends JFrame {

	private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JProgressBar loadBar;
    private JLabel lblPercentage;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SplashScreen frame = new SplashScreen();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                    frame.startLoading();
                    frame.setResizable(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SplashScreen() {
        setTitle("Splash Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 735, 523);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(228, 228, 228));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblGymSystem = new JLabel("Gym System");
        lblGymSystem.setFont(new Font("Century Gothic", Font.BOLD, 30));
        lblGymSystem.setBounds(261, 11, 204, 64);
        contentPane.add(lblGymSystem);
        
        JLabel lblNewLabel_1 = new JLabel("New label");
        lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\Rey Benedict Rico\\eclipse-workspace\\PinoyFlexGymSystem\\images\\gym-logo.JPG"));
        lblNewLabel_1.setBounds(113, 103, 258, 258);
        contentPane.add(lblNewLabel_1);
        
        JLabel lblNewLabel = new JLabel("PinoyFlex");
        lblNewLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
        lblNewLabel.setBounds(381, 201, 147, 47);
        contentPane.add(lblNewLabel);

        loadBar = new JProgressBar();
        loadBar.setBackground(Color.WHITE);
        loadBar.setForeground(SystemColor.textInactiveText);
        loadBar.setBounds(215, 387, 250, 30);
        contentPane.add(loadBar);
        
        lblPercentage = new JLabel("0%");
        lblPercentage.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblPercentage.setBounds(165, 428, 415, 45);
        contentPane.add(lblPercentage);
    }
    
    public void startLoading() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    final int percent = i;
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            loadBar.setValue(percent);
                            lblPercentage.setText("Loading Application, Percentage: " + percent + "%");
                        }
                    });
                }
             // for closing of splash screen
                dispose();
                
                // then run login java
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            Login loginPage = new Login();
                            loginPage.setVisible(true);
                            loginPage.setLocationRelativeTo(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        thread.start();
    }
}


