package skills;

import processes.Location;
import processes.Skills;
import processes.UsefulCommands;

public class Look extends Skills {

	public Look() {
		super.name = "look";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	@Override
	protected void performSkill() {
		Location mobLocation = currentPlayer.getContainer();
		String dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		if (dir.equals("")) {
			doLook(mobLocation);
			return;
		}
		Location futureLocation = mobLocation.getContainer(dir);
		if (futureLocation == null) {
			messageSelf("There is no location that way.");
			return;
		}
		doLook(futureLocation);
	}
	
	private void doLook (Location lookHere) {
		messageSelf(UsefulCommands.ANSI.MAGENTA + lookHere.getName() + UsefulCommands.ANSI.SANE);
		messageSelf(UsefulCommands.ANSI.GREEN + lookHere.getDescription() + UsefulCommands.ANSI.SANE);
		lookHere.displayAll(currentPlayer);	//need to expand this so location doesn't do it anymore			
		messageSelf(UsefulCommands.ANSI.CYAN + lookHere.displayExits() + UsefulCommands.ANSI.SANE);
		messageSelf("(God sight) Location number: " + lookHere.getId() + ". Ground type: " + lookHere.getGroundType() + ".");  // GOD SIGHT
	}
	
}

