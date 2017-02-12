package Quests;

import interfaces.Holdable;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import processes.Location;

public abstract class Quest {
	
	private static Map<String, Quest> allQuests = new HashMap<String, Quest>();
	
	protected final String name = "";
//	protected List<State> states = new ArrayList<State>();
	protected State beginningState = null;
	protected State currentState = null;
	protected Location questLocation = null;

	public Quest() {
		allQuests.put(name, this);
	}
	
	public void triggered(Trigger triggerType) {
		if (testRequirementsMet()) {
			currentState.triggered(triggerType);
		}
	}
	
	public void bondLocation(Location loc) {
		this.questLocation = loc;
	}
	
	protected abstract boolean testRequirementsMet();
	
	public void progressQuest(State newState) {
		currentState = newState;
		newState.triggered(Trigger.NEWSTATE);
	}
	
	protected void sayHere(String msg) {
		for (Holdable m : questLocation.getInventory().values()) {
			if (m instanceof Mobile && ((Mobile)m).isControlled()) {
				((Mobile)m).tell(msg);
			}
		}
	}	
	
	public abstract boolean stateRequiresItem(Holdable item, Mobile mobile);	
	
	protected abstract class State {
		
		protected Quest bondedQuest;
		
		public State(Quest quest) {
			bondedQuest = quest;
		}
		
		public void triggered(Trigger triggerType) {
			switch(triggerType) {
			case GREETS:
				runGreets();
				break;
			case NODS:
				runNods();
				break;
			case NEWSTATE:
				runNewState();
				break;
			case GIVES:
				runGives();
				break;
			default:
			}
		}
		
		protected abstract void runGreets();
		protected abstract void runNods();
		protected abstract void runNewState();
		protected abstract void runGives();

		public boolean requiresItem(Holdable item, Mobile mobile) {
			return false;
		}
	}
	
	public enum Trigger {
		
		GREETS() {
			
		},
		NODS() {
			
		},
		NEWSTATE() {
			
		},
		GIVES() {
			
		};
	}

	
}
