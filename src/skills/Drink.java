package skills;

import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import items.Drinkable;

public class Drink extends Skills {
	
	private String potionName;
	private Holdable potionItem;

	public Drink(Mobile currentPlayer, String fullCommand) {
		super("drink", "Drinking liquids.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}	
	
	@Override
	protected void performSkill() {
		potionName = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (preSkillChecks()) {
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
	
	protected boolean preSkillChecks() {
		if (potionName.equals("")) {
			messageSelf("What are you trying to drink?");
			return false;
		}
		//find the item
		potionItem = currentPlayer.getHoldableFromString(potionName);
		if (potionItem == null) {
			messageSelf("You do not have a \"" + potionName + "\".");
			return false;
		}
		//check if the item is a potion
		if (!(potionItem instanceof Drinkable)) { 
			messageSelf("You cannot drink " + potionName + ".");
			return false;
		}
		return true;
	}
}
