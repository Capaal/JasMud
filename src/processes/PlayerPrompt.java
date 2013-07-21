package processes;
import java.net.*; // Needed for Socket.
import java.io.*; // Needed for PrintWriter and BufferReader.
import java.util.*; // Needed for keySet();

public class PlayerPrompt extends Thread {

	protected Socket incoming;
	protected SendMessage sendBack;	
	protected Mobiles currentPlayer;
	 	
	public PlayerPrompt(Socket incoming) {
	
		this.incoming = incoming;
		this.sendBack = new SendMessage(incoming);	
	}
	
	public void run() {	
		sendBack.printMessage("Hello, and welcome!");
		sendBack.printMessage("Your name? ");
		String enteredName = sendBack.getMessage();
		sendBack.printMessage("Your password? ");
		String enteredPass = sendBack.getMessage();
		sendBack.printSpace();
		boolean oldPlayer = false;
		boolean wrongPass = true;
		// Checks if the entered Name exists, if it does, loads it. Assuming password is right.
		if (enteredName != null && WorldServer.mobList.containsKey(enteredName.toLowerCase()) == true /* && 
				WorldServer.mobList.get(enteredName.toLowerCase()) instanceof Player*/) {
			Mobiles possiblePlayer = /*(Player)*/WorldServer.mobList.get(enteredName.toLowerCase());
			if (enteredPass.equals(possiblePlayer.getPassword())) {
				if (enteredName.equals(possiblePlayer.getName())) {
					this.currentPlayer = possiblePlayer;
					oldPlayer = true;
					wrongPass = false;
				}
			} else {
				sendBack.printMessage("Incorrect password.");
				oldPlayer = true;
				try {
					incoming.close();
				} catch (IOException io) {
				}
			}
		} 
		// Creates a new player with selected Name.
		if (oldPlayer == false && enteredName != null) {
			sendBack.printMessage("Would you like to create a new character? Y/N ");
			String create = sendBack.getMessage();
			String createPlayer = "n";
			if (create != null) {
				createPlayer = create;
			}
			if (createPlayer.toLowerCase().equals("y")) {
				this.currentPlayer = new Player(enteredName, enteredPass, WorldServer.locationCollection.get(1), sendBack);
//				int posID = 1;
//				Set s = WorldServer.mobList.keySet();
//				Iterator iter = s.iterator();
//				while (iter.hasNext()) {
//					Mobiles testMob = WorldServer.mobList.get(iter.next());
//					if (posID == testMob.id) {
//						posID += 1;
//					} 	
//				}
//				currentPlayer.id = posID;
				WorldServer.mobList.put(enteredName.toLowerCase(), currentPlayer);
		/*		boolean answered = false;
				while (answered == false) {
					sendBack.printMessageLine("Is this character male or female? m/f: ");
					String sex = sendBack.getMessage();
					sendBack.printSpace();
					if (sex.toLowerCase().equals("m") || sex.toLowerCase().equals("f")) {
						answered = true;
						currentPlayer.sex = sex;
					} else {
						sendBack.printMessage("Unacceptable answer.");
					}
				}*/
				wrongPass = false;
			} else {
				try {
					incoming.close();
				} catch (IOException io) {
				}
			}
		}
		// Displays successful log-on screen of Player.
		if (wrongPass == false) {
			if (currentPlayer.getMessagesSize() > 0) {
				sendBack.printMessage("You have messages. Type RMSG to read your messages.");
			}
			Location thisLocation = currentPlayer.getMobLocation();
			thisLocation.look(sendBack, currentPlayer.getName());
		}
		// The following is the User's infinite loop they play inside.
		try {
			while (wrongPass == false) {
				//WorldServer.saveSystem();
				PromptLoop newLoop = new PromptLoop(sendBack, currentPlayer);
				newLoop.start();
				// Obtains commands typed by user.
				UsefulCommands.displayPrompt(currentPlayer);
				String str = sendBack.getMessage();
				if (str == null) {
					break;
				} else {
					// This is what breaks the infinite loop and kills connection.
					if (str.trim().toLowerCase().equals("quit")) {
				//		sendBack.printMessage("Are you sure you want to quit?");
				//		newLoop.wait(2000);
				//		if (sendBack.getMessage().equals("y")) {
							newLoop.stop();
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
						if (currentPlayer.commandAllowed(command)) {
							Command com = currentPlayer.getCommand(command);
							com.execute(this, str);
							commandFound = true;
						}
						
						// containsKey() is constant O(1) time, while iteration is O(n), below is less efficient.
						Set<String> s = currentPlayer.getCommandKeySet();		
						Iterator iter = s.iterator();							
						while (commandFound == false && iter.hasNext()) {
							String currentCommand = (String) iter.next();
							if (currentCommand.startsWith(command) && currentPlayer.commandAllowed(currentCommand)) {
								Command com = currentPlayer.getCommand(currentCommand);
								com.execute(this, str);
								commandFound = true;
							}	
						}					
						if (commandFound == false) {
							printFailMessages();
						}
					/*	if (WorldServer.skillCommands.containsKey(command)) {
							Command com = (Command) WorldServer.skillCommands.get(command);
							com.execute(this, str);
						} else {
							
						}*/
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
	
	public Mobiles getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void printFailMessages() {
		Random rand = new Random();
		int selection = rand.nextInt(WorldServer.failMessages.length);
		sendBack.printMessage(WorldServer.failMessages[selection]);
	}
}

