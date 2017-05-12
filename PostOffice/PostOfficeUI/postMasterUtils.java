import java.awt.List;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class postMasterUtils {

	public static void loginOptions(Connection conn, String empId) throws ClassNotFoundException, SQLException{
		Scanner read = new Scanner(System.in);
		System.out.println("What would you like to do?");
		System.out.println("1-View unassigned routes");
		System.out.println("2-Route Options");
		System.out.println("3-schedule options");
		System.out.println("4-employee options");
		System.out.println("5-Vehicle options");

		int choice = read.nextInt();
		loginOptions(conn,choice,empId);	

	}
	
	public static void loginOptions(Connection conn, int choice, String empId) throws ClassNotFoundException, SQLException{
		switch(choice){
		case 1:
			viewUnassigned(conn,empId);
			break;
		case 2:
			routeOptions(conn,empId);
			break;
		case 3:
			scheduleOptions(conn,empId);
			break;
		case 4:
			empOptions(conn,empId);
			break;
		case 5:
			//vehicleOptions(conn);
			break;			
		}
	}
	public static void empOptions(Connection conn,String empId) throws ClassNotFoundException, SQLException {
		System.out.println("What do you want to do?");
		System.out.println("1-Hire an employee");
		System.out.println("2-Fire An Emplyee");
		System.out.println("3-Go back");
		Scanner read = new Scanner(System.in);
		int choice = read.nextInt();
		empOptions(conn,empId,choice);
		
		
	}

	private static void empOptions(Connection conn, String empId, int choice) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		switch(choice){
		case 1:
			hireEmployee(conn,empId);
			break;
		case 2:
			fireEmployee(conn,empId);
			break;
		case 3:
			loginOptions(conn,empId);
		}
	}

	private static void fireEmployee(Connection conn, String empId) {
		Scanner read = new Scanner(System.in);
		System.out.println("who do you want to fire");
		String id = read.nextLine();
		
		try{
			String sql = "{execute fireEmployee(?)";
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.setString(1, id);
			cstmt.execute();
			loginOptions(conn,empId);
		}catch(SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}

		
	}

	private static void hireEmployee(Connection conn, String empId) {
		Scanner read = new Scanner(System.in);
		System.out.println("What is the first name");
		String fname = read.nextLine();
		System.out.println("what is the last name");
		String lname = read.nextLine();
		System.out.println("What kind of employee");
		String type = read.nextLine();
		System.out.println("what is the phone number");
		String phone = read.nextLine();
		System.out.println("What is his salary");
		Double salary = read.nextDouble();
		read.nextLine();
		System.out.println("What is his email");
		String email = read.nextLine();
		System.out.println("what is his password");
		String pass = read.nextLine();
		
		try{
			String sql = "{execute hireEmplyee(?,?,?,?,?,?)}";
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.setString(1,fname);
			cstmt.setString(2,lname);
			cstmt.setString(3,type);
			cstmt.setDouble(4,salary);
			cstmt.setString(5,phone);
			cstmt.setString(6, email);
			
			cstmt.execute();
			
			LogInUtils.newUser(conn, fname, pass);
			loginOptions(conn,empId);
		}catch(SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}
		
	}

	public static void viewUnassigned(Connection conn,String empId){
		
		/*try{
			String sql = "{call getUnassignesRoutes}";
			CallableStatement cstmt = conn.prepareCall(sql);
			
		}catch(SQLExeption e){
			e.printStackTrace();
		}*/
		
	}
	public static void routeOptions(Connection conn,String empId) throws ClassNotFoundException, SQLException{
		Scanner read = new Scanner(System.in);
		System.out.println("What would you like to do?");
		System.out.println("1-new route");
		System.out.println("2-add a postal code to a route");
		System.out.println("3-remove a postal code from a route");
		System.out.println("4-view the average time for a route");
		System.out.println("5-go back");
		int choice = read.nextInt();
		routeOptions(conn,choice,empId);
		
	}
	public static void routeOptions(Connection conn, int choice,String empId) throws ClassNotFoundException, SQLException{
		
		switch(choice) {
		case 1:	// list schedule
			newRoute(empId,conn);
			break;
			
		case 2: // method call to view route
			addPostal(empId,conn);
			break; 
		case 3: // method call to view truck
			removePostal(empId,conn);
			break;
			
		case 4: // method call to list mail
			viewAverage(empId,conn);
			break;
			
		case 5: // 
			loginOptions(conn,empId);
			break;	
		}
		
	}
	public static void newRoute(String empId,Connection conn){
		Scanner read = new Scanner(System.in);
		System.out.println("What do you want to call the route");
		String name = read.nextLine();
		newRoute(name,empId,conn);
	}
	public static void newRoute(String name, String empId,Connection conn){
		try{
			String sql = "{call newRoute(?)}";
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.setString(1, name);
			cstmt.execute();
			loginOptions(conn,empId);
		}catch(SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}
		
	}
	
	public static void addPostal(String empId, Connection conn){
		Scanner read = new Scanner(System.in);
		System.out.println("what route do yuo want to add to");
		String route_id = read.nextLine();
		System.out.println("what postalcode do you want to add to it");
		String postalCode = read.nextLine();
		
		try{
			String sql = "{call addCodeToRoute(?,?)}";
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.setString(1, postalCode);
			cstmt.setString(2, route_id);
			cstmt.execute();
			loginOptions(conn,empId);
		}catch(SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}
		
	}
	public static void removePostal(String empId, Connection conn){
		Scanner read = new Scanner(System.in);
		System.out.println("what route do yuo want to remove from");
		String route_id = read.nextLine();
		System.out.println("what postalcode do you want to remove from it");
		String postalCode = read.nextLine();
		
		try{
			String sql = "{call removeCodeFromRoute(?,?)}";
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.setString(1, postalCode);
			cstmt.setString(2, route_id);
			cstmt.execute();
			loginOptions(conn,empId);
		}catch(SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}
		
	}
	
	public static void viewAverage(String empId, Connection conn){
		Scanner read = new Scanner(System.in);
		System.out.println("What route do you want the average for");
		String id = read.nextLine();
		String sql = "{? = GetAverageRouteTime(?)}";
		try {
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.registerOutParameter(1, Types.NUMERIC);
			cstmt.setString(2, id);
			cstmt.execute();
			System.out.println(cstmt.getInt(1));
			loginOptions(conn, empId);
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void scheduleOptions(Connection conn, String empId){
		Scanner read = new Scanner(System.in);
		System.out.println("What would you like to do?");
		System.out.println("1-new schedule");
		System.out.println("2-go back");
		int choice = read.nextInt();
		scheduleOptions(conn,choice,empId);
	}
	public static void scheduleOptions(Connection conn, int choice, String empId){
		switch(choice){
		case 1:
			newSchedule(conn,empId);
			break;
		case 2:
			try {
				loginOptions(conn, empId);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		}
	}
	public static void newSchedule(Connection conn, String empId){
		Scanner read = new Scanner(System.in);
		System.out.println("who do you want to be the carrier?");
		String carrierid = read.nextLine();
		System.out.println("Which vehicle will be scheduled for it");
		String vID = read.nextLine();
		System.out.println("what route is this schedule for");
		String rID = read.nextLine();
		System.out.println("when will it start");
		String starttime = read.nextLine();
		SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy-hh-mm");
		Date theStart = null;
		try{
			theStart = df.parse(starttime);
		}catch (ParseException | java.text.ParseException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
		try{
			String sql = "{call newSchedule(?,?,?,?)";
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.setString(1, carrierid);
			cstmt.setString(2, vID);
			cstmt.setString(3, rID);
			cstmt.setDate(4, (java.sql.Date) theStart);
			
			cstmt.execute();
			loginOptions(conn,empId);
		}catch(SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
	

