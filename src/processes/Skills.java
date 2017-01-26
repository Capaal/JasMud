package processes;

import interfaces.*;

import java.util.*;

import TargettingStrategies.WhatStrategySelf;
import TargettingStrategies.WhereStrategyHere;
import actions.Message;

public abstract class Skills {
	
	protected String name;
	protected int id; 
	protected String description;
	protected List<Syntax> syntaxList = new ArrayList<Syntax>();	
	
	public abstract void perform(String fullCommand, Mobile currentPlayer);		
	
	 // should be inside syntax enum?
	public String getStringInfo(Syntax neededInfo, String fullCommand) {
		return neededInfo.getStringInfo(neededInfo, fullCommand, this);	
	}
	
	public Boolean hasBalance(Mobile currentPlayer) {
	//	messageSelf("You're off balance", currentPlayer);
		return true;
	}
	
	public Boolean isBlocking(Mobile target) {
		// TODO check for block
		return false;
	}
	
	protected void messageSelf(String msg, Mobile currentPlayer) {
		currentPlayer.tell(msg);
	}
	
	protected void messageOthers(String msg, Mobile currentPlayer, List<Mobile> toIgnore) {
		for (Holdable h : currentPlayer.getContainer().getInventory()) {
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

	protected void messageTarget(String msg, List<Mobile> targets) {
		for (Mobile m : targets) {
			m.tell(msg);
		}
	}

	public enum Syntax {
		
		SKILL() {
			
		},
		
		SELF() {  // Special case for message. Should not be used in skill syntax.
			
		},
		
		TARGET() {
			
		},
		
		ITEM() {
			
		},
		
		LIST() {
			@Override
			public String getStringInfo(Syntax neededInfo, String fullCommand, Skills s) {
				ArrayList<String> fullCommandArray = new ArrayList<String>(); // To becomes list of each word from fullCommand
				StringTokenizer st = new StringTokenizer(fullCommand);  // Breaks fullCommand into individual characters.
				while (st.hasMoreTokens()) {
					fullCommandArray.add(st.nextToken()); // adds individual words to fullCommandArray
				}
				int syntaxPos = s.syntaxList.indexOf(neededInfo); // Queries THIS skill for the given Syntax (neededInfo) for which position that word should be.
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
		
		TIMES() {
			
		};
		
		
		private Syntax() {}
		
		public String getStringInfo(Syntax neededInfo, String fullCommand, Skills s) {
			ArrayList<String> fullCommandArray = new ArrayList<String>(); // To becomes list of each word from fullCommand
			StringTokenizer st = new StringTokenizer(fullCommand);  // Breaks fullCommand into individual characters.
			while (st.hasMoreTokens()) {
				fullCommandArray.add(st.nextToken()); // adds individual words to fullCommandArray
			}
			int syntaxPos = s.syntaxList.indexOf(neededInfo); // Queries THIS skill for the given Syntax (neededInfo) for which position that word should be.
			if (syntaxPos != -1) { // Would be -1 IF the neededInfo Syntax was NOT defined by this Skill.				
				if (fullCommandArray.size() > syntaxPos) { // If fullCommand has enough words.
					return fullCommandArray.get(syntaxPos); // Return the word at the given position.
				}
			}
			return "";
		}
	}

	public Object getName() {
		return name;
	}
}