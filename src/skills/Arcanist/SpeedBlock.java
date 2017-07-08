package skills.Arcanist;

import effects.PassiveCondition;

public class SpeedBlock implements ArcanistBlock {
	
	private final int duration;
	// Cooldowns? Inductions?
	
	public SpeedBlock(double length) {
		this.duration = (int) (length * 1000); // Convert to milliseconds.
	}
	
	@Override
	public void perform(ArcanistSkill skill) {
		skill.getCurrentPlayer().addPassiveCondition(PassiveCondition.BALANCE, duration);
	}
	
	public int getSpeed() {
		return duration;
	}

	@Override
	public int determineCost() {
		return (duration - 3000) / 1000 * 20;		
	}

	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		sb.append(System.lineSeparator());
		sb.append("Balance time: ");
		sb.append((double)duration/1000);
		sb.append(" seconds.");
		sb.append(" Cost: ");
		sb.append(determineCost());
		return sb;
	}

	@Override
	public boolean isValid() {
		if (duration > 0) {
			return true;
		}
		return false;
	}

	

}
