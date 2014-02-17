package actions;

import java.sql.SQLException;
import java.util.HashMap;

import processes.SQLInterface;
import processes.Skill;
import processes.StdMob;
import interfaces.Action;
import interfaces.Mobile;

public class Godcreate extends Action {
	
	public Godcreate() {
		
	}

	@Override
	public boolean activate(Skill s) {
		Mobile player = s.getCurrentPlayer();
		player.tell("Welcome to the creation Menu.");
		player.tell("Here you can create an enormous variety of skills, items, locations, and mobiles");
		player.tell("But remember that this is an out of game menu and to exit you must type \"exit\".");
		return processCreateType(player);
	}
	
	private boolean processCreateType(Mobile player) {
		player.tell("What do you want to do?: 1:Skills  2:Items  3:Locations  4:Mobiles OR exit.");
		String selection = player.getSendBack().getMessage();
		switch(selection) {
		case "1":
			return processCreateSkill(player);
		
		case "2":
			return processCreateItem(player);
			
		case "3":
			return processCreateLocation(player);
			
		case "4":
			return processCreateMobile(player);
			
		case "exit":
			return false;
		
		default:
			player.tell("That isn't one of the options.");
			return processCreateType(player);
		}
	}
	
	private boolean processCreateSkill(Mobile player) {
		return false;
	}
	
	private boolean processCreateItem(Mobile player) {
		return false;
	}
	
	private boolean processCreateLocation(Mobile player) {
		return false;
	}
	
	private boolean processCreateMobile(Mobile player) {
		return false;
	}
	
	private String askQuestion(String question, Mobile player) {
		player.tell(question);
		return player.getSendBack().getMessage();
	}
		
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='GODCREATE' AND BLOCKPOS=" + position + ";";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO BLOCK (BLOCKTYPE, BLOCKPOS) VALUES ('GODCREATE', " + position + ",);";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("Godcreate failed to save via sql : " + sql);
			e.printStackTrace();
		}
	}

}
