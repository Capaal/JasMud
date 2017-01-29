package skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import interfaces.Holdable;
import items.StdItem;
import processes.CreateWorld;
import processes.ItemBuilder;
import processes.Skills;
import processes.Skills.Syntax;
import processes.WorldServer;



public class CraftItem extends Skills {

	public CraftItem() {
		super.name = "craft";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}
	
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
		List<Holdable> componentsOnHand = new ArrayList<Holdable> ();
		List<String> componentsNeeded = copyThis.getComponents(); 
		for (String i : componentsNeeded) {
			Holdable playerItem = currentPlayer.getHoldableFromString(i);
			if (playerItem == null) {
				messageSelf("You don't have the correct components to make that item.");
				return;
			}
			componentsOnHand.add(playerItem);
		}		
		//what the skill actually does:
		for (Holdable d : componentsOnHand) {d.removeFromWorld(); } //removes the components from the world
		Set<StdItem> allItems = WorldServer.gameState.viewAllItems(); //need better way to determine a good ID
		ItemBuilder newItem = new ItemBuilder();
		newItem.setId(allItems.size()+1);
		newItem.setName(copyThis.getName());
		newItem.setDescription(copyThis.getDescription());
		newItem.setItemContainer(currentPlayer.getContainer()); //may not always create the item in the same place
		newItem.complete();
		messageSelf("You have created: " + copyThis.getName() + ".");	
	}
	
}
