package skills;

import interfaces.Container;
import interfaces.Holdable;
import items.HerbPouch;
import items.Mineable;
import processes.Skills;

public class Info extends Skills {
	
	public Info() {
		super.name = "info";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM); //could get item or target or location, is always the 2nd word
	}
	
	@Override
	protected void performSkill() {
		String toInfo = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (toInfo.equals("")) {
			messageSelf("Info what/where?");
			return;
		}
		if (toInfo.equals("here")) {
			listAllInfo(currentPlayer.getContainer());
		} else if (toInfo.equals("inventory")) {
			listAllInfo(currentPlayer);
		} else { //specific item			
			if (searchForItem(currentPlayer.getContainer())) {return;} // Search on ground.
			else if (searchForItem(currentPlayer)) {return;} // Search in inventory.
			else {
				messageSelf("You can't find that item."); //maybe add "you can't pickup mobiles"
			}
		}
	}

	private void listAllInfo (Container where) {
		for (String i : where.getInventory().keySet()) {
			messageSelf(i);
		}
	}
	
	private boolean searchForItem(Container where) {
		Holdable possibleItem = where.getHoldableFromString(Syntax.ITEM.getStringInfo(fullCommand, this));
		if (possibleItem != null) {
			StringBuilder s = new StringBuilder();
			s.append(possibleItem.getInfo());
			messageSelf(s.toString());
			return true;
		} 
		return false;
	}	
}
