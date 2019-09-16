package skills.Mercenary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import effects.PassiveCondition;
import interfaces.Mobile;
import processes.Skills;

public class Straighten extends Skills {
	
	private String slot;
//	private PassiveCondition broken;
	private Map<String,PassiveCondition> affectedList = new HashMap<String,PassiveCondition>();
	
	public Straighten(Mobile currentPlayer, String fullCommand) {
		super("straighten", "Splinting of a broken arm or leg.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.SLOT);
		affectedList.put("righthand", PassiveCondition.BROKENRIGHTARM);
		affectedList.put("lefthand", PassiveCondition.BROKENLEFTARM);
		affectedList.put("legs", PassiveCondition.BROKENLEGS);
	}	
	
	@Override
	protected void performSkill() {
		slot = Syntax.SLOT.getStringInfo(fullCommand, this);
		if (slot.equals("")) {
			for (PassiveCondition a : affectedList.values()) {
				if(fixBroken(a)) {return;}
			}
		} else {
			if (affectedList.get(slot) == null) {
				messageSelf("Straighten <RIGHTHAND/LEFTHAND/LEGS>.");
			}
			fixBroken(affectedList.get(slot));
		}
		failMsg();
	}		
	
	private boolean fixBroken(PassiveCondition thisLimb) {
		if (currentPlayer.hasCondition(thisLimb)) {
			currentPlayer.removeCondition(thisLimb);
			messageSelf("You straighten your broken " + thisLimb.limbName() + " by force of will.");
			messageOthers(currentPlayer.getNameColored() + " straights a broken " + thisLimb.limbName() + ".", Arrays.asList(currentPlayer));
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
			return true;
		} 
		return false;
	}
	
	private void failMsg() {
		if (slot.equals("")) {
			messageSelf("You don't have any broken limbs to straighten.");
		} else {
			messageSelf("Your " + slot.toString().toLowerCase() + " is not broken.");
		}
	}

	@Override
	protected boolean preSkillChecks() {
		// TODO Auto-generated method stub
		return true;
	}
}
