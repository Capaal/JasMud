package skills;

import interfaces.Holdable;
import interfaces.Mobile;
import processes.Location;
import processes.Location.Direction;
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
		displayAll(lookHere);	//need to expand this so location doesn't do it anymore	
		displayExits(lookHere);
		
		messageSelf("(God sight) Location number: " + lookHere.getId() + ". Ground type: " + lookHere.getGroundType() + ".");  // GOD SIGHT
	}
	
	private void displayAll(Location lookHere) {
		boolean anItem = false;
		StringBuilder sb = new StringBuilder();
		sb.append("Looking around you see: ");
		for (Mobile h : lookHere.getMobiles().values()) {
			sb.append(UsefulCommands.ANSI.YELLOW + h.getName() + ". " + UsefulCommands.ANSI.SANE);
			anItem = true;
		}
		sb.append("Lying on the ground: ");
		for (Holdable h : lookHere.getInventory().values()) {
			sb.append(UsefulCommands.ANSI.YELLOW + h.getName() + ". " + UsefulCommands.ANSI.SANE);
			anItem = true;
		}
		if (anItem) {
			currentPlayer.tell(sb.toString());
		}
	}
	
	private void displayExits(Location lookHere) {
		boolean atLeastOne = false;
		String toSay = "You can see no exits.";
		StringBuffer sb = new StringBuffer();
		for (Direction k : lookHere.getLocationMap().keySet()) {
			if (!atLeastOne) {
				sb.append("You can see these exits: ");
				sb.append(k.toString());
				atLeastOne = true;
			} else {
				sb.append(", " + k.toString());
			}			
		}
		if (atLeastOne) {
			sb.append(".");
			toSay = sb.toString();
		} 
		messageSelf(UsefulCommands.ANSI.CYAN + toSay + UsefulCommands.ANSI.SANE);
	}
}

