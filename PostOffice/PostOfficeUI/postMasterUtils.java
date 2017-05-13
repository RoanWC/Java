import java.awt.List;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class postMasterUtils {

	/*Displays all the options that a post master can chose*/
	public static void loginOptions(Connection conn, String empId) throws ClassNotFoundException, SQLException{
		Scanner read = new Scanner(System.in);
		System.out.println("What would you like to do?");
		System.out.println("1-View unassigned routes");
		System.out.println("2-Route Options");
		System.out.println("3-schedule options");
		System.out.println("4-employee options");
		System.out.println("5-Vehicle options");
		System.out.println("6-Change your password");


		int choice = read.nextInt();
		loginOptions(conn,choice,empId);	

	}
	/*choses what method to call based on what the user chose*/
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
			vehicleOptions(conn,empId);
			break;	
		case 6:
			Utilities.ModifyPassword(conn, empId);
		}
	}
	/*list of options that the user can chose related to users*/
	public static void vehicleOptions(Connection conn,String empId) throws ClassNotFoundException, SQLException {
		Scanner read = new Scanner(System.in);
		System.out.println("What do you want to do?");
		System.out.println("1-Add a vehicle");
		System.out.println("2-decomition a vehicle");
		System.out.println("3-recomition a vehicle");
		System.out.println("4-go Back");
		int choice = read.nextInt();
		switch(choice){
		case 1:
			addVehicle(conn,empId);
			break;
		case 2:
			decomitionVehicle(conn,empId);
			break;
		case 3:
			recomitionVehicle(conn,empId);
			break;
		case 4:
			loginOptions(conn,empId);
			break;
			
		}
		
		
	}

	public static void decomitionVehicle(Connection conn, String empId) throws SQLException {
		Scanner read = new Scanner(System.in);
		System.out.println("what is the liscence for the vehicle you want to decomition?");
		String lisence = read.nextLine();
		CallableStatement cstmt = null;
		try{
		String SQL = "{call decomitionVehicle(?)}";
		cstmt = conn.prepareCall(SQL);
		cstmt.setString(1, lisence);
		cstmt.execute();
		
		} catch(SQLException e){
		}
		finally{
			cstmt.close();
		}				
	}
	
	public static void recomitionVehicle(Connection conn, String empId) throws SQLException {
		Scanner read = new Scanner(System.in);
		System.out.println("what is the liscence for the vehicle you want to recomition?");
		String lisence = read.nextLine();
		CallableStatement cstmt = null;
		try{
		String SQL = "{call recomitionVehicle(?)}";
		cstmt = conn.prepareCall(SQL);
		cstmt.setString(1, lisence);
		cstmt.execute();
		
		} catch(SQLException e){
		}
		finally{
			cstmt.close();
		}				
	}

	public static void addVehicle(Connection conn, String empId) throws SQLException {
		Scanner read = new Scanner(System.in);
		System.out.println("what is the liscence for the vehicle you want to add?");
		String lisence = read.nextLine();
		
		CallableStatement cstmt = null;
		try{
		String SQL = "{call addVehicle(?)}";
		cstmt = conn.prepareCall(SQL);
		cstmt.setString(1, lisence);
		cstmt.execute();
		
		} catch(SQLException e){
		}
		finally{
			cstmt.close();
		}

		
	}

	public static void empOptions(Connection conn,String empId) throws ClassNotFoundException, SQLException {
		System.out.println("What do you want to do?");
		System.out.println("1-Hire an employee");
		System.out.println("2-Fire An Employee");
		Scanner read = new Scanner(System.in);
		int choice = read.nextInt();
		switch(choice){
		case 1:
			hireEmployee(conn,empId);
			break;
		case 2:
			//fireEmployee(conn,empId);
			break;
		case 3:
			loginOptions(conn,empId);
		}
		
	}

	public static void hireEmployee(Connection conn, String empId) throws SQLException, ClassNotFoundException {
		Scanner read = new Scanner(System.in);
		
		System.out.println("what do you want his name to be");
		String fname = read.nextLine();
		System.out.println("what do you want his last name to be");
		String lname = read.nextLine();
		System.out.println("what do you want his type to be");
		String type = read.nextLine();
		System.out.println("what do you want his salary to be");
		Double salary = read.nextDouble();
		read.nextLine();
		System.out.println("what do you want his phone to be");
		String phone = read.nextLine();
		System.out.println("what do you want his email to be");
		String email = read.nextLine();
		System.out.println("what do you want his password to be");
		String pass = read.nextLine();
		
		CallableStatement cstmt = null;
		/*try{
		String SQL = "{call hireEmployee(?,?,?,?,?,?)}";
		cstmt = conn.prepareCall(SQL);
		cstmt.setString(1, fname);
		cstmt.setString(2, lname);
		cstmt.setString(3, type);
		cstmt.setDouble(4, salary);
		cstmt.setString(5, phone);
		cstmt.setString(6, email);
		
		cstmt.execute();
		cstmt.close();
		LogInUtils.newUser(conn, fname, pass);
		loginOptions(conn,empId);
		}catch(SQLException | ClassNotFoundException e){
		}*/
		try{
		LogInUtils.newUser(conn, fname, pass);

		String sql = "insert into employee values(emp_id_seq.nextval,?,?,?,?,?,?,user_id_seq.nextval)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, fname);
		stmt.setString(2, lname);
		stmt.setString(3, type);
		stmt.setDouble(4, salary);
		stmt.setString(5, phone);
		stmt.setString(6, email);
		stmt.executeUpdate();
		conn.close();
		}catch(SQLException e){
		}
	}
	public static void fireEmployee(Connection conn,String empId) throws SQLException{
		System.out.println("who do you want to fire");
		Scanner read = new Scanner(System.in);
		String id = read.nextLine();
		
		String sql = "delete from employee where employee_id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, id);
		stmt.execute();
		
	}

	public static void viewUnassigned(Connection conn,String empId){
		
		/*try{
			String sql = "{call getUnassignesRoutes}";
			CallableStatement cstmt = conn.prepareCall(sql);
			
		}catch(SQLExeption e){
			e.printStackTrace();
		}*/
		
	}
	/*shows options related to routes*/
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
		case 1:	
			newRoute(empId,conn);
			break;
			
		case 2:
			addPostal(empId,conn);
			break; 
		case 3:
			removePostal(empId,conn);
			break;
			
		case 4:
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
	/*creates a new route*/
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
	/*adds a postal code to a spesified route*/
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
	/*removes a spesified postal code from a spesified route*/
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
	/*shows the average times for a spesified route*/
	public static void viewAverage(String empId, Connection conn){
		Scanner read = new Scanner(System.in);
		System.out.println("What route do you want the average for");
		String id = read.nextLine();
		String sql = "{? = call GetAverageRouteTime(?)}";
		try {
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.registerOutParameter(1, Types.INTEGER);
			cstmt.setString(2, id);
			cstmt.execute();
			System.out.println(cstmt.getInt(1));
			loginOptions(conn, empId);
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/*displays options related to the schedule*/
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
	/*should create a new schedule entry. i am having a problem with the sheduled  start portion.*/
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
	

