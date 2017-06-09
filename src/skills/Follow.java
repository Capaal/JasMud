package skills;

import java.util.Arrays;

import interfaces.Mobile;
import processes.Skills;

public class Follow extends Skills {
	
	Mobile finalTarget;
	Move following;
	MoveFollow fm;
	
	public Follow() {	
		super.name = "follow";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}

	@Override
	protected void performSkill() {		
		if (!findTarget()) {return;}		
		startFollowing();
		messageSelf("You are now following " + finalTarget.getName());
		messageTarget(currentPlayer.getName() + " has started following you.", Arrays.asList(finalTarget));
	}
	
	private void startFollowing() {
		following = ((Move)finalTarget.getCommand("move"));
		following.addFollower(this);
	}
	
	public void move(String fullCommand) {
		if (fm == null) {
			fm = new MoveFollow();
		}
		fm.perform(fullCommand, currentPlayer);
	}
	
	public void stopFollowing() {
		if (following != null) {
			following.removeFollower(this);
			following = null;
		}
	}
	
	private boolean findTarget() {
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
