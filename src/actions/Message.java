package actions;

import java.util.ArrayList;
import java.util.HashMap;

import processes.SQLInterface;
import processes.Skill;
import processes.Skill.Syntax;
import processes.UsefulCommands;
import interfaces.*;

// Does not seem to be safe from developer mistakes when making new skills.
// At the moment it can only accept directions to subsitute %s with names, not anything else like "north".
public class Message extends Action {
	
	private final String msg;
	private final Who who;
	private final Where where;
//	private final ArrayList<Who> targetNames;
	private final ArrayList<msgStrings> msgList;

	public Message(String m, Who who, Where where, ArrayList<msgStrings> msgstringslist) {
		this.msg = m;
		this.who = who;
		this.where = where;
		this.msgList = msgstringslist;
	}
	
	@Override
	public boolean activate(Skill s) {
		ArrayList<String> tNames = new ArrayList<String>();
		for (msgStrings msg : msgList) {
			tNames.add(msg.getString(s));
		}
		ArrayList<Mobile> targs = who.findTarget(s, where.findLoc(s));
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
			if (!SQLInterface.saveAction(msgStringsInsert)) {
				return false;
			}
			String msgStringsTableInsert = "INSERT IGNORE INTO msgstringstable (BLOCKID, MSGSTRINGSID) values (" + id + ", "
					+ selectMsgStringsId(msgStringsCount, ms.toString()) + ");";
			if (!SQLInterface.saveAction(msgStringsTableInsert)) {
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
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='MESSAGE' AND BLOCKPOS=" + position + " AND STRINGONE='" + msg
				+ "' AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';"; 
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) VALUES ('MESSAGE', " 
				+ position + ", '" +  msg + "', '" + who.toString() + "', '" + where.toString() + "');";
		SQLInterface.saveAction(sql);
	}
	
	public enum msgStrings {
		
		SELF() {
			@Override
			public String getString(Skill s) {
				return s.getCurrentPlayer().getName();
			}
			
		},
		
		TARGET() {
			@Override
			public String getString(Skill s) {
				return s.getStringInfo(Syntax.TARGET);
			}
		},
		
		MOVE() {
			@Override
			public String getString(Skill s) {
				return s.getStringInfo(Syntax.DIRECTION);
			}
		},
		
		OPPMOVE() {
			@Override
			public String getString(Skill s) {
				oppDirections opp = oppDirections.valueOf((s.getStringInfo(Syntax.DIRECTION)).toUpperCase());
				return opp.getOpp();
			}
		},
		
		ITEM() {
			@Override
			public String getString(Skill s) {
				return s.getStringInfo(Syntax.ITEM);
			}
		};
		
		private msgStrings(){}
		public abstract String getString(Skill s);
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
