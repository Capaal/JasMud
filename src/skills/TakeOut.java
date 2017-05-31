package skills;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;

import interfaces.Container;
import interfaces.Holdable;
import items.Herb;
import items.HerbPouch;
import items.ItemBuilder;
import items.StackableItem;
import items.StdItem;
import processes.ContainerErrors;
import processes.CreateWorld;
import processes.Skills;
import processes.Skills.Syntax;

public class TakeOut extends Skills {
	
	private String itemName;
	private String containerName;
	private Collection<Holdable> containerList;

//	private Container endContainer;
	private int qty; 

	public TakeOut() {
		super.name = "take";
		super.description = "Take an item out of a container, such as a pouch.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.FILLER);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.QUANTITY);
	}	

	@Override
	protected void performSkill() {
		if (!preSkillChecks()) {return;}
		Iterator<Holdable> i = containerList.iterator();
		int origQty = qty;
		while (i.hasNext() && qty > 0) {
			Container c = (Container) i.next();
			Holdable item = c.getHoldableFromString(itemName);		
			if (item != null) {				
				if (item instanceof StackableItem) { 
					StackableItem sItem = (StackableItem) item; 
					int qtyAvailable = sItem.getQuantity();
					ContainerErrors internal = sItem.moveHoldable(currentPlayer, qty); 				
					qty = qty - qtyAvailable;
				} else {					
					while (qty > 0) { // Loops through putting away items.
						item.moveHoldable(c);
						messageSelf("You take " + itemName + " from your " + c.getName() + ".");
						qty --;	
						item = c.getHoldableFromString(itemName);	
						if (item == null) {
							messageSelf("You don't have any more " + itemName + " to take out.");
							return;
						}								
					}			
				}
			}					
		}
		if (qty == 0) {
			messageSelf("You take " + origQty + " " + itemName + " from your " + containerName + ".");
		//if exited out of while loop and still has not put away all herbs
		} else if (qty > 0 && qty != origQty) {
			messageSelf("You take  " +  (origQty - qty) + " " + itemName + " from your " + containerName + "."); 
		//if all containers are full to begin with or wrong type
		} else { 
			messageSelf("You can't find " + itemName + " anywhere.");
		}
	}
		
	//checks all syntax is valid and finds container
	private boolean preSkillChecks() {
		//checks item word
		itemName = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (itemName.equals("")) {
			messageSelf("Specify an item.");
			return false;
		}
		//check filler word
		String in = Syntax.FILLER.getStringInfo(fullCommand, this);
		if (!in.equals("from")) {
			messageSelf("Syntax: TAKE (ITEM) FROM (CONTAINER).");
			return false;
		}
		//find container word
		containerName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (containerName.equals("")) {
			messageSelf("What are you trying to take that from?");
			return false;
		} 
		// Tries to set containerList.
		containerList = currentPlayer.getListMatchingString(containerName);
		if (containerList == null) { // If not in player's inventory
			containerList = currentPlayer.getContainer().getListMatchingString(containerName);
			if (containerList == null) { // If also not on the ground.
				messageSelf("You don't see a \"" + containerName + "\".");
				return false;
			}
		}
		for (Holdable h : containerList) {
			if (!(h instanceof Container)) {
				messageSelf("That \"" + containerName + "\" is not a valid container.");
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
	


}
