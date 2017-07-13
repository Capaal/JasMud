package skills;

import interfaces.Mobile;

public class MoveFollow extends Move {
	
	public MoveFollow(Mobile currentPlayer, String fullCommand) {
		super(currentPlayer, fullCommand);
	}

	@Override protected void stopFollowing() {
		// Blank, we do not want to stop following whenever we move and follow.
	}
	
	// Stops following if you fail to follow.
	@Override
	protected void onFail() {
		super.stopFollowing();
	}
}
