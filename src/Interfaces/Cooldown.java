package interfaces;

public interface Cooldown {
	
	public boolean isOnCooldown(Mobile currentPlayer);
	public void addCooldown(Mobile currentPlayer);
	public void removeCooldown(Mobile currentPlayer);
}
