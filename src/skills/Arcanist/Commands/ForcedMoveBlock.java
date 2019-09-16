package skills.Arcanist.Commands;

import java.util.List;

import interfaces.Mobile;
import processes.WorldServer;
import processes.Location.Direction;
import processes.Skills.Syntax;
import skills.MoveShove;
import skills.Arcanist.ArcanistBlock;
import skills.Arcanist.ArcanistBuilder;
import skills.Arcanist.ArcanistSkill;

public class ForcedMoveBlock implements ArcanistBlock {
	
	public final int positionInBuildSyntax;
	
	public ForcedMoveBlock(ArcanistBuilder build) {
		List<Syntax> syntax = build.getSyntax();
		positionInBuildSyntax = syntax.size();
		syntax.add(getSyntax());		
	}
	
	private Syntax getSyntax() {
		return Syntax.SLOT; // Arbitary word, we can't use DIRECTION because where might use it!
	}

	@Override
	public void perform(ArcanistSkill skill) {
		String dir = Syntax.SLOT.getStringInfo(skill.getFullCommand(), skill);
		Direction direction = Direction.getDirectionName(dir);
		if (direction != null) {
			skill.getCurrentTargets().stream().forEach(x -> applyForcedMove(x, direction, skill.getCurrentPlayer()));
		}
	}
	
	private void applyForcedMove(Mobile target, Direction dir, Mobile currentPlayer) {
		MoveShove move = new MoveShove(target, "move " + dir);
		move.setShover(currentPlayer); // TODO use last aggressor?
		WorldServer.getGameState().addToQueue(move);
	}

	@Override
	public int determineCost() {
		return -40;
	}

	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		sb.append(System.lineSeparator());
		sb.append("Forces target to move in supplied direction");
		sb.append(". Cost shown in damage: " + determineCost());
		return sb;
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
