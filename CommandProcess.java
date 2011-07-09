import java.io.*; //Needed for PrintWriter.
import java.util.*; //Needed for String Tokenizer

public class CommandProcess {
	
	protected SendMessage sendBack;
	protected Player currentPlayer;
	
	//Messages when user input command does not exist.
	static protected String[] failMessages = new String[] {
		"You stumble around uselessly.", 
		"You stare blankly into the distance.",
		"Unfortunatly, comprehension fails to dawn.", 
		"Luckly, your non-sensical action goes unnoticed.", 
		"Your body fails to enact your command.",
		"You have no idea how to perform such an action.", 
		"You blink, realizing that you have no idea what that action implies.", 
		"You stop, wondering what you should actually be doing.",
		"Your body halts, questioning your mind's desires.", 
		"You question your sanity for desiring such an action, but realize that questioning your sanity clearly means you are sane... right?"};

	public CommandProcess(SendMessage sendBack, Player currentPlayer) {
		this.sendBack = sendBack;
		this.currentPlayer = currentPlayer;
	}	   
	
	// Takes the inputted command and analyzes it.
	public void command(String fullCommand, Player currentPlayer) {
		System.out.println("CommandProcess opened.");
		this.currentPlayer = currentPlayer;
		StringTokenizer st = new StringTokenizer(fullCommand);
		String command = st.nextToken();
		boolean commandFound = false;
		// Commands accessible by all
		if (WorldServer.allAbilities.containsKey(command.toLowerCase())) {
			int commandNum = WorldServer.allAbilities.get(command.toLowerCase()).commandNum;
			DoCommand(currentPlayer, fullCommand, sendBack, commandNum);
			commandFound = true;
		}
		// Survival skills
		if (WorldServer.survAbilities.containsKey(command.toLowerCase())) {
			int commandNum = WorldServer.survAbilities.get(command).commandNum;
			DoCommand(currentPlayer, fullCommand, sendBack, commandNum);
			commandFound = true;
		}
		// Tests for possible generic commands.
		if (WorldServer.allowableCommands.containsKey(command.toLowerCase())) {
			int commandNum = WorldServer.allowableCommands.get(command.toLowerCase());
			DoCommand(currentPlayer, fullCommand, sendBack, commandNum);
			commandFound = true;
		// Mage skills as objects
		} else if (WorldServer.mageAbilities.containsKey(command.toLowerCase())) {
			int commandNum = WorldServer.mageAbilities.get(command.toLowerCase()).commandNum;
			DoCommand(currentPlayer, fullCommand, sendBack, commandNum);
			commandFound = true;
		// Tests for sending tells to players via name only.
		} else if (commandFound == false) {
			Iterator iter = WorldServer.activeClients.iterator();
			while (iter.hasNext()) {
				PlayerPrompt t = (PlayerPrompt) iter.next();
				if (t.currentPlayer.name.toLowerCase().equals(command.toLowerCase()) 
						&& !t.currentPlayer.name.equals(currentPlayer.name)) {
					DoCommand(currentPlayer, fullCommand, sendBack, 17);
					commandFound = true;
				}
			}	
		}
		// If command does none of the above, gives the fail message.
		if (commandFound == false) {
			Random rand = new Random();
			int selection = rand.nextInt(failMessages.length);
			sendBack.printMessage(failMessages[selection]);
		}
	}
	
	public void DoCommand(Player currentPlayer, String fullCommand, SendMessage sendBack, int commandNum) {
		// Grabs Player's location.
		int currentLocation = currentPlayer.mobLocation;
		// Grabs that location's Info from the location HashMap.
		Location thisLocation = WorldServer.locationCollection.get(currentLocation);
		
		// If case 0-11 then it is movement and processed here.
		if (commandNum >= 0 && commandNum <= 11) {
			if (currentPlayer.balance == false) {
				sendBack.printMessage("You are off balance.");
			} else {
		//		Move movePlayer = new Move();
				if (Move.detGroundType(thisLocation, sendBack, commandNum)) {
					Move.moveMob(sendBack, currentPlayer, thisLocation, commandNum);
				} else if (Move.detFutureLoc(thisLocation, commandNum) == null) {
					sendBack.printMessage("You can't go that way.");
				}
			}
		}
		boolean balanceCheck = false;
		
		// Entered commands correspond to a number, that number runs the correct "case". 
		// These commands/numbers are in PossibleCommands.
		if (commandNum > 11 && commandNum < 50 && 0 <= currentPlayer.generals && 
				WorldServer.allAbilities.get(UsefulCommands.getFirstWord(fullCommand)).skillLevel <= currentPlayer.generals) {
			System.out.println("GeneralCommands opened.");
			switch (commandNum) {
			
			case 12:
				Location thisLoc = WorldServer.locationCollection.get(currentPlayer.mobLocation);
				thisLoc.look(sendBack, currentPlayer.name);	
			break;
			
			case 13:
				GeneralCommands.doSay(fullCommand, currentPlayer);
			break;
			
			case 14:
				GeneralCommands.doSkills(currentPlayer, sendBack);
			break;

			case 15:
				GeneralCommands.doAB(fullCommand, sendBack, currentPlayer);
			break;
			
			case 16:
				GeneralCommands.doCreate(fullCommand, currentPlayer, sendBack);
			break;
			
			case 17:
				GeneralCommands.doWield(fullCommand, currentPlayer, sendBack);
			break;
			
			case 18:
				GeneralCommands.doUnwield(currentPlayer, sendBack, fullCommand);
			break;
			
			case 19:
				GeneralCommands.doDelete(fullCommand, sendBack);
			break;
			
			case 20:
				GeneralCommands.doInventory(currentPlayer, sendBack);
			break;
			
			case 21:
				GeneralCommands.doInfo(fullCommand, currentPlayer, sendBack);
			break;
			
			case 22:
				GeneralCommands.doDrop(fullCommand, currentPlayer, sendBack);
			break;
			
			case 23:
				GeneralCommands.doGet(fullCommand, currentPlayer, sendBack);
			break;
			
			case 24:
				GeneralCommands.doCreateLoc(sendBack);
			break;
			
			case 25:
				GeneralCommands.doDeleteLoc(currentPlayer, sendBack);
			break;
			
			case 26:
				GeneralCommands.doCreateMob(sendBack);
			break;
			
			case 27:
				GeneralCommands.doDeleteMob(sendBack);
			break;

			case 28:
				GeneralCommands.doGreet(currentPlayer, sendBack, fullCommand);
			break;
			
			case 29:
			   GeneralCommands.doBug(fullCommand, sendBack, currentPlayer);
			break;
			
			case 30:
				GeneralCommands.doBugCheck(currentPlayer, sendBack, fullCommand);
			break;
			
			case 31:
				GeneralCommands.doBugEdit(sendBack, fullCommand);
			break;
			
			case 32:
				GeneralCommands.doMsg(sendBack, fullCommand, currentPlayer);
			break;
			
			case 33:
				GeneralCommands.doRmsg(sendBack, currentPlayer);
			break;
				
			case 34:
				GeneralCommands.doCmsg(sendBack, currentPlayer);
			break;
			
			case 35:
				GeneralCommands.doStat(sendBack, currentPlayer);
			break;
			
			case 36:
				GeneralCommands.doExamine(fullCommand, sendBack, currentPlayer);
			break;						

			case 37:
				GeneralCommands.doMap(sendBack, currentPlayer);
			break;
			
			case 38:
				GeneralCommands.doLearn(fullCommand, currentPlayer, sendBack);		
			break;
			
			case 39:
				sendBack.printMessage("You close your eyes and concentrate, recovering your lost balance.");
				currentPlayer.balance = true;
			break;
			
			case 40:
				GeneralCommands.doTell(fullCommand, currentPlayer, sendBack);
			break;
			
			case 41:
				GeneralCommands.doGlance(fullCommand, currentPlayer, sendBack);
			break;
			
			case 42:
				GeneralCommands.doGiveLevel(sendBack, currentPlayer, fullCommand);
			break;
			
			case 43:
				GeneralCommands.doGive(currentPlayer, fullCommand, sendBack);
			break;
			
			case 44:
				GeneralCommands.doWho(currentPlayer, fullCommand, sendBack);
			break;
			
			case 45:
				GeneralCommands.doCreateNewLoc(fullCommand, sendBack);
			break;
			
			//Only goes up to case 50, increase at top
			}
		}
		
		//Survival skills as objects
		if (commandNum > 99 && commandNum < 200 && 
				WorldServer.survAbilities.get(UsefulCommands.getFirstWord(fullCommand)).skillLevel <= 
				currentPlayer.survival) {
			System.out.println("GeneralCommands opened.");
			switch(commandNum) {
			
			case 100:	

				GeneralCommands.doPunch(fullCommand, currentPlayer, sendBack);	
			break;
			
			case 101: //Swim
			   //This and move needs to be fixed up still.
				if (fullCommand.toLowerCase().equals("swim")) {
					sendBack.printMessage("You must swim in a direction. i.e. SWIM N");
				} else if (currentPlayer.balance == false) {
					sendBack.printMessage("You are off balance.");
				} else {
					if (Swim.detGroundType(sendBack, thisLocation, fullCommand, 
							Swim.detDir(thisLocation, fullCommand, commandNum))) {
						Swim.moveMob(sendBack, currentPlayer, thisLocation, Swim.detDir(thisLocation, fullCommand, commandNum));
					} else if (Swim.detFutureLoc(thisLocation, Swim.detDir(thisLocation, fullCommand, commandNum)) == null) {
						sendBack.printMessage("You can't go that way.");
					}
				}
			break;
			
			case 102:
				GeneralCommands.doStab(fullCommand, currentPlayer, sendBack);		
			break;
			
			}
		}
			
		//Mage skills as objects
		if (commandNum > 299 && commandNum < 400 && 
				WorldServer.mageAbilities.get(UsefulCommands.getFirstWord(fullCommand)).skillLevel <= currentPlayer.mage) {
			System.out.println("MageCommands opened.");
			switch(commandNum) {
			
			case 300:	
				MageCommands.doFirebolt(fullCommand, currentPlayer, sendBack);
			break;
			
			case 301:
				balanceCheck = checkBalance(currentPlayer, sendBack);
				if (balanceCheck == true) {
					MageCommands.doHeals(fullCommand, currentPlayer, sendBack);
				}
			break;
			
			case 302:
				balanceCheck = checkBalance(currentPlayer, sendBack);
				if (balanceCheck == true) {
					MageCommands.doShield(currentPlayer, sendBack);
				}
			break;
			
			case 303:
				balanceCheck = checkBalance(currentPlayer, sendBack);
				if (balanceCheck == true) {
					MageCommands.doAura(currentPlayer, sendBack);
				}
			break;
			
			case 304:
				MageCommands.doIcebolt(fullCommand, currentPlayer, sendBack);
			}
		} else if (commandNum > 299 && commandNum < 400 && 
				WorldServer.mageAbilities.get(UsefulCommands.getFirstWord(fullCommand)).skillLevel > currentPlayer.mage) {
			Random rand = new Random();
			int selection = rand.nextInt(failMessages.length);
			sendBack.printMessage(failMessages[selection]);
		}
		
	}
	// Checks if the player is off balance or not, used if balance is necessary for a skill.
	public boolean checkBalance(Player currentPlayer, SendMessage sendBack) {
		System.out.println("checkBalance accessed.");
		if (currentPlayer.balance == false) {
			sendBack.printMessage("You are off balance");
			return false;
		} else {
			return true;
		}
	}
}