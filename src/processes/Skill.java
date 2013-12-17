package processes;

import interfaces.*;

import java.util.*;

import processes.Skill.Syntax;

public class Skill {
	
	private String name;
	private Mobile currentPlayer;	
	private String fullCommand;	
	private Queue<Action> actions;
	private ArrayList<Type> types;
	private ArrayList<Syntax> syntax;
	private ArrayList<String> fullCommandArray;
	
	public String failMsg = "default";
	
	public Skill() {
	}
	
	public Skill(SkillBuilder build, Mobile currentPlayer) {
		this.name = build.getName();
		this.actions = build.getActions();
		this.types = build.getTypes();
		this.currentPlayer = currentPlayer;
		this.failMsg = build.getFailMsg();
		this.syntax = build.getSyntax();
	}
	
	public String getName() {return name;}	
	
	// Called when attempting to cast the finished spell. May or may not have a target, depending on spell.
	public void perform(String fullCommand) {
		this.fullCommand = fullCommand;
		fullCommandArray = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(fullCommand);
		while (st.hasMoreTokens()) {
			fullCommandArray.add(st.nextToken());
		}
		for (Action a : actions){
			if (!a.activate(this)) {
				System.out.println(a + " returned false.");
				if (failMsg != null) {
					currentPlayer.tell(failMsg);
				}
				break;
			}
		}
	}
	
	public Container getContainer() {
		return currentPlayer.getContainer();
	}
	
	public String getFullCommand() {
		return fullCommand;
	}
	
	public Mobile getCurrentPlayer() {
		return this.currentPlayer;
	}	
	
	public List<Type> getTypes() {
		List<Type> t = new ArrayList<Type>();
		t.addAll(types);
		return t;
	}
	
	public String getStringInfo(Syntax neededInfo) {
		int syntaxPos = syntax.indexOf(neededInfo);
		if (syntaxPos != -1) {
			if (neededInfo.equals(Syntax.LIST)){
				StringBuffer sb = new StringBuffer();
				for (int i = syntaxPos; i < fullCommandArray.size(); i++) {
					sb.append(fullCommandArray.get(i));
				}
				return sb.toString();
			}
			return fullCommandArray.get(syntaxPos);
		}
		return null;
	}
	
	public enum Syntax {
		
		SKILL() {
			
		},
		
		TARGET() {
			
		},
		
		ITEM() {
			
		},
		
		LIST() {
			
		},
		
		DIRECTION() {
			
		};
		
		
		private Syntax() {}
	}
}