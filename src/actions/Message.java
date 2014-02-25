package actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import processes.SQLInterface;
import processes.Skill;
import processes.Skill.Syntax;
import interfaces.*;
import interfaces.Action.Where;
import interfaces.Action.Who;

// Does not seem to be safe from developer mistakes when making new skills.
// At the moment it can only accept directions to subsitute %s with names, not anything else like "north".
public class Message extends Action {
	
	private final String msg;
	private final Who who;
	private final Where where;
//	private final ArrayList<Who> targetNames;
	private final ArrayList<msgStrings> msgList;
	
	public Message() {
		this("", Who.SELF, Where.HERE, new ArrayList<msgStrings>());
	}

	public Message(String m, Who who, Where where, ArrayList<msgStrings> msgstringslist) {
		this.msg = m;
		this.who = who;
		this.where = where;
		this.msgList = msgstringslist;
	}
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		ArrayList<String> tNames = new ArrayList<String>();
		for (msgStrings msg : msgList) {
			tNames.add(msg.getString(s, fullCommand, currentPlayer));
		}
		ArrayList<Mobile> targs = who.findTarget(s, fullCommand, currentPlayer, where.findLoc(s, fullCommand, currentPlayer));
		if (targs != null) {
			for (Mobile m : targs) {
				if (m != null && m.isControlled()) {
					System.out.println(msg);
					System.out.println(tNames);
					m.tell(String.format(msg, tNames.toArray()));
				}
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
		Who newWho = who;
		Where newWhere = where;
		ArrayList<msgStrings> newMsgList = msgList;
		String newMsg = Godcreate.askQuestion("What message will be displayed? Remember to use %s to replace strings you don't know yet.", player);
		int numMsgStrings = 0;
		try {
			numMsgStrings = Integer.parseInt(Godcreate.askQuestion("How many %s were in that message?", player));
		} catch (NumberFormatException e) {
			player.tell("You must indicate a valid number");
			return this.newBlock(player);
		}
		for (int i = 0; i < numMsgStrings; i++) {
			try {
				newMsgList.add(msgStrings.valueOf(Godcreate.askQuestion("What syntax goes into %s number " + i + "?", player)));
			} catch (IllegalArgumentException e) {
				player.tell("That last one wasn't a valid syntax.");
				i--;
			}
		}
		try {
			newWho = Who.valueOf((Godcreate.askQuestion("Who do you want to see this message? (this is using Syntax).", player)).toUpperCase());
			newWhere = Where.valueOf((Godcreate.askQuestion("Where will these people be? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new Message(newMsg, newWho, newWhere, newMsgList);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='MESSAGE' AND BLOCKPOS=" + position + " AND STRINGONE='" + msg
				+ "' AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';"; 
		return SQLInterface.returnBlockView(blockQuery);
	}
	@Override
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) VALUES ('MESSAGE', " 
				+ position + ", '" +  msg + "', '" + who.toString() + "', '" + where.toString() + "');";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("Message failed to save via sql : " + sql);
			e.printStackTrace();
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
		player.tell("Who: " + who.toString() + " Where: " + where.toString());		
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
				oppDirections opp = oppDirections.valueOf((s.getStringInfo(Syntax.DIRECTION, fullCommand)).toUpperCase());
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
	
	//Maybe this should be in MOVE instead? SO far this is the only place using these as opposites, which is why it is here.
	private enum oppDirections {
		
		NORTH() {
			@Override
			protected String getOpp() {
				return "south";
			}
		},
		
		NORTHEAST() {
			@Override
			protected String getOpp() {
				return "southwest";
			}
		},
		
		EAST() {
			@Override
			protected String getOpp() {
				return "west";
			}
		},
		
		SOUTHEAST() {
			@Override
			protected String getOpp() {
				return "northwest";
			}
		},
		
		SOUTH() {
			@Override
			protected String getOpp() {
				return "north";
			}
		},
		
		SOUTHWEST() {
			@Override
			protected String getOpp() {
				return "northeast";
			}
		},
		
		WEST() {
			@Override
			protected String getOpp() {
				return "east";
			}
		},
		
		NORTHWEST() {
			@Override
			protected String getOpp() {
				return "southeast";
			}
		},
		
		UP() {
			@Override
			protected String getOpp() {
				return "down";
			}
		},
		
		DOWN() {
			@Override
			protected String getOpp() {
				return "up";
			}
		},
		
		IN() {
			@Override
			protected String getOpp() {
				return "out";
			}
		},
		
		OUT() {
			@Override
			protected String getOpp() {
				return "in";
			}
		};
		
		private oppDirections() {}
		
		protected abstract String getOpp();
		
	}
}
