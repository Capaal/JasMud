package skills.Arcane;

import interfaces.*;
import java.util.*;
import effects.*;
import processes.*;

//******* Skill is in an awkward spot, where it was made for the arcane class, then turned into something for all skills. ************//


public class Skill {
	
	private String name;
	private Mobile currentPlayer;
	
	private String fullCommand;
	
	private Queue<Action> actions;
	private ArrayList<Type> types;
	
	public Skill() {
	}
	
	public Skill(SkillBuilder build, Mobile currentPlayer) {
		this.name = build.getName();
		this.actions = build.getActions();
		this.types = build.getTypes();
		this.currentPlayer = currentPlayer;
	}
	
	public String getName() {return name;}	
	
	// Called when attempting to cast the finished spell. May or may not have a target, depending on spell.
	public void perform(String fullCommand) {
		this.fullCommand = fullCommand;
		for (Action a : actions){
			if (a.activate(this) == false) {
				System.out.println(a + " returned false.");
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
}