package processes;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import skills.Punch;
import interfaces.Mobile;

public class AggresiveMobileDecorator extends MobileDecorator {
	
	private final Skills appointedSkill = new Punch(null, null);
	
	private Mobile lastAggressor;		
	private int noTargetTimer = 0;	
	private ScheduledFuture<?> future;
	
	public AggresiveMobileDecorator(Mobile decoratedMobile) {
		super(decoratedMobile);
	}
	
	public void makeDecision() {
		if (decoratedMobile.isDead() || lastAggressor == null || lastAggressor.isDead()) {
			future.cancel(false);
			noTargetTimer = 0;
			return;			
		}
		if (lastAggressor.getContainer().equals(decoratedMobile.getContainer() )) {
			WorldServer.getGameState().addToQueue(appointedSkill.getNewInstance(decoratedMobile, "attacking " + lastAggressor.getName()));
		} else {
			noTargetTimer ++;
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
