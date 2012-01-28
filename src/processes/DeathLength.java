package processes;
import java.util.*;

public class DeathLength extends Thread {
	
	protected Mob mobile;
		
	public DeathLength(Mob mobile) {
		this.mobile = mobile;
	}
	
	public void run() {
		try {
			mobile.dead = true;
			Thread.sleep(12000);
			mobile.dead = false;
			mobile.currentHp = mobile.maxHp;	
			UsefulCommands.displayToAllLocation(mobile.mobLocation, 
					UsefulCommands.firstToCap(UsefulCommands.firstToCap(mobile.shortDescription)) +  " enters the room, looking revitalized.");		
		} catch (InterruptedException io) {
		}
	}
}