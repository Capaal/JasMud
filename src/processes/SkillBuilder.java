package processes;

import interfaces.*;

import java.util.*;

import processes.Skill.Syntax;

public class SkillBuilder {
	
	private int id = 0;
	private String name = "default";	
	private Queue<Action> actions;
	private Set<Type> types;
	private String description = "";
	private String failMsg = "";
	private ArrayList<Syntax> syntax;
	private Set<SkillBook> attachedBooks;
	
	public SkillBuilder() {
		toDefault();		
	}
	
	private void toDefault() {
		actions = new LinkedList<Action>();
		types = EnumSet.noneOf(Type.class);
		syntax = new ArrayList<Syntax>();
		attachedBooks = new HashSet<SkillBook>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Set<Type> getTypes() {
		return types;
	}
	
	public void complete() {
		for (SkillBook book : attachedBooks) {
			book.addSkill(new Skill(this));
		}
		toDefault();
	}
	
	public void addBook(SkillBook skillBook) {
		attachedBooks.add(skillBook);
	}
	
	public Set<SkillBook> getAttachedBooks() {
		return attachedBooks;
	}
	
	public void addType(Type type) {
		this.types.add(type);
	}	
	
	public void clearTypes() {
		this.types.clear();
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
	
	public String getDescription() {
		return description;
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

	public void setDescription(String desc) {
		this.description = desc;
		
	}

	public void setType(ArrayList<Type> skillTypes) {
		this.types.addAll(skillTypes);		
	}
	
	public void setId(int newId) {
		this.id = newId;
	}
	
	public void setId() {
		String sqlQuery = "SELECT sequencetable.sequenceid FROM sequencetable"
				+ " LEFT JOIN skill ON sequencetable.sequenceid = skill.skillid"
				+ " WHERE skill.skillid IS NULL";		
		Object availableId = (int) SQLInterface.viewData(sqlQuery, "sequenceid");
		if (availableId == null || !(availableId instanceof Integer)) {
			SQLInterface.increaseSequencer();
			setId();
		} else {
			setId((int)availableId);
		}		
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
		sb.append("Types:");
		for (Type type : types) {
			sb.append(" ");
			sb.append(type.toString());
		}	
		player.tell(sb.toString());
		sb = new StringBuilder();
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