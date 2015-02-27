package main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.CardLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import Utility.Utility;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Inventory extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3035309723810078113L;
	private JPanel contentPane,userMainPagePane;
	private JPanel infoPanel;
	private JTextField uName;
	private JPasswordField pswrd;
	private JPanel loginPanel;
	private String first_name;
	private String last_name;
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private JPanel registerPanel, forgotUsernamePanel;
	private JPanel mainPane;
	private CardLayout cl = new CardLayout();
	private UserMainPage mainPage = null;
	private String dbQuery;
	private String username;
	private String psswd;

	/**
	 * Create the frame.
	 */
	public Inventory(MainInventory m) {
		this.dbQuery = m.getDBQuery();
		this.username = m.getDBUsername();
		this.psswd = m.getDBPassword();
		mainPane = new JPanel();
		contentPane = new JPanel();
		userMainPagePane = new JPanel();
		connect = Utility.makeDBConnection(dbQuery,username,psswd);
		setTitle("Microbial Matrix Inventory Tracker");
		setSize(1024, 768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 703, 396);
		mainPane.setLayout(cl);
		setLoginPane();
		setUserMainPage(userMainPagePane);
		setContentPane(mainPane);
		mainPane.add(contentPane, "login");
		mainPane.add(userMainPagePane,"user");
		cl.show(mainPane, "login");
	}
	
	/**
	 * 
	 */
	private void setLoginPane() {
		
		contentPane.setLayout(new GridLayout(3, 1));
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(2, 2));
		infoPanel.setBorder(new TitledBorder("Login"));
		contentPane.add(infoPanel);
		infoPanel.add(new JLabel("Username"));
		uName = new JTextField();
		infoPanel.add(uName);
		uName.setColumns(10);
		JLabel lblNewLabel = new JLabel("Password");
		infoPanel.add(lblNewLabel);

		pswrd = new JPasswordField();
		infoPanel.add(pswrd);
		pswrd.setColumns(10);

		loginPanel = new JPanel();
		loginPanel.setLayout(new GridLayout(1, 2));
		contentPane.add(loginPanel);
		JButton loginB = new JButton("Login");
		loginB.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				if (checkLogin(uName.getText(), pswrd.getText())) {
					JOptionPane.showMessageDialog(contentPane, "Welcome "
							+ first_name + " " + last_name + "!");
					mainPage.setUserDetail(uName.getText(),first_name,last_name);
					cl.show(mainPane, "user");
				} else {
					JOptionPane.showMessageDialog(contentPane,
							"Invalid login and password");
				}
			}
		});
		JButton registerB = new JButton("Register Account");
		registerPanel = new JPanel();
		registerPanel.setLayout(new GridLayout(5, 2));
		final JTextField newUName = new JTextField();
		newUName.setColumns(10);
		final JTextField newPswrd = new JTextField();
		newPswrd.setColumns(10);
		final JTextField newFName = new JTextField();
		newFName.setColumns(10);
		final JTextField newLName = new JTextField();
		newLName.setColumns(10);
		final JTextField newEmail = new JTextField();
		newEmail.setColumns(50);
		registerPanel.add(new JLabel("Enter Username"));
		registerPanel.add(newUName);
		registerPanel.add(new JLabel("Enter Password"));
		registerPanel.add(newPswrd);
		registerPanel.add(new JLabel("Enter First Name"));
		registerPanel.add(newFName);
		registerPanel.add(new JLabel("Enter Last Name"));
		registerPanel.add(newLName);
		registerPanel.add(new JLabel("Enter email id"));
		registerPanel.add(newEmail);
		registerB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(null, registerPanel,
						"Please enter your information",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					addNewUser(newUName.getText(), newPswrd.getText(),
							newFName.getText(), newLName.getText(),
							newEmail.getText());
				}
			}
		});
		forgotUsernamePanel = new JPanel();
		forgotUsernamePanel.setLayout(new GridLayout(5, 2));
		final JTextField emailAdd = new JTextField();
		emailAdd.setColumns(10);
		final JTextField firstName = new JTextField();
		firstName.setColumns(10);
		final JTextField lastName = new JTextField();
		lastName.setColumns(10);
		forgotUsernamePanel.add(new JLabel("Enter email address"));
		forgotUsernamePanel.add(emailAdd);
		forgotUsernamePanel.add(new JLabel("Enter First Name"));
		forgotUsernamePanel.add(firstName);
		forgotUsernamePanel.add(new JLabel("Enter Last Name"));
		forgotUsernamePanel.add(lastName);
		JButton forgotB = new JButton("Forgot username/password");
		forgotB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(null,
						forgotUsernamePanel, "Forgot username or password",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					getLogonInfo(forgotUsernamePanel, firstName.getText(),
							lastName.getText(), emailAdd.getText());

				}
			}
		});
		loginPanel.add(loginB);
		loginPanel.add(registerB);
		loginPanel.add(forgotB);
	}

	private boolean checkLogin(String uName, String pswrd) {
		try {
			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select count(*) as rowcount, firstname,lastname from login where username=\""
							+ uName + "\" and password = \"" + pswrd + "\"");
			int count = 0;
			if (resultSet.next()) {
				count = resultSet.getInt("rowcount");
				first_name = resultSet.getString("firstname");
				last_name = resultSet.getString("lastname");

			}
			return !(count == 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void addNewUser(String uName, String pswrd, String fName,
			String lName, String email) {
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connect
					.prepareStatement("insert into  inventory.login values (default, ?, ?, ?, ? , ?)");
			preparedStatement.setString(1, uName);
			preparedStatement.setString(2, pswrd);
			preparedStatement.setString(3, email);
			preparedStatement.setString(4, fName);
			preparedStatement.setString(5, lName);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void getLogonInfo(JPanel panel, String fName, String lName,
			String email) {
		try {
			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select username,password from login where firstname=\""
							+ fName
							+ "\" and lastname = \""
							+ lName
							+ "\" and email = \"" + email + "\";");
			String uName = "";
			String pswrd = "";
			if (resultSet.next()) {
				uName = resultSet.getString("username");
				pswrd = resultSet.getString("password");
			}
			if (uName.isEmpty()) {
				JOptionPane.showMessageDialog(panel,
						"Sorry we couldn't find you in the system");
			} else {
				JOptionPane.showMessageDialog(panel, "Your username is:"
						+ uName + " and your password is:" + pswrd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void setUserMainPage(JPanel userMainPage){
		mainPage = new UserMainPage(userMainPage,connect,this);
	}
	
	public void resetDetails(){
		cl.show(mainPane, "login");
		uName.setText("");
		pswrd.setText("");
	}
	

}
