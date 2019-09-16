package skills;

import java.util.ArrayList;
import java.util.Map;

import interfaces.Mobile;
import processes.Skills;
import processes.StdMob;
import processes.UsefulCommands;
import processes.WorldServer;

public class Messaging extends Skills {
	
	private String target;
	private String msg;
	private String keyword;
	private Map<String, Mobile> allPlayers;
	
	public Messaging(Mobile currentPlayer, String fullCommand) {
		super("message", "Long distance communication.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.SLOT);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.MESSAGE);
	}

	//player needs to see that they have messages when logging in
	@Override
	protected void performSkill() {
		target = Syntax.TARGET.getStringInfo(fullCommand, this);
		allPlayers = WorldServer.getGameState().viewAllPlayers();
		ArrayList<String> allMsgs = ((StdMob)currentPlayer).getAllMessages();
		switch (keyword) {
		case "showall":
			StringBuilder sb = new StringBuilder();
			if (!allMsgs.isEmpty()) {
				for (String s : allMsgs) {
					sb.append(s);
					sb.append("\n");
				}
				messageSelf(sb.toString());
			} else {
				messageSelf("You have 0 messages.");
			}
			break;
		case "read":
			if (!allMsgs.isEmpty()) {
				if (UsefulCommands.isInteger(target)) {
					messageSelf(allMsgs.get(Integer.parseInt(target)-1));
				} else {
					messageSelf("Specify a number to read messages. MESSAGE READ #");
				}
			} else {
				messageSelf("You have 0 messages.");
			}
			break;
		case "send":
			Mobile tg = allPlayers.get(target);
			if (tg != null) {
				((StdMob)tg).addMessage(currentPlayer.getName() + ":" + msg);
				messageSelf("Message sent.");
			} else {
				messageSelf("That player doesn't exist.");
			}
			break;
		case "delete":
			if (UsefulCommands.isInteger(target)) {
				if (allMsgs.size() >= Integer.parseInt(target)) {
					allMsgs.remove(Integer.parseInt(target)-1);
					messageSelf("Message deleted.");
				} else {
					messageSelf("You don't have that many messages.");
				}
			} else {
				messageSelf("Specify a number to read messages. MESSAGE DELETE #");
			}
			break;
		case "deleteall":
			if (allMsgs.size() > 0) {
				allMsgs.clear();
				messageSelf("You delete all your messages.");
			}
			break;
		default:
			messageSelf("Invalid keyword. MESSAGE <keyword:SEND/DELETE/READ/SHOWALL> ...");
			break;
		}	
	}

	@Override
	protected boolean preSkillChecks() {
		keyword = Syntax.SLOT.getStringInfo(fullCommand, this);
		msg = Syntax.MESSAGE.getStringInfo(fullCommand, this);
		if (keyword.equals("")) {
			messageSelf("Specify a keyword. MESSAGE <keyword:SEND/DELETE/READ/SHOWALL> ...");
			return false;
		}
	/*	if (msg.equals("")) {
			messageSelf("You can't send an empty message.");
			return false;
		} */
		//target could be an int
	//	if (!allPlayers.containsKey(target.toLowerCase())) {
	//		messageSelf("That player doesn't exist.");
	//		return false;
	//	}
		return true;
	}
	
	@Override
	public String displaySyntax() {
		return "MESSAGE [SHOWALL/READ #/SEND TARGET/DELETE #/DELETEALL]";
	}
}
