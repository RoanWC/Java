import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;
/**
 * This class holds all the utilities required by all the workers
 * in the post office.
 * @author Zahraa Horeibi
 *
 */
public class Utilities {
	private static SecureRandom random = new SecureRandom();
	
	/**
	 * The following method modifies the password of the user.
	 * The user picks the desired password.
	 * @author Zahraa Horeibi
	 * @param conn
	 * @param userId
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	
	public static void ModifyPassword(Connection conn, String userId) {
		try {
		// getting the username
		String sql = "SELECT username FROM post_user WHERE user_id = '" + userId + "'" ;
	
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		String username = rs.getString("username");
		
		Scanner read = new Scanner(System.in);
		// ask for current password to make sure nobody is  
		// trying to mess with their account
		System.out.println("Enter your current password: ");
		String oldPassword = read.nextLine();
		
		String salt = null;
		byte[] hashedPassword = null;

		boolean logInSuccess = LogInUtils.login(conn, username, oldPassword);
		
		// if the current password is right, we allow them to enter a new one
		if (logInSuccess){
			String newPassword = JOptionPane.showInputDialog("Enter your new password: ");

			salt = getSalt();
			hashedPassword = hash(newPassword, salt);
			
		}
			// updating their new password to the database using a procedure
			CallableStatement cstmt = conn.prepareCall("{call ModifyPassword (?,?,?)}");
			
			cstmt.setString(1,  username);
			cstmt.setBytes(2,  hashedPassword);
			cstmt.setString(3,  salt);
			cstmt.execute();
			System.out.println("Password Changed!");
			cstmt.close();					
			
		} catch (SQLException | ClassNotFoundException e) {
			e.getMessage();
		}
		
	}
	
	//Helper Functions below:
		//getConnection() - obtains a connection
		//getSalt() - creates a randomly generated string 
		//hash() - takes a password and a salt as input and then computes their hash

		//Creates a randomly generated String
		public static String getSalt(){
			return new BigInteger(140, random).toString(32);
		}

		
		//Takes a password and a salt a performs a one way hashing on them, returning an array of bytes.
		public static byte[] hash(String password, String salt){
			try{
				SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );

				/*When defining the keyspec, in addition to passing in the password and salt, we also pass in
					a number of iterations (1024) and a key size (256). The number of iterations, 1024, is the
					number of times we perform our hashing function on the input. Normally, you could increase security
					further by using a different number of iterations for each user (in the same way you use a different
					salt for each user) and storing that number of iterations. Here, we just use a constant number of
					iterations. The key size is the number of bits we want in the output hash*/ 
				PBEKeySpec spec = new PBEKeySpec( password.toCharArray(), salt.getBytes(), 1024, 256 );

				SecretKey key = skf.generateSecret( spec );
				byte[] hash = key.getEncoded();
				return hash;
			}catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
				throw new RuntimeException(e);
			}
		}

	
}
