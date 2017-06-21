package skills;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import interfaces.Holdable;
import interfaces.Mobile;
import items.ItemBuilder;
import items.StdItem;
import processes.CreateWorld;
import processes.Skills;
import processes.WorldServer;

public class Salvage extends Skills {
	
	private String itemToSalvage;
	private Holdable deleteThis;
	private Map<String, ItemBuilder> allItemTemplates;
	private ItemBuilder salvageThis;

	public Salvage(Mobile currentPlayer, String fullCommand) {
		super("salvage", "Salvaging components of an item.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}
	
	protected void performSkill() {
		if (preSkillChecks()) {
			deleteThis.removeFromWorld(); //removes the salvaged item
			//salvages. currently set at 50% chance to salvage each component
			List<StdItem> components = salvageThis.getComponents(); 
			for (StdItem i : components) {
				Random chance = new Random();
				int p = chance.nextInt(100);
				if (p>50) {
					ItemBuilder toCopy = allItemTemplates.get(i); // Are all components actually stored in this list?
					toCopy.setItemContainer(currentPlayer);
					toCopy.complete();
					messageSelf(toCopy.getName() + " salvaged.");
				} else {
					messageSelf("You weren't able to salvage the " + i + " this time.");
				}
			}	
		}
	}

	@Override
	protected boolean preSkillChecks() {
		itemToSalvage = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (itemToSalvage == "") {
			messageSelf("What are you trying to salvage?"); //fail from no item specified
			return false;
		}
		allItemTemplates = CreateWorld.viewItemTemplates(); //maybe list of only craftable items?
		salvageThis = allItemTemplates.get(itemToSalvage);
		if (salvageThis == null) {
			messageSelf("This item can't be salvaged."); //fail from no template for item specified
			return false;
		}
		if(!salvageThis.getSalvageable()) {
			messageSelf("This item is not salvageable."); //fail from salvageable = false, not a salvageable item (potions)
			return false;
		}
		//makes sure the player has the item
		deleteThis = currentPlayer.getHoldableFromString(itemToSalvage); //removes the salvaged item
		if (deleteThis == null) {
			messageSelf("You don't have that item to salvage it.");
			return false;
		}
		return true;
	}
	
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Salvage(currentPlayer, fullCommand);
	}
}
