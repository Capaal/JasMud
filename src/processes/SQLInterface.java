package processes;

import interfaces.Action;
import interfaces.Mobile;
import interfaces.Action.Where;
import interfaces.Action.Who;

import java.sql.*;
import java.util.ArrayList;

import checks.*;
import costs.*;
import effectors.*;
import actions.*;
import actions.Message.msgStrings;
import processes.Location.Builder;
import processes.Location.GroundType;
import processes.Skill.Syntax;

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
	
	public static Mobile loadPlayer(String name, String password) {
		makeConnection();
		Mobile loadedPlayer = null;
		int mobid = -1;
		try {
			stmt = con.createStatement();
			
			String sql = ("SELECT * FROM mobstats WHERE MOBNAME='" + name + "' AND MOBPASS='" + password + "'");
			
			ResultSet rs = stmt.executeQuery(sql);
			
			if (rs.next()) {
				String type = rs.getString("MOBTYPE");
				mobid = rs.getInt("MOBID");
				switch (type) {
				case "STDMOB":
					loadedPlayer = new StdMob.Builder(rs.getInt("MOBID"), rs.getString("MOBNAME")).description(rs.getString("MOBDESC"))
						.shortDescription(rs.getString("MOBSHORTD")).location(WorldServer.locationCollection.get(rs.getInt("MOBLOC"))).build();
					WorldServer.mobList.put(name.toLowerCase(), loadedPlayer);
					loadedPlayer.getContainer().acceptItem(loadedPlayer);
				break;				
				}
			} 
		
			SkillBook skillBook = new SkillBook();
			SkillBuilder skillBuild = new SkillBuilder();		
			
			sql = ("SELECT MOBID, SKILLID FROM skilltable WHERE MOBID='" + mobid + "'");
			rs = stmt.executeQuery(sql);
			ArrayList<Integer> mobSkills = new ArrayList<Integer>();
			while (rs.next()) {
				mobSkills.add(rs.getInt("SKILLID"));
			}
			for (int skillId : mobSkills) {
				
				sql = ("SELECT * FROM skill WHERE SKILLID = '" + skillId + "'");
				rs = stmt.executeQuery(sql);
				
				if (rs.next()) {
					
				
					skillBuild.setup(loadedPlayer, rs.getString("SKILLNAME"));		
					skillBuild.setFailMsg(rs.getString("SKILLFAILMSG"));
					skillBuild.setDescription(rs.getString("SKILLDES"));
					// description?
					
					sql = ("SELECT syntax.* FROM syntaxtable JOIN syntax ON syntaxtable.SYNTAXID = syntax.SYNTAXID"
							+ " WHERE syntaxtable.SKILLID = '" + skillId + "' ORDER BY SYNTAXPOS ASC");
					rs = stmt.executeQuery(sql);
					if (!rs.isBeforeFirst() ) {    
						 System.out.println("No data for syntax"); 
					} 
					ArrayList<Syntax> skillSyntax = new ArrayList<Syntax>();
					while (rs.next()) {
						
						skillSyntax.add(Syntax.valueOf(rs.getString("SYNTAXTYPE")));
					}
					skillBuild.setSyntax(skillSyntax);
					
					
					sql = ("SELECT skilltype.* FROM skilltypetable JOIN skilltype ON skilltypetable.SKILLTYPEID = skilltype.SKILLTYPEID"
							+ " WHERE skilltypetable.SKILLID = '" + skillId + "'");
					rs = stmt.executeQuery(sql);
					if (!rs.isBeforeFirst() ) {    
						 System.out.println("No data for skilltable for skill: " + skillId); 
					} 
					ArrayList<Type> skillType = new ArrayList<Type>();
					while (rs.next()) {
						
						skillType.add(Type.valueOf(rs.getString("SKILLTYPE")));
					}
					skillBuild.setType(skillType);					
					
					
				}
				sql = ("SELECT block.*, skilltype.skilltype FROM blocktable JOIN block ON blocktable.BLOCKID = block.BLOCKID"
						+ " LEFT JOIN skilltype ON block.SKILLTYPEID = skilltype.SKILLTYPEID"
						+ " WHERE blocktable.SKILLID = '" + skillId + "' ORDER BY BLOCKPOS ASC");
		//		sql = ("SELECT block.* FROM blocktable JOIN block ON blocktable.BLOCKID = block.BLOCKID "
		//				+ "WHERE blocktable.SKILLID = '" + skillId + "' ORDER BY BLOCKPOS ASC");
				rs = stmt.executeQuery(sql);							
				
				while (rs.next()) {						
					skillBuild.addAction(determineAction(rs));					
				}
				skillBuild.complete(skillBook);	
			}
			
			loadedPlayer.addBook("skillbook", skillBook);	
			
		} catch (SQLException e) {
			System.out.println("Error: " + e.toString());
		}
		disconnect();
		return loadedPlayer;
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
				return new WeaponEquippedCheck(Type.valueOf(rs.getString("SKILLTYPE")), Who.valueOf(rs.getString("TARGETWHO")), Where.valueOf(rs.getString("TARGETWHERE")));
			
			
			case "MESSAGE":
				Statement stmt3 = con.createStatement();
				ResultSet rs3 = stmt3.executeQuery("SELECT msgstrings.* FROM msgstringstable JOIN msgstrings ON msgstringstable.MSGSTRINGSID = msgstrings.MSGSTRINGSID "
						+ "WHERE msgstringstable.BLOCKID = '" + rs.getInt("BLOCKID") + "' ORDER BY MSGSTRINGSPOS ASC");
				ArrayList<msgStrings> msgstringslist = new ArrayList<msgStrings>();
				while (rs3.next()) {
					msgstringslist.add(msgStrings.valueOf(rs3.getString("MSGSTRINGSTYPE")));
				}
				return new Message(rs.getString("STRINGONE"), Who.valueOf(rs.getString("TARGETWHO")), Where.valueOf(rs.getString("TARGETWHERE")), msgstringslist);
						
			case "CHANCE":
				int internalActionNum = rs.getInt("BLOCKPOINTER");
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery("SELECT * FROM block WHERE BLOCKID='" + internalActionNum + "'");
				
				if (rs2.next()) {
					Action innerAction = determineAction(rs2);
					return new Chance(rs.getInt("INTVALUE"), innerAction);
				}
				return null;			
			
			default:
				System.out.println("Determine Action could not find appropriate case, failed.");
				return null;
		}
	}
	
	private static boolean checkBoolean(String ifBoolean) {
		if (ifBoolean.equalsIgnoreCase("true") || ifBoolean.equalsIgnoreCase("false")) {
		    return Boolean.valueOf(ifBoolean);
		} else {
		    System.out.println(ifBoolean + " is being outputed from some skill, fails to become a true boolean an autos to false");
		    return false;
		}
	}
	
	
	
}