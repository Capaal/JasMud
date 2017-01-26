package processes;

import interfaces.*;

import java.util.*;

public abstract class Skills {
	
	protected String name = "abstract";
	protected int id = -1; 
	protected String description = "not a real skill.";
	protected List<Syntax> syntax = new ArrayList<Syntax>();	
	protected Boolean failed = false;
	
	public Skills() {}
	
	public abstract void perform(String fullCommand, Mobile currentPlayer);		
	
	 // should be inside syntax enum?
	public String getStringInfo(Syntax neededInfo, String fullCommand) {
		return neededInfo.getStringInfo(neededInfo, fullCommand, this);	
	}
	
	public void checkForBalance() {
		failed = true;
		messageSelf("You're off balance");
	}
	
	private void messageSelf(String string) {
		// TODO Auto-generated method stub
		
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
				int syntaxPos = s.syntax.indexOf(neededInfo); // Queries THIS skill for the given Syntax (neededInfo) for which position that word should be.
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
			int syntaxPos = s.syntax.indexOf(neededInfo); // Queries THIS skill for the given Syntax (neededInfo) for which position that word should be.
			if (syntaxPos != -1) { // Would be -1 IF the neededInfo Syntax was NOT defined by this Skill.				
				if (fullCommandArray.size() > syntaxPos) { // If fullCommand has enough words.
					return fullCommandArray.get(syntaxPos); // Return the word at the given position.
				}
			}
			return "";
		}
	}

	public Object getName() {
		return this.name;
	}
}