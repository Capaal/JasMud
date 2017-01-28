package skills;

import java.util.Map;
import java.util.Set;

import items.StdItem;
import processes.CreateWorld;
import processes.ItemBuilder;
import processes.Skills;
import processes.Skills.Syntax;
import processes.WorldServer;



public class MakeItem extends Skills {

	public MakeItem() {
		super.name = "make";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}
	
	protected void performSkill() {
		String itemToMake = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (itemToMake == "") {
			messageSelf("What are you trying to make?");
			return;
		}
		Map<String, ItemBuilder> allItemTemplates = CreateWorld.viewItemTemplates();
		ItemBuilder copyThis = allItemTemplates.get(itemToMake);
		if (copyThis == null) {
			messageSelf("That is not an item you are able to make.");
			return;
		}
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
