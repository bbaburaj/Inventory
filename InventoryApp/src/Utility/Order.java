package Utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;

public class Order {
	String id;
	String prodName;
	String unitSize;
	String units;
	String date;
	String customerName;
	String customerAddress;
	String customerPhNumber;
	String pricePerUnit;
	String freightPrice;
	String totalPrice;
	File file;
	Writer writer;
	String filename = "Inventory-";

	public String getId() {
		return id;
	}

	public String getProdName() {
		return prodName;
	}

	public String getUnitSize() {
		return unitSize;
	}

	public String getUnits() {
		return units;
	}

	public String getDate() {
		return date;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getPricePerUnit() {
		return pricePerUnit;
	}

	public String getFreightPrice() {
		return freightPrice;
	}

	public String getTotalPrice() {
		return totalPrice;
	}
	
	public String getCustomerAddress() {
		return customerAddress;
	}

	public String getCustomerPhNumber() {
		return customerPhNumber;
	}

	public void generateInventory() {
		file = new File(filename);
		try {
			file.createNewFile();
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename), "utf-8"));
			writer.write(createHeaderForInventoryFile());
			writer.write(createBodyForInventoryFile());
			writer.write("</div></body></html>");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		copyFilesToDesktop();

	}

	public String createBodyForInventoryFile() {
		StringBuilder body = new StringBuilder();
		body.append("<body>" + "\r\n");
		body.append("<img src=\"Logo.png\" alt=\"Logo\" style=\"width:815px;height:200px\">");
		body.append("<div class=\"wrap\">"
				+ "\r\n"
				+ "	<h1 class=\"heading\"> INVOICE </h1><br>"
				+ "\r\n"
				+ "    <h2 class=\"companyName\">Microbial Matrix</h2>"
				+ "\r\n"
				+ "	<p class=\"companyLogo\">Let us help GROW your business</p>"
				+ "\r\n"
				+ "	<p class=\"companyDetails\">33935 Oregon 99E,<br> Tangent, OR 97389 <br>Phone:(541)967-0554</p>"
				+ "\r\n");
		body.append("	<h3 class=\"orderIdAndDate\">OrderID:" + this.id + "<br>"
				+ this.date + "</h3>" + "\r\n");
		body.append("<table style=\"width:50%\"><tr><td><span class=\"boldRow\">TO</span></td></tr><tr><td>"+this.customerName+"<br>"+this.customerAddress+"<br>"+this.customerPhNumber+"</td></tr>");
		body.append("</table><br><br>");
		String prodDesciption = this.prodName+"<br>"+this.units+" bottles of size "+this.unitSize+"Gal<br>@"+this.pricePerUnit+" dollars per unit";
		body.append("<table style=\"width:90%\">"+"\r\n"+"	  <tr>"+"\r\n"+"<td><span class=\"boldRow\">Description</span></td>"+"\r\n"+"		<td><span class=\"boldRow\">Amount</span></td>	"+"\r\n"+"	  </tr>"+"\r\n"+"	  <tr>"+"\r\n"+"\r\n"+"</tr><tr><td><span class=\"normalRow\">"+prodDesciption+"</span></td><td><span class=\"normalRow\"> $"+this.totalPrice+"</span></td></tr></table>");
		return body.toString();
	}

	public String createHeaderForInventoryFile() {
		String header = "<!doctype=html>" + "\r\n" + "<html>" + "\r\n"
				+ "<head>" + "\r\n" + "    <meta charset=\"utf-8\" />" + "\r\n"
				+ "    <title> Microbial Matrix Invoice </title>" + "\r\n"
				+ "    <link rel=\"stylesheet\" href=\"InventoryStyle.css\" />"
				+ "\r\n" + "	<style>" + "\r\n" + "	table, th, td {" + "\r\n"
				+ "    border: 1px solid black;" + "\r\n"
				+ "    border-collapse: collapse;}" + "\r\n" + "</style>"
				+ "\r\n" + "</head>" + "\r\n";
		return header;
	}

	public Order(ResultSet rs) {
		try {
			
			while (rs.next()) {
				this.id = rs.getString("OrderId");
				this.prodName = rs.getString("ProductName");
				this.unitSize = rs.getString("UnitSize");
				this.units = rs.getString("Units");
				this.date = rs.getString("OrderDate");
				this.customerName = rs.getString("CustomerName");
				this.pricePerUnit = rs.getString("DealerPricePerUnit");
				this.freightPrice = rs.getString("FreightPrice");
				this.totalPrice = rs.getString("TotalPrice");
				this.customerAddress = rs.getString("CustomerAddress");
				this.customerPhNumber=rs.getString("CustomerPhoneNumber");
				filename = filename+this.id+".html";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void copyFilesToDesktop(){
		File newFolder = new File("C:\\Inventory");
		newFolder.mkdir();
		File source1 = new File(filename);
		File dest1 = new File("C:\\Inventory\\"+filename);
		File source2 = new File("InventoryStyle.css");
		File dest2 = new File("C:\\Inventory\\InventoryStyle.css");
		File source3 = new File("Logo.png");
		File dest3 = new File("C:\\Inventory\\Logo.png");
		
		try {
		    FileUtils.copyFile(source1, dest1);
		    FileUtils.copyFile(source2, dest2);
		    FileUtils.copyFile(source3, dest3);
		    FileUtils.deleteQuietly(source1);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	
	}

}
