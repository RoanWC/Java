import java.awt.List;
import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

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
			//scheduleOptions(conn);
			break;
		case 4:
			//empOptions(conn);
			break;
		case 5:
			//vehicleOptions(conn);
			break;			
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
	public static void routeOptions(Connection conn,String empId){
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
	public static void routeOptions(Connection conn, int choice,String empId){
		
		switch(choice) {
		case 1:	// list schedule
			newRoute(empId,conn);
			break;
			
		case 2: // method call to view route
			addPostalCode(empId);
			break; 
		case 3: // method call to view truck
			removePostalCode(empId);
			break;
			
		case 4: // method call to list mail
			viewAverage(empId);
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
	
}
