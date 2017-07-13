package skills.Arcanist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import interfaces.Mobile;
import processes.Skills;
import skills.Arcanist.Targetting.TargettingBlock;

public class ArcanistSkill extends Skills implements interfaces.InformsAggro {
	
	private final DamageBlock damageBlock;
	private final SpeedBlock speedBlock;
	private final TargettingBlock targettingBlock;
	
	private final List<ArcanistBlockRequired> requiredBlocks;
	private final List<ArcanistBlock> selfBlocks;
//	private final int manaCost;
	
	private List<Mobile> currentTargets; // Right now just targets array, necessary?
	
	// Build from a ArcanistBuilder, probably build via CREATE and ALTER
	public ArcanistSkill(ArcanistBuilder build) {
		super(build.getName(), build.getDescrption(), null, null);
		super.syntaxList.addAll(build.getSyntax());
		currentTargets = new ArrayList<Mobile>();
		damageBlock = build.getDamageBlock();
		speedBlock = build.getSpeedBlock();
		targettingBlock = build.getTargettingBlock();
		requiredBlocks = build.getRequiredBlocks();
		selfBlocks = build.getSelfBlocks();
	//	manaCost = build.getMana();
	}
	
	public ArcanistSkill(ArcanistSkill self, Mobile currentPlayer, String fullCommand) {
		super(self.getName(), self.getDescription(), currentPlayer, fullCommand);
		super.syntaxList.addAll(self.syntaxList);
		currentTargets = new ArrayList<Mobile>();
		damageBlock = self.damageBlock;
		speedBlock = self.speedBlock;
		targettingBlock = self.targettingBlock;
		requiredBlocks = self.requiredBlocks;
		selfBlocks = self.selfBlocks;
//		manaCost = self.manaCost;
	}
	
	
	public Mobile getCurrentPlayer() {
		return currentPlayer;
	}
	
	public String getFullCommand() {
		return fullCommand;
	}
	
	public void messageCurrentPlayer(String message) {
		messageSelf(message);
	}

	public void setTargets(List<Mobile> targets) {
		currentTargets = new ArrayList<Mobile>(targets);
	}
	
	public List<Mobile> getCurrentTargets() {
		return currentTargets;
	}

	@Override
	protected void performSkill() {
		messages(); // Still messages people who block.
		damageBlock.perform(this);
		speedBlock.perform(this);
		for (ArcanistBlockRequired rb : requiredBlocks) {
			rb.perform(this);
		}
		for (ArcanistBlock sb : selfBlocks) {
			sb.perform(this);
		}
		informLastAggressor(currentPlayer, currentTargets);
		
	}
	
	private void messages() {
		// SUPER TEMP TODO 
		// Handle in targetting? damage? just a super generic that tells everyone who is hit that they got hit?
		// I LIKE THE LAST CHOICE. Simple and easy, just say they got hit by something, and effects will add extras
		// Maybe some choices later of different pre-made messages. and then we start selling messages....
		// What about targets that block the attack? So many things AOE complicate...
		for (Mobile t : currentTargets) {
			messageTarget(currentPlayer.getNameColored() + " casts a spell on you.", Arrays.asList(t));
			messageSelf("You cast a spell at " + t.getName());
			messageOthers(currentPlayer.getNameColored() + " casts a spell on " + t.getNameColored(), Arrays.asList(currentPlayer, t));
		}
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {
			return false;
		}	
		for (ArcanistBlockRequired rb : requiredBlocks) {
			if (!rb.doesMeetRequirement(currentPlayer)) {
				return false;
			}
		}
		// now checks for required mana in manablock
	//	if (!hasMana()) {
	///		return false;
		//}
//		if (isBlocking(finalTarget)) {  // Actually true? an effect to get extra points? oh geeze. Also, AOE doesn't care here? // Cost to make unblockable?
//			return false;
//		}
		targettingBlock.perform(this);
		if (currentTargets.size() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new ArcanistSkill(this, currentPlayer, fullCommand);
	}
	
	public ArcanistBuilder getNewBuilder() {
		ArcanistBuilder build = new ArcanistBuilder(getName());
		build.setDescription(getDescription());
		build.setDamage(damageBlock);
		build.setSpeed(speedBlock);
		build.setTargettingBlock(targettingBlock);
		build.setSyntax(targettingBlock.getSyntax());
		build.setRequiredBlocks(requiredBlocks);
		build.setSelfBlocks(selfBlocks);
		return build;
	}

	// Assumes all arcanist spells will cause aggro EXCEPT for heals. // TODO bit hacky, what about slight heal + paralyze?
	@Override
	public void informLastAggressor(Mobile currentPlayer, List<Mobile> toInform) {
		if (!(damageBlock instanceof DamageBlockHeal)) {
			toInform.stream().forEach(x -> x.informLastAggressor(currentPlayer));
		}		
	}
}
