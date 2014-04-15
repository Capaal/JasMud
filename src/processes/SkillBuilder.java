package processes;

import interfaces.*;

import java.util.*;

import processes.Skill.Syntax;

public class SkillBuilder {
	
	private int id = -1;
	private String name = "default";	
	private Queue<Action> actions;
	private String description = "";
	private String failMsg = "";
	private ArrayList<Syntax> syntax;
	private Set<SkillBook> attachedBooks;
	private Skill finishedSkill = null;
	
	private boolean buildCompleted = false;
	
	public SkillBuilder() {
		actions = new LinkedList<Action>();
		syntax = new ArrayList<Syntax>();
		attachedBooks = new HashSet<SkillBook>();		
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isCompleted() {
		return buildCompleted;
	}
	
	public String getName() {
		return name;
	}	
	
	public boolean complete() {
		if (id == -1) {
			try {
				setId();
			} catch (IllegalStateException e) {
				System.out.println("New skill failed to obtain unique id, it was not created.");
				return false;
			} 	
		}
		buildCompleted = true;
		finishedSkill = new Skill(this);
		for (SkillBook book : attachedBooks) {
			book.addSkill(finishedSkill);
		}		
		return true;		
	}
	
	public Skill getFinishedSkill() {
		return finishedSkill;
	}
	
	public void save() {
		finishedSkill.save();
		for (SkillBook finalSkillBooks : getAttachedBooks()) {		
			finalSkillBooks.save();					
		}	
	}
	
	private void setId() throws IllegalStateException {
		String sqlQuery = "SELECT sequencetable.sequenceid FROM sequencetable"
				+ " LEFT JOIN skill ON sequencetable.sequenceid = skill.skillid"
				+ " WHERE skill.skillid IS NULL";		
		Object availableId = WorldServer.databaseInterface.viewData(sqlQuery, "sequenceid");
		if (availableId == null || !(availableId instanceof Integer)) {
			WorldServer.databaseInterface.increaseSequencer();
			availableId = WorldServer.databaseInterface.viewData(sqlQuery, "sequenceid");
			if (availableId == null || !(availableId instanceof Integer)) {
				throw new IllegalStateException("The skill could not determine a valid id, it is invalid.");				
			} else {
				id = (int)availableId;
			}
		} else {
			id = (int)availableId;
		}		
	}
	
	public void addBook(SkillBook skillBook) {
		attachedBooks.add(skillBook);
	}
	
	public Set<SkillBook> getAttachedBooks() {
		return attachedBooks;
	}	
		
	public void addAction(Action a) {
		actions.add(a);
	}
	
	public void clearActions() {
		actions.clear();
	}
	
	public Queue<Action> getActions() {
		return actions;
	}
	
	public void setFailMsg(String msg) {
		this.failMsg = msg;
	}
	
	public String getFailMsg() {
		return failMsg;
	}
	
	public void setDescription(String desc) {
		this.description = desc;		
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setId(int newId) {
		this.id = newId;
	}
	
	public int getId() {
		return id;
	}
	
	public void setSyntax(List<Syntax> list) {
		syntax.clear();
		syntax.addAll(list);
	}
	
	public ArrayList<Syntax> getSyntax() {
		return syntax;
	}
	
	public void addSyntax(Syntax syn) {
		syntax.add(syn);
	}
	
	public void clearSyntax() {
		syntax.clear();
	}		
	
	public void preview(Mobile player) {
		player.tell("Name: " + name);
		player.tell("Description: " + description);
		player.tell("Skill actions are:");
		for (Action a : actions) {
			player.tell(" " + a.getClass().getName());
			a.explainOneself(player);
		}
		StringBuilder sb = new StringBuilder();	
		sb.append("Syntax: ");
		for (Syntax s : syntax) {
			sb.append(" ");
			sb.append(s.toString());
		}
		player.tell("Syntax: " + sb.toString());
		player.tell("Fail Message: " + failMsg);
		sb = new StringBuilder();
		sb.append("Books this will be attached to:");
		for (SkillBook skillBook : attachedBooks) {
			sb.append(" ");
			sb.append(skillBook.getName());
		}
		player.tell(sb.toString());
	}
}