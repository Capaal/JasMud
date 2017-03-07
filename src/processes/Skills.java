package processes;

import interfaces.*;
import java.util.*;

public abstract class Skills {
	
	protected String name;
	protected String description;
	protected List<Syntax> syntaxList = new ArrayList<Syntax>();		
	protected Mobile currentPlayer;
	protected String fullCommand;
	
	public void perform(String fullCommand, Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		this.fullCommand = fullCommand;
		if (currentPlayer.isDead()) {
			messageSelf("You are dead, you may be better off praying.");
			return;
		}
		testForInduction();
		performSkill();
	}
	
	protected abstract void performSkill();
	
	private void testForInduction() {
		if (currentPlayer.isInducting()) {
			currentPlayer.killInduction();
		}
	}
	
	public Boolean hasBalance() {
		if (currentPlayer.hasBalance()) {
			return true;
		}
		messageSelf("You're off balance.");
		return false;
	}
	
	public Boolean isBlocking(Mobile target) {
		if (target.isBlocking()) {
			messageSelf(target.getName() + " deftly blocks your attack.");
			messageTarget("Your careful defence blocks an attack from " + currentPlayer.getName(), Arrays.asList(target));
		}		
		return target.isBlocking();
	}
	
	protected void messageSelf(String msg) {
		currentPlayer.tell(msg);
	}
	
	public void messageOthers(String msg, List<Mobile> toIgnore) {
		for (Mobile h : currentPlayer.getContainer().getMobiles().values()) {
			if (h.isControlled()) {
				Boolean shouldTell = true;
				if (h.equals(currentPlayer)) {
					shouldTell = false;
				} else {
					for (Mobile m : toIgnore) {
						if (h.equals(m)) {
							shouldTell = false;
						}
					}
				}
				if (shouldTell) {
					h.tell(msg);
				}
			}			
		}
	}
	
	public void messageOthersAway(String msg, List<Mobile> toIgnore, Location otherLoc) {
		for (Holdable h : otherLoc.getInventory().values()) {
			if (h instanceof Mobile && ((Mobile) h).isControlled()) {
				Boolean shouldTell = true;
				if (h.equals(currentPlayer)) {
					shouldTell = false;
				} else {
					for (Mobile m : toIgnore) {
						if (h.equals(m)) {
							shouldTell = false;
						}
					}
				}
				if (shouldTell) {
					((Mobile)h).tell(msg);
				}
			}			
		}
	}

	public void messageTarget(String msg, List<Mobile> targets) {
		for (Mobile m : targets) {
			m.tell(msg);
		}
	}

	public enum Syntax {
		
		SKILL() {
			
		},
		
		TARGET() {
			
		},
		
		ITEM() {
			
		},
		
		LIST() {
			@Override
			public String getStringInfo(String fullCommand, Skills s) {
				ArrayList<String> fullCommandArray = new ArrayList<String>(); // To becomes list of each word from fullCommand
				StringTokenizer st = new StringTokenizer(fullCommand);  // Breaks fullCommand into individual Words.
				while (st.hasMoreTokens()) {
					fullCommandArray.add(st.nextToken()); // adds individual words to fullCommandArray
				}
				int syntaxPos = s.syntaxList.indexOf(this); // Queries THIS skill for the given Syntax (neededInfo) for which position that word should be.
				if (syntaxPos != -1) { // Would be -1 IF the neededInfo Syntax was NOT defined by this Skill.					
					StringBuffer sb = new StringBuffer();
					for (int i = syntaxPos; i < fullCommandArray.size(); i++) {
						sb.append(fullCommandArray.get(i));
					}
					return sb.toString();					
				}
				return "";
			}			
		},
		
		DIRECTION() {
			
		},
		
		SLOT() {
			
		},
		
		FILLER() {			
		},
		
		QUANTITY() {		
			@Override
			public String getStringInfo(String fullCommand, Skills s) {
				ArrayList<String> fullCommandArray = new ArrayList<String>(); // To becomes list of each word from fullCommand
				StringTokenizer st = new StringTokenizer(fullCommand);  // Breaks fullCommand into individual words.
				while (st.hasMoreTokens()) {
					fullCommandArray.add(st.nextToken()); // adds individual words to fullCommandArray
				}
				int syntaxPos = s.syntaxList.indexOf(this); // Queries THIS skill for the given Syntax (neededInfo) for which position that word should be.
				if (syntaxPos != -1) { // Would be -1 IF the neededInfo Syntax was NOT defined by this Skill.				
					if (fullCommandArray.size() > syntaxPos) { // If fullCommand has enough words.
						String numerics = fullCommandArray.get(syntaxPos);					
						if (numerics.matches("[0-9]+")) {
							return fullCommandArray.get(syntaxPos);
						}
						return "";
					}
				}
				return "";		
			}
		},
		
		TIMES() {
			
		};
		
		
		private Syntax() {}
		
		public String getStringInfo(String fullCommand, Skills s) {
			ArrayList<String> fullCommandArray = new ArrayList<String>(); // To becomes list of each word from fullCommand
			StringTokenizer st = new StringTokenizer(fullCommand);  // Breaks fullCommand into individual words.
			while (st.hasMoreTokens()) {
				fullCommandArray.add(st.nextToken()); // adds individual words to fullCommandArray
			}
			int syntaxPos = s.syntaxList.indexOf(this); // Queries THIS skill for the given Syntax (neededInfo) for which position that word should be.
			if (syntaxPos != -1) { // Would be -1 IF the neededInfo Syntax was NOT defined by this Skill.				
				if (fullCommandArray.size() > syntaxPos) { // If fullCommand has enough words.
					return fullCommandArray.get(syntaxPos); // Return the word at the given position.
				}
			}
			return "";
		}
	}

	public String getName() {
		return name;
	}
}