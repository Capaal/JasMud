package processes;

import interfaces.Mobile;
import interfaces.Action.Where;
import interfaces.Action.Who;

import java.sql.*;
import java.util.ArrayList;

import actions.Damage;
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
					// description?
					
					sql = ("SELECT syntax.* FROM syntaxtable JOIN syntax ON syntaxtable.SYNTAXID = syntax.SYNTAXID"
							+ " WHERE syntaxtable.SKILLID = '" + skillId + "' ORDER BY SYNTAXPOS ASC");
					rs = stmt.executeQuery(sql);
					ArrayList<Syntax> skillSyntax = new ArrayList<Syntax>();
					while (rs.next()) {
						skillSyntax.add(Syntax.valueOf(rs.getString("SYNTAXTYPE")));
					}
				//	skillBuild.setSyntax(Syntax.SKILL, Syntax.TARGET);
				}
				
				sql = ("SELECT block.* FROM blocktable JOIN block ON blocktable.BLOCKID = block.BLOCKID "
						+ "WHERE blocktable.SKILLID = '" + skillId + "' ORDER BY BLOCKPOS");
				rs = stmt.executeQuery(sql);							
				
				while (rs.next()) {				
					
					switch (rs.getString("BLOCKTYPE")) {				
					case "DAMAGE":
						skillBuild.addAction(new Damage(rs.getInt("INTVALUE"), Who.valueOf(rs.getString("TARGETWHO")), Where.valueOf(rs.getString("TARGETWHERE"))));
					break;
					}
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
}