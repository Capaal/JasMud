package skills;

import java.util.Arrays;

import processes.Skills;
import interfaces.Mobile;

public class MoveShove extends Move {
	
	public MoveShove(Mobile currentPlayer, String fullCommand) {
		super(currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.TARGET);
		followers = null;
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
		if (followers == null) {
			followers = ((Move)currentPlayer.getCommand("move")).followers;
		}
		messageOthersAway(currentPlayer.getName() + "is suddenly shoved into this location.", Arrays.asList(currentPlayer), endContainer);
	}
	
	@Override protected void moveFollowers() {
		for (Follow f : followers) {
			f.move("move " + dir);
		}
	}
	
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new MoveShove(currentPlayer, fullCommand);
	}
}
