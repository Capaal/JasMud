package processes;

import interfaces.Holdable;
import interfaces.Mobile;
import items.StackableItem;
import items.StdItem;

import java.net.*; // Needed for Socket.
import java.io.*; // Needed for PrintWriter and BufferReader.
import java.util.*; // Needed for keySet();

import com.thoughtworks.xstream.XStream;

import processes.Location.Direction;
import skills.Look;

// Represents a users connection to the game. They will connect, then choose what hero to play. It handles interaction with the system.
public class PlayerPrompt implements Runnable {

	protected Socket incoming;
	protected SendMessage sendBack;	
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

	public void run() {	
		LogIn();				
		Look look = new Look(currentPlayer, "");
		WorldServer.getGameState().addToQueue(look);
		messageOthers(currentPlayer.getName() + " appears before you.",Arrays.asList(currentPlayer));
		// The following is the User's infinite loop they play inside.	
		boolean stayInsideLoop = true;
		while (stayInsideLoop) {
			if (incoming.isClosed()) {
				stayInsideLoop = false;
				currentPlayer.removeFromWorld();
				destroyConnection();
				break;
			}
			//currentPlayer.displayPrompt();
			String str = sendBack.getMessage();
			if (str != null) {
				// This is what breaks the infinite loop and kills connection.
				if (str.trim().equalsIgnoreCase("quit")) {
					stayInsideLoop = false;
					currentPlayer.tell("Leaving the World...");
					messageOthers(currentPlayer.getName() + " has left the world.",Arrays.asList(currentPlayer));
					currentPlayer.removeFromWorld();
					destroyConnection();
					break;
				} else if (str.trim().equalsIgnoreCase("shutdown")) { // Starts FULL game shutdown. Probably doesn't work right.
					stayInsideLoop = false;
					for (PlayerPrompt player : WorldServer.getGameState().viewActiveClients()) {
			//			player.currentPlayer.save();
			//			player.currentPlayer.removeFromWorld();
						player.destroyConnection();
					}
					for (Location l : WorldServer.getGameState().viewLocations().values()) {
						for (Holdable h : l.inventory.values()) {
							h.save();
							h.removeFromWorld();
						}
					}
					WorldServer.saveGameState(WorldServer.GAMESTATEDEFAULTNAME);
				//	WorldServer.getGameState().saveIdMaps();
				//	WorldServer.getGameState().saveTemplates();					
					WorldServer.shutdownAndAwaitTermination(WorldServer.executor);
				} else {						
					StringTokenizer st = new StringTokenizer(str);
					String command = "";
					if (st.hasMoreTokens()) {
						command = st.nextToken();
					}					
					command = command.toLowerCase();
					Skills com = null;
					//TODO need alternate commands instead of this
					Direction posDir = Direction.getDirectionName(command);
					if (posDir != null) {
						com = currentPlayer.getCommand("move");
						str = "move " + posDir.toString();
					} else {
						com = currentPlayer.getCommand(command);
					}
					if (com != null) {		
						WorldServer.getGameState().addToQueue(com.getNewInstance(currentPlayer, str));
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
		sendBack.printMessage("Select a class: Mercenary, Admin"); //this is really ugly TODO
		String enteredClass = sendBack.getMessage();
		ArrayList<SkillBook> bookList;
		if (enteredClass.equalsIgnoreCase("mercenary")) { //this is really ugly TODO
			newPlayer.setClassName("mercenary");
			bookList = WorldServer.getGameState().getBooksFromClass("mercenary");
			for (SkillBook s : bookList) {
				newPlayer.addSkillBook(s, 100);
			}
		} else if (enteredClass.equalsIgnoreCase("admin")) { //this is really ugly TODO
			newPlayer.setClassName("admin");
			bookList = WorldServer.getGameState().getBooksFromClass("admin");
			for (SkillBook s : bookList) {
				newPlayer.addSkillBook(s, 100);
			}
		} else {
			sendBack.printMessage("That's not a valid class. Defaulting to Mercenary..."); //temporary ugly default for testings
			bookList = WorldServer.getGameState().getBooksFromClass("mercenary");
			newPlayer.setClassName("mercenary");
			System.out.println(bookList);
			for (SkillBook s : bookList) {
				newPlayer.addSkillBook(s, 100);
			}
		}
		
		newPlayer.setName(enteredName);					
		newPlayer.setPassword(enteredPass);		
		newPlayer.setLocation(WorldServer.getGameState().viewLocations().get(1));  // Default starting location.
		newPlayer.complete();
		this.currentPlayer = newPlayer.getFinishedMob();		
		
		
		
	//	WorldServer.gameState.addMob(currentPlayer.getName() + currentPlayer.getId(), currentPlayer); This should be happening on mob creation.
		
		//adding hardcoded skillbook TEMP
	//	currentPlayer.addBook(CreateWorld.generalSkills.duplicate(),100);	
		//currentPlayer.addBook(WorldServer.getGameState().getBook(1), 100);	
		//currentPlayer.addBook(WorldServer.getGameState().getBook(2), 100);
		//currentPlayer.addBook(WorldServer.getGameState().getBook(3), 100);
		currentPlayer.setSendBack(sendBack);
		currentPlayer.controlStatus(true);
	//	currentPlayer.save(); 
	//	WorldServer.gameState.addNewPlayer(enteredName, currentPlayer);
	}
	
	private void printFailMessages() {
		Random rand = new Random();
		int selection = rand.nextInt(failMessages.length);
		sendBack.printMessage(failMessages[selection]);
		currentPlayer.displayPrompt();
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
		for (Mobile h : currentPlayer.getContainer().getMobiles().values()) {
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

