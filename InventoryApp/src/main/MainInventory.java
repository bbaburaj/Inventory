package main;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import mail.SendMailSSL;
import Utility.Utility;


public class MainInventory {
	private String dbQuery;
	private String username;
	private String psswd;
	public static Logger logger = Logger.getLogger("Inventory");  
	public static FileHandler fh;  
	public static File newFolder;
	public static File subInvoiceFolder;
	public static void main(String[] args){
		try {
			subInvoiceFolder = new File("C:\\Inventory\\Invoice");
			subInvoiceFolder.mkdirs();
			fh = new FileHandler("C:\\Inventory\\Logs\\Inventory.log");
			logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter); 
		} catch (SecurityException e1) {
			logger.severe(e1.getMessage());
		} catch (IOException e1) {
			logger.severe(e1.getMessage());
		}  
         
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
						logger.severe(e.getMessage());
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
