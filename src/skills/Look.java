package skills;

import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.Mobile;
import items.Door;
import processes.Location;
import processes.Location.Direction;
import processes.Skills;
import processes.UsefulCommands;

public class Look extends Skills {

	public Look(Mobile currentPlayer, String fullCommand) {
		super("look", "Look around.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	@Override
	protected void performSkill() {
		Location mobLocation = currentPlayer.getContainer();
		String dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		Direction trueDir = Direction.getDirectionName(dir);
		if (dir.equals("")) {
			doLook(mobLocation);
			return;
		}
		Location futureLocation = mobLocation.getLocation(dir);
		if (futureLocation == null) {
			messageSelf("There is no location that way.");
			return;
		}
		if (isDirectionBlocked(mobLocation, trueDir)) {
			messageSelf("A door is closed and blocking your view.");
			return;
		}
		doLook(futureLocation);
	}
	
	private void doLook (Location lookHere) {
		messageSelf(UsefulCommands.ANSI.MAGENTA + lookHere.getName() + UsefulCommands.ANSI.SANE);
		messageSelf(lookHere.getDescription() + UsefulCommands.ANSI.SANE);
		displayAll(lookHere);	//need to expand this so location doesn't do it anymore	
		displayExits(lookHere);
		
		messageSelf("(Dev info) Location number: " + lookHere.getId() + ".");  
	}
	
	private void displayAll(Location lookHere) {
		boolean anItem = false;
		StringBuilder sb = new StringBuilder();
		sb.append("Looking around you see: ");
		for (Mobile h : lookHere.getMobiles().values()) {
			sb.append(UsefulCommands.ANSI.YELLOW + h.getShortDescription() + descriptor(h) + UsefulCommands.ANSI.SANE);
			anItem = true;
		}
		sb.append("Lying on the ground: ");
		for (Holdable h : lookHere.viewInventory().values()) {
			sb.append(UsefulCommands.ANSI.YELLOW + h.getShortDesc() + ". " + UsefulCommands.ANSI.SANE);
			anItem = true;
		}
		if (anItem) {
			currentPlayer.tell(sb.toString());
		}
	}
	
	//yeah this is ugly - condi should handle?
	public String descriptor(Mobile m) {
		if (m.hasCondition(PassiveCondition.SLEEP)) {
			return " is asleep here. ";
		}
		return " is here. ";
	}
	
	private void displayExits(Location lookHere) {
		boolean atLeastOne = false;
		String toSay = "You can see no exits.";
		StringBuffer sb = new StringBuffer();
		for (Direction k : lookHere.getLocationMap().keySet()) {
			if (!atLeastOne) {
				sb.append("You can see these exits: ");
				sb.append(k.toString());
				Door door = lookHere.getDoor(k);
				if (door != null) {
					if (!door.isOpen()) {
						sb.append("(Closed Door)");
					} else {
						sb.append("(Open Door)");
					}
				}
				atLeastOne = true;
			} else {
				sb.append(", " + k.toString());
				Door door = lookHere.getDoor(k);
				if (door != null) {
					if (!door.isOpen()) {
						sb.append("(Closed Door)");
					} else {
						sb.append("(Open Door)");
					}
				}
			}			
		}
		if (atLeastOne) {
			sb.append(".");
			toSay = sb.toString();
		} 
		messageSelf(UsefulCommands.ANSI.CYAN + toSay + UsefulCommands.ANSI.SANE);
	}

	@Override
	protected boolean preSkillChecks() {
		return true;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Look(currentPlayer, fullCommand);
	}
}

