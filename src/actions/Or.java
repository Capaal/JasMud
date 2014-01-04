package actions;

import processes.Skill;
import interfaces.Action;

// Using OR at the moment makes fail messages not work right. You get a fail message but still succeed on a different action.
public class Or implements Action {
	
	private final Action[] actions;
	
	public Or(Action... actions) {
		this.actions = actions;
	}

	@Override
	public boolean activate(Skill s) {
		for (Action a : actions) {
			if (a.activate(s)) {
				return true;
			}
		}
		return false;
	}
}
