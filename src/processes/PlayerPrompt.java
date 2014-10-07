package processes;

import interfaces.Mobile;
import items.StdItem;

import java.net.*; // Needed for Socket.
import java.io.*; // Needed for PrintWriter and BufferReader.
import java.util.*; // Needed for keySet();

import processes.Location.Direction;

// Represents a users connection to the game. They will connect, then choose what hero to play. It handles interaction with the system.
public class PlayerPrompt implements Runnable {

	protected Socket incoming;
	protected SendMessage sendBack;	
	protected Mobile currentPlayer;
	 	
	public PlayerPrompt(Socket incoming) throws IOException {	
		this.incoming = incoming;
		this.sendBack = new SendMessage(incoming);	
	}
	//TODO
	public void run() {	
		sendBack.printMessage("Hello, and welcome!");
		sendBack.printMessage("Your name? ");
		String enteredName = sendBack.getMessage();
		sendBack.printMessage("Your password? ");
		String enteredPass = sendBack.getMessage(); // NOT SAFE
		sendBack.printSpace();
		boolean oldPlayer = false;
		if (enteredName != null) {
			Mobile possiblePlayer = null;
			possiblePlayer = WorldServer.databaseInterface.loadPlayer(enteredName, enteredPass);	
			if (possiblePlayer != null) {
				currentPlayer = possiblePlayer;
				currentPlayer.setSendBack(sendBack);
				currentPlayer.controlStatus(true);
				oldPlayer = true;
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
				//ASSUMES STDMOB right now.
				try {
					StdMob.insertNewBlankMob(enteredName, enteredPass);
				} catch (IllegalStateException e1) {
					e1.printStackTrace();
					sendBack.printMessage("Critical error building new character, disconnecting.");
					System.out.println("New character creation failed to save to database.");
					destroyConnection();
				}			
				this.currentPlayer = WorldServer.databaseInterface.loadPlayer(enteredName, enteredPass);
				currentPlayer.setSendBack(sendBack);
				currentPlayer.controlStatus(true);
				currentPlayer.setStartup(false);
				currentPlayer.save();			
			} else {				
				destroyConnection();				
			}
		}
		currentPlayer.getContainer().look(currentPlayer);
		
		// The following is the User's infinite loop they play inside.	
		boolean stayInsideLoop = true;
		while (stayInsideLoop) {
			currentPlayer.displayPrompt();
			String str = sendBack.getMessage();
			if (str == null) {
				break;
			} else {
				// This is what breaks the infinite loop and kills connection.
				if (str.trim().toLowerCase().equals("quit")) {
					stayInsideLoop = false;
					currentPlayer.tell("Leaving the World...");
					currentPlayer.removeFromWorld();
					destroyConnection();
					break;
				} else if (str.trim().toLowerCase().equals("shutdown")) {
					stayInsideLoop = false;
					for (PlayerPrompt player : WorldServer.gameState.viewActiveClients()) {
						player.currentPlayer.removeFromWorld();
					}
					for (StdItem item : WorldServer.gameState.viewAllItems()) {
						item.removeFromWorld();
					}
					destroyConnection();
					WorldServer.shutdownAndAwaitTermination(WorldServer.executor);
				} else {
					long start = System.nanoTime();
					
					StringTokenizer st = new StringTokenizer(str);
					String command = st.nextToken();
					command = command.toLowerCase();
					Boolean commandFound = false;
					Skill com;
					Direction posDir = Direction.getDirectionName(command);
					if (posDir != null) {
						com = currentPlayer.getCommand("move");
						str = posDir.toString();
					} else {
						com = currentPlayer.getCommand(command);
					}
					if (com != null) {		
						WorldServer.gameState.addToQueue(com, str, currentPlayer);
				//		com.perform(str, currentPlayer);
						commandFound = true;
					}			
					if (commandFound == false) {
						printFailMessages();
					}
					long end = System.nanoTime();
					long elapsedTime = end - start;
					System.out.println(elapsedTime*(10E-7) + " milliseconds.");				
				}
			}
		}
		destroyConnection();			
	}
	
	public void printFailMessages() {
		Random rand = new Random();
		int selection = rand.nextInt(WorldServer.failMessages.length);
		sendBack.printMessage(WorldServer.failMessages[selection]);
	}
	
	private void destroyConnection() {
		try {
			incoming.close();
		} catch (IOException e) {
			System.out.println("Failed to close socket connection");
			e.printStackTrace();
		}
	//	WorldServer.gameState.activeClients.remove(this);
		WorldServer.gameState.removeClient(this);
	}
}

