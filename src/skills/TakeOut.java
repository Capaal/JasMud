package skills;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import interfaces.Container;
import interfaces.Holdable;
import items.Herb;
import items.HerbPouch;
import items.ItemBuilder;
import processes.CreateWorld;
import processes.Skills;
import processes.Skills.Syntax;

public class TakeOut extends Skills {
	
	private String itemName;
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
		qty = -1;
		if (!preSkillChecks()) {return;}
		
		if (possibleContainer instanceof HerbPouch) {
			if (currentPlayer.getHoldableFromString("herbpouch") == null) {
				messageSelf("You do not have any pouches.");
				return;
				}
			sortPouches();
			return;
		} else if (possibleContainer instanceof Container) {
			Holdable item = ((Container) possibleContainer).getHoldableFromString(itemName);
			if (qty > 1) {
				for (int i=0;i<qty;i++) {
					item.moveHoldable(currentPlayer);
				}
				messageSelf("You take " + qty + " " + item.getName() + " out of your " + possibleContainer.getName());
				return;
			} else {
				item.moveHoldable(currentPlayer);
				messageSelf("You take the " + item.getName() + " out of your " + possibleContainer.getName());
				return;
			}
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
	/*	if (possibleContainer instanceof Container) {
			endContainer = (Container) possibleContainer;
		} else {
			messageSelf("You can't take anything out of that.");
			return false;
		} 
		if (endContainer.isEmpty()) {
			messageSelf("Your " + endContainer.getName() + " is emtpy.");
			return false;
		} */
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
	
	private void sortPouches() {
		Collection<Holdable> inv = currentPlayer.getInventory().values();
		Iterator<Holdable> i = inv.iterator();
		int pouchQty = 0;
		Holdable possiblePouch;
		HerbPouch pouch = null;
		int originalQty = qty;
		while (i.hasNext() && (qty < 0)) {
			possiblePouch = i.next();
		     if (possiblePouch.getName().equalsIgnoreCase("herbpouch")) {
		          if (possiblePouch instanceof HerbPouch) {
		               pouch = (HerbPouch)possiblePouch;
		               pouchQty = pouch.getHerbQty();
		               if (pouchQty > 0 && (pouch.getHerbType().toString().equalsIgnoreCase(itemName))) { 
		           		int actualQty = pouch.changeHerbs(qty,null);
		        		qty = qty - actualQty;
		               }
				 }
		    }
		}
		if (qty == originalQty) {
			messageSelf("You don't have any \"" + itemName.toLowerCase() + "\" in your pouches."); 		               
			return;
			// expand else to specify if taken by other herbs or full or both TODO
			// add else if player has only 1 pouch TODO
		} else if (qty > 0 && qty != originalQty) {
			createHerbs(originalQty-qty, pouch);
			messageSelf("You remove " + -(originalQty-qty) + " " + itemName +" from your pouch."); //not enough in pouches
			return;
		} else {
			createHerbs(originalQty, pouch);
			messageSelf("You remove " + originalQty + " " + itemName + " from your pouch.");
			return;
		}
		
	}
	
	private void createHerbs(int qtyToCreate, HerbPouch pouch) {
		Map<String, ItemBuilder> allItemTemplates = CreateWorld.viewItemTemplates();
		ItemBuilder toCreate = allItemTemplates.get(itemName); 
		toCreate.setItemContainer(currentPlayer);
		toCreate.setQuantity(qtyToCreate);
		toCreate.complete();
	}

}
