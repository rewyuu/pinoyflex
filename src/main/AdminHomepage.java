package main;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

public class AdminHomepage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminHomepage frame = new AdminHomepage();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AdminHomepage() {
		setTitle("Home");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 933, 548);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(42, 42, 42));
		contentPane.setBorder(new CompoundBorder());
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(32, 32, 32));
		panel.setBounds(0, 0, 917, 100);
		contentPane.add(panel);
		panel.setLayout(null);

		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.setForeground(Color.WHITE);
		btnLogOut.setBounds(766, 52, 122, 37);
		panel.add(btnLogOut);
		btnLogOut.setFont(new Font("Century Gothic", Font.BOLD, 15));
		btnLogOut.setBackground(new Color(77, 77, 77));
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Logout successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
				Login loginPage = new Login();
				loginPage.main(new String[]{});
                dispose();
			}
		});

		JLabel lblAdminDashboard = new JLabel("Admin Dashboard");
		lblAdminDashboard.setForeground(Color.WHITE);
		lblAdminDashboard.setFont(new Font("Century Gothic", Font.BOLD, 30));
		lblAdminDashboard.setBounds(344, 32, 343, 37);
		panel.add(lblAdminDashboard);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Navigation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(-6, 83, 220, 432);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 16, 208, 410);
		panel_3.add(panel_1);
		panel_1.setBorder(new CompoundBorder());
		panel_1.setBackground(new Color(42, 42, 42));
		panel_1.setLayout(null);

		JButton btnHome = new JButton("Home");
		btnHome.setForeground(Color.WHITE);
		btnHome.setBackground(new Color(77, 77, 77));
		btnHome.setBounds(20, 24, 163, 37);
		panel_1.add(btnHome);
		btnHome.setFont(new Font("Century Gothic", Font.BOLD, 15));
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminHomepage adminPage = new AdminHomepage();
				adminPage.main(new String[]{});
                dispose();
			}
		});

		JButton btnCustomers = new JButton("Customers");
		btnCustomers.setForeground(Color.WHITE);
		btnCustomers.setBackground(new Color(77, 77, 77));
		btnCustomers.setBounds(20, 80, 163, 37);
		panel_1.add(btnCustomers);
		btnCustomers.setFont(new Font("Century Gothic", Font.BOLD, 15));
		btnCustomers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManageCustomers customersPage = new ManageCustomers();
				customersPage.main(new String[]{});
                dispose();
			}
		});

		JButton btnPackages = new JButton("Packages");
		btnPackages.setForeground(Color.WHITE);
		btnPackages.setBackground(new Color(77, 77, 77));
		btnPackages.setBounds(20, 138, 163, 38);
		panel_1.add(btnPackages);
		btnPackages.setFont(new Font("Century Gothic", Font.BOLD, 15));
		btnPackages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManagePackages packagesPage = new ManagePackages();
				packagesPage.main(new String[]{});
                dispose();
			}
		});

		JButton btnEvents = new JButton("Events");
		btnEvents.setForeground(Color.WHITE);
		btnEvents.setBackground(new Color(77, 77, 77));
		btnEvents.setBounds(20, 193, 163, 36);
		panel_1.add(btnEvents);
		btnEvents.setFont(new Font("Century Gothic", Font.BOLD, 15));
		btnEvents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManageEvents eventsPage = new ManageEvents();
				eventsPage.main(new String[]{});
                dispose();
			}
		});

		JButton btnTrainers = new JButton("Trainers");
		btnTrainers.setForeground(Color.WHITE);
		btnTrainers.setBackground(new Color(77, 77, 77));
		btnTrainers.setBounds(20, 248, 163, 37);
		panel_1.add(btnTrainers);
		btnTrainers.setFont(new Font("Century Gothic", Font.BOLD, 15));
		btnTrainers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManageTrainers trainersPage = new ManageTrainers();
				trainersPage.main(new String[]{});
				dispose();
			}
		});

		JButton btnTransactions = new JButton("Transactions");
		btnTransactions.setForeground(Color.WHITE);
		btnTransactions.setBackground(new Color(77, 77, 77));
		btnTransactions.setFont(new Font("Century Gothic", Font.BOLD, 15));
		btnTransactions.setBounds(20, 305, 163, 37);
		panel_1.add(btnTransactions);
		btnTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManageTransactions transactionsPage = new ManageTransactions();
				transactionsPage.main(new String[]{});
				dispose();
			}
		});

		JButton btnReports = new JButton("Reports");
		btnReports.setForeground(Color.WHITE);
		btnReports.setFont(new Font("Century Gothic", Font.BOLD, 15));
		btnReports.setBackground(new Color(77, 77, 77));
		btnReports.setBounds(20, 362, 163, 37);
		panel_1.add(btnReports);
		btnReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GenerateReports reportsPage = new GenerateReports();
				reportsPage.main(new String[]{});
				dispose();
			}
		});

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(32, 32, 32));
		panel_2.setBounds(204, 439, 713, 70);
		contentPane.add(panel_2);
		
		JLabel lblWelcome = new JLabel("Welcome to the Dashboard");
		lblWelcome.setForeground(Color.WHITE);
		lblWelcome.setFont(new Font("Century Gothic", Font.BOLD, 30));
		lblWelcome.setBounds(250, 125, 411, 57);
		contentPane.add(lblWelcome);
		
		JLabel lblAdministrator = new JLabel("Use the Navigation Bar");
		lblAdministrator.setForeground(Color.WHITE);
		lblAdministrator.setFont(new Font("Century Gothic", Font.BOLD, 25));
		lblAdministrator.setBounds(250, 196, 296, 57);
		contentPane.add(lblAdministrator);
		
		JLabel lblToStartUsing = new JLabel("to start using the system");
		lblToStartUsing.setForeground(Color.WHITE);
		lblToStartUsing.setFont(new Font("Century Gothic", Font.BOLD, 25));
		lblToStartUsing.setBounds(250, 250, 320, 40);
		contentPane.add(lblToStartUsing);
	}
}
