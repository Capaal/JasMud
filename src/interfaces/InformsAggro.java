package interfaces;

import java.util.List;

// Use with skills to ensure methods for triggering effects that are triggered on aggressive actions.
public interface InformsAggro {
	
	/**
	 * Sets last aggressor for all Mobiles in toInform.
	 * @param currentPlayer
	 * @param toInform Using Arrays.asList(arg1, arg2, ...) to inform mulitple Mobiles.
	 */
	default void informLastAggressor(Mobile currentPlayer, List<Mobile> toInform) {
		toInform.stream().forEach((x) -> x.informLastAggressor(currentPlayer));
	}
	/**
	 * Sets last aggressor.
	 * @param currentPlayer
	 * @param finalTarget
	 */
	default void informLastAggressor(Mobile currentPlayer, Mobile finalTarget) {
		finalTarget.informLastAggressor(currentPlayer);
	}
	
}
