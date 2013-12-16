package skills.Arcane;

import interfaces.Action;
import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import processes.Type;


import processes.PlayerPrompt;
import processes.UsefulCommands;

public class SkillBuilder {
	
//	private final int DEFAULTDAMAGE = 10;
//	private final int DEFAULTMANA = 10;
//	private final int DEFAULTSPEED = 2000;	
	private String name;
	
//	private ArrayList<Queue<Action>> queueList = new ArrayList<Queue<Action>>();
	
	private Queue<Action> actions = new LinkedList<Action>();
	private ArrayList<Type> types = new ArrayList<Type>();
	
//	private Queue<Queue<Action>> stages = new LinkedList<Queue<Action>>();
	
//	private int damage = DEFAULTDAMAGE;
//	private String type = "local";
//	private Target target = Target.SINGLE;
//	private int speed = DEFAULTSPEED;
//	private int mana = DEFAULTMANA;
	
//	private String personalDesc = "";
//	private String canSeeDesc = "";
//	private String targetDesc = "";
	/*
	public enum Target {
		// Enum types.
		SINGLE() {
			@Override
			public ArrayList<Mobile> findTarget(String t, Container loc) {
				Holdable castAt = UsefulCommands.stringToHoldable(t, loc);
				if (castAt instanceof Mobile) {
					ArrayList<Mobile> targ = new ArrayList<Mobile>();
					targ.add((Mobile) castAt);
					return targ;
				}
				return null;
			}
		},
		ALL() {
			@Override
			public ArrayList<Mobile> findTarget(String t, Container loc) {
				ArrayList<Mobile> targets = new ArrayList<Mobile>();
				for (Holdable h : loc.getInventory()) {
					if (h instanceof Mobile) {
						targets.add((Mobile)h);
					}
				}
				return targets;
			}
		},
		
		ENEMIES() {
			@Override
			public ArrayList<Mobile> findTarget(String t, Container loc) {
				return null;
			}
		},
		ALLIES(){
			@Override
			public ArrayList<Mobile> findTarget(String t, Container loc) {
				return null;
			}
		},
		NONEMEIES(){
			@Override
			public ArrayList<Mobile> findTarget(String t, Container loc) {
				return null;
			}
		},
		NONALLIES() {
			@Override
			public ArrayList<Mobile> findTarget(String t, Container loc) {
				return null;
			}
		};

		//Constructor
		private Target() {			
		}		
		public abstract ArrayList<Mobile> findTarget(String t, Container loc);
	}
	*/
//	private HashMap<String, Integer> effectList = new HashMap<String, Integer>();
	
	private Mobile currentPlayer;
		

	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		this.name = UsefulCommands.getSecondWord(fullCommand);
		this.currentPlayer = playerPrompt.getCurrentPlayer();	
		
		
	}
	
	public void setup(Mobile mob, String fullCommand) {
		this.name = fullCommand;
		this.currentPlayer = mob;	
	
	}
	
	public String getName() {return name;}	
//	public int getDamage() {return damage;}	
//	public String getType() {return type;}	
//	public Target getTarget() {return target;}
//	public int getSpeed() {return speed;}	
//	public int getMana() {return mana;}
//	public String getPersonalDesc() {return personalDesc;}
//	public String getCanSeeDesc() {return canSeeDesc;}
//	public String getTargetDesc() {return targetDesc;}	
	public Queue<Action> getActions() {return new LinkedList<Action>(actions);}
	public ArrayList<Type> getTypes() {return new ArrayList<Type>(types);}
	

//	public HashMap<String, Integer> getEffectList() {return new HashMap<String, Integer>(effectList);}
	
	public void complete() {
	//	for (Queue<Action> q : queueList) {
	//		if (!stages.offer(q)) {
	//			System.out.println("Stages offering failed.");
	//		}
	//	}
		SkillBook skillList = currentPlayer.getBook("skillbook");		
		if (skillList != null && skillList instanceof SkillBook) {
			((SkillBook)skillList).addSkill(new Skill(this, currentPlayer));
		}
//		currentPlayer.acceptCommand(name, skillList);  // Should probably only happen onces, when made.
		toDefault();
	}
	
	public void complete(SkillBook skillBook) {
	//	for (Queue<Action> q : queueList) {
	//		if (!stages.offer(q)) {
	//			System.out.println("Stages offering failed.");
	//		}
	//	}
		skillBook.addSkill(new Skill(this, currentPlayer));	
//		currentPlayer.acceptCommand(name, skillBook);   // Should probably only happen onces, when made.
		toDefault();
	}
	
	public void addType(Type type) {
		this.types.add(type);
	}
	
	
		
	public void addAction(Action a) {
		actions.add(a);
	//	if (queueList.size() > position + 1) {
	//		q = queueList.get(position);
	//	} else {
	//		queueList.add(new LinkedList<Action>());
	//		q = queueList.get(position);
	//	}
	//	if (!q.offer(a)) {
	//		System.out.println("Action offer fail in a skill.");
	//	}
	}
/*	public void setDamage(String incDmg) {
		int newDamage = Integer.parseInt(incDmg);
		this.damage = newDamage;
	}
	
	public void setMana(String incMana) {
		int newMana = Integer.parseInt(incMana);
		this.mana = newMana;		
	}
	
	public void setSpeed(String incSpeed) {
		int newSpeed = Integer.parseInt(incSpeed);
		this.speed = newSpeed;
	}
	
	public void setTarget(String incTarget) {
		Target t = Target.valueOf(incTarget);
		if (t != null) {
			this.target = t;
		}
	}
	
	public void setEffect(String effect) {
		String effectType = UsefulCommands.getFirstWord(effect);
		int effectDuration = Integer.parseInt(UsefulCommands.getSecondWord(effect));
		if (UsefulCommands.isValidEffect(effectType) && !effectList.containsKey(effectType)) {
			effectList.put(effectType, effectDuration);
		}
	}
	
	public void removeEffect(String effect) {
		if (effectList.containsKey(effect)) {
			effectList.remove(effect);
		}
	}
	
	public void setPersonalDesc(String desc) {
		personalDesc = desc;
	}
	
	public void setCanSeeDesc(String desc) {
		canSeeDesc = desc;
	}
	
	public void setTargetDesc(String desc) {
		targetDesc = desc;
	}
	*/
	private void toDefault() {
	//	this.damage = DEFAULTMANA;
	//	this.mana = DEFAULTMANA;	
	//	this.speed = DEFAULTSPEED;		
	//	effectList = new HashMap<String, Integer>();
		this.actions.clear();
		this.types.clear();
	}
}