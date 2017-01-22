package processes;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import processes.Location.Direction;
import processes.MobileDecorator.AITask;
import interfaces.Container;
import interfaces.Mobile;

public class ChasingMobileDecorator extends MobileDecorator {

	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private Mobile lastAggressor;
	private Boolean hasTask = false;
	
	public ChasingMobileDecorator(Mobile decoratedMobile) {
		super(decoratedMobile);
	}
	
	@Override
	public void takeDamage(Type types, int d) {
		decoratedMobile.takeDamage(types, d);
		if (!hasTask) {
			executor.schedule(new AITask(this), 100, TimeUnit.MILLISECONDS);
			hasTask = true;		
		}		
	}
	
	public void makeDecision() {
		if (lastAggressor != null && decoratedMobile.getContainer() instanceof Location && lastAggressor.getContainer() instanceof Location) {
			Container aggressorLocation = lastAggressor.getContainer();
			if (!aggressorLocation.equals(decoratedMobile.getContainer())) {
				Direction toAggressor = ((Location)decoratedMobile.getContainer()).getDirectionToLocation((Location)aggressorLocation);
				if (toAggressor != null) {
					Skill move = decoratedMobile.getCommand("move");
					StringBuilder sb = new StringBuilder();
					sb.append("move ");
					sb.append(toAggressor.toString());
					move.perform(sb.toString(), this);
		//			executor.schedule(new AITask(this), 900, TimeUnit.MILLISECONDS);					
				} else {
					hasTask = false;
				}
			}			
		} else {
			hasTask = false;
		}
		if (hasTask) {
			executor.schedule(new AITask(this), 900, TimeUnit.MILLISECONDS);
		}
	}
	
	@Override
	public void informLastAggressor(Mobile mob) {
		decoratedMobile.informLastAggressor(mob);
		this.lastAggressor = mob;
	}

}
