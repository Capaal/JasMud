package skills;

import effects.PassiveCondition;
import interfaces.Mobile;
import items.Plant.PlantType;
import processes.Skills;
import processes.Skills.Syntax;

public class ListAll extends Skills {
	
	String toList;

	public ListAll(Mobile currentPlayer, String fullCommand) {
		super("list", "Informational skill - list things in a category (plants, condis).", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}
	
	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			switch (toList.toLowerCase()) {
			case "plants":
				for (PlantType p : PlantType.values()) {
					messageSelf(p.toString().toLowerCase());
				}
				break;
			case "passivecondi":
				for (PassiveCondition pc : PassiveCondition.values()) {
					messageSelf(pc.toString().toLowerCase());
				}
				break;
			case "activecondi":
				messageSelf("Bleed");
				messageSelf("Fear");
				messageSelf("Levitate");
				messageSelf("Regen");
				messageSelf("MercRegen");
				messageSelf("Unbalanced");
				messageSelf("Hallucination");
				messageSelf("Belladonna");
				messageSelf("Note: this is not a self-updating list.");
				break;
			default:
				messageSelf("List is only for PLANTS/PASSIVECONDI/ACTIVECONDI.");
				break;
			}
		}
	}

	@Override
	protected boolean preSkillChecks() {
		toList = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (toList.equals("")) {
			messageSelf("Specify a category.");
			return false;
		}
		return true;
	}

}
