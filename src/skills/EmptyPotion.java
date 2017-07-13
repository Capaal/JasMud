package skills;

import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import items.Drinkable;

public class EmptyPotion extends Skills {
	
	private String potionName;
	private Holdable potionItem;

	public EmptyPotion(Mobile currentPlayer, String fullCommand) {
		super("empty", "Empying potions and other containers.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}	
	
	@Override
	protected void performSkill() {
		Drinkable potion = (Drinkable)potionItem;
		//empties - changes sips by -currentSips
		if(potion.getSips() == 0) {
			messageSelf("That potion is already empty.");
			return;
		}
		potion.changeSips(potion.getSips()); 
		messageSelf("You empty the potion.");
	}
	
	@Override
	protected boolean preSkillChecks() {
		potionName = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (potionName.equals("")) {
			messageSelf("What are you trying to empty?");
			return false;
		}
		//find potion
		potionItem = currentPlayer.getHoldableFromString(potionName);
		if (potionItem == null) {
			messageSelf("You do not have a \"" + potionName + "\".");
			return false;
		}
		if (!(potionItem instanceof Drinkable)) { 
			messageSelf("You cannot empty a " + potionName + ".");
			return false;
		}
		return true;
	}
}
