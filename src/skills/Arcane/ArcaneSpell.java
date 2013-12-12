package skills.Arcane;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.HashMap;

import effects.*;

import processes.Location;
import processes.UsefulCommands;

public class ArcaneSpell extends Skill {
	
	private String name;
	private int damage;
	private String type;
	private String target;
	private int speed;
	private int mana;	
	private int life;
	private int cost;
	private Mobile currentPlayer;
	private HashMap<String, Integer> effectList;
	
	public ArcaneSpell(ArcaneBuilder build, Mobile currentPlayer) {
		this.name = build.getName();
		this.damage = build.getDamage();
		this.type = build.getType();
		this.target = build.getTarget();
		this.speed = build.getSpeed();
		this.mana = build.getMana();
		this.life = build.getLife();
		this.cost = build.getCost();
		this.effectList = build.getEffectList();
		this.currentPlayer = currentPlayer;
	}
	
	public String getName() {return name;}	
	public int getDamage() {return damage;}	
	public String getType() {return type;}	
	public String getTarget() {return target;}	
	public int getSpeed() {return speed;}	
	public int getMana() {return mana;}
	public HashMap<String, Integer> getEffects() {return new HashMap<String, Integer>(effectList);}
	
	// Called when attempting to cast the finished spell. May or may not have a target, depending on spell.
	public void perform(String target) {
		if (currentPlayer.hasMana(mana)) {
			Container loc = findLocation();
			if (loc != null) {
				Mobile castAt = findTarget(target, loc);
				if (castAt != null) {
					castSpell(loc, castAt);
				}
			}
		}		
	}
	
	public Container findLocation() {
		switch(type) {
			case "local":
				return currentPlayer.getContainer();
		}
		return null;
	}
	
	public Mobile findTarget(String target, Container loc) {
		Holdable castAt = UsefulCommands.stringToHoldable(target, loc);
		if (castAt instanceof Mobile) {
			return (Mobile)castAt;
		}
		return null;
	}
	
	public void castSpell(Container loc, Mobile target) {
//		target.takeDamage(damage);
		applyEffects(target);
		currentPlayer.affectMana(-mana);
//		currentPlayer.takeDamage(life);
		currentPlayer.tell("You cast " + name + " for " + damage + " costing " + mana + " mana, " + life + " life, " + speed + " speed, " + cost + " cost.");
	}
	
	private void applyEffects(Mobile target) {
		for (String effect : effectList.keySet()) {
			switch(effect) {
				case "bleed":
//					target.addEffect(effect, new Bleed(target, effectList.get(effect)));
					currentPlayer.tell("You've added bleed for " + effectList.get(effect) + " seconds.");
					break;
			}			
		}
	}
}
