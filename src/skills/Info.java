package skills;

import interfaces.Container;
import interfaces.Holdable;
import items.StdItem;
import processes.Location;
import processes.Skills;
import processes.Skills.Syntax;

public class Info extends Skills {
	
	private Container where;
	private Holdable possibleItem;
	
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
			where = currentPlayer.getContainer();
			listAllInfo(where);
		} else if (toInfo.equals("inventory")) {
			where = currentPlayer;
			listAllInfo(where);
		} else { //specific item
			boolean found = false;

			if(found = listOneInfo(possibleItem, currentPlayer.getContainer())) {return;}
			
			if(found = listOneInfo(possibleItem, currentPlayer)) {return;}
			
			if (!found) {
				messageSelf("You can't find that item.");
			}
		}
	}

	private void listAllInfo (Container where) {
		for (String i : where.getInventory().keySet()) {
			messageSelf(i);
		}
	}
	
	private boolean listOneInfo (Holdable possibleItem, Container where) {
		possibleItem = where.getHoldableFromString(Syntax.ITEM.getStringInfo(fullCommand, this));
		if (possibleItem != null) {
			messageSelf(possibleItem.getName() + possibleItem.getId());
			return true;
		} 
		return false;
	}
	
}
