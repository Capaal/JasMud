package effects;

import java.util.Arrays;

import interfaces.Mobile;
import processes.UsefulCommands;


public enum PassiveCondition {
		
		BROKENRIGHTARM() {
			@Override public String limbName() {return "righthand";}
		},
		
		BROKENLEFTARM() {
			@Override public String limbName() {return "lefthand";}
		},
		
		BROKENLEGS() {
			@Override public String limbName() {return "legs";}
			//doOnDestruction yes/no? could be different per skill, or consistent so it's easy to recognize
		},
		
		BALANCE() {
			@Override public void doOnDestruction(Mobile currentPlayer) {
				currentPlayer.tell("You regain balance.");
			}
		},
		
		ROOT() {
			@Override public void doOnDestruction(Mobile currentPlayer) {
				currentPlayer.tell("You are no longer rooted.");
			}
		},
		
		SLEEP() {
			//maybe should be active condition that regens? or regens sleepiness?
			@Override
			public void doOnCreation(Mobile currentPlayer) {
				UsefulCommands.messageOthers(currentPlayer, currentPlayer.getName() + " falls asleep.", Arrays.asList(currentPlayer));
			}
			@Override
			public void doOnDestruction(Mobile currentPlayer) {
				UsefulCommands.messageOthers(currentPlayer, currentPlayer.getName() + " wakes up.", Arrays.asList(currentPlayer));
			}
		},
		
		DEFENCE() {
			@Override
			public void doOnCreation(Mobile currentPlayer) {
				currentPlayer.addDefense(10);
			}
			@Override
			public void doOnDestruction(Mobile currentPlayer) {
				currentPlayer.addDefense(-10);
			}
		},
		
		DIZZY() {
			
		};	
		
		private PassiveCondition() {}
		
		public void doOnCreation(Mobile currentPlayer) {
		//	currentPlayer.displayPrompt();
		}
		public void doOnDestruction(Mobile currentPlayer) {
		//	currentPlayer.tell("You regain balance.");
		}
		
		public String limbName() {return "";}
		
		//this doesn't work here
/*		public ConditionsEnum getBroken(String slot) {
			if (slot.equals("righthand")) {
				return ConditionsEnum.BROKENRIGHTARM;
			} else if (slot.equals("lefthand")) {
				return ConditionsEnum.BROKENLEFTARM;
			} else if (slot.equals("legs")) {
				return ConditionsEnum.BROKENLEGS;
			}
			return null;
		} */
	
}
