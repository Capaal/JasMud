package skills.Arcanist;

import interfaces.Mobile;

public interface ArcanistBlockRequired extends ArcanistBlock {
	
	public boolean doesMeetRequirement(Mobile currentPlayer);
}
