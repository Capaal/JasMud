package processes;
import java.net.*; // Needed for Socket.
import java.io.*; // Needed for PrintWriter and BufferReader.
import java.util.*; // Needed for keySet();

public class PlayerPrompt extends Thread {

	protected Socket incoming;
	protected CommandProcess here;
	protected SendMessage sendBack;	
	protected Player currentPlayer;
	 	
	public PlayerPrompt(Socket incoming) {
	
		this.incoming = incoming;
		this.sendBack = new SendMessage(incoming);
		this.here = new CommandProcess(sendBack);	
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
		if (WorldServer.mobList.containsKey(enteredName.toLowerCase()) == true && 
				WorldServer.mobList.get(enteredName.toLowerCase()) instanceof Player) {
			Player possiblePlayer = (Player)WorldServer.mobList.get(enteredName.toLowerCase());
			if (enteredPass.equals(possiblePlayer.password)) {
				if (enteredName.equals(possiblePlayer.name)) {
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
				this.currentPlayer = new Player(enteredName, enteredPass, 1);
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
				boolean answered = false;
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
				}
				wrongPass = false;
			} else {
				try {
					incoming.close();
				} catch (IOException io) {
				}
			}
		}
		here.setCurrentPlayer(currentPlayer);
		// Displays successful log-on screen of Player.
		if (wrongPass == false) {
			if (currentPlayer.messages.size() > 0) {
				sendBack.printMessage("You have messages. Type RMSG to read your messages.");
			}
			Location thisLocation = WorldServer.locationCollection.get(currentPlayer.mobLocation);
			thisLocation.look(sendBack, currentPlayer.name);
		}
		// The following is the User's infinite loop they play inside.
		try {
			while (wrongPass == false) {
				WorldServer.saveSystem();
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
						for (int i = 0; i < currentPlayer.questList.size(); i++) {
							Quest tryQuest = currentPlayer.questList.get(i);
							tryQuest.testFinishQuest(str, sendBack);
						}

						here.command(str, currentPlayer);
						
						for (int i = 0; i < WorldServer.allQuests.size(); i++) {
							Quest tryQuest = WorldServer.allQuests.get(i);
							tryQuest.testQuest(str, currentPlayer);
						}
						for (int i = 0; i < currentPlayer.questList.size(); i++) {
							Quest tryQuest = currentPlayer.questList.get(i);
							tryQuest.finishQuest(sendBack, str);
						}
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
}

