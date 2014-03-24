package processes;

import java.sql.SQLException;

public class CreateWorld {
	
	public static void createWorld() {
		makeWorldFromDatabase();
	}
	
	public static void makeWorldFromDatabase() {
		WorldServer.databaseInterface.connect("root", "".toCharArray());
		WorldServer.databaseInterface.loadLocations();
		WorldServer.databaseInterface.loadSkillBooks();
		WorldServer.databaseInterface.loadMobs();
		WorldServer.databaseInterface.loadLocationItems();
		WorldServer.databaseInterface.disconnect();
	}
}