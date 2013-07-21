package Interfaces;

import processes.Player;

public interface Wieldable {
	
	public String returnAttackerMSG(String target);
	public String returnAttackedMSG(String player);
	public int getDamage();
	public void wieldHand(Player currentPlayer);
}
