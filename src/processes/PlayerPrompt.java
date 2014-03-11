package processes;
import interfaces.Mobile;

import java.net.*; // Needed for Socket.
import java.io.*; // Needed for PrintWriter and BufferReader.
import java.sql.SQLException;
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
			try {
				possiblePlayer = SQLInterface.loadPlayer(enteredName, enteredPass);
			} catch (SQLException e) {
				System.out.println("Critical error, loadPlayer: " + enteredName + ", dropping connection.");
				e.printStackTrace();
				destroyConnection();
			}	
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
				// Need to figure out allowed id numbers.
				
				//ASSUMES STDMOB right now.
				try {
					StdMob.insertNewBlankMob(enteredName, enteredPass);
				} catch (IllegalStateException e1) {
					e1.printStackTrace();
					sendBack.printMessage("Critical error building new character, disconnecting.");
					System.out.println("New character creation failed to save to database.");
					destroyConnection();
				}		
			//	String blockQuery ="Select MOBID from MOBSTATS where  MOBNAME='" + enteredName + "';";
			//	int mobId = (int) SQLInterface.viewData(blockQuery, "MOBID");
				// Only accounts for a single book at the moment.
							
				try {
					this.currentPlayer = SQLInterface.loadPlayer(enteredName, enteredPass);
					currentPlayer.setSendBack(sendBack);
				} catch (SQLException e) {
					System.out.println("Critical error loading player: " + enteredName + ", closing connection.");
					destroyConnection();					
				}
				currentPlayer.controlStatus(true);
				currentPlayer.setStartup(false);
				currentPlayer.save();			
			} else {				
				destroyConnection();				
			}
		}
		currentPlayer.getContainer().look(currentPlayer);	
		// The following is the User's infinite loop they play inside.
	
		while (true) {
			// Obtains commands typed by user.
			currentPlayer.displayPrompt(); // should probably be inside StdMob instead.
			String str = sendBack.getMessage();
			if (str == null) {
				break;
			} else {
				// This is what breaks the infinite loop and kills connection.
				if (str.trim().toLowerCase().equals("quit")) {
					currentPlayer.tell("Leaving the World...");
					currentPlayer.removeFromWorld();
					destroyConnection();
					break;
				} else if (str.trim().toLowerCase().equals("shutdown")) {
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
						com.perform(str, currentPlayer);
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
//		Thread.interrupt();
		WorldServer.activeClients.remove(this);
	}
}

