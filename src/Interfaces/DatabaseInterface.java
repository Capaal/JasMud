package interfaces;

import java.sql.SQLException;
import java.util.HashMap;

public interface DatabaseInterface {

	/**
	 * Sets up the database connection for future use and tests for any connectivity problems.
	 * @param username1 String containing database username to be used for future connections.
	 * @param password1 Char array containing password corresponding to previous username.
	 */
	public abstract void connect(String username1, char[] password1);

	/**
	 * Accesses database and loops through every single location entry, creates a new location object from database data,
	 * and registers the location with the global location list.
	 * <p>
	 * This method effectively rebuilds the entire game world from the database, usually only called when restarting the game server.
	 * Must be run after "connect()" but before any other load methods, which require locations in game in order to function.
	 */
	public abstract void loadLocations();

	

	//TODO fix duplicate switch
//	public abstract void loadItems(String sql, Container container);

	// Needs to be refactored to use a single query.
	/**
	 * Loads a single Mobile from the database into the game, including all items, skills, saved stats, and position.
	 * <p>
	 * Must be run after "connect()" and "loadLocations()" and is very closely related to "determineActions()".
	 * @param name Name of the mobile being loaded, including id for non-heroes.
	 * @param password Password of the mobile being loaded, however correct password will have been checked previously.
	 * @return Returns the loaded and completely built and registered Mobile or Null.
	 */
	public abstract Mobile loadPlayer(String name, String password);

	public abstract void saveAction(String sql);

	public abstract Object viewData(String blockQuery, String column);

	public abstract HashMap<String, Object> returnBlockView(String blockQuery);

	/*
	 * Re-connects to the database using the settings saved from "connect()", which must have been run previously.
	 * <p>
	 * This connection does not auto-close, it is recommended that "disconnect()" is ran after each immediate use of the database connection.
	 */
	public abstract void makeConnection();

	/**
	 * Disconnects the game server from the database.
	 * <p>
	 * Should be used after each use of "makeConnection()"
	 */
	public abstract void disconnect();

	/**
	 * Attempts to load all Mobiles designated as "loadonstartup=true" in the database into the game server.
	 * <p>
	 * Will grab each Mobile and run "loadPlayer()" thus fully implementing them.
	 */
	public abstract void loadMobs();

	public abstract void loadSkillBooks();

	public abstract void increaseSequencer();

}