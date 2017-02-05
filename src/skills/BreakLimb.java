package skills;

import processes.Skills;
import processes.Skills.Syntax;

public class BreakLimb extends Skills {

	public BreakLimb() {
		super.name = "break";
		super.description = "Break an arm or leg.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.SLOT);
		super.syntaxList.add(Syntax.TARGET);
	}	
	
	//need to make wield first
	
	@Override
	protected void performSkill() {
		if (!hasBalance()) {
			return;
		}

	}

}
