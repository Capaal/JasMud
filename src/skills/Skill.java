package skills;

import java.util.Iterator;
import java.util.Set;

import processes.Command;
import processes.StdMob;
import processes.StdMob;
import processes.Player;
import processes.SendMessage;
import processes.UsefulCommands;
import processes.WorldServer;

public abstract class Skill implements Command {
	
	public boolean skillCheck(StdMob mob, StdMob currentPlayer, SendMessage sendBack) {
		if (currentPlayer.hasBalance()) {
			if (mob != null) {
				if (!mob.getIsDead()) {
					if (mob.getMobLocation().equals(currentPlayer.getMobLocation())) {
						return true;
					} else {
						sendBack.printMessage("You can't find your target.");
					}
				} else {
					sendBack.printMessage("Why kill what is already dead?");
				}
			} else {
				sendBack.printMessage("You can't find your target.");
			}
		} else {
			sendBack.printMessage("You are off balance");
		}
		return false;
	}
	
	protected StdMob obtainMobile(String fullCommand) {
		String target = UsefulCommands.returnTarget(fullCommand);
		String mobNum = UsefulCommands.getOnlyNumerics(target);	
		StdMob mob = null;
		if (mobNum.equals("")) {
			return WorldServer.mobList.get(target.toLowerCase());
		} else {
			Set<String> s = WorldServer.mobList.keySet();
			Iterator<String> iter = s.iterator();
			boolean found = false;
			while (!found && iter.hasNext()) {
				mob = WorldServer.mobList.get(iter.next());
				if (mob instanceof StdMob && (mob.getName().toLowerCase() + ((StdMob) mob).getId()).equals(target)) {
					return mob;
				}	
			}
		}
		return null;
	}
	
	protected void mobExtras(StdMob mob, StdMob currentPlayer) {
	//	if (!mob.isHostile()) {
		//	mob.startAttacks(currentPlayer);												
	//	} 
	}
}
