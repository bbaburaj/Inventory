package main;
import java.awt.EventQueue;
import java.io.IOException;

import mail.SendMailSSL;
import Utility.Utility;


public class MainInventory {
	private String dbQuery;
	private String username;
	private String psswd;

	public static void main(String[] args){
		final MainInventory m = new MainInventory();
		m.setDBDetails();
		if(args.length==2){
			if(args[0].equalsIgnoreCase("-m")|| args[0].equalsIgnoreCase("-mail")){
				String[] mailParam = args[1].split(";");
				SendMailSSL s = new SendMailSSL(mailParam,m);
				s.mail();
			}
		} else{
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Inventory frame = new Inventory(m);
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	private void setDBDetails() {
		try {
			Utility u = new Utility();
			dbQuery = u.getPropValue("dbQuery");
			username = u.getPropValue("username");
			psswd = u.getPropValue("password");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getDBQuery(){
		return dbQuery;
	}
	
	public String getDBUsername(){
		return username;
	}
	
	public String getDBPassword(){
		return psswd;
	}
}
