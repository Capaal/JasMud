package processes;

import interfaces.*;
import items.Door;

import java.util.*;

import effects.Blocking;
import effects.PassiveCondition;
import processes.Location.Direction;

public abstract class Skills implements Runnable {
	
	protected final String name;
	protected final String description;
	protected final List<Syntax> syntaxList;		
	protected final Mobile currentPlayer;
	protected final String fullCommand;
	
	public Skills(String name, String description, Mobile currentPlayer, String fullCommand) {
		this.name = name;
		this.description = description;
		this.currentPlayer = currentPlayer;
		this.fullCommand = fullCommand;
		this.syntaxList = new ArrayList<Syntax>();
	}
	
	public void run() {	
//		this.currentPlayer = currentPlayer;
//		this.fullCommand = fullCommand;
		if (preAllSkills()) {
		//	currentPlayer.tell("\n");
			performSkill();
			currentPlayer.displayPrompt();
		}
	}
	
	//method for checking conditions that prevent any skills
	protected boolean preAllSkills() {
		if (currentPlayer.isDead()) {
			messageSelf("You are dead, you may be better off praying.");
			return false;
		}
		if (!fullCommand.equals("wake")) { //this is ugly TODO
			if (currentPlayer.hasCondition(PassiveCondition.SLEEP)) {
				messageSelf("You are asleep and can't do anything.");
				return false;
			}
		}
		testForInduction();
		return true;
	}
	
	protected abstract void performSkill();
	protected abstract boolean preSkillChecks();
	public abstract Skills getNewInstance(Mobile currentPlayer, String fullCommand);
	
	protected void testForInduction() {
		if (currentPlayer.isInducting()) {
			currentPlayer.killInduction();
		}
	}
	
	protected boolean isDirectionBlocked(Location currentLocation, Direction interestedDir) {
		if (currentLocation == null) {
			throw new IllegalArgumentException("Supplied location may not be null.");
		}
		Door d = currentLocation.getDoor(interestedDir);
		if (d != null && !d.isOpen()) {
			messageSelf("A door is blocking the way.");
			return true;
		}
		if (currentLocation.isDirectionBlocked(interestedDir)) {
			for (Blocking b : currentLocation.getBlocking(interestedDir)) {
				messageSelf("A " + b.getDescription() + " is blocking the way.");
			}
			return true;
		}
		return false;
	}
	
	public boolean hasBalance() {
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
					h.displayPrompt();
				}
			}			
		}
	}
	
	public void messageOthersAway(String msg, List<Mobile> toIgnore, Location otherLoc) {
		for (Mobile h : otherLoc.getMobiles().values()) {
			if (h.isControlled()) {
				if (!toIgnore.contains(h)) {
					h.tell(msg);
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
						sb.append(" " + fullCommandArray.get(i));
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
	
	@Override
	public boolean equals(Object o) {
		return o.getClass().equals(this.getClass());	
	}
	
	@Override
	public int hashCode() {
        return this.getClass().getName().hashCode();
    }
 
}