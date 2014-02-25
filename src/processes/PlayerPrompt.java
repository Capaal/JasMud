package processes;
import interfaces.Mobile;

import java.net.*; // Needed for Socket.
import java.io.*; // Needed for PrintWriter and BufferReader.
import java.sql.SQLException;
import java.util.*; // Needed for keySet();

// Represents a users connection to the game. They will connect, then choose what hero to play. It handles interaction with the system.
public class PlayerPrompt extends Thread {

	protected Socket incoming;
	protected SendMessage sendBack;	
	protected Mobile currentPlayer;
	 	
	public PlayerPrompt(Socket incoming) throws IOException {	
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
				String insertStats = "INSERT INTO MOBSTATS (MOBNAME, MOBPASS, MOBDESC, MOBSHORTD, MOBLOC, MOBTYPE, LOADONSTARTUP) values "
						+ "('" + enteredName + "', '" + enteredPass + "', 'A boring looking youth.', 'Young and stupid', 1, 'StdMob', 0);";
				try {
					SQLInterface.saveAction(insertStats);
				} catch (SQLException e) {
					System.out.println("New character creation failed to save to database via: " + insertStats);
					destroyConnection();
				}			
			//	String blockQuery ="Select MOBID from MOBSTATS where  MOBNAME='" + enteredName + "';";
			//	int mobId = (int) SQLInterface.viewData(blockQuery, "MOBID");
				// Only accounts for a single book at the moment.
				String insertBook = "insert into SKILLBOOKTABLE (MOBID, SKILLBOOKID, MOBPROGRESS) values((SELECT MOBID FROM MOBSTATS"
						+ " WHERE MOBNAME='" + enteredName + "'), 1, 1);";
				try {
					SQLInterface.saveAction(insertBook);
				} catch (SQLException e) {
					System.out.println("New Book Addition failed to save to database via: " + insertBook);
					e.printStackTrace();
				}				
				try {
					this.currentPlayer = SQLInterface.loadPlayer(enteredName, enteredPass);
					currentPlayer.setSendBack(sendBack);
				} catch (SQLException e) {
					System.out.println("Critical error loading player: " + enteredName + ", closing connection.");
					destroyConnection();					
				}
				currentPlayer.controlStatus(true);
				currentPlayer.save();			
			} else {				
				destroyConnection();				
			}
		}
				/* NOT IMPLEMENTED
				if (currentPlayer.getMessagesSize() > 0) {
					sendBack.printMessage("You have messages. Type RMSG to read your messages.");
				}*/
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
				} else {
					long start = System.nanoTime();
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
		this.interrupt();
		WorldServer.activeClients.remove(this);
	}
}

