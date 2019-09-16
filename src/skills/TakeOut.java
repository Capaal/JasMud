package skills;

import java.util.Collection;
import java.util.Iterator;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.ContainerErrors;
import processes.Skills;

public class TakeOut extends Skills {
	
	private String itemName;
	private String containerName;
	private Collection<Holdable> containerList;
	private int origQty; 
	private int qty;

	public TakeOut(Mobile currentPlayer, String fullCommand) {
		super("take", "Taking items from containers.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.FILLER);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.QUANTITY);
	}	

	@Override
	protected void performSkill() {
		takeOut();		
	}
		
	//checks all syntax is valid and finds container
	@Override
	protected boolean preSkillChecks() {
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
		if (containerList.size() == 0) { // If not in player's inventory
			containerList = currentPlayer.getContainer().getListMatchingString(containerName);
			if (containerList.size() == 0) { // If also not on the ground.
				messageSelf("You don't see a \"" + containerName + "\".");
				return false;
			}
		}
		for (Holdable h : containerList) {
			if (!(h instanceof Container)) {
				messageSelf("That \"" + containerName + "\" is not a valid container.");
				return false;
			}
			containerName = "";
			if (containerName.equals("")) {containerName = h.getName();};
		}		
		//checks optional qty
		origQty = 1;
		if (!Syntax.QUANTITY.getStringInfo(fullCommand, this).equals("")) {
			try {
				origQty = Integer.parseInt(Syntax.QUANTITY.getStringInfo(fullCommand, this)); 
			} catch (NumberFormatException fail) {
				System.out.println("User error: 'Put' optional qty not a number. Optional ignored.");
			}
		}
		return true;
	}
	
	private void takeOut() {
		Iterator<Holdable> containersIterator = containerList.iterator();
		qty = origQty;
		String error = takeOutRecursion(containersIterator, (Container)containersIterator.next());
		if (error != null) {
			messageSelf(error);
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
	
	private String takeOutRecursion(Iterator<Holdable> containersIterator, Container c) {
		if (qty != 0) {			
			Holdable item = c.getHoldableFromString(itemName);		
			if (item != null) {	
				int qtyAvailable = item.getQuantity();
				int qtyToTry = qty;
				if (qty > qtyAvailable) {
					qtyToTry = qtyAvailable;
				}
				ContainerErrors error = item.moveHoldable(currentPlayer, qtyToTry); 	
				if (error != null) {
					return error.display(c.getName());					
				} else {
					qty -= qtyToTry;	
					return takeOutRecursion(containersIterator, c);
				}				
			} else {
				if (containersIterator.hasNext()) {
					c = (Container) containersIterator.next();
					return takeOutRecursion(containersIterator, c);
				}
			}
		}
		return null;		
	}
}
