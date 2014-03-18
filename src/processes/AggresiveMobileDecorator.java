package processes;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import interfaces.Mobile;

public class AggresiveMobileDecorator extends MobileDecorator {

	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private Mobile lastAggressor;
	
	public AggresiveMobileDecorator(Mobile decoratedMobile) {
		super(decoratedMobile);
	}
	
	@Override
	public void takeDamage(Type types, int d) {
		decoratedMobile.takeDamage(types, d);
		executor.schedule(new AITask(this), 100, TimeUnit.MILLISECONDS);
	}
	
	public void makeDecision() {
		if (lastAggressor != null && lastAggressor.getContainer() == decoratedMobile.getContainer()) {
			Skill basicSkill = getCommand("jab");
			StringBuilder sb = new StringBuilder();
			sb.append("jab ");
			sb.append(lastAggressor.getName());
			basicSkill.perform(sb.toString(), this);
			executor.schedule(new AITask(this), 800, TimeUnit.MILLISECONDS);
		}		
		
	}
	
	@Override
	public void informLastAggressor(Mobile mob) {
		decoratedMobile.informLastAggressor(mob);
		this.lastAggressor = mob;
	}
	
	public class AITask implements Runnable {
		
		private AggresiveMobileDecorator mobAI;
		
		public AITask(AggresiveMobileDecorator decoratedMobile) {
			this.mobAI = decoratedMobile;
		}
		
		public void run() {
			mobAI.makeDecision();
		}
	}

}
