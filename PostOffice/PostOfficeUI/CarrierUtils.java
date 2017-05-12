import java.awt.List;
import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;
/**
 * The following class contains all the required methods 
 * that will get the desired information for a carrier.
 * @author Zahraa Horeibi
 *
 */
public class CarrierUtils {

	/**
	 * Displays the options a carrier can take and sends their desired
	 * option to the overwritten loginOptions method.
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param empId
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void loginOptions(Connection conn, String empId) throws ClassNotFoundException, SQLException{

		Scanner read = new Scanner(System.in);
		System.out.println("Please enter the number for the desired option:");
		System.out.println("1 - View your schedule."
						 + "\n2 - View your assigned mail route. "
						 + "\n3 - View your assigned truck."
						 + "\n4 - View the list of mail you must deliver."
						 + "\n5 - Mark a route as started."
						 + "\n6 - Mark a route as completed. "
						 + "\n7 - Mark a mail as delivered or undelivered."
						 + "\n8 - Mark yourself as sick."
						 + "\n9 - Modify your password.");
		
		int userChoice = read.nextInt();
		loginOptions(conn, userChoice, empId);		

	}
	/**
	 * Calls the appropriate method based on the user choice which got 
	 * passed by.
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param choice
	 * @param empId
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void loginOptions(Connection conn, int choice, String empId) throws ClassNotFoundException, SQLException {
		switch(choice) {
		case 1:	// list schedule
			ListSchedule(conn, empId);
			break;
			
		case 2: // method call to view route
			ViewRoute(conn, empId);
			break; 
		case 3: // method call to view truck
			ViewTruck(conn, empId);
			break;
			
		case 4: // method call to list mail
			ListMailToDeliv(conn, empId);
			break;
			
		case 5: // 
			RouteStarted(conn, empId);
			break;
		case 6: 
			RouteCompleted(conn, empId);
		case 7: // 
			MarkMail(conn);
			break;
		case 8:
			MarkSick(conn, empId);
			break;
		case 9:  // modify their password
			Utilities.ModifyPassword(conn, empId);
			break;
	
		}
	}
	/**
	 * The following method lists the schedule of an employee.
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param empId
	 */
	public static void ListSchedule(Connection conn, String empId){
		
		try {
			String scheduleSql = "SELECT schedule_id, scheduled_start FROM schedule"
								+ " WHERE carrier_id = " + empId; 								
			
			PreparedStatement stmt = conn.prepareStatement(scheduleSql);
			ResultSet rs = stmt.executeQuery();
	
			System.out.println("Schedule:\n");
			while(rs.next()){
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(rs.getDate("scheduled_start").getTime());
				System.out.println("Schedule Id:\t" + rs.getString("schedule_id"));
				System.out.println("Scheduled Start:\t" + cal.getTime() + "\n");
			}
				
			stmt.close();
			
		} catch (SQLException e) {
			e.getMessage();
		}
		
	}
	
	
	/**
	 *  The following method lists a carrier's route based on the 
	 *  desired schedule_id.
	 *  @author Zahraa Horeibi
	 * @param conn
	 * @param empId
	 */
	public static void ViewRoute(Connection conn, String empId) {
			
		try {
			// lists the schedule so user can pick a schedule id
			ListSchedule(conn, empId);
			Scanner read = new Scanner(System.in);
			System.out.println("Please enter a schedule ID");
			String schId = read.nextLine();
			
			String viewRouteSQL = "SELECT postal_code FROM schedule LEFT JOIN postal_code "
								+ "USING(route_id) WHERE carrier_id = " + "'" + empId + "'"
								+ " AND schedule_id = '" + schId + "'";
			
			PreparedStatement stmt = conn.prepareStatement(viewRouteSQL);
			ResultSet rs = stmt.executeQuery();
			
			System.out.println("List of postal codes:");
			// listing postal codes in result set
			while(rs.next()){
				System.out.println("\t" + rs.getString("postal_code"));
			}
				
			stmt.close();
			
		} catch (SQLException e) {
			e.getMessage();
		}
		
		
	}
	
	/**
	 *  The following method allows a carrier to view their assigned truck
	 *  based on their specified schedule id
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param empId
	 */
	public static void ViewTruck(Connection conn, String empId) {
		try {
			ListSchedule(conn, empId);
			Scanner read = new Scanner(System.in);
			System.out.println("Please enter a schedule ID");
			String schId = read.nextLine();
			
			String viewTruckSQL = "{ call ViewTruck( ?, ?)}";
			CallableStatement cstmt = conn.prepareCall(viewTruckSQL);
			
			cstmt.setString(1, schId);
			cstmt.registerOutParameter(2, Types.VARCHAR);
			
				
			cstmt.execute();
			
			String plate = cstmt.getString(2);
		
			System.out.println("Truck plate:\t" + plate);
			
			cstmt.close();
			
		} catch (SQLException e) {
			e.getMessage();
		}
		
	}
	
	/**
	 * The following method allows a user to have a choice in the way 
	 * they want to list the mail they have to deliver.
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param empId
	 */
	public static void ListMailToDeliv(Connection conn, String empId) {
		Scanner read = new Scanner(System.in);
		System.out.println("Please enter the number for the desired option:");
		System.out.println("Would you like to view: \n1 - All the mail to deliver."
							+ "\n2 - All the mail to be delivered in a specific postal code."
							+ "\n3 - All the mail addressed to a specific building.");
		
		int userChoice = read.nextInt(); 
		switch(userChoice){
			case 1:	
				ListAllMail(conn, empId);
				break;
			case 2: 
				ListMailPostalCode(conn, empId);
				break;
			case 3:
				ListMailAddress(conn, empId);
				break;			
		}
	}
	
	/**
	 * The following method will list all the mails a carrier has to deliver.
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param empId
	 */
	public static void ListAllMail(Connection conn, String empId){
		try {
			ListSchedule(conn, empId);
			Scanner read = new Scanner(System.in);
			System.out.println("Please enter a schedule ID");
			String schId = read.nextLine();
			
			String viewAllMailSQL = "SELECT mail.mail_id, postal_code.postal_code, street, city " +
							"FROM schedule JOIN postal_code on schedule.route_id = postal_code.route_id "
							+ " JOIN address on postal_code.postal_code = address.postal_code"
						  + "  JOIN mail on address.address_id = mail.delievry_address_id "
						  + "WHERE carrier_id = '" + empId + "' AND schedule_id = '" + schId + "'";
			
			PreparedStatement stmt = conn.prepareStatement(viewAllMailSQL);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				System.out.println("Mail ID:\t" + rs.getString("mail_id"));
				System.out.println("Postal Code:\t" + rs.getString("postal_code"));
				System.out.println("Street:\t" + rs.getString("street"));
				System.out.println("City:\t" + rs.getString("city") + "\n");
				
			}

			stmt.close();
			
			
		} catch (SQLException e) {
			e.getMessage();
		}
	}
	
	/**
	 * The following method will list the mail based on the postal code
	 * inputed by the carrier.
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param empId
	 */
	public static void ListMailPostalCode(Connection conn, String empId){
		try {
			ListSchedule(conn, empId);
			Scanner read = new Scanner(System.in);
			System.out.println("Please enter a schedule ID");
			String schId = read.nextLine();
			
			Scanner readPc = new Scanner(System.in);
			System.out.println("Enter postal code: ");
			String postalCode = readPc.nextLine();
			
			String sql = "SELECT mail_id, registered, a.postal_code, a.street, a.city " + 
						 "FROM schedule s JOIN postal_code p ON s.route_id = " + 
						 "p.route_id JOIN address a ON p.postal_code = a.postal_code " + 
						 "JOIN mail m ON a.address_id = m.delievry_address_id"
						 + " WHERE carrier_id = '" + empId + "' AND p.postal_code = '" + postalCode + "'"
						 + " AND schedule_id = '" + schId + "'";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				System.out.println("Mail ID:\t" + rs.getString("mail_id"));
				String reg = rs.getString("registered");
				String regMail = "";
				if(reg.equals("1"))
					regMail = "Registered Mail";
				else if (reg.equals("0"))
					regMail = "Unregistered Mail";

				System.out.println(regMail);
				System.out.println("Postal Code:\t" + rs.getString("postal_code"));
				System.out.println("Street:\t" + rs.getString("street"));
				System.out.println("City:\t" + rs.getString("city") + "\n");
			}
						
		} catch (SQLException e) {
			e.getMessage();
		}
	}
	/**
	 * The following method will list the mail based on the address
	 * inputed by the carrier.
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param empId
	 */
	public static void ListMailAddress(Connection conn, String empId){
		try {
			ListSchedule(conn, empId);
			Scanner read = new Scanner(System.in);
			System.out.println("Please enter a schedule ID");
			String schId = read.nextLine();	
			
			// get the required address information
			Scanner readAddress = new Scanner(System.in);
			System.out.println("Enter postal code: ");
			String postalCode = readAddress.nextLine();
			System.out.println("Enter street name: ");
			String street = readAddress.nextLine();
			System.out.println("Enter city: ");
			String city = readAddress.nextLine();
			System.out.println("Enter country: ");
			String country = readAddress.nextLine();
			
			String sql = "SELECT mail_id, registered, a.postal_code, a.street, a.city, a.country " + 
						 "FROM schedule s LEFT JOIN postal_code p ON s.route_id = " + 
						 "p.route_id LEFT JOIN address a ON p.postal_code = a.postal_code " + 
						 "LEFT JOIN mail m ON a.address_id = m.delievry_address_id"
						 + " WHERE carrier_id = '" + empId + "' AND p.postal_code = '" + postalCode + "'"
						 + " AND a.street = '" + street + "' AND a.city = '" + city + 
						 "' AND country = '" + country + "' AND schedule_id = '" + schId + "'";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				System.out.println("Mail ID:\t" + rs.getString("mail_id"));
				String reg = rs.getString("registered");
				
				String regMail = "";
				// display what the data actually means to the user.
				// basically, a string instead of a number
				if(reg.equals("1"))
					regMail = "Registered Mail";
				else if (reg.equals("0"))
					regMail = "Unregistered Mail";

				System.out.println(regMail);
				System.out.println("Postal Code:\t" + rs.getString("postal_code"));
				System.out.println("Street:\t" + rs.getString("street"));
				System.out.println("City:\t" + rs.getString("city"));
				System.out.println("Country:\t" + rs.getString("country") + "\n");
			}
					
		} catch (SQLException e) {
			e.getMessage();
		}
	}
	/**
	 * The following method will allow a carrier to mark their route 
	 * as started based on the chosen route id
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param empId
	 */
	public static void RouteStarted(Connection conn, String empId){

		try {
			ListSchedule(conn, empId);
			Scanner read = new Scanner(System.in);
			System.out.println("Please enter a schedule ID");
			String schId = read.nextLine();
			
			String sql = "SELECT route_id FROM schedule WHERE carrier_id = '" + empId 
						+ "' AND schedule_id = '" + schId + "'";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
		    rs.next();
		    String routeId = rs.getString("route_id");
		    
			Calendar today = Calendar.getInstance();
			System.out.println("Enter day:");
			int day = read.nextInt();
			System.out.println("Enter month:");
			int month = read.nextInt() - 1;
			System.out.println("Enter year:");
			int year = read.nextInt();
			System.out.println("day:\t" + day + "\nmonth:\t" + month + "\nyear:\t" + year);
			today.set(year, month, day);
			System.out.println(today.getTime());
			java.sql.Date date = new java.sql.Date(today.getTimeInMillis());
			
			System.out.println(date);
			CallableStatement cstmt = conn.prepareCall("{ call RouteStarted(?, ?, ?)}");
			
			cstmt.setString(1, routeId);
			cstmt.setDate(2, date);
			cstmt.setString(3, schId);
			
			cstmt.execute();
			cstmt.close();
			
			
		} catch (SQLException e) {
			e.getMessage();
		}
	}
	/**
	 * The following method will allow a carrier to mark their route 
	 * as completed based on the chosen route id
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param empId
	 */
	public static void RouteCompleted(Connection conn, String empId){
		try {
			ListSchedule(conn, empId);
			Scanner read = new Scanner(System.in);
			System.out.println("Please enter a schedule ID");
			String schId = read.nextLine();
			
			String sql = "SELECT route_id FROM schedule WHERE carrier_id = '" + empId 
						+ "' AND schedule_id = '" + schId + "'";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
		    rs.next();
		    String routeId = rs.getString("route_id");
		    
			Calendar today = Calendar.getInstance();
			System.out.println("Enter day:");
			int day = read.nextInt();
			System.out.println("Enter month:");
			int month = read.nextInt() - 1;
			System.out.println("Enter year:");
			int year = read.nextInt();
			System.out.println("day:\t" + day + "\nmonth:\t" + month + "\nyear:\t" + year);
			today.set(year, month, day);
			System.out.println(today.getTime());
			java.sql.Date date = new java.sql.Date(today.getTimeInMillis());
			
			System.out.println(date);
			CallableStatement cstmt = conn.prepareCall("{ call RouteCompleted(?, ?, ?)}");
			
			cstmt.setString(1, routeId);
			cstmt.setDate(2, date);
			cstmt.setString(3, schId);
			
			cstmt.execute();
			cstmt.close();
			
			// allows the carrier to mark the undelivered mails
			MarkMail(conn);
			
		} catch (SQLException e) {
			e.getMessage();
		}
	}
	/**
	 * The following method will mark a mail as either Delivered or 
	 * Not Delivered based on what the carrier inputs.
	 * @author Zahraa Horeibi
	 * @param conn
	 */
	public static void MarkMail(Connection conn){
		try {
			Scanner read = new Scanner(System.in);
			System.out.println("Please enter the mail ID:");
			String mailId = read.nextLine();	
			System.out.println("Please enter the mail status:");
			String status = read.nextLine();
			
			CallableStatement cstmt = conn.prepareCall("{call MarkMail (?, ?)}");
			
			cstmt.setString(1,  mailId);
			cstmt.setString(2,  status);

			cstmt.execute();
			
			cstmt.close();					
			
		} catch (SQLException e) {
			e.getMessage();
		}
	}
	
	/**
	 * The following method will allow a carrier to mark themselves as 
	 * sick on a specified day. When a user mark themselves as sick,
	 * a trigger will automatically assign a new carrier to do the task.
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param empId
	 */
	public static void MarkSick(Connection conn, String empId){
		try {
			
			java.sql.Date sqlDate = null;
			
			Scanner userDate = new Scanner(System.in);
			System.out.println("Would you like to enter the date or mark today as sick?");
			System.out.println("Enter t for today or enter a day:");
			String userSickDay = userDate.nextLine();
			Calendar today = Calendar.getInstance();
			if (userSickDay.equals("t")) {
				sqlDate = new java.sql.Date(today.getTimeInMillis());
			} else {
				int sDay = Integer.parseInt(userSickDay);
				System.out.println("Enter month:");
				int sMonth = userDate.nextInt() - 1;
				System.out.println("Enter year:");
				int sYear = userDate.nextInt();
				today.set(sYear, sMonth, sDay);
				sqlDate = new java.sql.Date(today.getTimeInMillis());
			}
			
			CallableStatement cstmt = conn.prepareCall("{ call MarkSick(?, ?)}");
			
			cstmt.setString(1, empId);
			cstmt.setDate(2, sqlDate);
			
			cstmt.execute();
			cstmt.close();		
			
		} catch (SQLException e) {
			e.getMessage();
		}
	}
}





