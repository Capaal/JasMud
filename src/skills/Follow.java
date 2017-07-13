package skills;

import java.util.Arrays;

import interfaces.Mobile;
import processes.Skills;

public class Follow extends Skills {
	
	Mobile finalTarget;
	Move following;
	MoveFollow fm;
	
	public Follow(Mobile currentPlayer, String fullCommand) {
		super("follow", "Following others around.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}

	@Override
	protected void performSkill() {		
		startFollowing();
		messageSelf("You are now following " + finalTarget.getName());
		messageTarget(currentPlayer.getName() + " has started following you.", Arrays.asList(finalTarget));
	}
	
	private void startFollowing() {
		currentPlayer.setFollowing(finalTarget);
		finalTarget.addFollower(currentPlayer);
	}
	
	protected boolean preSkillChecks() {
		String possibleTarg = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (possibleTarg == "") {
			messageSelf("Who are you trying to follow?");
			return false;
		}							
		finalTarget = currentPlayer.getContainer().getMobileFromString(possibleTarg);
		if (finalTarget == null) {
			messageSelf("There is no \"" + possibleTarg + "\" for you to follow.");
			return false;
		}
		if (finalTarget == currentPlayer) {
			messageSelf("You can't follow yourself, that way leads to madness.");
			return false;
		}
		return true;
	}
}
