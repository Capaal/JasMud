package actions;

import interfaces.Action;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import processes.Skill.Syntax;
import TargettingStrategies.*;
import processes.Location.Direction;
import processes.Skill;
import processes.WorldServer;

// Used to display messages to controlled mobs. What/Where strategies obtains who should see the message, msg and syntaxList controls what is displayed.
public class Message extends Action {
	
	private final String msg;
	private final WhatStrategyInterface what;
	private final WhereStrategyInterface where;
	private final ArrayList<Syntax> syntaxList;
	
	public Message() {
		this("", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>());
	}
	/*
	 *  Typical construction: 
	 *  skillBuilder.addAction(new Message("%s punches %s.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.SELF, Syntax.TARGET))));
	 *  Would display the message: "(your name) punches (target's name)."
	 *  The Syntax's list ORDER matters, each syntax (in order) replaces each %s.
	 */
	public Message(String m, WhatStrategyInterface what, WhereStrategyInterface where, ArrayList<Syntax> syntaxList) {
		this.msg = m;
		this.what = what;
		this.where = where;
		this.syntaxList = syntaxList;
	}
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		ArrayList<String> substituteWords = new ArrayList<String>();
		for (Syntax msg : syntaxList) {
			if (msg.equals(Syntax.SELF)) { // Special Syntax to display currentPlayer's name.
				substituteWords.add(currentPlayer.getName());
			} else {
				substituteWords.add(s.getStringInfo(msg, fullCommand));
			}			
		}		
		List<Holdable> targs = what.findWhat(s, fullCommand, currentPlayer, where.findWhere(s, fullCommand, currentPlayer));
		for (Holdable m : targs) {
			if (m != null && m instanceof Mobile && ((Mobile)m).isControlled()) {
				System.out.println(msg); // Test Line
				System.out.println(substituteWords); // Test Line
				((Mobile) m).tell(String.format(msg, substituteWords.toArray()));
			}
		}		
		return true;
	}
	
	@Override
	public boolean save(int position) {	
/*		if (!super.save(position)) {
			return false;
		}		
		int msgStringsCount = 1;
		for (msgStrings ms : msgList) {
			String msgStringsInsert = "INSERT IGNORE INTO MSGSTRINGS (MSGSTRINGSPOS, MSGSTRINGSTYPE) values (" + msgStringsCount + ", '" + ms.toString() + "');"; 
			WorldServer.databaseInterface.saveAction(msgStringsInsert);
			String msgStringsTableInsert = "INSERT IGNORE INTO msgstringstable (BLOCKID, MSGSTRINGSID) values (" + id + ", "
					+ selectMsgStringsId(msgStringsCount, ms.toString()) + ");";
			WorldServer.databaseInterface.saveAction(msgStringsTableInsert);		
			msgStringsCount ++;
		}				*/
		return true;		
	}
	
	private int selectMsgStringsId(int pos, String type) {
		String blockQuery = "SELECT * FROM MSGSTRINGS WHERE MSGSTRINGSPOS=" + pos + " AND MSGSTRINGSTYPE='" + type.toString() + "';"; 
		HashMap<String, Object> blockView = WorldServer.databaseInterface.returnBlockView(blockQuery);
		return (int) blockView.get("MSGSTRINGSID");
	}
	@Override
	public Action newBlock(Mobile player) {
		WhatFactory whatFactory = new WhatFactory();
		WhereFactory whereFactory = new WhereFactory();
		WhatStrategyInterface newWhat = what;
		WhereStrategyInterface newWhere = where;
	//	ArrayList<msgStrings> newMsgList = msgList;
		String newMsg = Godcreate.askQuestion("What message will be displayed? Remember to use %s to replace strings you don't know yet.", player);
		int numMsgStrings = 0;
		try {
			numMsgStrings = Integer.parseInt(Godcreate.askQuestion("How many %s were in that message?", player));
		} catch (NumberFormatException e) {
			player.tell("You must indicate a valid number");
			return this.newBlock(player);
		}
		for (int i = 1; i <= numMsgStrings; i++) {
			try {
	//			newMsgList.add(msgStrings.valueOf(Godcreate.askQuestion("What syntax goes into %s number " + i + "?", player).toUpperCase()));
			} catch (IllegalArgumentException e) {
				player.tell("That last one wasn't a valid syntax.");
				i--;
			}
		}
		try {
			newWhat = whatFactory.parse((Godcreate.askQuestion("Who do you want to see this message? (this is using Syntax).", player)).toUpperCase());
			newWhere = whereFactory.parse((Godcreate.askQuestion("Where will these people be? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
//		return new Message(newMsg, newWhat, newWhere, newMsgList);
		return null;
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='MESSAGE' AND BLOCKPOS=" + position + " AND STRINGONE='" + msg
				+ "' AND TARGETWHO='" + what.toString() + "' AND TARGETWHERE='" + where.toString() + "';"; 
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}
	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) VALUES ('MESSAGE', " 
					+ position + ", '" +  msg + "', '" + what.toString() + "', '" + where.toString() + "');";
			WorldServer.databaseInterface.saveAction(sql);
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Displays messages to whomever you set it up to display to.");
		StringBuilder sb = new StringBuilder();
		sb.append("msgStrings:");
//		for (msgStrings ms : msgStrings.values()) {
//			sb.append(" ");
//			sb.append(ms.toString());
//		}
		player.tell(sb.toString());
		player.tell("Each msgString can be used to convert a %s in your message into the necessary word.");
		player.tell("Message: " + msg);
		sb = new StringBuilder();
		sb.append("Message Strings:");
//		for (msgStrings msgS : msgList) {
//			sb.append(" ");
//			sb.append(msgS.toString());
//		}
		player.tell(sb.toString());
		player.tell("What: " + what.toString() + " Where: " + where.toString());		
	}	
}
