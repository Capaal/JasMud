package skills.Arcane;

import interfaces.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

import effects.*;

import processes.Type;
import processes.UsefulCommands;
//import skills.Arcane.SkillBuilder.Target;

public class Skill {
	
	private String name;
	private int damage;
	private String type;
	//private Target target;
	private int targetSlot;
	private int speed;
	private int mana;	
	private Mobile currentPlayer;
	private HashMap<String, Integer> effectList;
	
	private String personalDesc;
	private String canSeeDesc;
	private String targetDesc;
	
	private String fullCommand;
	
	private Queue<Action> actions;
	private ArrayList<Type> types;
	
//	private Queue<Queue<Action>> stages;
	
//	private Queue<Action> stageOne;
//	private Queue<Action> stageTwo;
//	private Queue<Action> stageThree;
	
	public Skill() {
	}
	
	public Skill(SkillBuilder build, Mobile currentPlayer) {
		this.name = build.getName();
		this.actions = build.getActions();
		this.types = build.getTypes();
	//	this.damage = build.getDamage();
	//	this.type = build.getType();
	//	this.target = build.getTarget();
	//	this.speed = build.getSpeed();
	//	this.mana = build.getMana();
	//	this.effectList = build.getEffectList();
	//	this.personalDesc = build.getPersonalDesc();
	//	this.canSeeDesc = build.getCanSeeDesc();
	//	this.targetDesc = build.getTargetDesc();
		this.currentPlayer = currentPlayer;
	}
	
	public String getName() {return name;}	
	
	private ArrayList<ArrayList<Mobile>> targetList = new ArrayList<ArrayList<Mobile>>();
//	public int getDamage() {return damage;}	
//	public String getType() {return type;}	
//	public Target getTarget() {return target;}	
//	public int getSpeed() {return speed;}	
//	public int getMana() {return mana;}
//	public HashMap<String, Integer> getEffects() {return new HashMap<String, Integer>(effectList);}
	
	// Called when attempting to cast the finished spell. May or may not have a target, depending on spell.
	public void perform(String fullCommand) {
		this.fullCommand = fullCommand;
		for (Action a : actions){
			if (a.activate(this) == false) {
				System.out.println(a + " returned false.");
				break;
			}
		//	if (!stageActions(q)) {
		//		System.out.println("stages failed?");
		//		break;
		//	}
	//	if (stageActions(stageOne)) {
	//		if (stageActions(stageTwo)) {
	//			stageActions(stageThree);
	//		}
		}/*
		if (currentPlayer.hasMana(mana)) {
			Container loc = findLocation();
			if (loc != null) {
				ArrayList<Mobile> castAt = target.findTarget(t, loc);
				if (castAt != null) {
					castSpell(loc, castAt);
				}
			}
		}	*/	
	}
	public ArrayList getTargets(int i) {
		return targetList.get(i);
	}
	
	public Container getLocation() {
		return currentPlayer.getContainer();
	}
	
	public void setTargets(int position, ArrayList<Mobile> targets) {
		targetList.add(position, targets);
	}
	
	public String getFullCommand() {
		return fullCommand;
	}
	
	public Mobile getCurrentPlayer() {
		return this.currentPlayer;
	}
	
	private boolean stageActions(Queue<Action> actionList) {
		for (Action a : actionList) {
			if (!a.activate(this)) {
				return false;
			}
		}
		return true;
	}
/*	
	public Container findLocation() {
		switch(type) {
			case "local":
				return currentPlayer.getContainer();
		}
		return null;
	}*/
	
//	public ArrayList<Mobile> findTarget(String t, Container loc) {
//		return target.findTarget(t, loc);
		
	//	Holdable castAt = UsefulCommands.stringToHoldable(target, loc);
	//	if (castAt instanceof Mobile) {
	//		return (Mobile)castAt;
	//	}
	//	return null;
	//}
/*	
	public void castSpell(Container loc, ArrayList<Mobile> targets) {
		for (Mobile m : targets) {
			Object[] arguments = {currentPlayer.getName(), m.getName()};		
			m.takeDamage(damage);
			m.tell(MessageFormat.format(targetDesc, arguments));
			applyEffects(m);
			currentPlayer.affectMana(-mana);
			currentPlayer.tell(MessageFormat.format(personalDesc, arguments));
			UsefulCommands.tellAllInRoom(MessageFormat.format(canSeeDesc, arguments), loc);
			currentPlayer.tell("You cast " + name + " for " + damage + " costing " + mana + " mana, " + speed + " speed.");
		}
	}*/
	
	/*private void applyEffects(Mobile target) {
		for (String effect : effectList.keySet()) {
			switch(effect) {
				case "bleed":
					target.addEffect(effect, new Bleed(target, effectList.get(effect)));
					currentPlayer.tell("You've added bleed for " + effectList.get(effect) + " seconds.");
					break;
			}			
		}
	}*/
}