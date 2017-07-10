package MobileAI;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import processes.Location;
import processes.Skills;
import processes.WorldServer;
import processes.Location.Direction;
import skills.Move;
import interfaces.Mobile;

public class ChasingMobileDecorator extends MobileDecorator {

	private final Skills appointedSkill =  new Move(null, null);
	
	private Mobile lastAggressor;
	private int noTargetTimer = 0;	
	private ScheduledFuture<?> future;
	
	
	public ChasingMobileDecorator(Mobile decoratedMobile) {
		super(decoratedMobile);
	}
	
	@Override
	protected void makeDecision() {
		if (decoratedMobile.isDead() || lastAggressor == null) {
			future.cancel(false);
			noTargetTimer = 0;
			return;
		}
		Direction toAggressor = decoratedMobile.getContainer().getDirectionToLocation(lastAggressor.getContainer());
		if (toAggressor != null) {
			WorldServer.getGameState().addToQueue(appointedSkill.getNewInstance(decoratedMobile, "chasing " + toAggressor));
							
		} else {
			if (decoratedMobile.getContainer() != lastAggressor.getContainer()) {
				noTargetTimer ++;
			}
			if (noTargetTimer == 10) {
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
			future = WorldServer.getGameState().getEffectExecutor().scheduleWithFixedDelay(() -> makeDecision(), 800, 800, TimeUnit.MILLISECONDS);	
			noTargetTimer = 0;	
		}
	}
}
