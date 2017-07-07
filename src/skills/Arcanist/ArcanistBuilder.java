package skills.Arcanist;

import java.util.List;

import processes.Skills.Syntax;

import java.util.ArrayList;
import java.util.Collection;

public class ArcanistBuilder {
	
	private String name;
	private String description;
	
	private  DamageBlock damageBlock;
	private  SpeedBlock speedBlock;
	private  TargettingBlock targettingBlock;
	private  int mana;
	private List<Syntax> syntaxList;

//	private  ArrayList<ArcanistBlock> addedEffects; // Specically effects that do not hit the target. Necessary? targetted effects are in damage.
	
	public ArcanistBuilder(ArcanistSkill skill) {
		// Create from existing skill. TODO
	}
	
	@SuppressWarnings("unused")
	private ArcanistBuilder() {} // Not allowed.
	
	// New skill getting started.
	public ArcanistBuilder(String incomingName) {	
		this.name = incomingName;
		description = "Unremarkable";
		
		syntaxList = new ArrayList<Syntax>();
		syntaxList.add(Syntax.SKILL);
		syntaxList.add(Syntax.TARGET); // TEMP
		damageBlock = new DamageBlock(-1, null);
		speedBlock = new SpeedBlock(-1);
		targettingBlock = new TargettingBlock(null, null);
		mana = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescrption() {
		return description;
	}

	public int getManaCost() {
		return mana;
	}

	public TargettingBlock getTargettingBlock() {
		return targettingBlock;
	}

	public SpeedBlock getSpeedBlock() {
		return speedBlock;
	}

	public DamageBlock getDamageBlock() {
		return damageBlock;
	}

	public Collection<? extends Syntax> getSyntax() {
		return syntaxList;
	}

	public ArcanistSkill complete() {
		if (!isValid()) {
			return null;
		}
		return new ArcanistSkill(this);
	}

	public boolean isValid() {
		if (damageBlock == null || !damageBlock.isValid()) {
			return false;
		}
		if (speedBlock == null || !speedBlock.isValid()) {
			return false;
		}
		
		// TEEST syntax list somehow?
		if (syntaxList == null) {
			return false;
		}
		
		if (targettingBlock == null || !targettingBlock.isValid()) {
			return false;
		}
	//	for (ArcanistBlock e : addedEffects) {
	//		if (!e.isValid()) {
	//			return false;
	//		}
	//	}
		if (mana < 0 || mana > 250) {
			return false; // Arbitrary limits? Should this even ever come up?
		}
		if (getCost() < 0) {
			return false;
		}
		return true;
	}

	public void setTargettingBlock(TargettingBlock targettingBlock2) {
		targettingBlock = targettingBlock2;
	}

	public void setDamage(DamageBlock damageBlock2) {
		damageBlock = damageBlock2;
	}

	public void setSpeed(SpeedBlock speedBlock2) {
		speedBlock = speedBlock2;
	}

	public int getCost() {
		int cost = 80; // Initial balance number?
		cost += damageBlock.determineCost();
		cost += speedBlock.determineCost();
		cost += targettingBlock.determineCost();
		cost += calculateManaCost();
		return cost;		
	}
	
	public int calculateManaCost() {
		return mana;
	}

	public int getMana() {
		return mana;
	}
}
