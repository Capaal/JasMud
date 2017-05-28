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
	private Holdable item;
	private String containerName;
	private Holdable possibleContainer;

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
		qty = 1;
		if (!preSkillChecks()) {return;}
		
		if (possibleContainer instanceof Container) {
			item = ((Container) possibleContainer).getHoldableFromString(itemName);
			int actualQty = 0;
			actualQty = handleMultiple(actualQty);
			
			if (actualQty == 0) {
				messageSelf("You can't find any \"" + itemName + "\" in your " + possibleContainer.getName() + ".");
			} else if (qty > 1) {
				messageSelf("You take " + actualQty + " " + item.getName() + " out of your " + possibleContainer.getName() + ".");
			} else if (actualQty == 1 && qty == 1) {
				messageSelf("You take the " + item.getName() + " out of your " + possibleContainer.getName());
			} else {
				messageSelf("Not sure what happened here.");
			}
		}
	}
	
	//sorts through all containers of same name and takes qty from container
	//this handles 1 too
	private int handleMultiple(int actualQty) {
		NavigableMap <String,Holdable> containerList = currentPlayer.getListMatchingString(containerName);
		Iterator<Holdable> i = containerList.values().iterator();
		while (i.hasNext()) {
			Container c = (Container) i.next();
			Holdable itemInContainer = c.getHoldableFromString(itemName);  
			if (itemInContainer instanceof StackableItem) { 
				StackableItem sItem = (StackableItem) itemInContainer; 
				if (qty > sItem.getQuantity()) {
					actualQty = sItem.getQuantity();
				} else {actualQty = qty;}
				ContainerErrors internal = sItem.moveHoldable(currentPlayer, qty); 
				if (internal != null) {System.out.println("TakeOut handleMultiple error: " + internal.toString());}
				item = itemInContainer;
				return actualQty;
			} else if (itemInContainer instanceof StdItem) {
				actualQty += 1;
				if (actualQty <= qty) {
					itemInContainer.moveHoldable(currentPlayer);
					item = itemInContainer;
					handleMultiple(actualQty);
				}
			}
		}
		return actualQty;
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
			messageSelf("What are you trying to put that in?");
			return false;
		} 
		//checks if potential container exists (inventory or location)
		possibleContainer = currentPlayer.getHoldableFromString(containerName);
		if (possibleContainer == null) { //not in inventory, next line tries location
			possibleContainer = currentPlayer.getContainer().getHoldableFromString(containerName);
			if (possibleContainer == null) {
				messageSelf("You don't see a " + containerName + ".");
				return false;
			}
		}
		//checks item found is a container and not empty
		
		//checks optional qty
		if (Syntax.QUANTITY.getStringInfo(fullCommand, this).equals("")) {
			return true; //only returning true because this is the LAST check
		} else {
			try {
				qty = Integer.parseInt(Syntax.QUANTITY.getStringInfo(fullCommand, this)); 
			} catch (NumberFormatException fail) {
				System.out.println("User error: 'Put' optional qty not a number. Optional ignored.");
			}
		}
		return true;
	}
	


}
