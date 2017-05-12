import java.security.*;
import java.math.BigInteger;
import java.sql.*;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;

/**
 * The following class contains all the required 
 * method and information for a user to log in.
 * @author Zahraa Horeibi
 *
 */

public class LogInUtils {
	
	private static SecureRandom random = new SecureRandom();
	
	public static void main(String[] args) {
		askForLogIn();
	}

	/**
	 * The following class asks the user for their authentication information
	 * and sends them to the login method.
     * @author Zahraa Horeibi
     *
     */

	public static void askForLogIn(){

		try {
			Connection conn = null;
			// storing info in text so the teacher can access database
			conn = getConnection("A1010290", "password123");
			if(conn != null){	
						
				String username = JOptionPane.showInputDialog("Welcome to your local post office!\nPlease enter your username\n");			
				String password = JOptionPane.showInputDialog("Enter your password:");
		
				// * WHOEVER DELETES THE POST_USER TABLE MUST ADD THESE ONE BY ONE *
						
				//newUser(conn, username, password);
				//newUser(conn, "Janny", "Janny");
				//newUser(conn, "Peggy", "Peggy");
				//newUser(conn, "Ingamar", "Ingamar");
				//newUser(conn,  "Morse", "Morse");
				//newUser(conn, "Albertine", "Albertine");
				//newUser(conn,"Solomon", "Solomon");
				//newUser(conn, "Johnny", "Johnny");
				//newUser(conn,"Seymour", "Seymour");
				//newUser(conn, "Haven", "Haven");
				//newUser(conn,  "Giacopo", "Giacopo");
				//newUser(conn, "Erastus", "Erastus");
				//newUser(conn, "Latrena", "Latrena");
				//newUser(conn, "Tiffie", "Tiffie");
				//newUser(conn,  "Kasey", "Kasey");
				//newUser(conn, "Genvieve", "Genvieve");
				
				boolean isLoggedIn = login(conn, username, password);
				
				if (isLoggedIn)
					afterLogin(conn, username);
				
				else 
					System.out.println("Log in failed! Incorrect username or password");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.getMessage();
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
	// **** TEMPORARY, MUST REMOVEEEEE ****
	/*
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
	*/
	/**
	 * Method to create new user.
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param username
	 * @param password
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void newUser(Connection conn, String username, String password) throws SQLException, ClassNotFoundException{
	
			String salt = Utilities.getSalt();
			String query = "INSERT INTO POST_USER VALUES(user_id_seq.nextval,?,?,?)";
			byte[] hashedPassword = Utilities.hash(password, salt);
	

			PreparedStatement user = conn.prepareStatement(query);

			user.setString(1, username);
			user.setBytes(2, hashedPassword);		
			user.setString(3, salt);

			
			user.executeUpdate();
			System.out.println("New user registered!");
			conn.close();
	}
	/**
	 * 	The following method logs a user into their account whether they're a carrier, clerk or 
	 * postmaster. It returns a boolean value indicating whether the login was 
	 * successful or not. 
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param username
	 * @param password
	 * @return boolean 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static boolean login(Connection conn, String username, String password)throws SQLException, ClassNotFoundException{

		try {
			
			String getSaltQuery  = "SELECT salt_str FROM post_user WHERE username = '" + username + "'";		
			PreparedStatement preparedSalt = conn.prepareStatement(getSaltQuery);

			ResultSet saltRS = preparedSalt.executeQuery();			
			saltRS.next();
			// getting salt stored for the user
			String salt = saltRS.getString("salt_str");
			// hashing their password using the same salt
			byte[] hashedPassword = Utilities.hash(password, salt);

			// selecting username based on the hashed password
			String checkQuery = "SELECT username FROM post_user WHERE hashed_pass = ?" ;
			
			PreparedStatement hashQuery = conn.prepareStatement(checkQuery);
			hashQuery.setBytes(1, hashedPassword);
			ResultSet hashRS = hashQuery.executeQuery();


			hashRS.next();
			
			String user = hashRS.getString("username");
			// comparasion of username entered and the one returned from
			// the database
				if(user.equals(username)) 
					return true;
				
				 else if (!user.equals(username))
					 return false;													
			
		} catch (SQLException e){
			if(e.getMessage().equals("Exhausted Resultset"))
				System.out.println("Cannot find username!");	
			e.getMessage();
		}		
		return false;
	}
	/**
	 * The following method gets called after a user has successfully logged in. 
	 * It determines whether they are a carrier, clerk or postmaster 
	 * and calls the option method in their respective class. 
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param username
	 * @throws ClassNotFoundException
	 */
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
				postMasterUtils.loginOptions(conn,empId);
				break;

			}
			empRs.close();

		} catch (SQLException e) {
			e.getMessage();
		}

	}


}
