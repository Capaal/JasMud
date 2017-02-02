package skills;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import interfaces.Holdable;
import items.StdItem;
import processes.CreateWorld;
import processes.ItemBuilder;
import processes.Skills;
import processes.WorldServer;

public class CraftItem extends Skills {

	public CraftItem() {
		super.name = "craft";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}
	
	// JASON NOTES:
	// Why is allItemTemplates a MAP? to grab the template based on the string user typed.
	// Why view a copy of the template map?
	// 
	
	protected void performSkill() {
		String itemToMake = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (itemToMake == "") {
			messageSelf("What are you trying to make?"); //fail from no item specified
			return;
		}
		Map<String, ItemBuilder> allItemTemplates = CreateWorld.viewItemTemplates(); //maybe list of only craftable items?
		ItemBuilder copyThis = allItemTemplates.get(itemToMake);
		if (copyThis == null) {
			messageSelf("That is not an item you are able to make."); //fail from no template for item specified
			return;
		}
		//finds components needed to make the item, if any. Should return empty list if no components.
		Set<Holdable> componentsOnHand = new HashSet<Holdable> ();
		List<String> componentsNeeded = copyThis.getComponents(); 
		TreeMap<String, Holdable> copyPlayerInv = currentPlayer.getInventory();	
		
		
				
		for (String i : componentsNeeded) { 
			Map.Entry<String,Holdable> answer = copyPlayerInv.ceilingEntry(i);
			if (answer != null && answer.getValue().getName().equals(i)) {
				componentsOnHand.add(answer.getValue());
				copyPlayerInv.remove(answer.getKey()); 
			} else {
				messageSelf("You are missing the component: " + i);
				return;
			}
		}
		
		
//		if (answer != null && (answer.getKey().equals(holdableString) || answer.getValue().getName().equals(holdableString))) {
//			return answer.getValue();
//		}
//		return null;
		
		
		/*
		for (String i : componentsNeeded) {   //adds all items needed from playerInv to compOnHand				
			Holdable remove = null;
			for (Holdable h : copyPlayerInv.values()) {				
				if (h.getName().equals(i)) {
					componentsOnHand.add(h);
					remove = h;  
					break;
				} 
			}
			if (remove != null) {			   //removing so the same item isn't added back in (needs two ores, prevents counting same ore twice)
				copyPlayerInv.remove(remove);  //can't remove while iterating through inventory (THIS IS NOT the same as DELETE (which doesn't exist yet))
			} else {
				messageSelf("You are missing components.");
				return;
			}
		}*/
		//what the skill actually does:
		for (Holdable d : componentsOnHand) {d.removeFromWorld();} //removes the components from the world
		Set<StdItem> allItems = WorldServer.gameState.viewAllItems(); //need better way to determine a good ID
		ItemBuilder newItem = new ItemBuilder();
		newItem.setId(allItems.size()+1);
		newItem.setName(copyThis.getName());
		newItem.setDescription(copyThis.getDescription());
		newItem.setItemContainer(currentPlayer); //may not always create the item in the same place
		newItem.complete();
		messageSelf("You have created: " + copyThis.getName() + ".");	
	}
	
}
