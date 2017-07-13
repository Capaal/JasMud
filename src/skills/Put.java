package skills;

import java.util.Collection;
import java.util.Iterator;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.ContainerErrors;
import processes.Skills;

public class Put extends Skills {

	private String itemName;
	private Holdable item;
	private String possibleContainer;
	private Collection<Holdable> containerList;
	private int originalQty;
	private int qty;

	public Put(Mobile currentPlayer, String fullCommand) {
		super("put", "Place an item in a container.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.FILLER);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.QUANTITY); //optional
	}	
	
	@Override //checks each container if previous is full
	protected void performSkill() {
		//sets item, container, qty
		if (preSkillChecks()) {
			putItem();
		}
	}
	
	private void putItem() {		
		Iterator<Holdable> containerIterator = containerList.iterator();
		qty = originalQty;
		String error = moveItemRecursion(containerIterator, (Container)containerIterator.next());
		if (error != null) {
			messageSelf(error);
		} else {
			if (qty == 0) {
				messageSelf("You put " + originalQty + " " + itemName + " in your " + possibleContainer + ".");
			//if exited out of while loop and still has not put away all herbs
			} else if (qty > 0 && qty != originalQty) {
				messageSelf("You put " +  (originalQty - qty) + " " + itemName + " in your " + possibleContainer + "."); 
			//if all containers are full to begin with or wrong type
			}
		}
	}
	
	private String moveItemRecursion(Iterator<Holdable> containerIterator, Container c) {
		if (qty != 0) {	
			double spaceAvailable = (c.getMaxWeight() - c.getCurrentWeight());
			int allowedQty = (int) (spaceAvailable * item.getWeight());
			int qtyToTry = qty;
			if (qty > allowedQty) {
				qtyToTry = allowedQty;
			}
			int qtyAvailable = item.getQuantity();
			if (qtyToTry > qtyAvailable) {
				qtyToTry = qtyAvailable;
			}			
			ContainerErrors error = item.moveHoldable(c, qtyToTry); 	
			if (error != null) {
				if (error.equals(ContainerErrors.QTYFULL) && containerIterator.hasNext()) {
					return moveItemRecursion(containerIterator, (Container)containerIterator.next());
				} else {
					return error.display(c.getName());
				}
			} else {
				qty -= qtyToTry;	
				if (qtyToTry == qtyAvailable) {
					if (!checkItem()) {
						return null;
					}
				}
				return moveItemRecursion(containerIterator, c);
							
			}
		}
		return null;			
	}
	
	protected boolean preSkillChecks() {
		itemName = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (itemName.equals("")) {
			messageSelf("What are you trying to put?");
			System.out.println("Put precheck: no item specified.");
			return false;
		}
		//find the item
		if (!checkItem()) {
			messageSelf("You do not have a \"" + itemName + "\".");
			System.out.println("Put precheck: item not in inv.");
			return false;
		} 
		//check filler word
		String in = Syntax.FILLER.getStringInfo(fullCommand, this);
		if (!in.equalsIgnoreCase("in")) {
			messageSelf("Syntax: PUT (ITEM) IN (CONTAINER).");
			System.out.println("Put precheck: invalid filler (in).");
			return false;
		}
		//find target container
		possibleContainer = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (possibleContainer.equals("")) {
			messageSelf("What are you trying to put that in?");
			System.out.println("Put precheck: no container.");
			return false;
		} 		
		// Tries to set containerList.
		containerList = currentPlayer.getListMatchingString(possibleContainer);
		if (containerList == null || containerList.isEmpty()) { // If not in player's inventory
			containerList = currentPlayer.getContainer().getListMatchingString(possibleContainer);
			if (containerList == null || containerList.isEmpty()) { // If also not on the ground.
				messageSelf("You don't see a \"" + possibleContainer + "\".");
				System.out.println("Put precheck: no container in loc or inv.");
				return false;
			}
		}
		for (Holdable h : containerList) {
			if (!(h instanceof Container)) {
				messageSelf("That \"" + possibleContainer + "\" is not a valid container.");
			System.out.println("Put precheck: attempted container invalid.");
				return false;
			}
		}		
		possibleContainer = containerList.iterator().next().getName();
		//checks optional qty
		originalQty = 1;
		if (!Syntax.QUANTITY.getStringInfo(fullCommand, this).equals("")) {			
			try {
				originalQty = Integer.parseInt(Syntax.QUANTITY.getStringInfo(fullCommand, this)); 
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
	
	@Override
	public String displaySyntax() {
		return "PUT [ITEM] IN [CONTAINER] (QTY)";
	}
}
