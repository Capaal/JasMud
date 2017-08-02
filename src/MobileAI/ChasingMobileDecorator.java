package MobileAI;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import processes.Skills;
import processes.WorldServer;
import processes.Location.Direction;
import skills.Move;
import interfaces.Mobile;

// Defines AI that will chase any last aggressors.
// Aggressor are defined by the call on informLastAggressor()
// Attempts to chase target if within one location, will give up after x number of failures to find target.
public class ChasingMobileDecorator extends MobileDecorator {

	private final Skills appointedSkill =  new Move(null, null);
	
	private Mobile lastAggressor;
	private int noTargetTimer = 0;	
	private ScheduledFuture<?> future;
	
	private final int MAXTIMER = 10;
	private final int TIMEBETWEENTICKS = 800; // milliseconds	
	
	public ChasingMobileDecorator(Mobile decoratedMobile) {
		super(decoratedMobile);
	}
	
	@Override
	protected void makeDecision() {
		// If condition exists to immediately end AI action.
		if (decoratedMobile.isDead() || lastAggressor == null) {
			future.cancel(false);
			noTargetTimer = 0;
			return;
		}
		Direction toAggressor = decoratedMobile.getContainer().getDirectionToLocation(lastAggressor.getContainer());
		if (toAggressor != null) {
			WorldServer.getGameState().addToQueue(appointedSkill.getNewInstance(decoratedMobile, "chasing " + toAggressor));							
		} else {
			// If target is not in Mobile's location (and previously could not move to the target) then increment failure.
			if (decoratedMobile.getContainer() != lastAggressor.getContainer()) {
				noTargetTimer ++;
			}
			if (noTargetTimer == MAXTIMER) {
				future.cancel(false);
				noTargetTimer = 0;
			}
		}			
	}
	
	@Override
	public void informLastAggressor(Mobile mob) {
		decoratedMobile.informLastAggressor(mob);
		this.lastAggressor = mob;
		if (future == null || future.isDone()) {
			future = WorldServer.getGameState().getEffectExecutor().scheduleWithFixedDelay(() -> makeDecision(), TIMEBETWEENTICKS, TIMEBETWEENTICKS, TimeUnit.MILLISECONDS);	
			noTargetTimer = 0;	
		}
	}
}
