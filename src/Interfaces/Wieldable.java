package interfaces;

import processes.Player;

// Probably obsolete, I think we decided that all items can be wielded.
public interface Wieldable {
	
	public String returnAttackerMSG(String target);
	public String returnAttackedMSG(String player);
	public int getDamage();
	public void wieldHand(Player currentPlayer);
}
