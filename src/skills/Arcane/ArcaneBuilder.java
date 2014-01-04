package skills.Arcane;

import interfaces.Effect;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.HashMap;

import effects.*;
import processes.Command;
import processes.PlayerPrompt;
import processes.SkillBuilder;
import processes.UsefulCommands;

//Class used as the player plans out a spell, once "complete" it'll create a new Spell(this)
public class ArcaneBuilder extends SkillBuilder implements Command {
	
	private final int DEFAULTDAMAGE = 10;
	private final int DEFAULTMANA = 10;
	private final int DEFAULTLIFE = 0;
	private final int DEFAULTSPEED = 2000;
	private final int DEFAULTCOST = 0;
	
	private String name;
	private int damage = DEFAULTDAMAGE;
	private String type = "local";
	private String target = "target";
	private int speed = DEFAULTSPEED;
	private int mana = DEFAULTMANA;
	private int life = DEFAULTLIFE;
	private int cost = DEFAULTCOST;
	
	private HashMap<String, Integer> effectList = new HashMap<String, Integer>();
	
	private boolean speedLocked = false;
	private boolean manaLocked = false;
	private boolean lifeLocked = false;
	private boolean costLocked = false;
	
	private Mobile currentPlayer;
	
	public ArcaneBuilder() {		
	}
	
	@Override
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		this.name = UsefulCommands.getSecondWord(fullCommand);
		this.currentPlayer = playerPrompt.getCurrentPlayer();	
	}
	
	public String getName() {return name;}	
	public int getDamage() {return damage;}	
	public String getType() {return type;}	
	public String getTarget() {return target;}
	public int getSpeed() {return speed;}	
	public int getMana() {return mana;}
	public int getLife() {return life;}
	public int getCost() {return cost;}
	public HashMap<String, Integer> getEffectList() {return new HashMap<String, Integer>(effectList);}
	
	public void complete() {
		ArcaneBook cast = (ArcaneBook) currentPlayer.getBook("arcanebook");
		if (cast != null && cast instanceof ArcaneBook) {
			((ArcaneBook)cast).addSkill(new ArcaneSpell(this, currentPlayer));
		}
		toDefault();
	}
	// IF they set damage to 60, then the costs have to eat that increase, which is equal to ~100.
	// With just mana that means mana gets set to 100, if there is mana and life, then each equals 50. Or 1/2 cost.
	// With mana + life + speed then they all eat 1/3, mana=33, life=33, speed doesn't eat at a y=x ratio.
	// So I'll have to figure out that, but it should have a formula that defines its ratio.
	private void adjustCost(int adjust) {		
		int adjustPer = (int) adjust/(checkLocked());
		if (!manaLocked)
			this.mana = adjustPer  + DEFAULTMANA;
		if (!lifeLocked)
			this.life = adjustPer;
		if (!speedLocked)
			this.speed = adjustPer * 200 + DEFAULTSPEED;
		if (!costLocked)
			this.cost = adjustPer * 1000;
	}
	
	private int checkLocked() {
		int lockedCount = 4;
		if (manaLocked) 
			lockedCount--;
		if (lifeLocked)
			lockedCount--;
		if (speedLocked)
			lockedCount--;
		if (costLocked)
			lockedCount--;
		return lockedCount;
	}
	
	public void setDamage(String incDmg) {
		int newDamage = Integer.parseInt(incDmg);
		int adjust = (int) (Math.pow(newDamage, 2) * .0171 + .5714 * newDamage + 1.5714);
		adjustCost(adjust);
		this.damage = newDamage;
	}
	
	public void setMana(String incMana) {
		if (checkLocked() > 1) {
			int newMana = Integer.parseInt(incMana);
			int adjust = (int) -newMana;
			adjustCost(adjust);
			this.mana = newMana;		
			manaLocked = true;
		} else {
			currentPlayer.tell("You have already locked in every other cost balancer.");
		}
	}
	
	public void setLife(String incLife) {
		if (checkLocked() > 1) {
			int newLife = Integer.parseInt(incLife);
			int adjust = (int) -newLife;
			adjustCost(adjust);
			this.life = newLife;
			lifeLocked = true;
		} else {
			currentPlayer.tell("You have already locked in every other cost balancer.");
		}
	}
	
	public void setSpeed(String incSpeed) {
		if (checkLocked() > 1) {
			int newSpeed = Integer.parseInt(incSpeed);
			int adjust = (int) -newSpeed;
			adjustCost(adjust);
			this.speed = newSpeed;
			speedLocked = true;
		} else {
			currentPlayer.tell("You have already locked in every other cost balancer.");
		}
	}
	
	public void setCost(String incCost) {
		if (checkLocked() > 1) {		
			int newCost = Integer.parseInt(incCost);
			int adjust = (int) -newCost;
			adjustCost(adjust);
			this.cost = newCost;
			costLocked = true;
		} else {
			currentPlayer.tell("You have already locked in every other cost balancer.");
		}
	}
	
	public void setEffect(String effect) {
		String effectType = UsefulCommands.getFirstWord(effect);
		int effectDuration = Integer.parseInt(UsefulCommands.getSecondWord(effect));
		if (UsefulCommands.isValidEffect(effectType) && !effectList.containsKey(effectType)) {
			effectList.put(effectType, effectDuration);
		}
		adjustCost(effectCost(effect, effectDuration));
	}
	
	public void removeEffect(String effect) {
		if (effectList.containsKey(effect)) {
			effectList.remove(effect);
			adjustCost(-effectCost(effect, effectList.get(effect)));
		}
	}
	
	private int effectCost(String effect, int duration) {
		switch(effect) {
			case "bleed":
				return duration * 5;
		}
		return 0;
	}
	
	private void toDefault() {
		this.damage = DEFAULTMANA;
		this.mana = DEFAULTMANA;
		manaLocked = false;
		this.life = DEFAULTLIFE;
		lifeLocked = false;
		this.speed = DEFAULTSPEED;
		speedLocked = false;
		this.cost = DEFAULTCOST;
		costLocked = false;
		
		effectList = new HashMap<String, Integer>();
		
		// Should also set all locked costs to unlocked, or however I decide that should work.
	}
}
