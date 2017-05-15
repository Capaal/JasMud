package skills;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
	private int qty; //may be dangerous to set here if used by muliple ppl at once

	public Put() {
		super.name = "put";
		super.description = "Place an item in a container, such as a pouch.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.FILLER);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.QUANTITY);
	}	
	
	@Override
	protected void performSkill() {
		qty = 1;
		if (!preSkillChecks()) {return;}
		if (item instanceof Herb && endContainer instanceof HerbPouch) {
			if (currentPlayer.getHoldableFromString("herbpouch") == null) {
				messageSelf("You do not have any pouches.");
				return;
				}
			sortPouches();
			return;
		}
		messageSelf("Error failure Put: performSkill last line.");
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
		if (Syntax.QUANTITY.getStringInfo(fullCommand, this).equals("")) {
			return true; //only returning true because this is the LAST check
		} else {
			try {
				qty = Integer.parseInt(Syntax.QUANTITY.getStringInfo(fullCommand, this)); 
			} catch (NumberFormatException fail) {
				System.out.println("User error: 'Put' optional qty not a number. Optional ignored.");
			}
		}
		//DO NOT ADD MORE CHECKS WITHOUT READING QTY CHECK ABOVE
		return true;
	}
	
	private void sortPouches() {
		Collection<Holdable> inv = currentPlayer.getInventory().values();
		Iterator<Holdable> i = inv.iterator();
		int pouchQty = 0;
		Holdable possiblePouch;
		Herb herb = (Herb)item;
		HerbPouch pouch;
		int originalQty = qty;
		while (i.hasNext() && (qty > 0)) {
			possiblePouch = i.next();
		     if (possiblePouch.getName().equalsIgnoreCase("herbpouch")) {
		          if (possiblePouch instanceof HerbPouch) {
		               pouch = (HerbPouch)possiblePouch;
		               pouchQty = pouch.getHerbQty();
		               if (pouchQty < 1000) { addToPouch(pouch, herb); }
				 }
		    }
		}
		if (qty == originalQty) {
			messageSelf("Your pouches are too full or are used for other herbs."); 		               
			return;
			// expand else to specify if taken by other herbs or full or both TODO
			// add else if player has only 1 pouch TODO
		} else if (qty > 0 && qty != originalQty) {
			messageSelf("You put " + (originalQty-qty) + " " + herb.getName() +" in your pouch."); //not enough pouches
			return;
		} else {
			messageSelf("You put " + originalQty + " " + herb.getName() + " in your pouch.");
			return;
		}
		
	}
	
	// changes 'qty' tp how many could not go in pouch
	private void addToPouch(HerbPouch pouch, Herb herb) {
		if ((pouch.getHerbType() == null) || pouch.getHerbType().equals(herb.getHerbType())) {
			int actualQty = pouch.changeHerbs(qty,herb.getHerbType());
			herb.removeFromWorld(); //qty?
		//	messageSelf("You put " +  actualQty + " herbs in your pouch."); could have optional to note specific pouch used
			qty = qty - actualQty;
		}
		
	}

}
