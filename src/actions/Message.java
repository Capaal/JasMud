package actions;

import java.util.ArrayList;

import processes.UsefulCommands;
import skills.Arcane.Skill;
import interfaces.*;


// Does not seem to be safe from developer mistakes when making new skills.
// At the moment it can only accept directions to subsitute %s with names, not anything else like "north".
public class Message implements Action {
	
	private final String msg;
	private final Who who;
	private final Where where;
//	private final ArrayList<Who> targetNames;
	private final msgStrings[] msgList;

	public Message(String m, Who who, Where where, msgStrings... msgList) {
		this.msg = m;
		this.who = who;
		this.where = where;
		this.msgList = msgList;
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
				return UsefulCommands.getSecondWord(s.getFullCommand());
			}
		},
		
		MOVE() {
			@Override
			public String getString(Skill s) {
				return UsefulCommands.getSecondWord(s.getFullCommand());
			}
		},
		
		OPPMOVE() {
			@Override
			public String getString(Skill s) {
				return "";
			}
		};
		
		private msgStrings(){}
		public abstract String getString(Skill s);
	}
}
