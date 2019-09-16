package skills.Arcanist;

import interfaces.Mobile;


// TODO Utilized take damage, which displays prompt, which means you get TWO prompts displays.
// One for the skill running, the other for hurting yourself.
public class BloodBlock implements ArcanistBlockRequired {

private final int lifeRequired;
	
	public BloodBlock(int life) {
		this.lifeRequired = life;
	}

	@Override
	public void perform(ArcanistSkill skill) {
		skill.getCurrentPlayer().takeDamageIgnoresArmor(lifeRequired);
	}

	@Override
	public int determineCost() {
		return lifeRequired;
	}

	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		sb.append(System.lineSeparator() + "Life required: " + lifeRequired);
		sb.append(". Costing: ");
		sb.append(determineCost());
		return sb;
	}

	@Override
	public boolean isValid() {
		if (lifeRequired < 0 || lifeRequired > 100) {
			return false;
		}
		return true;
	}

	@Override
	public boolean doesMeetRequirement(Mobile currentPlayer) {
		if (currentPlayer.getCurrentHp() < lifeRequired) {
			currentPlayer.tell("You've too little blood left to cast such a spell.");
			return false;
		}
		return true;
	}

}