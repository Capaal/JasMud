package processes;

import interfaces.*;

import java.util.*;

import TargettingStrategies.*;
import actions.Damage;

public class Skill {
	
	private final String name;
	private final int id; 
	private final String description;
	private final Queue<Action> actions;
	private final List<Syntax> syntax;	
	private final String failMsg;
	
	public Skill(SkillBuilder build) {
		if (!build.isCompleted()) {
			throw new IllegalArgumentException("SkillBuilder was not completed properly.");
		}
		this.id = build.getId();		
		this.name = build.getName();
		this.description = build.getDescription();
		this.actions = build.getActions();
		this.failMsg = build.getFailMsg();
		this.syntax = build.getSyntax();
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
	
	public int getId() {
		return id;
	}
	
	public boolean save() {
		if (skillExists()) {
			updateSkill();
		} else {
			insertSkill();
		}
		return true;
	}
	
	private boolean skillExists() {
		String skillSelect = "SELECT * FROM SKILL WHERE SKILLNAME='" + name + "';";
		HashMap<String, Object> skillView = WorldServer.databaseInterface.returnBlockView(skillSelect);
		return skillView.isEmpty();
	}
	
	private void updateSkill() {
		updateSkillInformation();
		deleteOldSyntaxTable();
		insertSyntax();
		saveActions(); //TODO needs testing
	}
	
	private void insertSkill() {
		insertSkillInformation();
		insertSyntax();
		saveActions();
	}
	
	private void updateSkillInformation() {
		String skillUpdate = "UPDATE SKILL SET SKILLDES='" + description + "', SKILLFAILMSG='" + failMsg + "' WHERE SKILLID=" + id + ";";
		WorldServer.databaseInterface.saveAction(skillUpdate);	
	}
	
	private void insertSyntax() {
		for (Syntax s : syntax) {
			String syntaxInsert = "INSERT INTO SYNTAX (SYNTAXPOS, SYNTAXTYPE) VALUES (" + syntax.indexOf(s) + ", '" + s.toString() +
					"');";
			WorldServer.databaseInterface.saveAction(syntaxInsert);		
			insertSyntaxTable(s);
		}	
	}
	
	private void deleteOldSyntaxTable() {
		String deletePossiblyOldSyntaxTable = "DELETE FROM SYNTAXTABLE WHERE SKILLID=" + this.id + ";";
		WorldServer.databaseInterface.saveAction(deletePossiblyOldSyntaxTable);
	}
	
	private void insertSyntaxTable(Syntax s) {
		String syntaxTableInsert = "INSERT INTO SYNTAXTABLE (SKILLID, SYNTAXID) VALUES (" + this.id 
				+ ", (SELECT SYNTAXID FROM SYNTAX WHERE SYNTAXPOS=" + syntax.indexOf(s) + " AND SYNTAXTYPE='" + s.toString() + "'));";
		WorldServer.databaseInterface.saveAction(syntaxTableInsert);
	}
	
	private void saveActions() {
		int position =  1;
		for (Action a : actions) {
			a.save(position);
			position ++;
			String deleteOldSkillToActionLinks = "DELETE FROM BLOCKTABLE WHERE SKILLID=" + this.id + ";";
			WorldServer.databaseInterface.saveAction(deleteOldSkillToActionLinks);
			String linkInsert = "INSERT INTO BLOCKTABLE (SKILLID, BLOCKID) values (" + id + ", " + a.getId() + ");";
			WorldServer.databaseInterface.saveAction(linkInsert);
		}
	}
	
	private void insertSkillInformation() {
		String skillInsert = "INSERT INTO SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) VALUES (" + this.id + ", '"
				+ name + "', '" + description + "', '" + failMsg + "');";
		WorldServer.databaseInterface.saveAction(skillInsert);
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
}