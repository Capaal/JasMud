package skills;

import interfaces.Holdable;
import processes.Skills;
import items.Drinkable;

public class EmptyPotion extends Skills {
	
	private String potionName;
	private Holdable potionItem;

	public EmptyPotion() {
		super.name = "empty";
		super.description = "Empty a potion.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}	
	
	@Override
	protected void performSkill() {
		potionName = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (preSkillChecks()) {
			Drinkable potion = (Drinkable)potionItem;
			//empties - changes sips by -currentSips
			if(potion.getSips() == 0) {
				messageSelf("That potion is already empty.");
				return;
			}
			potion.changeSips(potion.getSips()); 
			messageSelf("You empty the potion.");
		}
	}
	
	@Override
	protected boolean preSkillChecks() {
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
