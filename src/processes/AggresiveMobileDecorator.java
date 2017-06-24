package processes;

import java.util.concurrent.TimeUnit;

import skills.Punch;
import interfaces.Mobile;

public class AggresiveMobileDecorator extends MobileDecorator {

	
	private Mobile lastAggressor;
	private Boolean hasTask = false;
	
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
		if (!hasTask) {
			executor.schedule(new AITask(this), 400, TimeUnit.MILLISECONDS);
			hasTask = true;		
		}		
	}
	
	// Will create another scheduler of attacks every time attacked, BAD
	// If player QUITS, will still keep trying to punch (but block returns false).
	// Should hold onto AITask, to consider clearing it?
	public void makeDecision() {
		if (decoratedMobile.isDead()) {
			hasTask = false;
			return;
		}
		if (lastAggressor != null && lastAggressor.getContainer().equals(decoratedMobile.getContainer() ) && !lastAggressor.isDead()) {
			StringBuilder sb = new StringBuilder();
			sb.append("punch ");
			sb.append(lastAggressor.getName());
			Skills basicSkill = new Punch(this, sb.toString());			
			WorldServer.gameState.addToQueue(basicSkill);
			executor.schedule(new AITask(this), 900, TimeUnit.MILLISECONDS);
		} else {
			hasTask = false;
		}
	}
	
	@Override
	public void informLastAggressor(Mobile mob) {
		decoratedMobile.informLastAggressor(mob);
		this.lastAggressor = mob;
	}

}
