package processes;
import interfaces.Mobile;

import java.net.*; // Needed for Socket.
import java.io.*; // Needed for PrintWriter and BufferReader.
import java.util.*; // Needed for keySet();

// Represents a users connection to the game. They will connect, then choose what hero to play. It handles interaction with the system.
public class PlayerPrompt extends Thread {

	protected Socket incoming;
	protected SendMessage sendBack;	
	protected Mobile currentPlayer;
	 	
	public PlayerPrompt(Socket incoming) {	
		this.incoming = incoming;
		this.sendBack = new SendMessage(incoming);	
	}
	
	public void run() {	
		sendBack.printMessage("Hello, and welcome!");
		sendBack.printMessage("Your name? ");
		String enteredName = sendBack.getMessage();
		sendBack.printMessage("Your password? ");
		String enteredPass = sendBack.getMessage(); // NOT SAFE
		sendBack.printSpace();
		boolean oldPlayer = false;
		boolean wrongPass = true;
		// Checks if the entered Name exists, if it does, loads it. Assuming password is right.
		
		if (enteredName != null) {
			Mobile possiblePlayer = SQLInterface.loadPlayer(enteredName, enteredPass);
			if (possiblePlayer != null) {
				currentPlayer = possiblePlayer;
				currentPlayer.controlStatus(true);
				oldPlayer = true;
				wrongPass = false;
			} else {
				wrongPass = true;
				sendBack.printMessage("Incorrect Username or Password");
				sendBack.printSpace();
				
				oldPlayer = true;
			
				try {
					incoming.close();
				} catch (IOException io) {
				}
			}
		}
		
		
		
		
	/*	if (enteredName != null && WorldServer.mobList.containsKey(enteredName.toLowerCase()) == true) {
			StdMob possiblePlayer = WorldServer.mobList.get(enteredName.toLowerCase());
			if (enteredPass.equals(possiblePlayer.getPassword())) {
				if (enteredName.equals(possiblePlayer.getName())) {
					this.currentPlayer = possiblePlayer;
					oldPlayer = true;
					wrongPass = false;
				}
			} else {
				sendBack.printMessage("Incorrect password.");
				sendBack.printSpace();
				
				oldPlayer = true;
			
				try {
					incoming.close();
				} catch (IOException io) {
				}
			}
		} */
		// Creates a new player with selected Name.
		if (oldPlayer == false && enteredName != null) {
			sendBack.printMessage("Would you like to create a new character? Y/N ");
			String create = sendBack.getMessage();
			String createPlayer = "n";
			if (create != null) {
				createPlayer = create;
			}
			if (createPlayer.toLowerCase().equals("y")) {
				// Need to figure out allowed id numbers.
				this.currentPlayer = new Player.Builder(1, enteredName).password(enteredPass).location(WorldServer.locationCollection.get(1)).build();
				WorldServer.mobList.put(enteredName.toLowerCase(), currentPlayer);
				WorldServer.locationCollection.get(1).acceptItem(currentPlayer);
				wrongPass = false;
			} else {
				try {
					incoming.close();
				} catch (IOException io) {
				}
			}
		}
		// Displays successful log-on screen of Player.
		if (!wrongPass) {
			if (currentPlayer.getMessagesSize() > 0) {
				sendBack.printMessage("You have messages. Type RMSG to read your messages.");
			}
			Location thisLocation = (Location) currentPlayer.getContainer();
			thisLocation.look(currentPlayer);
		}
		// The following is the User's infinite loop they play inside.
		try {
			while (wrongPass == false) {
				//WorldServer.saveSystem();
				// Obtains commands typed by user.
				UsefulCommands.displayPrompt(currentPlayer);
				String str = sendBack.getMessage();
				if (str == null) {
					break;
				} else {
					// This is what breaks the infinite loop and kills connection.
					if (str.trim().toLowerCase().equals("quit")) {
				//		sendBack.printMessage("Are you sure you want to quit?");
				//		if (sendBack.getMessage().equals("y")) {
							break;
				//		}
					} else {
						// This basically does what you want to do.

					//	long start = System.nanoTime();
					//	System.out.println("Start: " + start);
					/*	for (int i = 0; i < currentPlayer.questList.size(); i++) {
							Quest tryQuest = currentPlayer.questList.get(i);
							tryQuest.testFinishQuest(str, sendBack);
						}*/
						StringTokenizer st = new StringTokenizer(str);
						String command = st.nextToken();
						command = command.toLowerCase();
						Boolean commandFound = false;
						Skill com;
						String posDir = UsefulCommands.getDirName(command);
						if (posDir != null) {
							com = currentPlayer.getCommand("move");
							str = posDir;
						} else {
							com = currentPlayer.getCommand(command);
						}
						if (com != null) {
							com.perform(str);
							commandFound = true;
						}
						
						// containsKey() is constant O(1) time, while iteration is O(n), below is less efficient.
				//		SortedSet<String> s = currentPlayer.getCommandKeySet();
				//		Iterator iter = s.iterator();
				//		while (commandFound == false && iter.hasNext()) {							
				//			String commandName = (String) iter.next();
				//			System.out.println(commandName);
				//			if (commandName.startsWith(command)) {
				//				Command currentCommand = currentPlayer.getCommand(commandName);
				//				currentCommand.execute(this, str);
				//				commandFound = true;
				//			}
				//		}				
						if (commandFound == false) {
							printFailMessages();
						}
						
						// Temporarily used instead of thread timers. tickers clocks w/e
				//		currentPlayer.runEffects();
							
					/*	for (int i = 0; i < WorldServer.allQuests.size(); i++) {
							Quest tryQuest = WorldServer.allQuests.get(i);
							tryQuest.testQuest(str, currentPlayer);
						}*/
					/*	for (int i = 0; i < currentPlayer.questList.size(); i++) {
							Quest tryQuest = currentPlayer.questList.get(i);
							tryQuest.finishQuest(sendBack, str);
						}*/
					//	long end = System.nanoTime();
				     // System.out.println("End  : " + end);

			    //   long elapsedTime = end - start;
 
			     // Show how long it took to finish the process
  				   //System.out.println(elapsedTime*(10E-7) + " milliseconds.");
					
					
					}
				}
			}
			incoming.close();
			WorldServer.activeClients.remove(this);
			// Breaks socket, then saves Players and Locations. Too often?
			WorldServer.saveSystem();
			} catch (IOException e) {
		}
	}
	
	public SendMessage getSendBack() {
		return sendBack;
	}
	
	public Mobile getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void printFailMessages() {
		Random rand = new Random();
		int selection = rand.nextInt(WorldServer.failMessages.length);
		sendBack.printMessage(WorldServer.failMessages[selection]);
	}
}

