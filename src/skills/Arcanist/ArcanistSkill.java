package skills.Arcanist;

import java.util.Arrays;
import java.util.List;

import interfaces.Mobile;
import processes.Skills;

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
		manaCost = build.getManaCost();
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
			damageBlock.perform(this);
			messages();
			speedBlock.perform(this);
		}
	}
	
	private void messages() {
		// SUPER TEMP
		for (Mobile t : currentData.targets) {
			messageTarget(currentPlayer.getNameColored() + " punches you.", Arrays.asList(t));
			messageSelf("You punch " + t.getName());
			messageOthers(currentPlayer.getNameColored() + " punches " + t.getNameColored(), Arrays.asList(currentPlayer, t));
		}
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {
			return false;
		}	
//		if (isBlocking(finalTarget)) {  // Actually true? an effect to get extra points? oh geeze. Also, AOE doesn't care here?
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
