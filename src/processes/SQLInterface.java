package processes;

import interfaces.*;
import interfaces.Action.Where;
import interfaces.Action.Who;
import items.StdItem;
import items.StdItem.ItemType;

import java.sql.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import checks.*;
import costs.*;
import effectors.*;
import actions.*;
import actions.Message.msgStrings;
import processes.Equipment.EquipmentEnum;
import processes.Location.GroundType;
import processes.Skill.Syntax;

/**
 * Handles all interaction with the database, all load and saves of any type will pass through here as well as connecting and disconnecting.
 * <p>
 * The current database URL is found at the String "url", 
 * @author Jason
 */


// NEED TO CHECK FOR SQL INJECTIONS. Things like ";" in the name or anything else the use can input.
public class SQLInterface {

	private static java.sql.Connection con = null;
	private static String url = "jdbc:mysql://localhost:3306/jasmud";
	private static String username = null;
	private static char[] password = null;
	private static Statement stmt = null;

	/**
	 * Sets up the database connection for future use and tests for any connectivity problems.
	 * @param username1 String containing database username to be used for future connections.
	 * @param password1 Char array containing password corresponding to previous username.
	 */
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
	
	/**
	 * Accesses database and loops through every single location entry, creates a new location object from database data,
	 * and registers the location with the global location list.
	 * <p>
	 * This method effectively rebuilds the entire game world from the database, usually only called when restarting the game server.
	 * Must be run after "connect()" but before any other load methods, which require locations in game in order to function.
	 */
	public static void loadLocations() {
		makeConnection();
		try {
			stmt = con.createStatement();	
			String sql = ("insert into sequencetable values(NULL);");
			for (int i = 0; i < 25; i++) {
				stmt.execute(sql);
			}
			sql = ("SELECT * FROM locationstats ORDER BY locid ASC");
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("LOCID");
				String name = rs.getString("LOCNAME");
				String description = rs.getString("LOCDES");
	//			int inventory = rs.getInt("LOCINV");		// Not implemented
				GroundType g = GroundType.valueOf(rs.getString("LOCTYPE"));		// Not implemented		
				LocationBuilder newLocation = new LocationBuilder();
				newLocation.setName(name);
				newLocation.setId(id);
				newLocation.setDescription(description);
				newLocation.north(rs.getInt("LOCNORTH"), rs.getString("LOCNORTHDIR"));
				newLocation.northEast(rs.getInt("LOCNE"), rs.getString("LOCNEDIR"));
				newLocation.east(rs.getInt("LOCEAST"), rs.getString("LOCEASTDIR"));
				newLocation.southEast(rs.getInt("LOCSE"), rs.getString("LOCSEDIR"));
				newLocation.south(rs.getInt("LOCSOUTH"), rs.getString("LOCSOUTHDIR"));
				newLocation.southWest(rs.getInt("LOCSW"), rs.getString("LOCSWDIR"));
				newLocation.west(rs.getInt("LOCWEST"), rs.getString("LOCWESTDIR"));
				newLocation.northWest(rs.getInt("LOCNW"), rs.getString("LOCNWDIR"));
				newLocation.up(rs.getInt("LOCUP"), rs.getString("LOCUPDIR"));
				newLocation.down(rs.getInt("LOCDOWN"), rs.getString("LOCDOWNDIR"));
				newLocation.in(rs.getInt("LOCIN"), rs.getString("LOCINDIR"));
				newLocation.out(rs.getInt("LOCOUT"), rs.getString("LOCOUTDIR"));
				newLocation.complete();		
			}			
		} catch (SQLException e) {
			System.out.println("Error: " + e.toString());
			System.out.println("Locations failed to load, critical error, sql: SELECT * FROM locationstats ORDER BY locid ASC");
		}
//		disconnect();
	}
	
	// ONLY LOADS ITEMS ON THE GROUND
	public static void loadLocationItems() {
		String sql = "SELECT itemstats.*, slot.SLOT, type.TYPE FROM itemstats"
				+ " LEFT JOIN SLOTTABLE ON itemstats.ITEMID = slottable.ITEMID"
				+ " LEFT JOIN ITEMTYPETABLE ON itemstats.ITEMID = itemtypetable.ITEMID"
				+ " LEFT JOIN TYPE ON itemtypetable.TYPEID = type.TYPEID"
				+ " LEFT JOIN SLOT ON slottable.SLOTID = slot.SLOTID WHERE ITEMLOCTYPE='LOCATION';";
		try {
			loadItems(sql, null);
		} catch (SQLException e) {
			System.out.println("Error: " + e.toString());
			System.out.println("No ground items loaded, critical error, sql string = " + sql);
		}
		disconnect();		
	}
	//TODO fix duplicate switch
	public static void loadItems(String sql, Container container) throws SQLException {
		if (sql == null) {
			throw new NullPointerException("Sql string may not be null.");
		}
		makeConnection();
	//	try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int itemId = -1;
			String itemName = "";
			double itemPhysicalMult = 1;
			double itemBalanceMult = 1;
			String itemDescription = "";
			String itemShortDescription = "";
			int itemMaxDurability = 1;
			int itemCurrentDurability = 1;
			int itemLocation = 1;
			EquipmentEnum equipSlot = null;
			String itemLocType = "";
			EnumSet<EquipmentEnum> allowedEquipSlots = EnumSet.noneOf(EquipmentEnum.class);
			ArrayList<ItemType> itemTags = new ArrayList<ItemType>();
			ArrayList<Type> itemTypes = new ArrayList<Type>();
			while (rs.next()) {
				if (rs.getInt("ITEMID") != itemId) {
					if (itemId != -1) {
						ItemBuilder newItem = new ItemBuilder();
						newItem.setName(itemName);
						newItem.setId(itemId);
						newItem.setPhysicalMult(itemPhysicalMult);
						newItem.setBalanceMult(itemBalanceMult);
						newItem.setDescription(itemDescription);
						newItem.setShortDescription(itemShortDescription);
						newItem.setMaxDurability(itemMaxDurability);
						newItem.setCurrentDurability(itemCurrentDurability);
						newItem.setTypes(itemTypes);
						newItem.setItemTags(itemTags);
						newItem.setAllowedSlots(allowedEquipSlots);
						switch(itemLocType) {						
							case "LOCATION":
							/*	new StdItem.Builder(itemName, itemId).physicalMult(itemPhysicalMult).balanceMult(itemBalanceMult).description(itemDescription)
										.shortDescription(itemShortDescription).maxDurability(itemMaxDurability).currentDurability(itemCurrentDurability)
										.types(itemTypes).itemTags(itemTags)
										.allowedSlots(allowedEquipSlots).itemLocation(WorldServer.locationCollection.get(itemLocation)).build();
								*/								
								newItem.setItemLocation(WorldServer.locationCollection.get(itemLocation));
								newItem.complete();						
							break;
							case "INVENTORY":
							/*	new StdItem.Builder(itemName, itemId).physicalMult(itemPhysicalMult).balanceMult(itemBalanceMult).description(itemDescription)
										.shortDescription(itemShortDescription).maxDurability(itemMaxDurability).currentDurability(itemCurrentDurability)
										.types(itemTypes).itemTags(itemTags)
										.allowedSlots(allowedEquipSlots).itemLocation(container).build();*/
								newItem.setItemLocation(container);
								newItem.complete();
							break;
							case "EQUIPMENT":
								/*StdItem item = new StdItem.Builder(itemName, itemId).physicalMult(itemPhysicalMult).balanceMult(itemBalanceMult).description(itemDescription)
										.shortDescription(itemShortDescription).maxDurability(itemMaxDurability).currentDurability(itemCurrentDurability)
										.types(itemTypes).itemTags(itemTags)
										.allowedSlots(allowedEquipSlots).itemLocation(container).build();*/
								newItem.setItemLocation(container);
								StdItem createdItem = newItem.complete();
								((Mobile)container).equip(equipSlot, createdItem);
							break;
							default:
								System.out.println("itemLocType mismatch on item " + itemId + " giving " + itemLocType);
						}
					}
					allowedEquipSlots = EnumSet.noneOf(EquipmentEnum.class);
					itemTypes = new ArrayList<Type>();
				//	itemTags = new ArrayList<ItemType>();
					itemId = -1;
				}
				allowedEquipSlots.add(EquipmentEnum.valueOf(rs.getString("SLOT")));
				itemTypes.add(Type.valueOf(rs.getString("TYPE")));
			//	itemTags.add(ItemType.valueOf(rs.getString("TAG")));
				if (itemId == -1) {
					itemId = rs.getInt("ITEMID");
					itemName = rs.getString("ITEMNAME");
					itemPhysicalMult = rs.getDouble("ITEMPHYS");
					itemBalanceMult = rs.getDouble("ITEMBAL");
					itemDescription = rs.getString("ITEMDESC");
					itemShortDescription = rs.getString("ITEMSHORTD");
					itemMaxDurability = rs.getInt("ITEMMAXDUR");
					itemCurrentDurability = rs.getInt("ITEMCURDUR");
					itemLocation = rs.getInt("ITEMLOC");					
					itemLocType = rs.getString("ITEMLOCTYPE");
					if (itemLocType.equals("EQUIPMENT")) {
						equipSlot = EquipmentEnum.valueOf(rs.getString("EQUIPSLOT"));
					}
				} 
			}
		if (itemId != -1) {
			ItemBuilder newItem = new ItemBuilder();
			newItem.setName(itemName);
			newItem.setId(itemId);
			newItem.setPhysicalMult(itemPhysicalMult);
			newItem.setBalanceMult(itemBalanceMult);
			newItem.setDescription(itemDescription);
			newItem.setShortDescription(itemShortDescription);
			newItem.setMaxDurability(itemMaxDurability);
			newItem.setCurrentDurability(itemCurrentDurability);
			newItem.setTypes(itemTypes);
			newItem.setItemTags(itemTags);
			newItem.setAllowedSlots(allowedEquipSlots);
			switch(itemLocType) {						
				case "LOCATION":
				/*	new StdItem.Builder(itemName, itemId).physicalMult(itemPhysicalMult).balanceMult(itemBalanceMult).description(itemDescription)
							.shortDescription(itemShortDescription).maxDurability(itemMaxDurability).currentDurability(itemCurrentDurability)
							.types(itemTypes).itemTags(itemTags)
							.allowedSlots(allowedEquipSlots).itemLocation(WorldServer.locationCollection.get(itemLocation)).build();
					*/								
					newItem.setItemLocation(WorldServer.locationCollection.get(itemLocation));
					newItem.complete();						
				break;
				case "INVENTORY":
				/*	new StdItem.Builder(itemName, itemId).physicalMult(itemPhysicalMult).balanceMult(itemBalanceMult).description(itemDescription)
							.shortDescription(itemShortDescription).maxDurability(itemMaxDurability).currentDurability(itemCurrentDurability)
							.types(itemTypes).itemTags(itemTags)
							.allowedSlots(allowedEquipSlots).itemLocation(container).build();*/
					newItem.setItemLocation(container);
					newItem.complete();
				break;
				case "EQUIPMENT":
					/*StdItem item = new StdItem.Builder(itemName, itemId).physicalMult(itemPhysicalMult).balanceMult(itemBalanceMult).description(itemDescription)
							.shortDescription(itemShortDescription).maxDurability(itemMaxDurability).currentDurability(itemCurrentDurability)
							.types(itemTypes).itemTags(itemTags)
							.allowedSlots(allowedEquipSlots).itemLocation(container).build();*/
					newItem.setItemLocation(container);
					StdItem createdItem = newItem.complete();
					((Mobile)container).equip(equipSlot, createdItem);
				break;
				default:
					System.out.println("itemLocType mismatch on item " + itemId + " giving " + itemLocType);
			}
		}
	//	} catch(SQLException e) {
	//		System.out.println("Error " + e.toString());
	//	}
	//	disconnect();
	}
	// Needs to be refactored to use a single query.
	/**
	 * Loads a single Mobile from the database into the game, including all items, skills, saved stats, and position.
	 * <p>
	 * Must be run after "connect()" and "loadLocations()" and is very closely related to "determineActions()".
	 * @param name Name of the mobile being loaded, including id for non-heroes.
	 * @param password Password of the mobile being loaded, however correct password will have been checked previously.
	 * @return Returns the loaded and completely built and registered Mobile or Null.
	 */
	public static Mobile loadPlayer(String name, String password) throws SQLException {
		if (name == null || password == null) {
			throw new NullPointerException("Name or password may not be null.");
		}
		makeConnection();
		Mobile loadedPlayer = null;
		int mobid = -1;
	//	try {
			stmt = con.createStatement();			
			String sql = ("SELECT * FROM mobstats WHERE MOBNAME='" + name + "' AND MOBPASS='" + password + "'");			
			ResultSet rs = stmt.executeQuery(sql);	
			if (rs.next()) {
				String type = rs.getString("MOBTYPE");
				mobid = rs.getInt("MOBID");
				String mobname = rs.getString("MOBNAME");
				switch (type) {
				case "STDMOB":
					loadedPlayer = new StdMob.Builder(rs.getInt("MOBID"), mobname).description(rs.getString("MOBDESC")).password(rs.getString("MOBPASS"))
						.shortDescription(rs.getString("MOBSHORTD")).location(WorldServer.locationCollection.get(rs.getInt("MOBLOC"))).build();
					int startUp = rs.getInt("LOADONSTARTUP");
					if (startUp == 1) {
						loadedPlayer.setStartup(true);
					} else {
						loadedPlayer.setStartup(false);
					}
					sql = "SELECT itemstats.*, slot.SLOT, type.TYPE FROM itemstats LEFT JOIN SLOTTABLE ON itemstats.ITEMID = slottable.ITEMID"
							+ " LEFT JOIN SLOT ON slottable.SLOTID = slot.SLOTID"
							+ " LEFT JOIN ITEMTYPETABLE ON itemstats.ITEMID = itemtypetable.ITEMID"
							+ " LEFT JOIN TYPE ON itemtypetable.TYPEID = type.TYPEID"
							+ " WHERE ITEMLOC=" + loadedPlayer.getId() + " AND (ITEMLOCTYPE='INVENTORY' OR ITEMLOCTYPE ='EQUIPMENT');";
					loadItems(sql, loadedPlayer);					
					WorldServer.mobList.put(mobname + mobid, loadedPlayer);
					loadedPlayer.getContainer().acceptItem(loadedPlayer);
				break;				
				}
			} 	else {
				return null;
			}
			
			sql = ("SELECT SKILLBOOKID, MOBPROGRESS FROM skillbooktable WHERE MOBID='" + mobid + "'");
			rs = stmt.executeQuery(sql);
			//Skillbook id --> skillbook progress for this particular mob
			Map<Integer, Integer> mobSkillBooks = new HashMap<Integer, Integer>();
			while (rs.next()) {
				mobSkillBooks.put(rs.getInt("SKILLBOOKID"), rs.getInt("MOBPROGRESS"));			
			}			
			for (int skillBookId : mobSkillBooks.keySet()) {
				SkillBook skillBook = null;
				if (!WorldServer.AllSkillBooks.containsKey(skillBookId)) {
					System.out.println("Error, book not loaded.");
					throw new IllegalStateException("Critical error, skillbooks do not align.");				
				}
				loadedPlayer.addBook(WorldServer.AllSkillBooks.get(skillBookId), mobSkillBooks.get(skillBookId));
			}		
					
//		} catch (SQLException e) {
//			System.out.println("Error: " + e.toString());
//		}
//		disconnect();
		return loadedPlayer;
	}
	
	public static void saveAction(String sql) throws SQLException {
		makeConnection();
	//	try {
			stmt = con.createStatement();		
			stmt.executeUpdate(sql);
	//	} catch (SQLException e) {
	//		System.out.println("Error: " + e.toString());
	//		return false;
	//	}
	//	disconnect();		
	}
	
		
	public static Object viewData(String blockQuery, String column) {
		makeConnection();
		Object result = null;
		try {
			stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(blockQuery);
			ResultSetMetaData rsmd = rs.getMetaData();
			if (rs.next()) {
				int columnType = rsmd.getColumnType(1);
				if (columnType == Types.INTEGER) {
					result = rs.getInt(1);
				} else if (columnType == Types.VARCHAR || columnType == Types.CHAR){
					result = rs.getString(1);
				} else {
					result = rs.getObject(1);
				}	
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e.toString());
		}
	//	disconnect();
		return result;
	}
	
	public static HashMap<String, Object> returnBlockView(String blockQuery) {
		makeConnection();
		HashMap<String, Object> blockView = new HashMap<String, Object>();
		try {
			stmt = con.createStatement();
		
			ResultSet rs = stmt.executeQuery(blockQuery);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			if (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					int columnType = rsmd.getColumnType(i);
					Object o;
					if (columnType == Types.INTEGER) {
						o = rs.getInt(i);
					} else if (columnType == Types.VARCHAR || columnType == Types.CHAR){
						o = rs.getString(i);
					} else {
						o = rs.getObject(i);
					}
					String columnName = rsmd.getColumnName(i);
					blockView.put(columnName, o);				
				}
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e.toString());
		}
	//	disconnect();
		return blockView;
	}
	
	/*
	 * Re-connects to the database using the settings saved from "connect()", which must have been run previously.
	 * <p>
	 * This connection does not auto-close, it is recommended that "disconnect()" is ran after each immediate use of the database connection.
	 */
	public static void makeConnection() {
		if (con == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				con = DriverManager.getConnection(url, username, String.valueOf(password));	
			} catch (Exception e) {
				System.out.println("Problem: " + e.toString());
			}
		}
	}
	
	/**
	 * Disconnects the game server from the database.
	 * <p>
	 * Should be used after each use of "makeConnection()"
	 */
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

	/**
	 * Attempts to load all Mobiles designated as "loadonstartup=true" in the database into the game server.
	 * <p>
	 * Will grab each Mobile and run "loadPlayer()" thus fully implementing them.
	 */
	public static void loadMobs() {
		makeConnection();
		try {
			stmt = con.createStatement();
			
			String sql = ("SELECT * FROM mobstats WHERE loadonstartup=true");
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				loadPlayer(rs.getString("mobname"), rs.getString("mobpass"));
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e.toString());
		}
		disconnect();
	}
	
	public static void loadSkillBooks() throws SQLException {
		String sql = ("SELECT DISTINCT SKILLBOOKID FROM skillbooktable");
		ResultSet rs = stmt.executeQuery(sql);		
		ArrayList<Integer> skillBooks = new ArrayList<Integer>();
		while (rs.next()) {
			skillBooks.add(rs.getInt("SKILLBOOKID"));			
		}		
		for (int skillBookId : skillBooks) {
			SkillBook skillBook = null;
			if (!WorldServer.AllSkillBooks.containsKey(skillBookId)) {	
				SkillBuilder skillBuild = new SkillBuilder();				
				sql = ("SELECT skillbook.SKILLBOOKNAME, skill.SKILLID FROM skilltable JOIN skillbook ON skilltable.SKILLBOOKID = skillbook.SKILLBOOKID "
						+ " JOIN skill ON skill.SKILLID = skilltable.SKILLID WHERE skilltable.SKILLBOOKID='" + skillBookId + "'");
				rs = stmt.executeQuery(sql);
				
				ArrayList<Integer> mobSkills = new ArrayList<Integer>();
				String skillBookName = null;
				while (rs.next()) {
					mobSkills.add(rs.getInt("SKILLID"));
					if (skillBookName == null) {
						skillBookName = rs.getString("SKILLBOOKNAME");
					}
				}				
				for (int i : mobSkills) {
					if (skillBook == null)	{
						skillBook = new SkillBook(skillBookName, skillBookId);
					}
					String sql1 = "SELECT * FROM skill WHERE SKILLID=" + i + ";";
					ResultSet rs1 = stmt.executeQuery(sql1);
					if (!rs1.next()) {
						System.out.println("Select skill in SQLInterface failed.");
					}						
					skillBuild.setName(rs1.getString("SKILLNAME"));		
					skillBuild.setFailMsg(rs1.getString("SKILLFAILMSG"));
					skillBuild.setDescription(rs1.getString("SKILLDES"));
					skillBuild.setId(i);
					int skillId = i;
					sql1 = ("SELECT syntax.* FROM syntaxtable JOIN syntax ON syntaxtable.SYNTAXID = syntax.SYNTAXID"
							+ " WHERE syntaxtable.SKILLID = '" + skillId + "' ORDER BY SYNTAXPOS ASC");
					rs1 = stmt.executeQuery(sql1);
					if (!rs1.isBeforeFirst() ) {    
						 System.out.println("No data for syntax on skill: " + i); 				
					} 
					ArrayList<Syntax> skillSyntax = new ArrayList<Syntax>();
					while (rs1.next()) {						
						skillSyntax.add(Syntax.valueOf(rs1.getString("SYNTAXTYPE")));
					}
					skillBuild.setSyntax(skillSyntax);					
					sql1 = ("SELECT type.TYPE FROM skilltypetable JOIN type ON skilltypetable.TYPEID = type.TYPEID"
							+ " WHERE skilltypetable.SKILLID = " + skillId + ";");
					rs1 = stmt.executeQuery(sql1);
					ArrayList<Type> skillType = new ArrayList<Type>();
					while (rs1.next()) {						
						skillType.add(Type.valueOf(rs1.getString("TYPE")));
					}
					skillBuild.setType(skillType);
					sql1 = ("SELECT block.* FROM blocktable JOIN block ON blocktable.BLOCKID = block.BLOCKID"
							+ " WHERE blocktable.SKILLID = '" + skillId + "' ORDER BY BLOCKPOS ASC");
					rs1 = stmt.executeQuery(sql1);						
					while (rs1.next()) {						
						skillBuild.addAction(determineAction(rs1));					
					}
					skillBuild.addBook(skillBook);
					skillBuild.complete();	
				}
			}
			WorldServer.AllSkillBooks.put(skillBookId, skillBook);
		}
	}
	
	/**
	 * Takes a very specific ResultSet from "loadPlayer()" or recursively in order to exactly rebuild skills as they were designed.
	 * <p>
	 * As more skill blocks or actions are created the below must be updated so that they can interact with the database.
	 * @param rs ResultSet containing all the data needed to load any skill block, very specific.
	 * @return Returns the block or action requested, is rebuilt in terms of the game server from database stats.
	 * @throws SQLException
	 */
	private static Action determineAction(ResultSet rs) throws SQLException {
		
		switch (rs.getString("BLOCKTYPE")) {				
			case "DAMAGE":
				return new Damage(rs.getInt("INTVALUE"), Who.valueOf(rs.getString("TARGETWHO")), Where.valueOf(rs.getString("TARGETWHERE")));
		
			
			case "BALANCECOST":
				return new BalanceCost(checkBoolean(rs.getString("BOOLEANONE")), Who.valueOf(rs.getString("TARGETWHO")), Where.valueOf(rs.getString("TARGETWHERE")));
		
			case "BLEEDEFFECT":
				return new BleedEffect(rs.getInt("INTVALUE"), Who.valueOf(rs.getString("TARGETWHO")), Where.valueOf(rs.getString("TARGETWHERE")));
			
			case "WEAPONEQUIPPEDCHECK":
				// rs.getString("SKILLTYPE") actually returns an integer that represents a pointer at a real skilltype
				// So probably need to do another query for the answer. Or add it to the resultset above
				return new WeaponEquippedCheck(Type.valueOf(rs.getString("TYPE")), Who.valueOf(rs.getString("TARGETWHO")), Where.valueOf(rs.getString("TARGETWHERE")));
			
			
			case "MESSAGE":
				Statement stmt3 = con.createStatement();
				ResultSet rs3 = stmt3.executeQuery("SELECT msgstrings.* FROM msgstringstable JOIN msgstrings ON msgstringstable.MSGSTRINGSID = msgstrings.MSGSTRINGSID "
						+ "WHERE msgstringstable.BLOCKID =" + rs.getInt("BLOCKID") + " ORDER BY MSGSTRINGSPOS ASC;");
				ArrayList<msgStrings> msgstringslist = new ArrayList<msgStrings>();
				while (rs3.next()) {
					msgstringslist.add(msgStrings.valueOf(rs3.getString("MSGSTRINGSTYPE")));
				}
				return new Message(rs.getString("STRINGONE"), Who.valueOf(rs.getString("TARGETWHO")), Where.valueOf(rs.getString("TARGETWHERE")), msgstringslist);
						
			case "CHANCE":
				int internalActionNum = rs.getInt("BLOCKPOINTERONE");
				makeConnection();
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery("SELECT * FROM block WHERE BLOCKID='" + internalActionNum + "'");
				
				if (rs2.next()) {
					Action innerAction = determineAction(rs2);
					return new Chance(rs.getInt("INTVALUE"), innerAction);
				}
				return null;			
			
			case "SAY":
				return new Say();
				
			case "EXAMINE":
				return new Examine(Where.valueOf(rs.getString("TARGETWHERE")));
				
			case "OR":
				int internalActionNumOrOne = rs.getInt("BLOCKPOINTERONE");
				int internalActionNumOrTwo = rs.getInt("BLOCKPOINTERTWO");
				Statement stmtOR = con.createStatement();
				ResultSet rsOR = stmtOR.executeQuery("SELECT * FROM block WHERE BLOCKID='" + internalActionNumOrOne + "'");
				Action innerOrActionOne = null;
				Action innerOrActionTwo = null;
				if (rsOR.next()) {
					innerOrActionOne = determineAction(rsOR);
				}
				rsOR = stmtOR.executeQuery("SELECT * FROM block WHERE BLOCKID='" + internalActionNumOrTwo + "'");
				if (rsOR.next()) {
					innerOrActionTwo = determineAction(rsOR);
				}
				return new Or(innerOrActionOne, innerOrActionTwo);
				
			case "GET":
				return new Get(Who.valueOf(rs.getString("TARGETWHO")), Where.valueOf(rs.getString("TARGETWHERE")));
				
			case "LOOK":
				return new Look(Where.valueOf(rs.getString("TARGETWHERE")));
				
			case "MOVE":
				return new Move(Who.valueOf(rs.getString("TARGETWHO")), Where.valueOf(rs.getString("TARGETWHERE")), Where.valueOf(rs.getString("ENDWHERE")));
				
			case "BALANCECHECK":
				return new BalanceCheck(checkBoolean(rs.getString("BOOLEANONE")), Who.valueOf(rs.getString("TARGETWHO")), Where.valueOf(rs.getString("TARGETWHERE")));
				
			case "MOVECHECK":
				return new MoveCheck(GroundType.valueOf(rs.getString("GROUNDTYPE")), Who.valueOf(rs.getString("TARGETWHO")), Where.valueOf(rs.getString("TARGETWHERE")), Where.valueOf(rs.getString("ENDWHERE")));
				
			case "DROP":
				return new Drop(Who.valueOf(rs.getString("TARGETWHO")), Where.valueOf(rs.getString("TARGETWHERE")), Where.valueOf(rs.getString("ENDWHERE")));
				
			case "EQUIPCHANGE":
				return new EquipChange(Who.valueOf(rs.getString("TARGETWHO")), Where.valueOf(rs.getString("TARGETWHERE")), checkBoolean(rs.getString("BOOLEANONE")));
			
			case "GODCREATE":
				return new Godcreate();
			
			default:
				System.out.println("Determine Action could not find appropriate case, failed: " + rs.getString("BLOCKTYPE"));
				return null;
		}
	}
	 /**
	  * Translates an ENUM('true', 'false') from the database into a boolean primitive.
	  * @param ifBoolean String from the database enum.
	  * @return Returns a boolean primitive translated from the database enum of a boolean.
	  */
	private static boolean checkBoolean(String ifBoolean) {
		if (ifBoolean.equalsIgnoreCase("true") || ifBoolean.equalsIgnoreCase("false")) {
		    return Boolean.valueOf(ifBoolean);
		} else {
		    System.out.println(ifBoolean + " is being outputed from some skill, fails to become a true boolean and autos to false");
		    return false;
		}
	}
	
	public static void increaseSequencer() {
		makeConnection();
		try {
			stmt = con.createStatement();			
			String sql = ("insert into sequencetable values(NULL);");
			for (int i = 0; i < 25; i++) {
				stmt.execute(sql);
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e.toString());
		}
	//	disconnect();
	}		
}