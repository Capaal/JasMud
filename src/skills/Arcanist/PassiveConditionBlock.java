package skills.Arcanist;

import effects.PassiveCondition;

public class PassiveConditionBlock implements ArcanistBlock {
	
	public final PassiveCondition condiToApply;
	
	public PassiveConditionBlock(PassiveCondition condi) {
		this.condiToApply = condi;
	}

	@Override
	public void perform(ArcanistSkill skill) {
		skill.getCurrentTargets().stream().forEach(x -> x.addPassiveCondition(condiToApply,-1));		
	}

	@Override
	public int determineCost() {
		// TODO Auto-generated method stub
		return -30;
	}

	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		sb.append(System.lineSeparator());
		sb.append("Applies the designated condi on hit");
		sb.append(". Cost shown in damage: " + determineCost());
		return sb;
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
