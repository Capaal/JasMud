package skills;

import interfaces.Holdable;
import items.Herb;
import items.Herb.HerbType;
import processes.Skills;
import processes.Skills.Syntax;
import items.HerbPouch;

public class Put extends Skills {

	private String itemName;
	private Holdable item;
	private String possibleContainer;
	private Holdable endContainer;

	public Put() {
		super.name = "put";
		super.description = "Place an item in a container, such as a pouch.";
		super.syntaxList.add(Syntax.SKILL);
		//err how to handle qty shit
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.FILLER);
		super.syntaxList.add(Syntax.TARGET);
	}	
	
	@Override
	protected void performSkill() {
		if (!preSkillChecks()) {return;}
		if (item instanceof Herb && endContainer instanceof HerbPouch) {
			handleHerb();
			return;
		}
		messageSelf("What?");
	}
		
	private boolean preSkillChecks() {
		itemName = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (itemName.equals("")) {
			messageSelf("What are you trying to put?");
			return false;
		}
		//find the item
		item = currentPlayer.getHoldableFromString(itemName);
		if (itemName == null) {
			messageSelf("You do not have a \"" + itemName + "\".");
			return false;
		}
		//check filler word
		String in = Syntax.FILLER.getStringInfo(fullCommand, this);
		if (!in.equals("in")) {
			messageSelf("Syntax: PUT (ITEM) IN (CONTAINER).");
			return false;
		}
		//find target container
		possibleContainer = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (possibleContainer.equals("")) {
			messageSelf("What are you trying to put that in?");
			return false;
		} 
		endContainer = currentPlayer.getHoldableFromString(possibleContainer);
		if (endContainer == null) {
			endContainer = currentPlayer.getContainer().getHoldableFromString(possibleContainer);
			if (endContainer == null) {
				messageSelf("You don't see a " + possibleContainer + ".");
				return false;
			}
		}
		return true;
	}
	
	private void handleHerb() {
		//for all pouches player holds TODO
		//need to handle rejecting some/all if pouch full or no pouch available
		Herb herb = (Herb)item;
		HerbPouch pouch = (HerbPouch)endContainer;
		//qty: check qty herb in pouch to see remaining to leave in inv or if full
		if (pouch.getHerbType() == null) {
			pouch.addHerbs(1,herb.getHerbType());
			item.removeFromWorld();
			messageSelf("You put the herb in your pouch.");
		} else if (pouch.getHerbType().equals(herb.getHerbType())) {
			if (pouch.changeHerbs(1)) {
				item.removeFromWorld();
				messageSelf("You put the herb in your pouch.");
			} else {
				messageSelf("Your pouch is too full.");
			}	
		} else { //pouch has a different herb
			messageSelf("Your pouches are all full or used for other herbs.");
		}
		
	}

}
