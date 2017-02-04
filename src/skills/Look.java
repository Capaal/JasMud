package skills;

import interfaces.Container;
import processes.Location;
import processes.Skills;

public class Look extends Skills {

	public Look() {
		super.name = "look";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	@Override
	protected void performSkill() {
		Container mobLocation = currentPlayer.getContainer();
		String dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		if (dir.equals("")) {
//			mobLocation.look(currentPlayer);
			return;
		}
		Location futureLocation = ((Location)mobLocation).getContainer(dir);
		if (futureLocation == null) {
			messageSelf("There is no location that way.");
			return;
		}
		futureLocation.look(currentPlayer);
	}
}
