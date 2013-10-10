package Interfaces;

public interface Effect {
	
	public Mobile getCurrentPlayer();
	public void setCurrentPlayer(Mobile currentPlayer);

	public void destroyEffect();
	public boolean doEffect();
}
