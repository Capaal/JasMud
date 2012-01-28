import java.sql.*;

public class SQLInterface {

	private static java.sql.Connection con = null;
	private static String url = "jdbc:mysql:///jasmud";
	private static String username = null;
	private static char[] password = null;
	private static Statement stmt = null;

	public static void connect(String username1, char[] password1) {
		
		try {			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, username1, String.valueOf(password1));
			if (con != null) {
				System.out.println(" A database connection has been established!");
				username = username1;
				password = password1;
			}		
			
		} catch (Exception e) {
			System.out.println("Problem: " + e.toString());
		}
		disconnect();
	}
	
	public static void newMobile(int user, String name, String sex, String birthdate) {
		makeConnection();
		try {
			stmt = con.createStatement();
			int affected = 0;
			affected = stmt.executeUpdate("INSERT INTO mobile VALUES (NULL, '" + user + "', '" + name + "', CURDATE(), '" + birthdate + "')");
			if (affected > 0) {
				System.out.println("Insert successful.");
			} else {
				System.out.println("Insert failure.");
			} 
		} catch (SQLException e) {
				System.out.println("Error: " + e.toString());
		}
		disconnect();
	}
	
	private static void makeConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, username, String.valueOf(password));	
		} catch (Exception e) {
			System.out.println("Problem: " + e.toString());
		}
	}
	
	public static void disconnect() {
	
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			con = null;
		}
	}
}