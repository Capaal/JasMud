package actions;

import interfaces.Action;
import interfaces.Holdable;
import interfaces.Mobile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import TargettingStrategies.*;
import processes.Location.Direction;
import processes.SQLInterface;
import processes.Skill;
import processes.Skill.Syntax;

// Does not seem to be safe from developer mistakes when making new skills.
// At the moment it can only accept directions to subsitute %s with names, not anything else like "north".
public class Message extends Action {
	
	private final String msg;
	private final WhatTargettingStrategy what;
	private final WhereTargettingStrategy where;
//	private final ArrayList<Who> targetNames;
	private final ArrayList<msgStrings> msgList;
	
	public Message() {
		this("", new TargetSelfWhatStrategy(), new TargetHereWhereStrategy(), new ArrayList<msgStrings>());
	}

	public Message(String m, WhatTargettingStrategy what, WhereTargettingStrategy where, ArrayList<msgStrings> msgstringslist) {
		this.msg = m;
		this.what = what;
		this.where = where;
		this.msgList = msgstringslist;
	}
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		ArrayList<String> tNames = new ArrayList<String>();
		for (msgStrings msg : msgList) {
			tNames.add(msg.getString(s, fullCommand, currentPlayer));
		}
		List<Holdable> targs = what.findWhat(s, fullCommand, currentPlayer, where.findWhere(s, fullCommand, currentPlayer));
		for (Holdable m : targs) {
			if (m != null && m instanceof Mobile && ((Mobile)m).isControlled()) {
				System.out.println(msg);
				System.out.println(tNames);
				((Mobile) m).tell(String.format(msg, tNames.toArray()));
			}
		}		
		return true;
	}
	
	@Override
	public boolean save(int position) {	
		if (!super.save(position)) {
			return false;
		}		
		int msgStringsCount = 1;
		for (msgStrings ms : msgList) {
			String msgStringsInsert = "INSERT IGNORE INTO MSGSTRINGS (MSGSTRINGSPOS, MSGSTRINGSTYPE) values (" + msgStringsCount + ", '" + ms.toString() + "');"; 
			try {
				SQLInterface.saveAction(msgStringsInsert);				
			} catch (SQLException e) {
				System.out.println("Msgstrings failed to save via sql : " + msgStringsInsert);
				e.printStackTrace();
				return false;				
			}
			String msgStringsTableInsert = "INSERT IGNORE INTO msgstringstable (BLOCKID, MSGSTRINGSID) values (" + id + ", "
					+ selectMsgStringsId(msgStringsCount, ms.toString()) + ");";
			try {
				SQLInterface.saveAction(msgStringsTableInsert);
			} catch (SQLException e) {
				System.out.println("Msgstringstable failed to save via sql : " + msgStringsTableInsert);
				e.printStackTrace();
				return false;
			}		
			msgStringsCount ++;
		}				
		return true;			
	}
	
	private int selectMsgStringsId(int pos, String type) {
		String blockQuery = "SELECT * FROM MSGSTRINGS WHERE MSGSTRINGSPOS=" + pos + " AND MSGSTRINGSTYPE='" + type.toString() + "';"; 
		HashMap<String, Object> blockView = SQLInterface.returnBlockView(blockQuery);
		return (int) blockView.get("MSGSTRINGSID");
	}
	@Override
	public Action newBlock(Mobile player) {
		WhatTargettingFactory whatFactory = new WhatTargettingFactory();
		WhereTargettingFactory whereFactory = new WhereTargettingFactory();
		WhatTargettingStrategy newWhat = what;
		WhereTargettingStrategy newWhere = where;
		ArrayList<msgStrings> newMsgList = msgList;
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
				newMsgList.add(msgStrings.valueOf(Godcreate.askQuestion("What syntax goes into %s number " + i + "?", player).toUpperCase()));
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
		return new Message(newMsg, newWhat, newWhere, newMsgList);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='MESSAGE' AND BLOCKPOS=" + position + " AND STRINGONE='" + msg
				+ "' AND TARGETWHO='" + what.toString() + "' AND TARGETWHERE='" + where.toString() + "';"; 
		return SQLInterface.returnBlockView(blockQuery);
	}
	@Override
	protected void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) VALUES ('MESSAGE', " 
					+ position + ", '" +  msg + "', '" + what.toString() + "', '" + where.toString() + "');";
			try {
				SQLInterface.saveAction(sql);
			} catch (SQLException e) {
				System.out.println("Message failed to save via sql : " + sql);
				e.printStackTrace();
			}
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Displays messages to whomever you set it up to display to.");
		StringBuilder sb = new StringBuilder();
		sb.append("msgStrings:");
		for (msgStrings ms : msgStrings.values()) {
			sb.append(" ");
			sb.append(ms.toString());
		}
		player.tell(sb.toString());
		player.tell("Each msgString can be used to convert a %s in your message into the necessary word.");
		player.tell("Message: " + msg);
		sb = new StringBuilder();
		sb.append("Message Strings:");
		for (msgStrings msgS : msgList) {
			sb.append(" ");
			sb.append(msgS.toString());
		}
		player.tell(sb.toString());
		player.tell("What: " + what.toString() + " Where: " + where.toString());		
	}
	
	public enum msgStrings {
		
		SELF() {
			@Override
			public String getString(Skill s, String fullCommand, Mobile currentPlayer) {
				return currentPlayer.getName();
			}
			
		},
		
		TARGET() {
			@Override
			public String getString(Skill s, String fullCommand, Mobile currentPlayer) {
				return s.getStringInfo(Syntax.TARGET, fullCommand);
			}
		},
		
		MOVE() {
			@Override
			public String getString(Skill s, String fullCommand, Mobile currentPlayer) {
				return s.getStringInfo(Syntax.DIRECTION, fullCommand);
			}
		},
		
		OPPMOVE() {
			@Override
			public String getString(Skill s, String fullCommand, Mobile currentPlayer) {
				Direction opp = Direction.valueOf((s.getStringInfo(Syntax.DIRECTION, fullCommand)).toUpperCase());
				return opp.getOpp();
			}
		},
		
		ITEM() {
			@Override
			public String getString(Skill s, String fullCommand, Mobile currentPlayer) {
				return s.getStringInfo(Syntax.ITEM, fullCommand);
			}
		};
		
		private msgStrings(){}
		public abstract String getString(Skill s, String fullCommand, Mobile currentPlayer);
	}
}
