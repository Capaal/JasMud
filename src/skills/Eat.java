package skills;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import interfaces.Holdable;
import interfaces.Mobile;
import items.Plant;
import items.Plant.PlantType;
import items.Pouch;
import processes.InductionSkill;
import processes.Skills;
import processes.WorldServer;

//currently only for herbs
public class Eat extends Skills {
	
	private String itemName;
	private Collection<Holdable> containerList;
	private Plant finalHerb;
	
	public Eat(Mobile currentPlayer, String fullCommand) {
		super("eat", "Eating things.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}	

	@Override
	protected void performSkill() {
		if(preSkillChecks()) {	
			PlantType pt = finalHerb.getPlantType();
			if (!currentPlayer.checkCooldown(pt)) {
				messageSelf(finalHerb.use(currentPlayer));
				triggerCooldown(finalHerb.getPlantType(), finalHerb.getPlantType().COOLDOWN);
			} else {
				messageSelf("That herb won't have any effect so soon.");
			}
			messageOthers(currentPlayer.getNameColored() + " eats a " + finalHerb.getName() + ".", Arrays.asList(currentPlayer));
			finalHerb.removeFromStack(1); 	
		}
	}
	
	@Override
	protected boolean preSkillChecks() {
		itemName = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (itemName.equals("")) {
			messageSelf("What are you trying to eat?");
			return false;
		}
		//find the item
		//need to check if eatName is an herbtype?
		Holdable itemToEat = currentPlayer.getHoldableFromString(itemName); //in inventory
		if (itemToEat == null) { //null = herb not in inv (maybe in pouch)		
			try {
				PlantType.valueOf(itemName.toUpperCase()); 
			} catch (IllegalArgumentException e) {
				messageSelf("You don't see a \"" + itemName + "\" to eat.");
				return false; //not an herb
			}
			
			// Tries to set containerList.
			containerList = currentPlayer.getListMatchingString("pouch");
			if (containerList == null) { // If not in player's inventory
				containerList = currentPlayer.getContainer().getListMatchingString("pouch");
				if (containerList == null) { // If also not on the ground.
					messageSelf("You can't find a " + itemName + " anywhere.");
					return false;
				}
			}	
			for (Holdable h : containerList) {
				Pouch pouch = (Pouch) h;
				Plant herb = (Plant) pouch.getHoldableFromString(itemName);
				if (herb != null) {
					finalHerb = herb;
					return true;
				}
			}	
			messageSelf("You have no " + itemName + ".");
			return false;
		//if item is in inv and is an herb
		} else if (itemToEat instanceof Plant) { 
			finalHerb = (Plant) itemToEat;
			return true;
		} else {
			messageSelf("You cannot eat the \"" + itemName + "\".");
			return false;
		}
	}
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Eat(currentPlayer, fullCommand);
	}

	// Called to start cooldown period.
		protected  void triggerCooldown(PlantType p, int length) {
			currentPlayer.cooldownOn(p);
			offCooldownIn(p,length);
		}
		
		// Called when cooldown period ends. Override to add messages. But call super.setOffCooldown()
		protected  void setOffCooldown(PlantType p) {
			currentPlayer.cooldownOff(p);
			messageSelf("You can eat a " + p.toString().toLowerCase() + " again.");
		}

		private void offCooldownIn(PlantType p, int duration) {
			if (duration <= 0) {
				throw new IllegalArgumentException("Invalid duration " + duration);
			}
			CooldownWrapper wrapper = new CooldownWrapper(this, p);
			WorldServer.getGameState().getEffectExecutor().schedule(wrapper, duration, TimeUnit.MILLISECONDS);				
		}
		
			protected class CooldownWrapper implements Runnable {		
				Eat wrappedSkill;		
				PlantType p;
				public CooldownWrapper(Eat s, PlantType p) {
					this.p = p;
					wrappedSkill = s;
				}			
				public void run() {
					wrappedSkill.setOffCooldown(p);			
				}
			}			
	
	
}
