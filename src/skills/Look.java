package skills;

import interfaces.Container;
import interfaces.Mobile;
import processes.Location;
import processes.Skills;
import processes.Skills.Syntax;


public class Look extends Skills {

	public Look() {
		super.name = "look";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	@Override
	protected void performSkill() {
		String dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		if (dir.equals("")) {
			currentPlayer.getContainer().look(currentPlayer);
			return;
		}
		Container mobLocation = currentPlayer.getContainer();
		Location futureLocation = ((Location)mobLocation).getContainer(dir);
		if (futureLocation == null) {
			messageSelf("There is no location that way.");
			return;
		}
		futureLocation.look(currentPlayer);
	}

}
