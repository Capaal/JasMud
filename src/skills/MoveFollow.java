package skills;

import interfaces.Mobile;
import processes.Skills;

public class MoveFollow extends Move {
	
	public MoveFollow(Mobile currentPlayer, String fullCommand) {
		super(currentPlayer, fullCommand);
	}

	@Override protected void stopFollowing() {
		// Blank, we do not want to stop following.
	}
	
	//@Override
//	public void removeFollower(Follow follow) {
		// Blank because we don't want to remove our followers.
//	}
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new MoveFollow(currentPlayer, fullCommand);
	}
}
