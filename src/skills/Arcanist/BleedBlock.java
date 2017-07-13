package skills.Arcanist;

import effects.Bleed;
import interfaces.Mobile;

public class BleedBlock implements ArcanistBlock {
	
	private final int intensity;
	
	public BleedBlock(int intensity) {
		this.intensity = intensity;
	}

	@Override
	public void perform(ArcanistSkill skill) {
		for (Mobile t : skill.getCurrentTargets()) {
			t.addActiveCondition(new Bleed(t, intensity), 2); // The 1 is meaningless? Just need to be larger than 1 to keep ticking correctly.
			t.tell("The spell tears your flesh, you begin to bleed.");
		}
	}

	@Override
	public int determineCost() {
        int cost = intensity; // this  will be the result
        for (int i = intensity - 1; i > 0; i--) {
        	cost += i;
        }
        return -(cost + 10);
	}

	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		sb.append(System.lineSeparator());
		sb.append("Bleed effect per tick: " + intensity);
		sb.append(". Cost shown in damage: " + determineCost());
		return sb;
	}

	@Override
	public boolean isValid() {
		if (intensity <= 0) {
			return false;
		}
		return true;
	}

}
