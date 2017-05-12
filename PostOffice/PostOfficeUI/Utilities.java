import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;

public class Utilities {
	private static SecureRandom random = new SecureRandom();
	
	/*public static void ModifyPassword(Connection conn, String username) throws ClassNotFoundException, SQLException {
		Scanner read = new Scanner(System.in);
		System.out.println("Enter your current password: ");
		String oldPassword = read.nextLine();
		
		boolean logInSuccess = LogInUtils.login(conn, username, oldPassword);
		
		if (logInSuccess){
			String newPassword = JOptionPane.showInputDialog("Enter your new password: ");
			// confirm password?
			//String confirmPwd = JOptionPane.showInputDialog("Confirm your password: ");
			String salt = getSalt();
			byte[] hashedPassword = hash(newPassword, salt);
			
			// update table from here or call a procedure?
		
		
		}
		
		
	}*/
	
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
