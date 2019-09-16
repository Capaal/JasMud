package skills.Arcanist.Targetting;

import java.util.Arrays;
import java.util.List;

import interfaces.Mobile;
import processes.Location;
import processes.Skills.Syntax;
import skills.Arcanist.ArcanistSkill;

// Target is defined by syntax as a particular word entered by the user.
public class WhoTargettingBlockTarget implements WhoTargettingBlock {

	@Override
	public List<Mobile> findWho(ArcanistSkill skill, List<Location> locations) {
		Mobile finalTarget = null;
		String targetName = Syntax.TARGET.getStringInfo(skill.getFullCommand(), skill);
		if (targetName.equals("")) {
			skill.messageCurrentPlayer("Who are you trying to target?");
			return null;
		}
		for (Location l : locations) {
			Mobile temp = l.getMobileFromString(targetName);
			if (temp != null) {
				finalTarget = temp;
				break;
			}
		}	
		if (finalTarget == null) {
			skill.messageCurrentPlayer("There is no " + targetName + " to target.");
			return null;
		}
		return Arrays.asList(finalTarget);
	}

	@Override
	public int determineCost() {
		return 0;
	}

	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		sb.append(System.lineSeparator());
		sb.append("Targetting: Single designated target.");
		sb.append(" Cost: ");
		sb.append(determineCost());
		return sb;
	}

	@Override
	public Syntax requestSyntax() {
		return Syntax.TARGET;
	}

}
