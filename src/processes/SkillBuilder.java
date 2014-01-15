package processes;

import interfaces.*;

import java.util.*;

import processes.*;
import processes.Skill.Syntax;

public class SkillBuilder {
	
	private String name;	
	private Queue<Action> actions = new LinkedList<Action>();
	private ArrayList<Type> types = new ArrayList<Type>();
	private Mobile currentPlayer;
	private String description;
	private String failMsg = null;
	private ArrayList<Syntax> syntax = new ArrayList<Syntax>();

	public void setup(Mobile mob, String fullCommand) {
		this.name = fullCommand;
		this.currentPlayer = mob;	
		syntax.add(Syntax.SKILL);
	}
	
	public String getName() {return name;}	
	public Queue<Action> getActions() {return new LinkedList<Action>(actions);}
	public ArrayList<Type> getTypes() {return new ArrayList<Type>(types);}
	
	// Just forced to work, needs to be re-made.
	public void complete() {
		SkillBook skillList = currentPlayer.getBook("skillbook");		
		if (skillList != null && skillList instanceof SkillBook) {
			((SkillBook)skillList).addSkill(new Skill(this, currentPlayer));
		}
		toDefault();
	}
	
	public void complete(SkillBook skillBook) {
		skillBook.addSkill(new Skill(this, currentPlayer));	
		toDefault();
	}
	
	public void addType(Type type) {
		this.types.add(type);
	}	
		
	public void addAction(Action a) {
		actions.add(a);
	}

	private void toDefault() {
		this.actions.clear();
		this.types.clear();
	}
	
	public void setFailMsg(String msg) {
		this.failMsg = msg;
	}
	
	public String getFailMsg() {
		return failMsg;
	}
	
	public void setSyntax(ArrayList<Syntax> syntaxList) {
		syntax.clear();
		syntax.addAll(syntaxList);
		
	//	for (Syntax s : sList) {
	//		syntax.add(s);
	//	}
	}
	
	public ArrayList<Syntax> getSyntax() {
		return new ArrayList<Syntax>(syntax);
	}

	public void setDescription(String desc) {
		this.description = desc;
		
	}

	public void setType(ArrayList<Type> skillTypes) {
		this.types.addAll(skillTypes);		
	}
}