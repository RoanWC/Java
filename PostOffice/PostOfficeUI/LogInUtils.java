import java.security.*;
import java.math.BigInteger;
import java.sql.*;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;

/**
 * @author Zahraa Horeibi
 *
 */

public class LogInUtils {
	
	private static SecureRandom random = new SecureRandom();
	
	public static void main(String[] args) {
		askForLogIn();
	}

	public static void askForLogIn(){

		try {
			Connection conn = null;
			conn = getConnection("A1537595", "database2");
			if(conn != null){	

				String userName = JOptionPane.showInputDialog("Welcome to your local post office!\nPlease enter your username\n");
				//String userPassword = JOptionPane.showInputDialog("Enter your password: ");
				boolean isLoggedIn = login(conn, userName);

				//boolean isLoggedIn = login(conn, userName, userPassword);

				if (isLoggedIn)
					afterLogin(conn, userName);
				
				else 
					System.out.println("Log in failed! Incorrect username or password");
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}	

	public static Connection getConnection(String username, String password) throws SQLException, ClassNotFoundException {
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Connection connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@198.168.52.73:1521:orad11g", username, password);
			// ask the user for their username and password in this method so we dont have it written there
			System.out.println("Connected to database");
			return connection;

		} catch (SQLException e){
			throw new SQLException(e.getMessage());

		} catch (ClassNotFoundException cnf) {
			//cnf.printStackTrace();
			throw new ClassNotFoundException(cnf.getMessage());
		}
	}
	
	public static boolean login(Connection conn, String username)throws SQLException, ClassNotFoundException{

		try {
								
			String usrQuery = "SELECT user_id FROM post_user WHERE username = " + "'" + username + "'";
			
			PreparedStatement stmt = conn.prepareStatement(usrQuery);
			ResultSet user = stmt.executeQuery();
			user.next();
			String user_name = user.getString("user_id");
			
			if(!user_name.isEmpty())
					return true;
				
				 else 
					 return false;													
			
			
		} catch (SQLException e){
			if(e.getMessage().equals("Exhausted Resultset"))
				System.out.println("Cannot find username!");
			e.printStackTrace();
		}		
		return false;
	}
	

	/*public static boolean login(Connection conn, String username, String password)throws SQLException, ClassNotFoundException{

		try {
			
			String getSaltQuery  = "SELECT salt FROM user WHERE username = " + username;		
			PreparedStatement preparedSalt = conn.prepareStatement(getSaltQuery);

			ResultSet saltRS = preparedSalt.executeQuery();			
			saltRS.next();
	
			String salt = saltRS.getString("salt");
			
			byte[] hashedPassword = Utilities.hash(password, salt);
			
			String checkQuery = "SELECT username FROM user WHERE hashed_pass = " + hashedPassword;
			
			PreparedStatement hashQuery = conn.prepareStatement(checkQuery);
			ResultSet hashRS = hashQuery.executeQuery();
			
			while(hashRS.next()){	
				String user = hashRS.getString("username");
				
				if(user.equals(username)) 
					return true;
				
				 else 
					 return false;													
			}
			
		} catch (SQLException e){
			if(e.getMessage().equals("Exhausted Resultset"))
				System.out.println("Cannot find username!");						
		}		
		return false;
	}*/
	
	public static void afterLogin(Connection conn, String username) throws ClassNotFoundException{
		System.out.println("Welcome back " + username + "!");
		try {
			String empTypeQuery = "SELECT type, employee_id, username FROM post_user "
								   + "JOIN employee using(user_id)"
								   + "WHERE username = " + "'" + username + "'";
				
			PreparedStatement preparedEmpType = conn.prepareStatement(empTypeQuery);
			ResultSet empRs = preparedEmpType.executeQuery();

			empRs.next();
			String empType = empRs.getString("type");
			String empId = empRs.getString("employee_id");
			
			switch(empType) {

			case "carrier" : // call class for carrier
				CarrierUtils.loginOptions(conn, empId);					 
				break;

			case "clerk" :	// call class for carrier 

				break;

			case "postmaster" :
				// call class for postmaster
				
				break;

			}
			empRs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
