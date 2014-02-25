package processes;

import interfaces.*;
import java.sql.SQLException;
import java.util.*;

public class Skill {
	
	private final String name;
	private int id; // related only to it's database id, probably don't even need to know what it is.
	private final String description;
	private final Queue<Action> actions;
	private final Set<Type> types;
	private final List<Syntax> syntax; //Are we sure we'll be maintaining order?	
	private final String failMsg;
	
	public Skill(SkillBuilder build) {
		this.name = build.getName();
		this.description = build.getDescription();
		this.actions = build.getActions();
		this.types = build.getTypes();
		this.failMsg = build.getFailMsg();
		this.syntax = build.getSyntax();
		this.id = build.getId();
	}
	
	public String getName() {return name;}	
	
	// Called when attempting to cast the finished spell. May or may not have a target, depending on spell.
	public void perform(String fullCommand, Mobile currentPlayer) {
		for (Action a : actions){
			if (!a.activate(this, fullCommand, currentPlayer)) {
				System.out.println(a + " returned false.");
				currentPlayer.tell(failMsg);
				break;
			}
		}
	}	
	
	public Set<Type> getTypes() {
		return types;
	}
	
	public String getStringInfo(Syntax neededInfo, String fullCommand) {
		ArrayList<String> fullCommandArray = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(fullCommand);
		while (st.hasMoreTokens()) {
			fullCommandArray.add(st.nextToken());
		}
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
		return actions;
	}
	
	public boolean save() {
//		String skillSelect = "SELECT * FROM SKILL WHERE SKILLNAME='" + name + "';";
	//	HashMap<String, Object> skillView = SQLInterface.returnBlockView(skillSelect);
//		if (skillView.isEmpty()) {
		String skillInsert = "UPDATE SKILL SET SKILLDES='" + description + "', SKILLFAILMSG='" + failMsg + "' WHERE SKILLID=" + id + ";";
		//	String skillInsert = "INSERT INTO SKILL (SKILLNAME, SKILLDES, SKILLFAILMSG) VALUES ('" + name + "', '" + description 
		//		+ "', '" + failMsg + "');";
		try {
			SQLInterface.saveAction(skillInsert);
		} catch (SQLException e) {
			System.out.println("Attempt to save overarching skill failed via: " + skillInsert);
			e.printStackTrace();
			return false;
		}			
//			skillView = SQLInterface.returnBlockView(skillSelect);
//		}
//		this.id = (int) skillView.get("SKILLID");
		System.out.println("I'm not sure if skills know their id: " + name + " " + id);
		for (Syntax s : syntax) {
			String syntaxIdQuery = "SELECT * FROM SYNTAX WHERE SYNTAXPOS=" + syntax.indexOf(s) + " AND SYNTAXTYPE='" + s.toString() + "';";
			Object syntaxId = SQLInterface.viewData(syntaxIdQuery, "SYNTAXID");
			//This won't be necessary often, I guess I should just check and see if it exists, and if not, then insert.
			if (syntaxId == null) {
				String syntaxInsert = "INSERT INTO SYNTAX (SYNTAXPOS, SYNTAXTYPE) VALUES (" + syntax.indexOf(s) + ", '" + s.toString() +
						"');";
				try {
					SQLInterface.saveAction(syntaxInsert);
				} catch (SQLException e) {
					System.out.println("Skill syntax failed to save via: " + syntaxInsert);
					e.printStackTrace();
					return false;
				}			
			}
		//	String syntaxIdQuery = "SELECT * FROM SYNTAX WHERE SYNTAXPOS=" + syntax.indexOf(s) + " AND SYNTAXTYPE='" + s.toString() + "';";
		//	HashMap<String, Object> syntaxView = SQLInterface.returnBlockView(syntaxIdQuery);
			String syntaxTableInsert = "INSERT INTO SYNTAXTABLE (SKILLID, SYNTAXID) VALUES (" + this.id 
					+ ", (SELECT SYNTAXID FROM SYNTAX WHERE SYNTAXPOS=" + syntax.indexOf(s) + " AND SYNTAXTYPE='" + s.toString() + "'))"
							+ " ON DUPLICATE KEY UPDATE SKILLID=" + this.id + ";";
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
		//	String linkSelect = "SELECT * FROM BLOCKTABLE WHERE SKILLID=" + id + " AND BLOCKID =" + a.getId() + ";";
		//	HashMap<String, Object> linkView = SQLInterface.returnBlockView(linkSelect);
		//	if (linkView.isEmpty()) {
			String linkInsert = "INSERT INTO BLOCKTABLE (SKILLID, BLOCKID) values (" + id + ", " + a.getId() 
					+ ") ON DUPLICATE KEY UPDATE SKILLID=" + id + ";";
			try {
				SQLInterface.saveAction(linkInsert);
			} catch (SQLException e) {
				System.out.println("Skill blocktable failed to save via: " + linkInsert);
				e.printStackTrace();
				return false;
			}				
		//	}
		}
		return true;		
	}
	
	public void preview(Mobile player) {
		player.tell("Name: " + name);
		player.tell("Description: " + description);
		StringBuilder sb = new StringBuilder();
		player.tell("Actions: " + description);
		for (Action a : actions) {
			a.explainOneself(player);
		}
		sb.append("Types:");
		for (Type t : types) {
			sb.append(" ");
			sb.append(t.toString());
		}
		player.tell(sb.toString());
		sb = new StringBuilder();
		sb.append("Syntax:");
		for (Syntax s : syntax) {
			sb.append(" ");
			sb.append(s.toString());
		}
		player.tell(sb.toString());
		player.tell("Fail Message: " + failMsg);
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