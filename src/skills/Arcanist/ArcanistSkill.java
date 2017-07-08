package skills.Arcanist;

import java.util.Arrays;
import java.util.List;

import interfaces.Mobile;
import processes.Skills;
import skills.Arcanist.Targetting.TargettingBlock;

public class ArcanistSkill extends Skills {
	
	private final DamageBlock damageBlock;
	private final SpeedBlock speedBlock;
	private final TargettingBlock targettingBlock;
	private final int manaCost;
	
	private ArcanistsData currentData; // Right now just targets array, necessary?
	
	// Build from a ArcanistBuilder, probably build via CREATE and ALTER
	public ArcanistSkill(ArcanistBuilder build) {
		super(build.getName(), build.getDescrption(), null, null);
		super.syntaxList.addAll(build.getSyntax());
		currentData = new ArcanistsData();
		damageBlock = build.getDamageBlock();
		speedBlock = build.getSpeedBlock();
		targettingBlock = build.getTargettingBlock();
		manaCost = build.getMana();
	}
	// Syntax?
	public ArcanistSkill(ArcanistSkill self, Mobile currentPlayer, String fullCommand) {
		super(self.getName(), self.getDescription(), currentPlayer, fullCommand);
		super.syntaxList.addAll(self.syntaxList);
		currentData = new ArcanistsData();
		damageBlock = self.damageBlock;
		speedBlock = self.speedBlock;
		targettingBlock = self.targettingBlock;
		manaCost = self.manaCost;
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
		getCurrentData().targets = targets;
	}
	
	public ArcanistsData getCurrentData() {
		return currentData;
	}
	
	class ArcanistsData {
		protected List<Mobile> targets;		
	}

	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			messages(); // Still messages people who block.
			damageBlock.perform(this);
			speedBlock.perform(this);
			// TODO subtract from MANA
		}
	}
	
	private void messages() {
		// SUPER TEMP TODO 
		// Handle in targetting? damage? just a super generic that tells everyone who is hit that they got hit?
		// I LIKE THE LAST CHOICE. Simple and easy, just say they got hit by something, and effects will add extras
		// Maybe some choices later of different pre-made messages. and then we start selling messages....
		// What about targets that block the attack? So many things AOE complicate...
		for (Mobile t : currentData.targets) {
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
//		if (isBlocking(finalTarget)) {  // Actually true? an effect to get extra points? oh geeze. Also, AOE doesn't care here? // Cost to make unblockable?
//			return false;
//		}
		targettingBlock.perform(this);
		if (currentData.targets == null) {
			return false;
		}
		return true;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new ArcanistSkill(this, currentPlayer, fullCommand);
	}
}
