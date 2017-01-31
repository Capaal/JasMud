package skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import interfaces.Holdable;
import items.StdItem;
import processes.CreateWorld;
import processes.ItemBuilder;
import processes.Skills;
import processes.Skills.Syntax;
import processes.WorldServer;



public class Salvage extends Skills {

	public Salvage() {
		super.name = "salvage";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}
	
	protected void performSkill() {
		String itemToSalvage = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (itemToSalvage == "") {
			messageSelf("What are you trying to salvage?"); //fail from no item specified
			return;
		}
		Map<String, ItemBuilder> allItemTemplates = CreateWorld.viewItemTemplates(); //maybe list of only craftable items?
		ItemBuilder salvageThis = allItemTemplates.get(itemToSalvage);
		if (salvageThis == null) {
			messageSelf("This item can't be salvaged."); //fail from no template for item specified
			return;
		}
		if(!salvageThis.getSalvageable()) {
			messageSelf("This item is not salvageable."); //fail from salvageable = false, not a salvageable item (potions)
			return;
		}
		//makes sure the player has the item
		Holdable deleteThis = currentPlayer.getHoldableFromString(itemToSalvage); //removes the salvaged item
		if (deleteThis == null) {
			messageSelf("You don't have that item to salvage it.");
			return;
		}
		deleteThis.removeFromWorld(); //removes the salvaged item
		//salvages. currently set at 50% chance to salvage each component
		List<String> components = salvageThis.getComponents(); 
		for (String i : components) {
			Random chance = new Random();
			int p = chance.nextInt(100);
			if (p>50) {
				ItemBuilder toCopy = allItemTemplates.get(i);
				Set<StdItem> allItems = WorldServer.gameState.viewAllItems();
				ItemBuilder newItem = new ItemBuilder();
				newItem.setId(allItems.size()+1);
				newItem.setName(toCopy.getName());
				newItem.setDescription(toCopy.getDescription());
				newItem.setItemContainer(currentPlayer);
				newItem.complete();
				messageSelf(newItem.getName() + " salvaged.");
			} else {
				messageSelf("You weren't able to salvage the " + i + " this time.");
			}
		}		
	}
	
}
