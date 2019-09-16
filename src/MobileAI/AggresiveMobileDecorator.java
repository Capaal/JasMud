package MobileAI;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import skills.Punch;
import interfaces.Mobile;
import processes.Skills;
import processes.WorldServer;

// Defines AI that BECOMES aggressive upon having an aggressive act performed on them.
// Becoming aggressive means to continually attempt to attack said aggressor until someone is dead, or no longer present.
// This is triggered via informLastAggressor.
public class AggresiveMobileDecorator extends MobileDecorator {
	
	private final Skills appointedSkill = new Punch(null, null);
	
	private Mobile lastAggressor;		
	private int noTargetTimer = 0;
	private final int MAXTIMER = 10; // Number of ticks of failed attacks before giving up.
	private final int TIMEBETWEENTICKS = 800;// milliseconds
	private ScheduledFuture<?> future;
	
	public AggresiveMobileDecorator(Mobile decoratedMobile) {
		super(decoratedMobile);
	}
	
	@Override
	protected void makeDecision() {
		// If condition exists to immediately end aggression.
		if (decoratedMobile.isDead() || lastAggressor == null || lastAggressor.isDead()) {
			future.cancel(false);
			noTargetTimer = 0;
			return;			
		}
		if (lastAggressor.getContainer().equals(decoratedMobile.getContainer() )) {
			WorldServer.getGameState().addToQueue(appointedSkill.getNewInstance(decoratedMobile, "attacking " + lastAggressor.getName()));
		} else { // If cannot attack, count, if maxed then give up.
			noTargetTimer ++;
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
