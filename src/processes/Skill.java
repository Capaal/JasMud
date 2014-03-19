package processes;

import interfaces.*;

import java.sql.SQLException;
import java.util.*;

import TargettingStrategies.*;
import actions.Damage;

public class Skill {
	
	private final String name;
	private int id; 
	private final String description;
	private final Queue<Action> actions;
	private final List<Syntax> syntax; //Are we sure we'll be maintaining order?	
	private final String failMsg;
	
	public Skill(SkillBuilder build) {
		int possibleId;
		if (build.getId() == -1) {
			possibleId = setId();
		} else {
			possibleId = build.getId();
		}		
		this.name = build.getName();
		this.description = build.getDescription();
		this.actions = build.getActions();
		this.failMsg = build.getFailMsg();
		this.syntax = build.getSyntax();
		this.id = possibleId;
		save();
	}
	
	public String getName() {return name;}	
	
	public void perform(String fullCommand, Mobile currentPlayer) {
		boolean shouldInformTarget = false;
		for (Action a : actions){
			if (a instanceof Damage) {
				shouldInformTarget = true;
			}
			if (!a.activate(this, fullCommand, currentPlayer)) {
				System.out.println(a + " returned false.");
				currentPlayer.tell(failMsg);
				shouldInformTarget = false; // Is this correct? Should a skill that fails to complete not inform the target of an aggressor?
				break;
			}
			if (shouldInformTarget) {
				List<Container> loc = new TargetHereWhereStrategy().findWhere(this, fullCommand, currentPlayer);
				List<Holdable> target = new TargetTargetWhatStrategy().findWhat(this, fullCommand, currentPlayer, loc);
				if (!target.isEmpty() && target.get(0) instanceof Mobile) {
					((Mobile)target.get(0)).informLastAggressor(currentPlayer);
				}
			}
		}
	}	
	 // should be inside syntax enum?
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
		String skillSelect = "SELECT * FROM SKILL WHERE SKILLNAME='" + name + "';";
		HashMap<String, Object> skillView = SQLInterface.returnBlockView(skillSelect);
		String skillInsert;
		if (skillView.isEmpty()) {
			skillInsert = "INSERT INTO SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) VALUES (" + this.id + ", '" + name + "', '" + description 
						+ "', '" + failMsg + "');";
		} else {
			skillInsert = "UPDATE SKILL SET SKILLDES='" + description + "', SKILLFAILMSG='" + failMsg + "' WHERE SKILLID=" + id + ";";
		}
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
			position ++;
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
	
	public int setId() {
		String sqlQuery = "SELECT sequencetable.sequenceid FROM sequencetable"
				+ " LEFT JOIN skill ON sequencetable.sequenceid = skill.skillid"
				+ " WHERE skill.skillid IS NULL";		
		Object availableId = (int) SQLInterface.viewData(sqlQuery, "sequenceid");
		if (availableId == null || !(availableId instanceof Integer)) {
			SQLInterface.increaseSequencer();
			return setId();
		} else {
			return (int)availableId;
		}		
	}
	
	public void preview(Mobile player) {
		player.tell("Name: " + name);
		player.tell("Description: " + description);
		StringBuilder sb = new StringBuilder();
		player.tell("Actions: " + description);
		for (Action a : actions) {
			a.explainOneself(player);
		}
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
			
		},
		
		TIMES() {
			
		};
		
		
		private Syntax() {}
	}

	public int getId() {
		return id;
	}

	

	
}