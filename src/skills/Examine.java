package skills;

import interfaces.Holdable;
import processes.Skills;

public class Examine extends Skills {
	
	private Holdable possibleItem;
	private String toExamine;
	
	public Examine() {
		super.name = "examine";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}

	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			if (searchForItem()) {
				messageSelf(possibleItem.getExamine());
				return;
			}
			
			messageSelf("You can't find that item."); 
		}
	}

	private boolean searchForItem() {
		//check inv
		possibleItem = currentPlayer.getHoldableFromString(toExamine);
		//check location
		if (possibleItem == null) {possibleItem = currentPlayer.getContainer().getHoldableFromString(toExamine);}
		//not found anywhere
		if (possibleItem == null) {return false;}
	/*	//do if found
		StringBuilder s = new StringBuilder();
		s.append(System.getProperty("line.separator"));
		s.append(possibleItem.getDescription());
		s.append(" contains:");
			
			if (possibleItem instanceof Container) {
				Container c = (Container) possibleItem;
				if (c.getInventory().isEmpty()) {
					s.append(System.getProperty("line.separator"));
					s.append("There is nothing inside.");
				} else {
					for (Holdable h : c.getInventory().values()) {
						s.append(System.getProperty("line.separator"));
						s.append(h.getName());
					}
				}
			}
		
		messageSelf(s.toString()); */
		return true;
	}

	@Override
	protected boolean preSkillChecks() {
		toExamine = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (toExamine.equals("")) {
			messageSelf("Examine what?");
			return false;
		}
		return true;
	}
}

