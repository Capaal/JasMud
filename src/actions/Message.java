package actions;

import java.util.ArrayList;

import processes.Skill;
import processes.Skill.Syntax;
import processes.UsefulCommands;
import interfaces.*;


// Does not seem to be safe from developer mistakes when making new skills.
// At the moment it can only accept directions to subsitute %s with names, not anything else like "north".
public class Message implements Action {
	
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
				if (m != null) {
					m.tell(String.format(msg, tNames.toArray()));
				}
			}
		}
		return true;
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
