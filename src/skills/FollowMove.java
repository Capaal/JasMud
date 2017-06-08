package skills;

public class FollowMove extends Move {
	
	@Override protected void stopFollowing() {
		// Blank, we do not want to stop following.
	}
	
	@Override
	public void removeFollower(Follow follow) {
		// Blank because we don't want to remove our followers.
	}
}
