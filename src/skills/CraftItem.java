package skills;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import interfaces.Holdable;
import items.ItemBuilder;
import items.StackableItem;
import items.StackableItem.StackableItemBuilder;
import processes.CreateWorld;
import processes.Skills;

public class CraftItem extends Skills {

	private int quantity = 1; //default will make 1
	
	public CraftItem() {
		super.name = "craft";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.TIMES);
	}
	
	protected void performSkill() {
		String itemToMake = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (itemToMake == "") {
			messageSelf("What are you trying to make? CRAFT LIST for all craftables."); //fail from no item specified
			return;
		}
		Map<String, ItemBuilder> allItemTemplates = CreateWorld.viewItemTemplates(); //maybe list of only craftable items?
		if (itemToMake.equals("list")) {
			for (String s : allItemTemplates.keySet()) {
				StringBuilder display = new StringBuilder();
				display.append(s + ":");
				List<String> componentsNeeded = allItemTemplates.get(s).getComponents(); 
				for (String c : componentsNeeded) {
					display.append(" " + c);
				}
				if (componentsNeeded.isEmpty()) {
					display.append(" no components needed.");
				}
				messageSelf(display.toString());
			}
			return;
		}
		ItemBuilder copyThis = allItemTemplates.get(itemToMake);
		if (copyThis == null) {
			messageSelf("That is not an item you are able to make."); //fail from no template for item specified
			return;
		}
		//looking for TIMES specified:
		quantity = 1;
		if (!Syntax.TIMES.getStringInfo(fullCommand, this).equals("")) {
			try {
				quantity = Integer.parseInt(Syntax.TIMES.getStringInfo(fullCommand, this));
			} catch(NumberFormatException e) {
				messageSelf("You can only specify numbers.");
				messageSelf("Syntax: CRAFT [item] (quantity).");
				return;
			}
		}
		if (quantity < 1) {
			messageSelf("We don't have a delete skill yet.");
			return;
		}
		//finds components needed to make the item, if any. Should return empty list if no components.
		Set<Holdable> componentsOnHand = new HashSet<Holdable> ();
		List<String> componentsNeeded = copyThis.getComponents(); 
		TreeMap<String, Holdable> copyPlayerInv = currentPlayer.getInventory();	
		for (int i=1; i<=quantity; i++) {
			for (String s : componentsNeeded) { 
				Map.Entry<String,Holdable> answer = copyPlayerInv.ceilingEntry(s);
				if (answer != null && answer.getValue().getName().equals(s)) {
					componentsOnHand.add(answer.getValue());
					copyPlayerInv.remove(answer.getKey()); 
				} else {
					messageSelf("You are missing the component: " + s);
					return;
				}
			}
		}
		//what the skill actually does:
		for (Holdable d : componentsOnHand) {d.removeFromWorld();} //wrong method, needs a new delete method. Needs to work with stackable.
		if (copyThis instanceof StackableItemBuilder) {
			((StackableItemBuilder) copyThis).setQuantity(quantity);
			copyThis.setItemContainer(currentPlayer);
			copyThis.complete();
			messageSelf("You have created: " + quantity + " " + copyThis.getName() + ".");
		} else {
			for (int i=1; i<=quantity; i++) {
				copyThis.setItemContainer(currentPlayer); //may not always create the item in the same place
				copyThis.complete(); //should make a copy with new stats since template is Builders
				messageSelf("You have created: " + copyThis.getName() + ".");	
			}
		}
	}
	
}
