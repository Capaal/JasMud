package skills;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import interfaces.Mobile;
import processes.Location;
import processes.PlayerPrompt;
import processes.Skills;
import processes.WorldServer;
import processes.Skills.Syntax;

public class GoTo extends Skills {
	
	String posstarget;
	Mobile target;

	public GoTo(Mobile currentPlayer, String fullCommand) {
		super("goto", "Teleport to someone.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}

	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			Location l = target.getContainer();
			currentPlayer.moveHoldable(l);
			messageSelf("You go-to " + target.getNameColored() + ".");
			messageOthers(currentPlayer.getNameColored() + " suddenly appears here.", Arrays.asList(currentPlayer));
		}
	}

	@Override
	protected boolean preSkillChecks() {
		posstarget = Syntax.TARGET.getStringInfo(fullCommand, this).toLowerCase();
		if (posstarget.equals("")) {
			messageSelf("Need a target to go-to.");
			return false;
		}
		Set<PlayerPrompt> allOnlinePlayers = WorldServer.getGameState().viewActiveClients();
		Iterator<PlayerPrompt> i = allOnlinePlayers.iterator();
		//check all online players, returns true if found
		while (i.hasNext()) {
			target = i.next().getPlayer();
			if (target.getName().equals(posstarget)) {
				return true;
			} else if (target.getName().toLowerCase().startsWith(posstarget)) {
				return true;
			}
		}
		messageSelf("Player not online.");
		return false;
	}
	
	@Override
	public String displaySyntax() {
		return "GOTO [TARGET]";
	}

}
