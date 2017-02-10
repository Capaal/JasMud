package skills;

import interfaces.Holdable;
import processes.Skills;
import processes.Skills.Syntax;
import processes.StdMob;
import items.Drinkable;

public class Drink extends Skills {
	
	private String potionName;
	private Holdable potionItem;

	public Drink() {
		super.name = "drink";
		super.description = "Drinking potions and maybe other things.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}	
	
	@Override
	protected void performSkill() {
		potionName = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (potionName.equals("")) {
			messageSelf("What are you trying to drink?");
			return;
		}
		//find the item
		potionItem = currentPlayer.getHoldableFromString(potionName);
		if (potionItem == null) {
			messageSelf("You do not have a \"" + potionName + "\".");
			return;
		}
		//check if the item is a potion
		if (!(potionItem instanceof Drinkable)) { 
			messageSelf("You cannot drink " + potionName + ".");
			return;
		}
		Drinkable potion = (Drinkable)potionItem;		
		//drinks the damn thing
		if(!potion.changeSips(1)) {
			messageSelf("There are no more sips left in your potion.");
			return;
		}
		messageSelf(potion.drink(currentPlayer));
		messageSelf("You take a sip of your " + potionName + ".");


	}

}
