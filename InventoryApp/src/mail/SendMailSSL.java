package mail;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import Utility.Utility;
import main.MainInventory;


public class SendMailSSL {

	private Connection connect;
	private Statement statement;
	private ResultSet resultSet;
	private static HashMap<String, Integer> prodQnty = new HashMap<String, Integer>();
	private static String textMessage = "====================QUANTITY UPDATE====================\n";
	private static boolean incomplete = false;
	private String dbQuery;
	private String username;
	private String psswd;
	
	public SendMailSSL(String[] args, MainInventory m) {
		try {
			this.dbQuery = m.getDBQuery();
			this.username = m.getDBUsername();
			this.psswd = m.getDBPassword();
			if(args==null||args.length==0 || args.length%2!=0){
				textMessage = "====================ARGUMENT IS INCOMPLETE====================\n";
				incomplete = true;
			}
			else{
				for(int i = 0; i<args.length-1;i=i+2){
					prodQnty.put(args[i],Integer.valueOf(args[i+1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mail() {
		final String username = "microbial.matrix@gmail.com";
		final String password = "m_matrix_2014";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		if(!incomplete){
		HashMap<String, Float> data = getData();
		for (String s : data.keySet()) {
			String d[] = s.split("Gal");
			textMessage += "The quantity for " + d[0] + " Gallon bottles of "+d[1]+" is LOW. It is only "
					+ data.get(s) + "\n";
		}
		textMessage = textMessage+ "=======================================================";
		}
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("microbial.matrix@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("baradisuccess@gmail.com"));
			message.setSubject("LOW Quantity Update");
			message.setText(textMessage);
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public HashMap<String, Float> getData() {

		try {
			HashMap<String, Float> data = new HashMap<String, Float>();
			connect = Utility.makeDBConnection(dbQuery,username,psswd);
			statement = connect.createStatement();
			for(String s:prodQnty.keySet()){
				String query = "Select Quantity from product where ProductId='"+s+"' and Quantity<"
							+ prodQnty.get(s) + ";";
				data.put(s, getProdQnty(query));
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private float getProdQnty(String query) throws SQLException{
		resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getFloat("Quantity");	
		}
		return 0;		
	}
}