package skills;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import interfaces.Holdable;
import items.ItemBuilder;
import items.StackableItem;
import items.StackableItem.StackableItemBuilder;
import items.StdItem;
import processes.CreateWorld;
import processes.Skills;

public class CraftItem extends Skills {

	private int quantity = 1; //default will make 1
	private ItemBuilder copyThis;
	private String itemToMake;
	private Map<String, ItemBuilder> allItemTemplates;
	
	public CraftItem() {
		super.name = "craft";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.TIMES);
		this.copyThis = new ItemBuilder();
	}
	
	protected void performSkill() {
		itemToMake = Syntax.ITEM.getStringInfo(fullCommand, this);
		allItemTemplates = CreateWorld.viewItemTemplates(); //maybe list of only craftable items?
		
		if (!preSkillChecks()) {return;}
		


		//attempting recursion for the find/do sections below
		for (int i=0; i<quantity; i++) {
			List<StdItem> componentsNeeded = copyThis.getComponents();
			Iterator<StdItem> it = componentsNeeded.iterator();
			if (copyThis.getNonexistentFinishedItem() instanceof StackableItem) {i=quantity;}
			if (it.hasNext()) {
				TreeMap<String, Holdable> copyPlayerInv = currentPlayer.getInventory();
				//check components, remove components, then create the item
				if (!checkRemoveCreate(it, copyPlayerInv)) {
					return;
				}
			}  else {
				andCreate();
			}
		}
		
	}
	
	
	private boolean checkRemoveCreate(Iterator<StdItem> it, TreeMap<String, Holdable> copyPlayerInv) {
		StdItem componentToTest = null;
		if (it.hasNext()) {
			componentToTest = it.next();
			//check if component in inv
			if (copyPlayerInv.ceilingEntry(componentToTest.getName()) != null) {
				if (componentToTest instanceof StackableItem) {
					int qtyNeed = ((StackableItem)componentToTest).getQuantity();
					int qtyHave = ((StackableItem)copyPlayerInv.ceilingEntry(componentToTest.getName()).getValue()).getQuantity();
					if ( qtyNeed > qtyHave ) {
						messageSelf("You are missing the component: " + componentToTest.getName());
						return false;
					}
				} else {
					copyPlayerInv.remove(componentToTest.getName());
				}
				//if yes, check the next component
				if (it.hasNext()) {
					checkRemoveCreate(it, copyPlayerInv);
				}
			} else {
				messageSelf("You are missing the component: " + componentToTest.getName());
				return false; //else return false? does this even end the recursion?
			}
		}
		//when empty, remove components from inventory
		thenRemove(componentToTest);
		andCreate();
		return true;
	}
	
	private void thenRemove(StdItem thisItem) {
		//when empty, remove components from inventory
		if (thisItem instanceof StackableItem) {
			StackableItem componentItem = (StackableItem) thisItem;
			StackableItem matchingInvItem = (StackableItem) currentPlayer.getHoldableFromString(componentItem.getName());
			matchingInvItem.removeFromStack((componentItem.getQuantity()));;
		} else {
	//		copyPlayerInv.remove(thisItem.getName());
			currentPlayer.getHoldableFromString(thisItem.getName()).removeFromWorld(); 
		}
	}
	
	private void andCreate() {
		if (copyThis instanceof StackableItemBuilder) {
			((StackableItemBuilder) copyThis).setQuantity(quantity);
			copyThis.setItemContainer(currentPlayer);
			copyThis.complete();
			messageSelf("You have created: " + quantity + " " + copyThis.getName() + ".");
		} else {
	//		for (int i=1; i<=quantity; i++) {
				copyThis.setItemContainer(currentPlayer); //may not always create the item in the same place
				copyThis.complete(); //should make a copy with new stats since template is Builders
				messageSelf("You have created: " + copyThis.getName() + ".");	
	//		}
		}
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
	
	private void listCraftables() {
		for (String s : allItemTemplates.keySet()) {
			StringBuilder display = new StringBuilder();
			display.append(s + ":");
			List<StdItem> componentsNeeded = allItemTemplates.get(s).getComponents(); 
			for (StdItem c : componentsNeeded) {
				display.append(" " + c);
			}
			if (componentsNeeded.isEmpty()) {
				display.append(" no components needed.");
			}
			messageSelf(display.toString());
		}
		return;
	}
	
}
