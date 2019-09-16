package processes;

import interfaces.Holdable;
import interfaces.Mobile;

import java.net.*; // Needed for Socket.
import java.io.*; // Needed for PrintWriter and BufferReader.
import java.util.*; // Needed for keySet();

import processes.Location.Direction;
import skills.Look;

// Represents a users connection to the game. They will connect, then choose what hero to play. It handles interaction with the system.
public class PlayerPrompt implements Runnable {

	protected final Socket incoming;
	protected final SendMessage sendBack;	
	protected Mobile currentPlayer;
	
	public static final String[] failMessages = new String[] {
		"You stumble around uselessly.", 
		"You stare blankly into the distance.",
		"Unfortunatly, comprehension fails to dawn.", 
		"Luckly, your non-sensical action goes unnoticed.", 
		"Your body fails to enact your command.",
		"You have no idea how to perform such an action.", 
		"You blink, realizing that you have no idea what that action implies.", 
		"You stop, wondering what you should actually be doing.",
		"Your body halts, questioning your mind's desires.", 
		"You question your sanity for desiring such an action, but realize that questioning your sanity clearly means you are sane... right?",
		"You look at yourself expectantly."};
	 	
	public PlayerPrompt(Socket incoming) throws IOException {	
		this.incoming = incoming;
		this.sendBack = new SendMessage(incoming);	
	}	

	// Called when WorldServer successfully connects an incoming socket and creates the PlayerPrompt.
	public void run() {	
		LogIn();	// Log in or creating new Mobile.
		WorldServer.getGameState().addToQueue(new Look(currentPlayer, ""));
		messageOthers(currentPlayer.getName() + " appears before you.",Arrays.asList(currentPlayer));
		// The following is the User's infinite loop they play inside.	
		boolean stayInsideLoop = true;
		while (stayInsideLoop) {
			if (testForDisconnect()) {
				stayInsideLoop = false;
				break;
			}			
			String str = sendBack.getMessage(); // Get User's message.
			if (str != null) {
				if (checkForExceptionalCommands(str)) {
					stayInsideLoop = false;
				} else {
					Skills com = getCommand(str);					
					if (com != null) {		
						WorldServer.getGameState().addToQueue(com);
					} else {		
						printFailMessages();
					}
				}
			}
		}
		destroyConnection();	// If we somehow exit the loop and haven't broken the connection.		
	}
	
	private void LogIn() {
		sendBack.printMessage("[ Welcome screen ]");
		sendBack.printSpace();
		sendBack.printMessage("Hello, and welcome!");
		sendBack.printMessage("Please type anything just once.");
		sendBack.getMessage();
		sendBack.printMessage("Your name? ");
		String enteredName = sendBack.getMessage();
		sendBack.printMessage("Your password? ");
		String enteredPass = sendBack.getMessage(); // NOT SAFE
		sendBack.printSpace();
		if (enteredName.equals("") || enteredPass.equals("")) {
			sendBack.printMessage("Your name or password is invalid");
			LogIn();
		} else {
			Mobile m = WorldServer.getGameState().getPlayer(enteredName);
			if (m != null && m.getPassword().equals(enteredPass)) {
				currentPlayer = m;
				currentPlayer.setSendBack(sendBack);
				currentPlayer.getContainer().acceptItem(currentPlayer);
				currentPlayer.controlStatus(true);
			} else if (m == null) {
				sendBack.printMessage("Would you like to create a new character? Y/N ");
				String create = sendBack.getMessage();
				if (create.equalsIgnoreCase("n") || create.equalsIgnoreCase("no")) {
					LogIn();
				} else {
					createNewPlayer(enteredName, enteredPass);
				}
			} else {
				sendBack.printMessage("Username or Password invalid.");
				LogIn();				
			}
		}
	}
	
	private void createNewPlayer(String enteredName, String enteredPass) {
		MobileBuilder newPlayer = new MobileBuilder();	
		sendBack.printMessage("Select a class: Mercenary, Arcanist, Admin"); //this is really ugly TODO
		String enteredClass = sendBack.getMessage();
		ArrayList<SkillBook> bookList;
		if (enteredClass.equalsIgnoreCase("mercenary")) { //this is really ugly TODO
			newPlayer.setClassName("mercenary");
			bookList = WorldServer.getGameState().getBooksFromClass("mercenary");
			for (SkillBook s : bookList) {
				newPlayer.addSkillBook(s.duplicate(), 100);
			}
		} else if (enteredClass.equalsIgnoreCase("admin")) { //this is really ugly TODO
			newPlayer.setClassName("admin");
			bookList = WorldServer.getGameState().getBooksFromClass("admin");
			for (SkillBook s : bookList) {
				newPlayer.addSkillBook(s.duplicate(), 100);
			}
		} else if (enteredClass.equalsIgnoreCase("arcanist")) { //this is really ugly TODO
			newPlayer.setClassName("arcanist");
			bookList = WorldServer.getGameState().getBooksFromClass("arcanist");
			for (SkillBook s : bookList) {
				newPlayer.addSkillBook(s.duplicate(), 100);
			}
		} else {
			sendBack.printMessage("That's not a valid class. Defaulting to Admin..."); //temporary ugly default for testings
			bookList = WorldServer.getGameState().getBooksFromClass("admin");
			newPlayer.setClassName("admin");
			for (SkillBook s : bookList) {
				newPlayer.addSkillBook(s.duplicate(), 100);
			}
		}		
		newPlayer.setName(enteredName);					
		newPlayer.setPassword(enteredPass);		
		newPlayer.setLocation(WorldServer.getGameState().viewLocations().get(1));  // Default starting location.
		newPlayer.complete();
		this.currentPlayer = newPlayer.getFinishedMob();	
		currentPlayer.setSendBack(sendBack);
		currentPlayer.controlStatus(true);
	}
	
	private void printFailMessages() {
		Random rand = new Random();
		int selection = rand.nextInt(failMessages.length);
		sendBack.printMessage(failMessages[selection]);
		currentPlayer.displayPrompt();
	}
	
	private boolean testForDisconnect() {
		if (incoming.isClosed()) {
			currentPlayer.removeFromWorld();
			destroyConnection();
			return true;
		}
		return false;
	}
	
	private boolean checkForExceptionalCommands(String str) {
		if (str.trim().equalsIgnoreCase("quit")) {
			currentPlayer.tell("Leaving the World...");
			messageOthers(currentPlayer.getName() + " has left the world.",Arrays.asList(currentPlayer));
			currentPlayer.removeFromWorld();
			destroyConnection();
			return true;
		}
		if (str.trim().equalsIgnoreCase("shutdown")) { // Starts FULL game shutdown.
			for (PlayerPrompt player : WorldServer.getGameState().viewActiveClients()) {
				player.destroyConnection();
			}
			// Save all items
			for (Location l : WorldServer.getGameState().viewLocations().values()) {
				for (Holdable h : l.inventory.values()) {
					h.save();
					h.removeFromWorld();
				}
			}
			// Save GameState.
			WorldServer.saveGameState(WorldServer.getGameState().getSaveName());				
			WorldServer.shutdownAndAwaitTermination(WorldServer.executor);
			return true;
		}
		return false;
	}
	
	private Skills getCommand(String str) {
		StringTokenizer st = new StringTokenizer(str);
		String command = "";
		if (st.hasMoreTokens()) {
			command = st.nextToken();
		}					
		command = command.toLowerCase();
		Skills com = null;
		//TODO need alternate commands instead of this for shortcutting movement
		Direction posDir = Direction.getDirectionName(command);
		if (posDir != null) {
			com = currentPlayer.getCommand("move");
			str = "move " + posDir.toString();
		} else {
			com = currentPlayer.getCommand(command);
		}
		if (com != null) {
			return com.getNewInstance(currentPlayer, str);
		}
		return null;
		
	}
	
	private void destroyConnection() {
		try {
			incoming.close();
		} catch (IOException e) {
			System.out.println("Failed to close socket connection");
			e.printStackTrace();
		}
		WorldServer.getGameState().removeClient(this);
	}	
	
	//duplicate of messageOthers in Skills
	public void messageOthers(String msg, List<Mobile> toIgnore) {
		for (Mobile h : currentPlayer.getContainer().viewMobiles().values()) {
			if (h.isControlled()) {
				Boolean shouldTell = true;
				if (h.equals(currentPlayer)) {
					shouldTell = false;
				} else {
					for (Mobile m : toIgnore) {
						if (h.equals(m)) {
							shouldTell = false;
						}
					}
				}
				if (shouldTell) {
					h.tell(msg);
					h.displayPrompt();
				}
			}			
		}
	}
	
	public Mobile getPlayer() {
		return this.currentPlayer;
	}	
}

