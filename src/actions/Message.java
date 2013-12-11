package actions;

import java.util.ArrayList;

import processes.Player;

import skills.Arcane.Skill;
import interfaces.Action;
import interfaces.Mobile;
import interfaces.Action.Where;
import interfaces.Action.Who;

public class Message implements Action {
	
	private final String msg;
//	private final MessageType type;
	private final Who who;
	private final Where where;
//	private final ArrayList<Who> targetNames;
	private final Who[] whoList;

	public Message(String m, Where where, Who who, Who... whoList) {
		this.msg = m;
		this.who = who;
		this.where = where;
		this.whoList= whoList;
//		this.targetNames = targetNames;
//		this.type = type;
	}
	
	@Override
	public boolean activate(Skill s) {
		ArrayList<String> tNames = new ArrayList<String>();
		for (Who w : whoList) {
			for (Mobile m : w.findTarget(s, where.findLoc(s))) {
				tNames.add(m.getName());
			}
		}
		ArrayList<Mobile> targs = who.findTarget(s, where.findLoc(s));
		if (targs != null) {
			for (Mobile m : who.findTarget(s, where.findLoc(s))) {
				if (m != null) {
					m.tell(String.format(msg, tNames.toArray()));
				}
			}
		}
		return true;
	}
}
