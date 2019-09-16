package skills;

import java.util.Arrays;

import processes.Location;
import processes.Skills;
import interfaces.Mobile;

public class MoveShove extends Move {
	
	public MoveShove(Mobile currentPlayer, String fullCommand) {
		super(currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.TARGET);
	}
	
	//shove ignores these conditions
	@Override public boolean hasBalance() { return true;}
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
		dir = Location.Direction.getDirectionName(dir).toString().toLowerCase();
		messageTarget("You shove " + currentPlayer.getNameColored() + " to the " + dir + ".", Arrays.asList(shoverP));
		messageSelf(shoverP.getNameColored() + " shoves you away.");
		messageOthers(shover + " shoves " + currentPlayer.getNameColored() + " to the " + dir + ".", Arrays.asList(currentPlayer, shoverP));
	}
	
	@Override protected void displayEnterMsg() {
		messageOthersAway(currentPlayer.getNameColored() + "is suddenly shoved into this location.", Arrays.asList(currentPlayer), endContainer);
	}
	
	@Override protected void moveFollowers() {
		currentPlayer.moveFollowers("move " + dir);
	}
}
