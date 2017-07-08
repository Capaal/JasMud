package skills.Arcanist.Targetting;

import java.util.Arrays;
import java.util.List;

import processes.Location;
import processes.Skills.Syntax;
import skills.Arcanist.ArcanistSkill;

public class WhereTargettingBlockHere implements WhereTargettingBlock {

	@Override
	public List<Location> findWhere(ArcanistSkill skill) {
		return Arrays.asList(skill.getCurrentPlayer().getContainer());	
	}

	@Override
	public int determineCost() {
		return 0;
	}

	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		sb.append(System.lineSeparator());
		sb.append("Locations: Here only.");
		sb.append(" Cost: ");
		sb.append(determineCost());
		return sb;
	}

	@Override
	public Syntax requestSyntax() {
		return null;
	}
}
