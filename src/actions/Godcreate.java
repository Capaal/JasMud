package actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import processes.LocationBuilder;
import costs.*;
import checks.*;
import effectors.*;
import processes.ItemBuilder;
import processes.Location;
import processes.SQLInterface;
import processes.Skill;
import processes.SkillBook;
import processes.SkillBuilder;
import processes.Type;
import processes.Skill.Syntax;
import processes.WorldServer;
import interfaces.Action;
import interfaces.Mobile;
import items.StdItem;

public class Godcreate extends Action {
	
	public static Map<String, Action> actionMap;
	
	public Godcreate() {
		
	}

	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		Mobile player = currentPlayer;
		createActionMap();
		player.tell("Welcome to the creation Menu.");
		player.tell("Here you can create an enormous variety of skills, items, locations, and mobiles");
		player.tell("But remember that this is an out of game menu and to exit you must type \"exit\".");
		return processCreateType(player);
	}
	
	private boolean processCreateType(Mobile player) {
		String selection = askQuestion("What do you want to do?: 1:Skills  2:Items  3:Locations  4:Mobiles OR 0:exit.", player);
		switch(selection) {
		case "1":
			return processCreateSkill(player);		
		case "2":
			return processCreateItem(player);			
		case "3":
			return processCreateLocation(player);			
		case "4":
			return processCreateMobile(player);			
		case "0":
		case "exit":
			return false;		
		default:
			player.tell("That isn't one of the options.");
			return processCreateType(player);
		}
	}
	
	@Override
	public void explainOneself(Mobile player) {
		player.tell("A full block skill combo fake thing that you can't use, but are using right now.");		
	}
	
	/*************************************************************************************
	 * 
	 * SKILL CREATION
	 *
	 **************************************************************************************/
	
	private boolean processCreateSkill(Mobile player) {
		String newOrOld = askQuestion("Is the skill \"new\" or \"old\"?", player);
		String name = askQuestion("What is the skill's name?", player); // max 50 char
		switch(newOrOld) {
		case "new":
			SkillBuilder newSkill = new SkillBuilder();
			newSkill.setName(name);
			return processCreateNewSkill(player, newSkill);		
		case "old":
			// Maybe not this, just overwrite skills with the new one, and make sure they know.
			return processAdjustSkill(player, name);
		case "exit":
			return false;
		default:
			player.tell("Please enter \"new\" or \"old\" or \"exit\".");
			return processCreateSkill(player);
		}
	}
	
	private boolean processCreateNewSkill(Mobile player, SkillBuilder newSkill) {	
		String adjust = askQuestion("What would you like to do? 1:Description 2:types 3:syntax 4:fail message"
				+ " 5:actions 6:skillbook 7:preview 8:complete 9:quit", player);
		switch(adjust) {
		case "1":
			player.tell("The current skill description is: " + newSkill.getDescription());
			newSkill.setDescription(askQuestion("What is the skill's description?", player));
			return processCreateNewSkill(player, newSkill);	
		case "2":
			String clearTypes = askQuestion("Do you want to clear the types list? y/n", player);
			if (clearTypes.equals("y")) {
				newSkill.clearTypes();
			}
			StringBuilder sb = new StringBuilder();
			sb.append("The current list of types are:");
			for (Type t : newSkill.getTypes()) {
				sb.append(" ");
				sb.append(t.toString());
			}
			player.tell(sb.toString());
			sb = new StringBuilder();
			sb.append("The possible list of types are:");
			for (Type types : Type.values()) {
				sb.append(" ");
				sb.append(types.toString());
			}
			player.tell(sb.toString());
			try {
				newSkill.addType(Type.valueOf(askQuestion("Which type do you want to add?", player).toUpperCase()));
			} catch (IllegalArgumentException e) {
				player.tell("You must be exact when indicating a type.");
				return processCreateNewSkill(player, newSkill);	
			}
			return processCreateNewSkill(player, newSkill);	
		case "3":
			String clearSyntax = askQuestion("Do you want to clear the types list? y/n", player);
			if (clearSyntax.equals("y")) {
				newSkill.clearSyntax();
			}
			StringBuilder currentSyntax = new StringBuilder();
			currentSyntax.append("The current list of syntax, in order, is: ");
			for (Syntax syn : newSkill.getSyntax()) {
				currentSyntax.append(" ");
				currentSyntax.append(syn.toString());				
			}
			player.tell(currentSyntax.toString());
			StringBuilder syntaxTypes = new StringBuilder();
			syntaxTypes.append("The types of syntax are:");
			for (Syntax synTypes : Syntax.values()) {
				syntaxTypes.append(" ");
				syntaxTypes.append(synTypes.toString());				
			}
			player.tell(syntaxTypes.toString());
			try {
				newSkill.addSyntax(Syntax.valueOf(askQuestion("Which syntax do you want to add? Order does matter.", player).toUpperCase()));
			} catch (IllegalArgumentException e) {
				player.tell("You must be exact when indicating a syntax.");
				return processCreateNewSkill(player, newSkill);	
			}
			return processCreateNewSkill(player, newSkill);	
		case "4":
			player.tell("The current fail message is: " + newSkill.getFailMsg());
			newSkill.setFailMsg(askQuestion("What do you want the fail message to be?", player));
			return processCreateNewSkill(player, newSkill);	
		case "5":
			String clearActions = askQuestion("Do you want to clear the actions list? y/n", player);
			if (clearActions.equals("y")) {
				newSkill.clearActions();
			}
			StringBuilder currentActions = new StringBuilder();
			currentActions.append("The current list of actions, in order, is:");
			for (Action action : newSkill.getActions()) {
				currentActions.append(" ");
				currentActions.append(action.getClass().getName());			
			}
			player.tell(currentActions.toString());	
			displayActions(player);
			newSkill.addAction(selectAction(player));
			return processCreateNewSkill(player, newSkill);			
		case "6":
			StringBuilder currentBooks = new StringBuilder();
			currentBooks.append("Current skillbooks attached are: ");
			for (SkillBook skillBook : newSkill.getAttachedBooks()) {				
				currentBooks.append(skillBook.getName());
				currentBooks.append(" ");
			}
			StringBuilder possibleBooks = new StringBuilder();
			possibleBooks.append("Which skillbook do you want this skill attached to?: ");
			for (SkillBook skillBook : WorldServer.AllSkillBooks.values()) {
				possibleBooks.append(skillBook.getId());
				possibleBooks.append(":");
				possibleBooks.append(skillBook.getName());
				possibleBooks.append(" ");
			}
			player.tell(currentBooks.toString());
			Integer skillBookId = Integer.parseInt(askQuestion(possibleBooks.toString(), player));
			if (WorldServer.AllSkillBooks.containsKey(skillBookId)) {
				newSkill.addBook(WorldServer.AllSkillBooks.get(skillBookId));
			}
			return processCreateNewSkill(player, newSkill);	
		case "7":
			newSkill.preview(player);
			return processCreateNewSkill(player, newSkill);	
		case "8":
			newSkill.complete();
			return true;
		case "9":
			return false;
		default:
			player.tell("That is not a valid option.");
			return processCreateNewSkill(player, newSkill);			
		}
	}
	
	private boolean processAdjustSkill(Mobile player, String skillName) {
		return false;
	}
	
	public static void createActionMap() {
		actionMap = new HashMap<String, Action>();	
		actionMap.put("damage", new Damage());
		actionMap.put("chance", new Chance());
		actionMap.put("drop", new Drop());
		actionMap.put("equipchange", new EquipChange());
		actionMap.put("examine", new Examine());
		actionMap.put("get", new Get());
		actionMap.put("look", new Look());
		actionMap.put("message", new Message());
		actionMap.put("move", new Move());
		actionMap.put("or", new Or());
		actionMap.put("say", new Say());
		actionMap.put("balancecheck", new BalanceCheck());
		actionMap.put("movecheck", new MoveCheck());
		actionMap.put("balancecost", new BalanceCost());		
		actionMap.put("bleedeffect", new BleedEffect());	
		actionMap.put("defenceeffect", new DefenceEffect());	
		actionMap.put("weaponequippedcheck", new WeaponEquippedCheck());	
	}
	
	public static void displayActions(Mobile player) {
		StringBuilder actionTypes = new StringBuilder();
		actionTypes.append("The types of actions are:");
		for (String actionType : actionMap.keySet()) {
			actionTypes.append("\n");
			actionTypes.append(actionType);							
		}
		player.tell(actionTypes.toString());
	}
	
	public static Action selectAction(Mobile player) {
		String actionName = askQuestion("Which action do you want to add? Order does matter.", player);
		if (actionMap.containsKey(actionName)) {
			return actionMap.get(actionName).newBlock(player);
		} else if (actionName.equals("exit")) {
			return null;
		} else {
			player.tell("That is not a valid choice.");
			return selectAction(player);
		}
	}
	
	/***************************************************************************
	 * 
	 * ITEM CREATION
	 * 
	 ****************************************************************************/
	//TODO Should ItemBuilder handle what type of item to return.
	private boolean processCreateItem(Mobile player) {
		player.tell("OK! Lets get started with item creation.");
		player.tell("The choices of items we can create are:");
		ArrayList<String> itemTypes = new ArrayList<String>(Arrays.asList("stditem"));
		for (String itemType : itemTypes) {
			player.tell(itemType);
		}
		String itemTypeToMake = askQuestion("Which type of item would you like to build?", player);
		switch(itemTypeToMake) {
		case "stditem":
			return ItemBuilder.newItem(player);
		case "exit":
		case "quit":
			return false;
		default:
			return processCreateItem(player);
		}	
	}
	
	/***************************************************************************
	 * 
	 * LOCATION CREATION
	 * 
	 ****************************************************************************/
	private boolean processCreateLocation(Mobile player) {
		player.tell("OK! Lets get started with location creation.");
		return LocationBuilder.newLocation(player, new LocationBuilder());
	}
	/***************************************************************************
	 * 
	 * MOBILE CREATION
	 * 
	 ****************************************************************************/
	private boolean processCreateMobile(Mobile player) {
		return false;
	}
	
	
	
	
	
	public static String askQuestion(String question, Mobile player) {
		player.tell(question);
		String answer = player.getSendBack().getMessage();
		if (answer.equals("")) {
			player.tell("Typing nothing will not create something.");
			answer = askQuestion(question, player);
		}
		return answer.toLowerCase();
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='GODCREATE' AND BLOCKPOS=" + position + ";";
		return SQLInterface.returnBlockView(blockQuery);
	}
	@Override
	protected void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO BLOCK (BLOCKTYPE, BLOCKPOS) VALUES ('GODCREATE', " + position + ",);";
			try {
				SQLInterface.saveAction(sql);
			} catch (SQLException e) {
				System.out.println("Godcreate failed to save via sql : " + sql);
				e.printStackTrace();
			}
		}
	}

	@Override
	public Action newBlock(Mobile player) {
		player.tell("You should not be able to get this far.");
		return null;
	}
}
