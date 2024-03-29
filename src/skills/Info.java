package skills;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;

public class Info extends Skills {
	
	private String toInfo;
	
	public Info(Mobile currentPlayer, String fullCommand) {
		super("info", "Specific details for objects.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM); //could get item or target or location, is always the 2nd word
	}
	
	@Override
	protected void performSkill() {
		if (toInfo.equals("here")) {
			if (currentPlayer.getContainer().viewInventory().keySet().isEmpty()) {
				messageSelf("There is nothing on the ground.");
			} else {
				listAllInfo(currentPlayer.getContainer());
			}
		} else if (toInfo.equals("inventory")) {
			if (currentPlayer.viewInventory().keySet().isEmpty()) {
				messageSelf("You are holding nothing.");
			} else {
				listAllInfo(currentPlayer);
			}
		} else { //specific item			
			if (searchForItem(currentPlayer.getContainer())) {return;} // Search on ground.
			else if (searchForItem(currentPlayer)) {return;} // Search in inventory.
			else {
				messageSelf("You can't find that item."); 
			}
		}
	}

	private void listAllInfo (Container where) {
		for (String i : where.viewInventory().keySet()) {
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

	@Override
	protected boolean preSkillChecks() {
		toInfo = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (toInfo.equals("")) {
			messageSelf("Info what/where?");
			return false;
		}
		return true;
	}	
}
