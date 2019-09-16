package skills.Arcanist.Targetting;

import java.util.ArrayList;
import java.util.List;

import interfaces.Mobile;
import processes.Location;
import processes.Skills.Syntax;
import skills.Arcanist.ArcanistSkill;

public class WhoTargettingBlockAll implements WhoTargettingBlock {

	@Override
	public List<Mobile> findWho(ArcanistSkill skill, List<Location> locations) {
		List<Mobile> everyone = new ArrayList<Mobile>();
		for (Location l : locations) {
			for (Mobile m : l.viewMobiles().values()) {
				everyone.add(m);
			}
		}
		return everyone;
	}

	@Override
	public int determineCost() {
		return -40;
	}

	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		sb.append(System.lineSeparator());
		sb.append("Who: Everyone. Cost: ");
		sb.append(determineCost());
		return sb;
	}

	@Override
	public Syntax requestSyntax() {
		return null;
	}

}
