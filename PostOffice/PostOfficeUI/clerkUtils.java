import java.awt.List;
import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class clerkUtils {

	public static void loginOptions(Connection conn, String empId) throws ClassNotFoundException, SQLException{

		Scanner read = new Scanner(System.in);
		System.out.println("Please enter the number for the desired option:");
		System.out.println("1 - Add mail to the system"
						 + "\n2 - Upgrade a customer to premium"
						 + "\n3 - Remove the premium status from a customer"
						 + "\n4 - View all mail."
						 + "\n5 - Modify your password.");
		
		
		
		int userChoice = read.nextInt();
		loginOptions(conn, userChoice, empId);	
	}
	
	public static void loginOptions(Connection conn, int choice, String empId) throws ClassNotFoundException, SQLException {
		switch(choice) {
		case 1:	// list schedule
			AddMail(conn, empId);
			break;
		
		case 2: // method call to view truck
			Upgrade(conn, empId);
			break;
			
		case 3: // method call to list mail
			Downgrade(conn, empId);
			break;
			
		case 4: // 
			ViewMail(conn);
			break;
			
		case 5:
			ModifyPassword(conn, empId);
			break;
	
		}
	}

	private static void AddMail(Connection conn, String empId) throws SQLException {
		Scanner read = new Scanner(System.in);
		System.out.println("Please enter the weight of the mail");
		int weight = read.nextInt();
		
		System.out.println("Please enter the postage");
		int postage = read.nextInt();
		
		System.out.println("Please enter 1 if the mail is registered and 0 if it is not reistered");
		String registered = read.nextLine();
		
		System.out.println("Please enter a delivery address id");
		String deliveryAddress = read.nextLine();
		
		System.out.println("Please enter a return address id");
		String returnAddress = read.nextLine();
		
		System.out.println("Please enter a postal code");
		String postalCode = read.nextLine();
		
		System.out.println("Please enter the street address to delivery to");
		String address = read.nextLine();
		
		System.out.println("Please enter the city to deliver to");
		String city = read.nextLine();
		
		System.out.println("Please enter the country to deliver to");
		String country = read.nextLine();
		
		System.out.println("Please enter if the customer is premium or not");
		String premium = read.nextLine();
		
		
		
		CallableStatement cstmt = null;
		try{
		String SQL = "{call addMail(?,?,?,?,?,?,?,?,?,?)}";
		cstmt = conn.prepareCall(SQL);
		cstmt.setInt(1, weight);
		cstmt.setInt(2, postage);
		cstmt.setString(3, registered);
		cstmt.setString(4, deliveryAddress);
		cstmt.setString(5, returnAddress);
		cstmt.setString(6, postalCode);
		cstmt.setString(7, address);
		cstmt.setString(8, city);
		cstmt.setString(9, country);
		cstmt.setString(10, premium);
		cstmt.execute();
		
		} catch(SQLException e){
		}
		finally{
			cstmt.close();
		}
	}

	private static void Upgrade(Connection conn, String empId) {
		// TODO Auto-generated method stub
		
	}

	private static void Downgrade(Connection conn, String empId) {
		// TODO Auto-generated method stub
		
	}

	private static void ViewMail(Connection conn) {
		// TODO Auto-generated method stub
		
	}

	private static void ModifyPassword(Connection conn, String empId) {
		// TODO Auto-generated method stub
		
	}
}
