package processes;

import interfaces.*;
import items.StdItem;
import items.StdItem.ItemType;

import java.sql.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import checks.*;
import effectors.*;
import TargettingStrategies.WhatFactory;
import TargettingStrategies.WhereFactory;
import actions.*;
import processes.Equipment.EquipmentEnum;
import processes.Location.GroundType;
import processes.Skill.Syntax;

/**
 * Handles all interaction with the database, all load and saves of any type will pass through here as well as connecting and disconnecting.
 * <p>
 * The current database URL is found at the String "url".
 */


// NEED TO CHECK FOR SQL INJECTIONS. Things like ";" in the name or anything else the use can input.
public class SQLInterface implements DatabaseInterface{

	protected  java.sql.Connection con = null;
	protected  String url = "jdbc:mysql://localhost:3306/jasmud";
	protected  String username = null;
	protected  char[] password = null;
	protected  Statement stmt = null;

	@Override
	public  void connect(String username1, char[] password1) {		
		try {			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			this.con = DriverManager.getConnection(url, username1, String.valueOf(password1));
			if (this.con != null) {
				System.out.println(" A database connection has been established!: " + url);
				username = username1;
				password = password1;
			}		
			
		} catch (Exception e) {
			System.out.println("Problem: " + e.toString());
		}
		disconnect();
	}
	
	/* (non-Javadoc)
	 * @see processes.DatabaseInterface#loadLocations()
	 */
	@Override
	public  void loadLocations() {
		makeConnection();
		try {
			stmt = this.con.createStatement();				
			increaseSequenceTable(stmt);			
			ResultSet allLocations = obtainAllLocations(stmt);			
			while (allLocations.next()) {				
				buildLocation(allLocations);					
			}			
		} catch (SQLException e) {
			System.out.println("Error: " + e.toString());
			System.out.println("Locations failed to load, critical error, sql: SELECT * FROM locationstats ORDER BY locid ASC");
		}
	}
	
	// TODO Join the player table and the item tables, so that it can grab everything.
	
	private ResultSet obtainAllLocations(Statement stmt) throws SQLException {
		String sql = ("SELECT * FROM locationstats ORDER BY locid ASC");
		return stmt.executeQuery(sql);
	}
	
	
	private void buildLocation(ResultSet currentLocation) throws SQLException {
		int id = currentLocation.getInt("LOCID");
		String name = currentLocation.getString("LOCNAME");
		String description = currentLocation.getString("LOCDES");
//			int inventory = currentLocation.getInt("LOCINV");		// TODO  Not implemented
//			GroundType g = GroundType.valueOf(currentLocation.getString("LOCTYPE"));		// TODO  Not implemented		
		LocationBuilder newLocation = new LocationBuilder();
		newLocation.setName(name);
		newLocation.setId(id);
		newLocation.setDescription(description);
		newLocation.north(currentLocation.getInt("LOCNORTH"), currentLocation.getString("LOCNORTHDIR"));
		newLocation.northEast(currentLocation.getInt("LOCNORTHEAST"), currentLocation.getString("LOCNORTHEASTDIR"));
		newLocation.east(currentLocation.getInt("LOCEAST"), currentLocation.getString("LOCEASTDIR"));
		newLocation.southEast(currentLocation.getInt("LOCSOUTHEAST"), currentLocation.getString("LOCSOUTHEASTDIR"));
		newLocation.south(currentLocation.getInt("LOCSOUTH"), currentLocation.getString("LOCSOUTHDIR"));
		newLocation.southWest(currentLocation.getInt("LOCSOUTHWEST"), currentLocation.getString("LOCSOUTHWESTDIR"));
		newLocation.west(currentLocation.getInt("LOCWEST"), currentLocation.getString("LOCWESTDIR"));
		newLocation.northWest(currentLocation.getInt("LOCNORTHWEST"), currentLocation.getString("LOCNORTHWESTDIR"));
		newLocation.up(currentLocation.getInt("LOCUP"), currentLocation.getString("LOCUPDIR"));
		newLocation.down(currentLocation.getInt("LOCDOWN"), currentLocation.getString("LOCDOWNDIR"));
		newLocation.in(currentLocation.getInt("LOCIN"), currentLocation.getString("LOCINDIR"));
		newLocation.out(currentLocation.getInt("LOCOUT"), currentLocation.getString("LOCOUTDIR"));
		newLocation.complete();		
		
		loadLocationItems(id, newLocation.getFinishedLocation());		
	}
	
	private void loadLocationItems(int id, Location newLocation) {
		String sql = "SELECT itemstats.*, equipableslottable.SLOTTYPE, itemtypetable.TYPE FROM itemstats"
				+ " LEFT JOIN EQUIPABLESLOTTABLE ON itemstats.ITEMID = equipableslottable.ITEMID"
				+ " LEFT JOIN ITEMTYPETABLE ON itemstats.ITEMID = itemtypetable.ITEMID"
				+ " LEFT JOIN LOCINV ON locinv.LOCID = " + id + ";";
		loadItems(sql, newLocation);
	}
	
	// Try using group by with a nested select for the repeatable items below. I think you'd then be able to convert it into a list that you'd just need to process.
	private  void loadItems(String sql, Container container) {
		if (sql == null) {
			throw new NullPointerException("Sql string may not be null.");
		}
		makeConnection(); // Will this break things if there are resultsets above?
		try {			
			Statement stmt = this.con.createStatement();
			ResultSet itemsList = stmt.executeQuery(sql);				
			ItemBuilder newItem = null;			
			while (itemsList.next()) { // one item at a time that is present in given container.
				
				// Below two ifs can be nested TODO
				if (isFirstViewOfItem(itemsList, newItem) && newItem != null) {
					if (newItem.complete()) {									
						StdItem finishedItem = newItem.getFinishedItem();
						finishedItem.getContainer().acceptItem(finishedItem);
					} else {
						System.out.println("Item failed to complete while loading from SQL: " + sql);
					}
				}
				if (isFirstViewOfItem(itemsList, newItem)) {
					newItem = new ItemBuilder(); // All defaults set already...
					// IF container is a mob { TODO
					//		IF MOBINV EQUIPEDSLOT is NOT 'NONE'
					// 				Equip this item instead of put in inventory	
					// 		Else :
							newItem.setItemContainer(container);
					setItemStats(itemsList, newItem);
					setRepeatableItemStats(itemsList, newItem);						
				} else {
					setRepeatableItemStats(itemsList, newItem);
				}
				
			}
			if (newItem.complete()) {									
				StdItem finishedItem = newItem.getFinishedItem();
				finishedItem.getContainer().acceptItem(finishedItem);
			} else {
				System.out.println("Item failed to complete while loading from SQL: " + sql);
			}
		} catch(SQLException e) {
			System.out.println("Load items failed via: " + sql);
			System.out.println("Error: " + e.toString());
		}		
	}
	
	private boolean isFirstViewOfItem(ResultSet rs, ItemBuilder newItem) throws SQLException {
		if (newItem == null) {
			return true;
		}
		return rs.getInt("ITEMID") != newItem.getId();
	}
	
	private void setItemStats(ResultSet itemInfo, ItemBuilder newItem) throws SQLException {
		newItem.setId(itemInfo.getInt("ITEMID"));
		newItem.setName(itemInfo.getString("ITEMNAME"));
		newItem.setPhysicalMult(itemInfo.getDouble("ITEMPHYS"));
		newItem.setBalanceMult(itemInfo.getDouble("ITEMBAL"));
		newItem.setDescription(itemInfo.getString("ITEMDESC"));
		newItem.setMaxDurability(itemInfo.getInt("ITEMMAXDUR"));
		newItem.setCurrentDurability(itemInfo.getInt("ITEMCURDUR"));
	}
	
	// TODO fix valueOf so that I don't have to do this stupid check
	private void setRepeatableItemStats(ResultSet itemInfo, ItemBuilder newItem) throws SQLException {
		String allowedEquipSlot = itemInfo.getString("SLOTTYPE");
		String itemType = itemInfo.getString("TYPE");
		if (allowedEquipSlot != null) {
			newItem.setAllowedSlots(EquipmentEnum.valueOf(itemInfo.getString("SLOTTYPE")));
		}
		if (itemType != null) {
			newItem.setTypes(Type.valueOf(itemInfo.getString("TYPE")));
		}
	}
	
	@Override
	public  Mobile loadPlayer(String name, String password) {
		if (name == null || password == null || !UsefulCommands.checkIfValidCharacters(name) || !UsefulCommands.checkIfValidCharacters(password)) {
			throw new NullPointerException("Name or password is invalid.");
		}
		makeConnection();
		Mobile finishedPlayer = null;
		MobileBuilder loadedPlayer = new MobileBuilder();
		try {
			stmt = this.con.createStatement();			
			String sql = ("SELECT * FROM mobstats WHERE MOBNAME='" + name + "' AND MOBPASS='" + password + "'");			
			ResultSet rs = stmt.executeQuery(sql);	
			if (rs.next()) {
				finishedPlayer = setMobStats(rs, loadedPlayer, finishedPlayer);
			} 	else {
				System.out.println("Player not found?");
				return null;
			}				
			setMobSkillBooks(rs, finishedPlayer);					
		} catch (SQLException e) {
			System.out.println("Error: " + e.toString());
		}
		return finishedPlayer;
	}
		
	private Mobile setMobStats(ResultSet rs, MobileBuilder loadedPlayer, Mobile finishedPlayer) throws SQLException {
		String type = rs.getString("MOBTYPE"); // NOT IMPLEMENTS TODO
		loadedPlayer.setId(rs.getInt("MOBID"));
		loadedPlayer.setName(rs.getString("MOBNAME"));
		switch (type) {
		case "STDMOB":
			loadedPlayer.setDescription(rs.getString("MOBDESC"));
			loadedPlayer.setPassword(rs.getString("MOBPASS"));
			loadedPlayer.setShortDescription(rs.getString("MOBSHORTD"));
			loadedPlayer.setLocation(WorldServer.gameState.viewLocations().get(rs.getInt("MOBLOC")));
		//	loadedPlayer.maxHp(rs.getInt("MOBMAXHP"));
			loadedPlayer.setCurrentHp(rs.getInt("MOBCURRENTHP"));
			loadedPlayer.setIsDead(rs.getInt("MOBDEAD"));
			loadedPlayer.setXpWorth(rs.getInt("MOBXPWORTH"));
			loadedPlayer.setExperience(rs.getInt("MOBCURRENTXP"));
			loadedPlayer.setLevel(rs.getInt("MOBCURRENTLEVEL"));
			loadedPlayer.setAge(rs.getInt("MOBAGE"));
			int startUp = rs.getInt("LOADONSTARTUP");					
			if (startUp == 1) {
				loadedPlayer.setLoadOnStartUp(true);
				loadedPlayer.complete();
				finishedPlayer = new AggresiveMobileDecorator(loadedPlayer.getFinishedMob());
			} else {
				loadedPlayer.setLoadOnStartUp(false);
				loadedPlayer.complete();
				finishedPlayer = loadedPlayer.getFinishedMob();
			}
			String sql = "SELECT itemstats.*, EQUIPABLESLOTTABLE.SLOTTYPE, itemtypetable.TYPE, MOBINV.EQUIPEDSLOT FROM itemstats"
					+ " LEFT JOIN EQUIPABLESLOTTABLE ON EQUIPABLESLOTTABLE.ITEMID = itemstats.ITEMID"
					+ " LEFT JOIN ITEMTYPETABLE ON itemstats.ITEMID = itemtypetable.ITEMID"
					+ " LEFT JOIN MOBINV ON mobinv.MOBID = " + finishedPlayer.getId() + ";";
			loadItems(sql, finishedPlayer);					
			WorldServer.gameState.addMob(finishedPlayer.getName() + finishedPlayer.getId(), finishedPlayer);
			finishedPlayer.getContainer().acceptItem(finishedPlayer);
		break;				
		}
		return finishedPlayer;	
	}
	
	private void setMobSkillBooks(ResultSet rs, Mobile finishedPlayer) throws SQLException {
		String sql = ("SELECT SKILLBOOKID, MOBPROGRESS FROM skillbooktable WHERE MOBID='" + finishedPlayer.getId() + "'");
		rs = stmt.executeQuery(sql);
		//Skillbook id --> skillbook progress for this particular mob
		Map<Integer, Integer> mobSkillBooks = new HashMap<Integer, Integer>();
		while (rs.next()) {
			mobSkillBooks.put(rs.getInt("SKILLBOOKID"), rs.getInt("MOBPROGRESS"));			
		}			
		for (int skillBookId : mobSkillBooks.keySet()) {
			if (!WorldServer.gameState.checkForBookId(skillBookId)) {
				System.out.println("Error, book not loaded.");
				throw new IllegalStateException("Critical error, skillbooks do not align.");				
			}
			finishedPlayer.addBook(WorldServer.gameState.getBook(skillBookId), mobSkillBooks.get(skillBookId));
		}	
	}	
	
	
	// TODO This seems very dangerous, should probably hide this under controllable actions, like saveMobile and saveLocation and saveItem. Accepting an interface like Saveable.
	@Override
	public void saveAction(String sql)  {
		System.out.println(sql);
		makeConnection();
		try {
			stmt = this.con.createStatement();		
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(sql);
		}	
	}
	
	@Override
	public  Object viewData(String blockQuery, String column) {
		makeConnection();
		Object result = null;
		try {
			stmt = this.con.createStatement();
			
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
		return result;
	}
	
	@Override
	public  HashMap<String, Object> returnBlockView(String blockQuery) {
		makeConnection();
		HashMap<String, Object> blockView = new HashMap<String, Object>();
		try {
			stmt = this.con.createStatement();
		
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
		return blockView;
	}
	
	/*
	 * Re-connects to the database using the settings saved from "connect()", which must have been run previously.
	 * <p>
	 * This connection does not auto-close, it is recommended that "disconnect()" is ran after each immediate use of the database connection.
	 */	
	@Override
	public  void makeConnection() {
		if (this.con == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				this.con = DriverManager.getConnection(url, username, String.valueOf(password));	
			} catch (Exception e) {
				System.out.println("Problem: " + e.toString());
			}
		}
	}
	
	@Override
	public  void disconnect() {	
		if (this.con != null) {
			try {
				this.con.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			this.con = null;
		}
	}

	@Override
	public  void loadMobs() {
		makeConnection();
		try {
			stmt = this.con.createStatement();			
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
	
	// Loads all skillbooks, then loaded mobs grab a pointer to already existing books.
	@Override
	public  void loadSkillBooks() {
		try {	
			String sql = ("SELECT skillbook.skillbookname, skillbook.skillbookid, skill.*, syntax.* FROM skillbook" +
					" LEFT JOIN skilltable ON skilltable.skillbookid = skillbook.skillbookid" +
					" LEFT JOIN skill ON skill.skillid = skilltable.skillid" +
					" LEFT JOIN syntaxtable ON syntaxtable.skillid = skill.skillid" +
					" LEFT JOIN syntax ON syntax.syntaxid = syntaxtable.syntaxid" +
					" ORDER BY skillbookid ASC, skillid ASC, SYNTAXPOS ASC;");
			ResultSet rsMain = stmt.executeQuery(sql);
			SkillBook tempBook = null;
			SkillBuilder tempSkill = null;
			while (rsMain.next()) {
				int currentBookId = Integer.parseInt(rsMain.getString("skillbookid"));
				do {					
					if (tempBook == null) {
						tempBook = new SkillBook(rsMain.getString("skillbookname"), currentBookId);
					}
					int currentSkillId = Integer.parseInt(rsMain.getString("skillid"));
					do {						
						if (tempSkill == null) {
							tempSkill = new SkillBuilder();
							tempSkill.setName(rsMain.getString("SKILLNAME"));		
//							tempSkill.setFailMsg(rsMain.getString("SKILLFAILMSG"));
							tempSkill.setDescription(rsMain.getString("SKILLDES"));
							tempSkill.setId(currentSkillId);
							
							String blockSQL = ("SELECT block.* FROM block" +
									" INNER JOIN blocktable on blocktable.blockid = block.blockid" +
									" WHERE blocktable.skillid = " + currentSkillId +
									" ORDER BY blockpos ASC;");
							Statement stmtTwo = this.con.createStatement();	
							ResultSet rsBlock = stmtTwo.executeQuery(blockSQL);
							while (rsBlock.next()) {
								tempSkill.addAction(determineAction(rsBlock));							
							}							
						}						
						tempSkill.addSyntax(Syntax.valueOf(rsMain.getString("syntaxtype")));						
						if (rsMain.next()) {
							currentSkillId = Integer.parseInt(rsMain.getString("skillid"));
						} else {
							currentSkillId = -1;
						}
					} while (currentSkillId == tempSkill.getId());	
					tempSkill.addBook(tempBook);  
					tempSkill.complete();	
					tempSkill = null;
					if (currentSkillId != -1) {
						currentBookId = Integer.parseInt(rsMain.getString("skillbookid"));
					} else {
						currentBookId = -1;
					}
				} while (currentBookId == tempBook.getId());	
				WorldServer.gameState.addBook(tempBook.getId(), tempBook.duplicate(tempBook));	
				tempBook = null;
			}	
		} catch (SQLException e) {
			System.out.println("Critical error loading skillbooks.");
			e.printStackTrace();
		}
	}	
	
	/**
	 * Takes a very specific ResultSet from "loadPlayer()" or recursively in order to exactly rebuild skills as they were designed.
	 * <p>
	 * As more skill blocks or actions are created the below must be updated so that they can interact with the database.
	 * @param rs ResultSet containing all the data needed to load any skill block, very specific.
	 * @return Returns the block or action requested, is rebuilt in terms of the game server from database stats.
	 */
	private  Action determineAction(ResultSet rs) {
		WhatFactory whatFactory = new WhatFactory();
		WhereFactory whereFactory = new WhereFactory();
		String blockType = null;
		try {
			blockType = rs.getString("BLOCKTYPE");
			switch (blockType) {				
				case "DAMAGE":
					return new Damage(rs.getInt("INTVALUE"), whatFactory.parse(rs.getString("TARGETWHO")), whereFactory.parse(rs.getString("TARGETWHERE")),
							checkBoolean(rs.getString("BOOLEANONE")), checkType(rs.getString("TYPE")));		
				
				case "BALANCECOST":
					return new BalanceEffect(rs.getInt("INTVALUE"), whatFactory.parse(rs.getString("TARGETWHO")), whereFactory.parse(rs.getString("TARGETWHERE")));
			
				case "BLEEDEFFECT":
					return new DOTEffect(rs.getInt("INTVALUE"), rs.getInt("INTVALUETWO"), whatFactory.parse(rs.getString("TARGETWHO")), whereFactory.parse(rs.getString("TARGETWHERE")));
				
				case "WEAPONEQUIPPEDCHECK":
					// rs.getString("SKILLTYPE") actually returns an integer that represents a pointer at a real skilltype
					// So probably need to do another query for the answer. Or add it to the resultset above
					return new WeaponEquippedCheck(Type.valueOf(rs.getString("TYPE")), whatFactory.parse(rs.getString("TARGETWHO")), whereFactory.parse(rs.getString("TARGETWHERE")));
				
				
				case "MESSAGE":
/*					Statement stmt3 = this.con.createStatement();
					ResultSet rs3 = stmt3.executeQuery("SELECT msgstrings.* FROM msgstringstable JOIN msgstrings ON msgstringstable.MSGSTRINGSID = msgstrings.MSGSTRINGSID "
							+ "WHERE msgstringstable.BLOCKID =" + rs.getInt("BLOCKID") + " ORDER BY MSGSTRINGSPOS ASC;");
					ArrayList<msgStrings> msgstringslist = new ArrayList<msgStrings>();
					while (rs3.next()) {
						msgstringslist.add(msgStrings.valueOf(rs3.getString("MSGSTRINGSTYPE")));
					}
					return new Message(rs.getString("STRINGONE"), whatFactory.parse(rs.getString("TARGETWHO")), whereFactory.parse(rs.getString("TARGETWHERE")), msgstringslist);
*/							
				case "CHANCE":
					makeConnection();
					Statement stmt2 = this.con.createStatement();
					ResultSet rs2 = stmt2.executeQuery("SELECT blockpointertable.blockpointer FROM blockpointertable WHERE BLOCKID=" + rs.getInt("BLOCKID") + ";");
					if (rs2.next()) {
						Action innerAction = determineAction(rs2);
						return new Chance(rs.getInt("INTVALUE"), innerAction);
					}
					return null;
					/*
					int internalActionNum = rs.getInt("BLOCKPOINTER");
					makeConnection();
					Statement stmt2 = this.con.createStatement();
					ResultSet rs2 = stmt2.executeQuery("SELECT * FROM block WHERE BLOCKID='" + internalActionNum + "'");
					
					if (rs2.next()) {
						Action innerAction = determineAction(rs2);
						return new Chance(rs.getInt("INTVALUE"), innerAction);
					}
					return null;			
				*/
			//	case "SAY":
			//		return new Say();
					
				case "EXAMINE":
					return new Examine(whereFactory.parse(rs.getString("TARGETWHERE")));
					
				case "OR":
					makeConnection();
					Statement stmtOr = this.con.createStatement();
					ResultSet rsOr = stmtOr.executeQuery("SELECT blockpointertable.blockpointer FROM blockpointertable WHERE BLOCKID=" + rs.getInt("BLOCKID") + ";");
					Action innerOrActionOne = null;
					Action innerOrActionTwo = null;
					if (rsOr.next()) {
						innerOrActionOne = determineAction(rsOr);
					}
					if (rsOr.next()) {
						innerOrActionTwo = determineAction(rsOr);
					}
					return new Or(innerOrActionOne, innerOrActionTwo);
					/*
					int internalActionNumOrOne = rs.getInt("BLOCKPOINTERONE");
					int internalActionNumOrTwo = rs.getInt("BLOCKPOINTERTWO");
					Statement stmtOR = this.con.createStatement();
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
					*/
			//	case "GET":
			//		return new Get(whatFactory.parse(rs.getString("TARGETWHO")), whereFactory.parse(rs.getString("TARGETWHERE")));
				case "LOOK":
					return new Look(whereFactory.parse(rs.getString("TARGETWHERE")));
					
		//		case "MOVE":
		//			return new Move(whatFactory.parse(rs.getString("TARGETWHO")), whereFactory.parse(rs.getString("TARGETWHERE")), whereFactory.parse(rs.getString("ENDWHERE")));
					
				case "BALANCECHECK":
					return new BalanceCheck(whatFactory.parse(rs.getString("TARGETWHO")), whereFactory.parse(rs.getString("TARGETWHERE")));
					
				case "MOVECHECK":
					return new MoveCheck(GroundType.valueOf(rs.getString("GROUNDTYPE")), whatFactory.parse(rs.getString("TARGETWHO")), whereFactory.parse(rs.getString("TARGETWHERE")), whereFactory.parse(rs.getString("ENDWHERE")));
					
			//	case "DROP":
			//		return new Drop(whatFactory.parse(rs.getString("TARGETWHO")), whereFactory.parse(rs.getString("TARGETWHERE")), whereFactory.parse(rs.getString("ENDWHERE")));
					
				case "EQUIPCHANGE":
					return new EquipChange(whatFactory.parse(rs.getString("TARGETWHO")), whereFactory.parse(rs.getString("TARGETWHERE")), checkBoolean(rs.getString("BOOLEANONE")));
				
				case "GODCREATE":
					return new Godcreate();
				
				default:
					System.out.println("Determine Action could not find appropriate case, failed: " + rs.getString("BLOCKTYPE"));
					return null;
			}
		} catch (SQLException e) {
			System.out.println("Critical error loading block: " + blockType);
			e.printStackTrace();
			return null;
		}
	}
	
	private  boolean checkBoolean(String ifBoolean) {
		if (ifBoolean != null && (ifBoolean.equalsIgnoreCase("true") || ifBoolean.equalsIgnoreCase("false"))) {
		    return Boolean.valueOf(ifBoolean);
		} else {
		    System.out.println(ifBoolean + " is being outputed from some skill, fails to become a true boolean and autos to false");
		    return false;
		}
	}
	
	private  Type checkType(String possibleType) {
		Type newType;
		try {
			newType = Type.valueOf(possibleType);
		} catch(Exception e) {
			e.printStackTrace();
			newType = Type.NULL;
		}
		return newType;
	}
	
	@Override
	public void increaseSequencer() {
		makeConnection();
		try {
			stmt = this.con.createStatement();			
			increaseSequenceTable(stmt);
		} catch (SQLException e) {
			System.out.println("Error: " + e.toString());
		}
	}	
	
	private void increaseSequenceTable(Statement stmt) throws SQLException {
		String sql = ("insert into sequencetable values(NULL);");
		for (int i = 0; i < 25; i++) {
			stmt.execute(sql);
		}
	}
}