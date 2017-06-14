package skills;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import interfaces.Holdable;
import items.ItemBuilder;
import items.StackableItem;
import items.StdItem;
import processes.CreateWorld;
import processes.Skills;

public class CraftItem extends Skills {

	private int quantity; //default will make 1
	private ItemBuilder copyThis;
	private String itemToMake;
	private Map<String, ItemBuilder> allItemTemplates;
	
	public CraftItem() {
		super.name = "craft";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.TIMES);
		allItemTemplates = CreateWorld.viewItemTemplates(); //maybe list of only craftable items?
	}
	
	protected void performSkill() {
		itemToMake = Syntax.ITEM.getStringInfo(fullCommand, this);		
		if (!preSkillChecks()) {return;}		
		List<StdItem> componentsNeeded = copyThis.getComponents();
		while (quantity > 0) {			
			Iterator<StdItem> it = componentsNeeded.iterator();
			TreeMap<String, Holdable> copyPlayerInv = currentPlayer.getInventory();
			if (!checkRemoveCreate(it, copyPlayerInv)) {
				return;
			}
		}
	}	
	
	private boolean checkRemoveCreate(Iterator<StdItem> it, TreeMap<String, Holdable> copyPlayerInv) {
		StdItem componentToTest;
		if (it.hasNext()) {
			componentToTest = it.next();
			int qtyNeed = componentToTest.getQuantity();
			Holdable posItem = copyPlayerInv.ceilingEntry(componentToTest.getName()).getValue();
			if (posItem != null && posItem.getName().equalsIgnoreCase(componentToTest.getName())) {					
				int qtyHave = posItem.getQuantity();
				if ( qtyNeed > qtyHave ) {
					messageSelf("You are missing the component: " + componentToTest.getQuantity() + " " + componentToTest.getName());
					return false;
				}
				if (qtyNeed == qtyHave) {
				//	copyPlayerInv.remove(posItem);
					copyPlayerInv.remove(componentToTest.getName());
				}
				boolean success = checkRemoveCreate(it, copyPlayerInv);
				//once checked
				if (success) {thenRemove(posItem, qtyNeed);}
			} else {
				messageSelf("You are missing the component: " + componentToTest.getName());
				return false; //else return false? does this even end the recursion?
			}
		} else {
			andCreate();
		}
		return true;
	}
	
	private void thenRemove(Holdable thisItem, int qty) {
		if (qty == thisItem.getQuantity()) {
			thisItem.removeFromWorld();
			thisItem.delete();			
		} else {
			thisItem.removeFromStack(qty);
		}
	}
	
	private void andCreate() {
		try {
			copyThis.setQuantity(quantity);
		} catch (IllegalArgumentException e) {
			copyThis.setQuantity(1);
		}
		copyThis.setItemContainer(currentPlayer);
		copyThis.complete();
		messageSelf("You have created: " + copyThis.getQuantity() + " " + copyThis.getName() + ".");		
		quantity -= copyThis.getQuantity();
	}
	
	private boolean preSkillChecks() {
		if (itemToMake == "") {
			messageSelf("What are you trying to make? CRAFT LIST for all craftables."); //fail from no item specified
			return false;
		}		
		if (itemToMake.equals("list")) {
			listCraftables();
			return false;
		}		
		copyThis = allItemTemplates.get(itemToMake);
		if (copyThis == null) {
			messageSelf("That is not an item you are able to make."); //fail from no template for item specified
			return false;
		}		
		//looking for TIMES specified:
		quantity = 1;
		if (!Syntax.TIMES.getStringInfo(fullCommand, this).equals("")) {
			try {
				quantity = Integer.parseInt(Syntax.TIMES.getStringInfo(fullCommand, this));
			} catch(NumberFormatException e) {
				messageSelf("You can only specify numbers.");
				messageSelf("Syntax: CRAFT [item] (quantity).");
				return false;
			}
		}
		if (quantity < 1) {
			messageSelf("We don't have a delete skill yet.");
			return false;
		}		
		return true;
	}
	
	//TODO needs fenceposting for the first component and commas
	private void listCraftables() {
		for (String s : allItemTemplates.keySet()) {
			StringBuilder display = new StringBuilder();
			display.append(s + ":");
			List<StdItem> componentsNeeded = allItemTemplates.get(s).getComponents(); 
			for (StdItem c : componentsNeeded) {
				if (c instanceof StackableItem) {
					display.append(" ");
					display.append(((StackableItem)c).getQuantity());
					display.append(" ");
					display.append(c.getName());
				} else {
					display.append(" " + c.getName());
				}
			}
			if (componentsNeeded.isEmpty()) {
				display.append(" no components needed.");
			}
			messageSelf(display.toString());
		}
	}	
}
