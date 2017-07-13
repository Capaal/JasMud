package skills.Arcanist;

import interfaces.Mobile;

public class ManaBlock implements ArcanistBlockRequired {
	
	private final int manaRequired;
	
	public ManaBlock(int mana) {
		this.manaRequired = mana;
	}

	@Override
	public void perform(ArcanistSkill skill) {
		skill.getCurrentPlayer().changeMana(-manaRequired);
	}

	@Override
	public int determineCost() {
		return manaRequired;
	}

	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		sb.append(System.lineSeparator() + "Mana required: " + manaRequired);
		sb.append(". Costing: ");
		sb.append(determineCost());
		return sb;
	}

	@Override
	public boolean isValid() {
		if (manaRequired < 0 || manaRequired > 100) {
			return false;
		}
		return true;
	}

	@Override
	public boolean doesMeetRequirement(Mobile currentPlayer) {
		if (currentPlayer.getCurrentMana() < manaRequired) {
			currentPlayer.tell("You're too mentally drained to cast.");
			return false;
		}
		return true;
	}

}
