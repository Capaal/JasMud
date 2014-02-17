package processes;

import interfaces.*;

import java.sql.SQLException;
import java.util.*;

import processes.Skill.Syntax;

public class Skill {
	
	private String name;
	private int id;
	private String description;
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
			if (fullCommandArray.size() > syntaxPos) {
				return fullCommandArray.get(syntaxPos);
			}
		}
		return "";
	}
	
	public Queue<Action> getActions() {
		return new LinkedList<Action>(actions);
	}
	
	public boolean save() {
		String skillSelect = "SELECT * FROM SKILL WHERE SKILLNAME='" + name + "';";
		HashMap<String, Object> skillView = SQLInterface.returnBlockView(skillSelect);
		// Does skillView actually ever equal null, or just an empty map?
		//TODO
		if (skillView == null) {
			String skillInsert = "INSERT INTO SKILL (SKILLNAME, SKILLDES, SKILLFAILMSG) VALUES ('" + name + "', '" + description 
				+ "', '" + failMsg + "');";
			try {
				SQLInterface.saveAction(skillInsert);
			} catch (SQLException e) {
				System.out.println("Attempt to overarching skill failed via: " + skillInsert);
				e.printStackTrace();
				return false;
			}			
			skillView = SQLInterface.returnBlockView(skillSelect);
			this.id = (int) skillView.get("SKILLID");
		}
		for (Syntax s : syntax) {
			String syntaxInsert = "INSERT IGNORE INTO SYNTAX (SYNTAXPOS, SYNTAXTYPE) VALUES (" + syntax.indexOf(s) + ", '" + s.toString() + "');";
			try {
				SQLInterface.saveAction(syntaxInsert);
			} catch (SQLException e) {
				System.out.println("Skill syntax failed to save via: " + syntaxInsert);
				e.printStackTrace();
				return false;
			}			
			String syntaxIdQuery = "SELECT * FROM SYNTAX WHERE SYNTAXPOS=" + syntax.indexOf(s) + " AND SYNTAXTYPE='" + s.toString() + "';";
			HashMap<String, Object> syntaxView = SQLInterface.returnBlockView(syntaxIdQuery);
			String syntaxTableInsert = "INSERT IGNORE INTO SYNTAXTABLE (SKILLID, SYNTAXID) VALUES (" + this.id + ", " + syntaxView.get("SYNTAXID") + ");";
			try {
				SQLInterface.saveAction(syntaxTableInsert);
			} catch (SQLException e) {
				System.out.println("Skill syntax table failed to save via: " + syntaxTableInsert);
				e.printStackTrace();
				return false;
			}			
		}	
		int position =  1;
		for (Action a : actions) {
			if (!a.save(position)) {
				return false;
			}
			String linkSelect = "SELECT * FROM BLOCKTABLE WHERE SKILLID=" + id + " AND BLOCKID =" + a.getId() + ";";
			HashMap<String, Object> linkView = SQLInterface.returnBlockView(linkSelect);
			if (linkView == null) {
				String linkInsert = "INSERT INTO BLOCKTABLE (SKILLID, BLOCKID) values (" + id + ", " + a.getId() + ");";
				try {
					SQLInterface.saveAction(linkInsert);
				} catch (SQLException e) {
					System.out.println("Skill blocktable failed to save via: " + linkInsert);
					e.printStackTrace();
					return false;
				}				
			}
		}
		return true;		
	}
	
//	public void changePlayer(Mobile newPlayer) {
//		this.currentPlayer = newPlayer;		
//	}
	
	public Skill copy(Mobile newPlayer) {
		Skill newSkill = new Skill();
		newSkill.name = this.name;
		newSkill.actions = this.actions;
		newSkill.types = this.types;
		newSkill.currentPlayer = newPlayer;
		newSkill.failMsg = this.failMsg;
		newSkill.syntax = this.syntax;
		return newSkill;
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
			
		},
		
		SLOT() {
			
		};
		
		
		private Syntax() {}
	}

	

	
}