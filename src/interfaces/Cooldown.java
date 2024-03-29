package interfaces;

import java.util.concurrent.TimeUnit;

import processes.WorldServer;

// Defines an object as having a "cooldown".
// Cooldown is defined as: May not utilize this object whilst on cooldown.
// Typically added to a Mobile's cooldown list when on cooldown and scheduled
// to be removed after a specified time.
public interface Cooldown {
	
	
	/** Asks designated Mobile if self is on cooldown.
	 * @param currentPlayer Defines mobile for which to check if self is on it's cooldown list.
	 * @return boolean True if on cooldown, false otherwise;
	 */
	default boolean isOnCooldown(Mobile currentPlayer) {
		return currentPlayer.isOnCooldown(this);
	}
	/** Adds self to designated Mobile's list of cooldowns.
	 * @param currentPlayer Mobile on which to add self.
	 */
	default void addCooldown(Mobile currentPlayer, int cooldownDuration) {
		currentPlayer.addCooldown(this);
		WorldServer.getGameState().getEffectExecutor().schedule(() -> removeCooldown(currentPlayer), cooldownDuration, TimeUnit.MILLISECONDS);	
	}
	/** Removes self from designated Mobile's list of cooldowns.
	 * @param currentPlayer Mobile on which to remove self.
	 */
	default void removeCooldown(Mobile currentPlayer) {
		currentPlayer.removeCooldown(this);
		currentPlayer.tell(coolDownRemovedMessages());
	}
	
	public abstract String coolDownRemovedMessages();
}
