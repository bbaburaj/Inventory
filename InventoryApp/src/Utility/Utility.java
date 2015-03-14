package Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import main.MainInventory;

import org.apache.commons.io.FileUtils;


public class Utility {
	public String getPropValue(String propName) throws IOException{
		Properties prop = new Properties();
		String propFile = "config.properties";
		InputStream ip = getClass().getClassLoader().getResourceAsStream(propFile);
		if(ip!=null){
			prop.load(ip);
		}else{
			MainInventory.logger.severe("Property file "+propFile+" not found in the class path");
			throw new FileNotFoundException("Property file "+propFile+" not found in the class path");
		}
		return prop.getProperty(propName);		
	}
	
	public static Connection makeDBConnection(String dbQuery, String username, String psswd) {

		try {
			return DriverManager
					.getConnection(dbQuery
							+ "user="+username+"&password="+psswd);
		} catch (SQLException e) {
			MainInventory.logger.severe(e.getMessage());
		}
		return null;
	}
	
	public static void getProductDetail(ArrayList<Object> list,String column, String table, Connection connect, Statement statement, ResultSet resultSet) {
		try {
			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select distinct "+column+" from "+table+";");
			MainInventory.logger.info("select distinct "+column+" from "+table+";");
				while (resultSet.next()) {
					list.add(resultSet.getObject(column));
				}
		} catch (SQLException e) {
			MainInventory.logger.severe(e.getMessage());
		}
	}
	


	public static ResultSet getDetailsBasedOnOneCol(String select, String clause, String table, Connection connect, Statement statement, ResultSet resultSet) {
		try {
			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select "+select+" from "+table+" where "+clause+";");
			MainInventory.logger.info("select "+select+" from "+table+" where "+clause+";");
			return resultSet;
		} catch (SQLException e) {
			MainInventory.logger.severe(e.getMessage());
		}
		return null;
	}
	public static void addNewUnits(String prodname, float qnty, float unitsize,Connection connect,Statement statement) {
		try {
			statement = connect.createStatement();
			statement.executeUpdate("UPDATE Product SET Quantity="+qnty+" where productname=\""+prodname+"\" and unitsize="+unitsize+";");
			
		} catch (SQLException e) {
			MainInventory.logger.severe(e.getMessage());
		}
	}
	
	public static int updateProdInfo(String whereclause, String clause, Connection connect,Statement statement) {
		try {
			statement = connect.createStatement();
			return statement.executeUpdate("UPDATE Product SET "+clause+" where "+whereclause+";");
			
		} catch (SQLException e) {
			MainInventory.logger.severe(e.getMessage());
		}
		return 0;
	}
	
	public static void addNewProduct(String prodname, String prodId,float unitSize, float qty,float rp, float dp, float rpF, Connection connect,Statement statement) {
		try {
			statement = connect.createStatement();
			String query = "INSERT INTO product(`ProductId`,`ProductName`,`UnitSize`,`Quantity`,`DealerPrice`,`RetailPrice`,`RetailPriceWithFreight`) VALUES ('"+prodId+"','"+prodname+"','"+unitSize+"','"+qty+"','"+rp+"','"+dp+"','"+rpF+"');";
			statement.executeUpdate(query);
		} catch (SQLException e) {
			MainInventory.logger.severe(e.getMessage());
		}
	}
	
	public static void addOrder(String orderId, String prodname, String prodId,float unitSize, float qty, String customerName, String address, String phNumber, String date, String dealerName, String dealerId, float dp, float freight, float total,byte[] invoice, Statement statement, Connection connect) {
		try {
			
			statement = connect.createStatement();
			String query = "INSERT INTO `inventory`.`orders`(`OrderId`,`ProductName`,`ProductId`,`OrderDate`,`UnitSize`,`Units`,`CustomerName`,`CustomerAddress`,`CustomerPhoneNumber`,`DealerName`,`DealerId`,`Invoice`,`DealerPricePerUnit`,`FreightPrice`,`TotalPrice`) VALUES ('"+orderId+"','"+prodname+"','"+prodId+"','"+date+"',"+unitSize+","+qty+",'"+customerName+"','"+address+"','"+phNumber+"','"+dealerName+"','"+dealerId+"',"+invoice+","+dp+","+freight+","+total+");";
			
			statement.executeUpdate(query);
		} catch (SQLException e) {
			MainInventory.logger.severe(e.getMessage());
		}
	}
	
	public static void copyFilesToDesktop(String filename){
		File source1 = new File(filename);
		File dest1 = new File("C:\\Inventory\\Invoice\\"+filename);
		File source2 = new File("InventoryStyle.css");
		File dest2 = new File("C:\\Inventory\\Invoice\\InventoryStyle.css");
		File source3 = new File("Logo.png");
		File dest3 = new File("C:\\Inventory\\Invoice\\Logo.png");
		
		try {
		    FileUtils.copyFile(source1, dest1);
		    FileUtils.copyFile(source2, dest2);
		    FileUtils.copyFile(source3, dest3);
		    FileUtils.deleteQuietly(source1);
		} catch (IOException e) {
		    MainInventory.logger.severe(e.getMessage());
		}
	
	}
	
	


}
