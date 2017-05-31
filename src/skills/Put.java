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
	private Collection<Holdable> containerList;
	private int qty;

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
			moveStack();		
		} else {
			Iterator<Holdable> i = containerList.iterator();
			while (i.hasNext() && qty > 0) { // Loops through containers
				Container c = (Container) i.next();			
				while (qty > 0) { // Loops through putting away items.
					if(!checkItem()) { // If out of things to put away
						messageSelf("You don't have any more " + itemName + " to put away.");
						return;
					}
					item.moveHoldable(c);
					messageSelf("You put " + itemName + " in your " + c.getName() + ".");
					qty --;			
				}	
			}				
		}
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
		if (!in.equalsIgnoreCase("in")) {
			messageSelf("Syntax: PUT (ITEM) IN (CONTAINER).");
			return false;
		}
		//find target container
		possibleContainer = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (possibleContainer.equals("")) {
			messageSelf("What are you trying to put that in?");
			return false;
		} 		
		// Tries to set containerList.
		containerList = currentPlayer.getListMatchingString(possibleContainer);
		if (containerList == null) { // If not in player's inventory
			containerList = currentPlayer.getContainer().getListMatchingString(possibleContainer);
			if (containerList == null) { // If also not on the ground.
				messageSelf("You don't see a \"" + possibleContainer + "\".");
				return false;
			}
		}
		for (Holdable h : containerList) {
			if (!(h instanceof Container)) {
				messageSelf("That \"" + possibleContainer + "\" is not a valid container.");
				return false;
			}
		}		
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
	
	private void moveStack() {
		StackableItem sItem = (StackableItem) item;
		ContainerErrors error = null;					
		Iterator<Holdable> i = containerList.iterator();
		if (sItem.getQuantity() < qty) {
			qty = sItem.getQuantity();
		}
		int origQty = qty;				
		while (i.hasNext() && qty > 0) {
			Container c = (Container) i.next();				
			int containerQty = c.getCurrentQty();
			error = sItem.moveHoldable(c,qty);
			qty = qty - (c.getCurrentQty() - containerQty); //to handle remaining qty if some are put in
		}
		if (qty == 0) {
			messageSelf("You put " + origQty + " " + sItem.getName() + " in your " + possibleContainer + ".");
		//if exited out of while loop and still has not put away all herbs
		} else if (qty > 0 && qty != origQty) {
			messageSelf("You put " +  (origQty - qty) + " " + sItem.getName() + " in your " + possibleContainer + "."); 
		//if all containers are full to begin with or wrong type
		} else { 
			// BUG: returned: "That aloe is full." when the bag was full.
			messageSelf(error.display(sItem.getName()));
		}
	}	
}
