package skills.Arcanist;

import java.util.List;

import processes.Skills.Syntax;
import skills.Arcanist.Targetting.TargettingBlock;
import skills.Arcanist.Targetting.WhereTargettingBlockHere;
import skills.Arcanist.Targetting.WhoTargettingBlockTarget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ArcanistBuilder {
	
	private String name;
	private String description;
	
	private DamageBlock damageBlock;
	private SpeedBlock speedBlock;
	private TargettingBlock targettingBlock;
	
	private List<ArcanistBlockRequired> requiredBlocks;
	private List<ArcanistBlock> selfBlocks;
//	private int mana;
	private List<Syntax> syntaxList;
	
	@SuppressWarnings("unused")
	private ArcanistBuilder() {} // Not allowed.
	
	// New skill getting started.
	public ArcanistBuilder(String incomingName) {	
		this.name = incomingName;
		description = "Unremarkable";		
		syntaxList = new ArrayList<Syntax>();
		damageBlock = new DamageBlock(0, null);
		speedBlock = new SpeedBlock(3);
		requiredBlocks = new ArrayList<ArcanistBlockRequired>();
		selfBlocks = new ArrayList<ArcanistBlock>();
		targettingBlock = new TargettingBlock(new WhoTargettingBlockTarget(), new WhereTargettingBlockHere());
		addRequiredBlock(new ManaBlock(0));
		setSyntax(getTargettingBlock().getSyntax());
//		mana = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescrption() {
		return description;
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
		if (syntaxList == null) {
			return false;
		}
		
		if (targettingBlock == null || !targettingBlock.isValid()) {
			return false;
		}
		
		for (ArcanistBlockRequired rb: requiredBlocks) {
			if (!rb.isValid()) {
				return false;
			}
		}
		for (ArcanistBlock sb : selfBlocks) {
			if (!sb.isValid()) {
				return false;
			}
		}
//		if (mana < 0 || mana > 250) {
//			return false; // Arbitrary limits? Should this even ever come up?
//		}
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
		int cost = 20; // Initial balance number?
		cost += damageBlock.determineCost();
		cost += speedBlock.determineCost();
		cost += targettingBlock.determineCost();
		for (ArcanistBlockRequired rb: requiredBlocks) {
			cost += rb.determineCost();
		}
		for (ArcanistBlock sb : selfBlocks) {
			cost += sb.determineCost();
		}
//		cost += calculateManaCost();
		return cost;		
	}
	
//	public int calculateManaCost() {
//		return mana;
//	}

//	public int getMana() {
///		return mana;
//	}

	public void setSyntax(List<Syntax> syntax) {
		syntaxList = syntax;
	}

//	public void setMana(int manaCost) {
//		mana = manaCost;
//	}

	public void setDescription(String description2) {
		description = description2;
	}

	public List<ArcanistBlockRequired> getRequiredBlocks() {
		return requiredBlocks;
	}

	public List<ArcanistBlock> getSelfBlocks() {
		return selfBlocks;
	}

	public void setRequiredBlocks(List<ArcanistBlockRequired> requiredBlocks2) {
		requiredBlocks = requiredBlocks2;
	}

	public void setSelfBlocks(List<ArcanistBlock> selfBlocks2) {
		selfBlocks = selfBlocks2;
	}

	public void addRequiredBlock(ArcanistBlockRequired newRequiredBlock) {
		requiredBlocks.add(newRequiredBlock);
	}
	
	public void addSelfBlock(ArcanistBlock newSelfBlock) {
		selfBlocks.add(newSelfBlock);		
	}
}
