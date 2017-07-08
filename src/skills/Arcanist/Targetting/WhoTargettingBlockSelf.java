package skills.Arcanist.Targetting;

import java.util.Arrays;
import java.util.List;

import interfaces.Mobile;
import processes.Location;
import processes.Skills.Syntax;
import skills.Arcanist.ArcanistSkill;

public class WhoTargettingBlockSelf implements WhoTargettingBlock {

	@Override
	public List<Mobile> findWho(ArcanistSkill skill, List<Location> locations) {
		return Arrays.asList(skill.getCurrentPlayer());
	}

	@Override
	public int determineCost() {
		return 5;
	}

	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		sb.append(System.lineSeparator());
		sb.append("Who: Targetting yourself. Cost: ");
		sb.append(determineCost());
		return sb;
	}

	@Override
	public Syntax requestSyntax() {
		return null;
	}

}
