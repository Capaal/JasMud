package processes;

import java.sql.SQLException;

public class CreateWorld {
	
	public static void createWorld() {
//		makeWorldFromDatabase();
		makeWorldFromNowhere();
	}
	
	public static void makeWorldFromDatabase() {
//		WorldServer.databaseInterface.connect("root", "".toCharArray());
//		WorldServer.databaseInterface.loadLocations();
//		WorldServer.databaseInterface.loadSkillBooks();
//		WorldServer.databaseInterface.loadMobs();
//		WorldServer.databaseInterface.disconnect();
	}
	
	public static void makeWorldFromNowhere() {
		int id = 1;
			
		LocationBuilder newLocation = new LocationBuilder();
		newLocation.setId(id);
		
		newLocation.complete();	
	}
}