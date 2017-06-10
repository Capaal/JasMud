package skills;

import java.util.Arrays;

import interfaces.Mobile;
import processes.Location;
import processes.Skills.Syntax;

public class MoveShove extends Move {
	
	public MoveShove() {
		super.name = "move";
		super.description = "Move around.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.DIRECTION);
		super.syntaxList.add(Syntax.TARGET);
	}
	
	//shove ignores these conditions
	@Override protected boolean legsOk() { return true;}
	@Override protected void ifDizzy() { return;}
	@Override protected boolean isRooted() {return false;}
	
	//*no findDirection override, just send in correct direction from Shove skill
	
	String shover;
	Mobile shoverP;
	
	public void setShover(Mobile shoverPlayer) {
		shoverP = shoverPlayer;
	}
	
	@Override protected void displayLeaveMsg() {
		shover = Syntax.TARGET.getStringInfo(fullCommand, this);
		messageTarget("You shove " + currentPlayer.getName() + " to the " + dir + ".", Arrays.asList(shoverP));
		messageSelf(shoverP.getName() + " shoves you away.");
		messageOthers(shover + " shoves " + currentPlayer.getName() + " away.", Arrays.asList(currentPlayer, shoverP));
	}
	
	@Override protected void displayEnterMsg() {
		messageOthersAway(currentPlayer.getName() + "is suddenly shoved into this location.", Arrays.asList(currentPlayer), endContainer);
	}
}
