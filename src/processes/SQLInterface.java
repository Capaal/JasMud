package processes;

import java.sql.*;

import processes.Location.Builder;
import processes.Location.GroundType;

public class SQLInterface {

	private static java.sql.Connection con = null;
	private static String url = "jdbc:mysql://localhost:3306/jasmud";
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
	
	public static void loadLocations() {
		makeConnection();
		try {
			stmt = con.createStatement();
				
			String sql = ("SELECT * FROM locationstats ORDER BY locid ASC");
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("LOCID");
				String name = rs.getString("LOCNAME");
				String description = rs.getString("LOCDES");
				int inventory = rs.getInt("LOCINV");		
				GroundType g = GroundType.valueOf(rs.getString("LOCTYPE"));				
				Location loadLoc = new Location.Builder(id).name(name).description(description).north(rs.getInt("LOCNORTH"), rs.getString("LOCNORTHDIR"))
						.northEast(rs.getInt("LOCNE"), rs.getString("LOCNEDIR")).east(rs.getInt("LOCEAST"), rs.getString("LOCEASTDIR"))
						.southEast(rs.getInt("LOCSE"), rs.getString("LOCSEDIR")).south(rs.getInt("LOCSOUTH"), rs.getString("LOCSOUTHDIR"))
						.southWest(rs.getInt("LOCSW"), rs.getString("LOCSWDIR")).west(rs.getInt("LOCWEST"), rs.getString("LOCWESTDIR"))
						.northWest(rs.getInt("LOCNW"), rs.getString("LOCNWDIR")).up(rs.getInt("LOCUP"), rs.getString("LOCUPDIR"))
						.down(rs.getInt("LOCDOWN"), rs.getString("LOCDOWNDIR")).in(rs.getInt("LOCIN"), rs.getString("LOCINDIR"))
						.out(rs.getInt("LOCOUT"), rs.getString("LOCOUTDIR")).build();		
			}			
		} catch (SQLException e) {
				System.out.println("Error: " + e.toString());
		}
		disconnect();
	}
	
	public static void loadPlayers() {
		makeConnection();
		try {
			stmt = con.createStatement();
			
			String sql = ("SELECT * FROM mobstats ORDER BY mobid ASC");
			
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