package processes;
import java.util.*;

public class MobAttack extends Thread {
	
	protected StdMob thisCreature;
	protected int time;
	protected Player attackingPlayer;
	
	public MobAttack(StdMob thisCreature, Player attackingPlayer) {
		this.thisCreature = thisCreature;
		this.time = thisCreature.speed;
		this.attackingPlayer = attackingPlayer;
	}
	
	public void run() {
		try {
			Thread.sleep(400);
			while (thisCreature.getMobLocation() == attackingPlayer.getMobLocation() && !thisCreature.isDead()) {
				PlayerPrompt t = UsefulCommands.getPlayerPromptFromPlayer(attackingPlayer);
				Random rand = new Random();
				int damage = ((int) (rand.nextInt((int)((thisCreature.maxHit)*.1 + 1))));
				t.sendBack.printMessage(thisCreature.name + " viciously strikes you.");
				attackingPlayer.takeDamage((int)((thisCreature.maxHit - (.5*(thisCreature.maxHit*.1))) + damage));
				t.sendBack.printMessage(Integer.toString(damage));
				UsefulCommands.displayPrompt(attackingPlayer);
				Thread.sleep(time-400);
			}
			thisCreature.hostile = false;
		} catch (InterruptedException io) {
		}
	}
}