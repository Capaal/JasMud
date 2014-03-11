package processes;

import java.sql.SQLException;

public class CreateWorld {
	
	public static void createWorld() {
		makeWorldFromDatabase();
	}
	
	public static void makeWorldFromDatabase() {
		SQLInterface.connect("root", "".toCharArray());
		SQLInterface.loadLocations();
		try {
			SQLInterface.loadSkillBooks();
		} catch (SQLException e) {
			System.out.println("All skill creation failed.");
			e.printStackTrace();
		}
		SQLInterface.loadMobs();
		SQLInterface.loadLocationItems();
		SQLInterface.disconnect();
	}
}