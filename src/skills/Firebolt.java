package skills;

import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import processes.*;

public class Firebolt implements Command {
	
	public final int DAMAGE = 10;
	public final String TYPE = "magical";
	public final int BALANCELENGTH = 3200;
	
	public void execute(CommandProcess com, String fullCommand) {
		System.out.println("doFirebolt accessed.");
		String target = UsefulCommands.returnTarget(fullCommand);
		Player currentPlayer = com.getCurrentPlayer();
		attack(com, fullCommand);
	//	GeneralCommands.attackPlayer(target, "You firebolt " + target + ".", currentPlayer.name + 
	//			" firebolts you.", DAMAGE, TYPE, currentPlayer, com.getSendBack(), 3200);
	}
	
	private void attack(CommandProcess com, String fullCommand) {
		String target = UsefulCommands.returnTarget(fullCommand);
		String mobNoNum = UsefulCommands.getOnlyStrings(target);
		String mobNum = UsefulCommands.getOnlyNumerics(target);
		Mobiles mob = null;
		int dmg = 0;
		Player currentPlayer = com.getCurrentPlayer();
		SendMessage sendBack = com.getSendBack();
		if (mobNum.equals("")) {
			mob = WorldServer.mobList.get(target.toLowerCase());
		} else {
			Set<String> s = WorldServer.mobList.keySet();
			Iterator<String> iter = s.iterator();
			while (iter.hasNext()) {
				Mob mobb = (Mob) WorldServer.mobList.get(iter.next());
				if ( (mobb instanceof Mob) && (mobb.name.toLowerCase() + mobb.getId()).equals(target)) {
					mob = mobb;
					break;
				}	
				mob = null;
			}
		}
		if (mob != null) { //Code below is not a good idea....
			if (!mob.isDead()) {
				if (mob.getMobLocation() == currentPlayer.getMobLocation()) {
					if (mob.hasAura()) {
						dmg = (int) (DAMAGE - (DAMAGE * .15));
					} else {
						dmg = DAMAGE;
					}
				}
				if (currentPlayer.hasBalance()) {
					sendBack.printMessage("You firebolt " + target + ".");
					mob.takeDamage(dmg);	
					if (mob instanceof Player) {
						Player mobPlayer = (Player) mob;
						(UsefulCommands.getPlayerPromptFromPlayer(mobPlayer)).getSendBack().printSpace();
						(UsefulCommands.getPlayerPromptFromPlayer(mobPlayer)).getSendBack().printMessage(currentPlayer.name + " shoots at firebolt at you.");
						UsefulCommands.displayPrompt(mobPlayer);
					}
					DefenceLength newDefence = new DefenceLength(currentPlayer, sendBack, "balance", BALANCELENGTH, 
							"You have regained your balance.");
					newDefence.start();
					
					if (mob.getCurrentHp() <= 0 && !mob.isDead()) {
						sendBack.printMessage("You have killed " + mob.getShortDescription() + ".");
						currentPlayer.addExperience(mob.getXpWorth()) ;
						currentPlayer.levelPlayer();
						if ((mob instanceof Mob)) {	
							Mob mobm = (Mob) mob;
							DeathLength death = new DeathLength(mobm);
							death.start();
							mobm.setHostile(false);
						}
					}
				}
				if (((Mob)mob).isHostile() && (mob instanceof Mob)) {
					Thread mobAttack = new MobAttack(((Mob)mob), currentPlayer);
					mobAttack.start();
					((Mob)mob).setHostile(true);
				
				} else if (!currentPlayer.hasBalance()) {
					sendBack.printMessage("You are off balance");
					
				} else {
					sendBack.printMessage("You can't find your target.");
				}	
			} else {
				sendBack.printMessage("Why kill what is already dead?");
			}
		} else {
			sendBack.printMessage("You can't find your target.");
		}
	}
}
