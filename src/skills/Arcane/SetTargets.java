package skills.Arcane;

import java.util.ArrayList;

import processes.UsefulCommands;

import Interfaces.Action;
import Interfaces.Container;
import Interfaces.Holdable;
import Interfaces.Mobile;

public class SetTargets implements Action {
	
	public enum Target {
		// Enum types.
		SINGLE() {
			@Override
			public ArrayList<Mobile> findTarget(String t, Container loc) {
				Holdable castAt = UsefulCommands.stringToHoldable(t, loc);
				if (castAt instanceof Mobile) {
					ArrayList<Mobile> targ = new ArrayList<Mobile>();
					targ.add((Mobile) castAt);
					return targ;
				}
				return null;
			}
		},
		ALL() {
			@Override
			public ArrayList<Mobile> findTarget(String t, Container loc) {
				ArrayList<Mobile> targets = new ArrayList<Mobile>();
				for (Holdable h : loc.getInventory()) {
					if (h instanceof Mobile) {
						targets.add((Mobile)h);
					}
				}
				return targets;
			}
		},
		
		ENEMIES() {
			@Override
			public ArrayList<Mobile> findTarget(String t, Container loc) {
				return null;
			}
		},
		ALLIES(){
			@Override
			public ArrayList<Mobile> findTarget(String t, Container loc) {
				return null;
			}
		},
		NONEMEIES(){
			@Override
			public ArrayList<Mobile> findTarget(String t, Container loc) {
				return null;
			}
		},
		NONALLIES() {
			@Override
			public ArrayList<Mobile> findTarget(String t, Container loc) {
				return null;
			}
		};

		//Constructor
		private Target() {			
		}		
		public abstract ArrayList<Mobile> findTarget(String t, Container loc);
	}
	
	private Target type;
	private int position;

	public SetTargets(int i, Target type) {
		position = i;
		this.type = type;
	}
	
	
	@Override
	public boolean activate(Skill s) {
		String fullCommand = s.getFullCommand();
		ArrayList<Mobile> targets = type.findTarget(UsefulCommands.returnTarget(fullCommand), s.getLocation());
		s.setTargets(position, targets);
		return true;
	}
}