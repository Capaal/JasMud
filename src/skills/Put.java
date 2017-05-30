package skills;

import java.util.Collection;
import java.util.Iterator;
import interfaces.Container;
import interfaces.Holdable;
import processes.ContainerErrors;
import processes.Skills;
import items.StackableItem;

public class Put extends Skills {

	private String itemName;
	private Holdable item;
	private String possibleContainer;
	private Container endContainer;
	private int qty; //may be dangerous to set here if used by muliple ppl at once

	public Put() {
		super.name = "put";
		super.description = "Place an item in a container, such as a pouch.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.FILLER);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.QUANTITY); //optional
	}	
	
	@Override //checks each container if previous is full
	protected void performSkill() {
		//sets item, container, qty
		if (!preSkillChecks()) {return;}
			//check qty/space/weight of container - errors?
		
		if (item instanceof StackableItem) {
			Collection<Holdable> containerList = currentPlayer.getListMatchingString(possibleContainer);
			StackableItem sItem = (StackableItem) item;
			ContainerErrors error = null;					
			Iterator<Holdable> i = containerList.iterator();
			
			// SOMEWHERE if I say put 30 in, but I have 28, it still puts in 30!! BUG
			if (sItem.getQuantity() < qty) {
				qty = sItem.getQuantity();
			}
			int origQty = qty;	
			
			while (i.hasNext() && qty > 0) {
				
				Container c = (Container) i.next();
				if (!(c instanceof Container)) {
					System.out.println("Bug? Reached stage with something that is not a container.");
					break;
				}
				int containerQty = c.getCurrentQty();
				error = sItem.moveHoldable(c,qty);
				if (error == null) { //only null if 1st try works
					messageSelf("You put " + origQty + " " + item.getName() + " in your " + endContainer.getName() + ".");
					return;
				} else {
					qty = qty - (c.getCurrentQty() - containerQty); //to handle remaining qty if some are put in
				}
			}
				
			//if exited out of while loop and still has not put away all herbs
			if (qty > 0 && qty != origQty) {
				messageSelf("You put " +  qty + " " + item.getName() + " in your " + endContainer.getName() + ".");
				return;
			} 
			//if all containers are full to begin with or wrong type
			if (qty == origQty) { 
				messageSelf(error.display(sItem.getName()));
				return;
			}
			System.out.println("Error Put performSkill after Stackable if, should not reach this line.");
			
		} else if (qty > 1) {
			item.moveHoldable(endContainer);
			int actualQty = 1;
			while (handleMultiple() && actualQty < qty) {
				actualQty += 1;				
			}
			messageSelf("You put " + actualQty + " " + itemName + " in your " + endContainer.getName() + ".");
			return;
		} else {
			item.moveHoldable(endContainer);
			messageSelf("You put the " + item.getName() + " in your " + endContainer.getName() + ".");
			return;
		}

		System.out.println("Error failure Put: performSkill last line. Should never reach this point.");
	}
	
	private boolean handleMultiple() {
		if(!checkItem()) {return false;}
		item.moveHoldable(endContainer);
		return true;
	}
	
		
	private boolean preSkillChecks() {
		itemName = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (itemName.equals("")) {
			messageSelf("What are you trying to put?");
			return false;
		}
		//find the item
		if (!checkItem()) {
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
		//tries to set endContainer
		Holdable aContainer = currentPlayer.getHoldableFromString(possibleContainer);
		if (aContainer == null) {
			aContainer = currentPlayer.getContainer().getHoldableFromString(possibleContainer);
			if (aContainer == null) {
				messageSelf("You don't see a \"" + possibleContainer + "\".");
				return false;
			}
		}
		if (!(aContainer instanceof Container)) {
			messageSelf("That \"" + possibleContainer + "\" is not a valid container.");
			return false;
		}
		endContainer = (Container)aContainer;		
		//checks optional qty
		qty = 1;
		if (!Syntax.QUANTITY.getStringInfo(fullCommand, this).equals("")) {			
			try {
				qty = Integer.parseInt(Syntax.QUANTITY.getStringInfo(fullCommand, this)); 
			} catch (NumberFormatException fail) {
				System.out.println("User error: 'Put' optional qty not a number. Optional ignored.");
			}
		}
		return true;
	}
	
	private boolean checkItem() {
		item = currentPlayer.getHoldableFromString(itemName);
		if (item == null) {
			return false;
		}
		return true;
	}
	
}
