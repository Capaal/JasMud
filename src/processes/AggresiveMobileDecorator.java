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
	
	// Real problem of many attacks is here. Should NOT schedule a new AITask always. maybe clear last task, (but then hitting resets)
	// So, AI just starts attacking, and it's "lastAggressor" might change, but otherwise it's timer to attack keeps running.
	// Until last aggressor = null or it tries to punch and find's it's aggressor is gone.
	// In which case, the currently stored AITASK should go to null.
	@Override
	public void takeDamage(Type types, int d) {
		decoratedMobile.takeDamage(types, d);
		executor.schedule(new AITask(this), 100, TimeUnit.MILLISECONDS);
	}
	
	// Will create another scheduler of attacks every time attacked, BAD
	// If player QUITS, will still keep trying to punch (but block returns false).
	// Should hold onto AITask, to consider clearing it?
	public void makeDecision() {
		if (lastAggressor != null && lastAggressor.getContainer().equals(decoratedMobile.getContainer())) {
			Skill basicSkill = getCommand("punch");
			StringBuilder sb = new StringBuilder();
			sb.append("punch ");
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
