package skills;

import Interfaces.Attachment;
import processes.Player;

public class HideAttachment implements Attachment {

	private Player currentPlayer;
	private int missChance;
	
	public HideAttachment(Player currentPlayer, int missChance) {
		this.currentPlayer = currentPlayer;
		this.missChance = missChance;
		if (!currentPlayer.searchAttachments(this)) {
			currentPlayer.addAttachment(this);
		}
		currentPlayer.affectMiss(missChance);
	}	
	
	public void destroyAttachment() {
		currentPlayer.affectMiss(-missChance);
		currentPlayer.removeAttachment(this);
	}
}
