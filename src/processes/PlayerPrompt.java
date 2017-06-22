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
		WorldServer.gameState.addToQueue(look);
		// The following is the User's infinite loop they play inside.	
		boolean stayInsideLoop = true;
		while (stayInsideLoop) {
			if (incoming.isClosed()) {
				stayInsideLoop = false;
				currentPlayer.removeFromWorld();
				destroyConnection();
				break;
			}
			currentPlayer.displayPrompt();
			String str = sendBack.getMessage();
			if (str != null) {
				// This is what breaks the infinite loop and kills connection.
				if (str.trim().equalsIgnoreCase("quit")) {
					stayInsideLoop = false;
					currentPlayer.tell("Leaving the World...");
					currentPlayer.removeFromWorld();
					destroyConnection();
					break;
				} else if (str.trim().equalsIgnoreCase("shutdown")) { // Starts FULL game shutdown. Probably doesn't work right.
					stayInsideLoop = false;
					for (PlayerPrompt player : WorldServer.gameState.viewActiveClients()) {
						player.currentPlayer.removeFromWorld();
					}
					for (Location l : WorldServer.gameState.viewLocations().values()) {
						for (Holdable h : l.inventory.values()) {
							h.save();
							h.removeFromWorld();
						}
					}
					WorldServer.saveIdMaps();
					WorldServer.saveTemplates();
					destroyConnection();
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
						WorldServer.gameState.addToQueue(com.getNewInstance(currentPlayer, str));
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
		sendBack.printMessage("Your name? ");
		String enteredName = sendBack.getMessage();
		sendBack.printMessage("Your password? ");
		String enteredPass = sendBack.getMessage(); // NOT SAFE
		sendBack.printSpace();
		if (enteredName.equals("") || enteredPass.equals("")) {
			sendBack.printMessage("Your name or password is invalid");
			LogIn();
		} else {
			boolean oldPlayer = lookForExistingPlayer(enteredName, enteredPass);			
			// Creates a new player with selected Name if load failed.
			if (oldPlayer == false && !enteredName.equals("")) {
				sendBack.printMessage("Would you like to create a new character? Y/N ");
				String create = sendBack.getMessage();
				if (create.equalsIgnoreCase("n") || create.equalsIgnoreCase("no")) {
					LogIn();
				} else {
					createNewPlayer(enteredName, enteredPass);
				}
			}
		}
	}
	
	private boolean lookForExistingPlayer(String enteredName, String enteredPass) {
		StdMob person = null;
		    try{		    	
		        File xmlFile = new File("./Players/" + enteredName + enteredPass + ".xml");
		        if (!xmlFile.exists()) {
		        	return false;
		        }
		        person = (StdMob) WorldServer.xstream.fromXML(xmlFile);       
		    } catch(Exception e){
		        System.err.println("Error in XML Read: " + e.getMessage());
		    }
	    if (person != null) {
	    	currentPlayer = person;   	
	    	currentPlayer.setSendBack(sendBack);
	    	for (Holdable h : currentPlayer.getInventory().values()) {
	    		h.setContainer(currentPlayer);
	    	}
	    	return true;
	    }
	    return false;
	}
	
	private void createNewPlayer(String enteredName, String enteredPass) {
		MobileBuilder newPlayer = new MobileBuilder();		
		newPlayer.setName(enteredName);					
		newPlayer.setPassword(enteredPass);		
		newPlayer.setLocation(WorldServer.gameState.viewLocations().get(1));  // Default starting location.	
		newPlayer.complete();
		this.currentPlayer = newPlayer.getFinishedMob();						
	//	WorldServer.gameState.addMob(currentPlayer.getName() + currentPlayer.getId(), currentPlayer); This should be happening on mob creation.
		
		//adding hardcoded skillbook TEMP
	//	currentPlayer.addBook(CreateWorld.generalSkills.duplicate(),100);	
		currentPlayer.addBook(WorldServer.gameState.getBook(1), 100);	
		currentPlayer.addBook(WorldServer.gameState.getBook(2), 100);
		currentPlayer.addBook(WorldServer.gameState.getBook(3), 100);
		currentPlayer.setSendBack(sendBack);
		currentPlayer.controlStatus(true);
		currentPlayer.save(); 
	}
	
	private void printFailMessages() {
		Random rand = new Random();
		int selection = rand.nextInt(failMessages.length);
		sendBack.printMessage(failMessages[selection]);
	}
	
	private void destroyConnection() {
		try {
			incoming.close();
		} catch (IOException e) {
			System.out.println("Failed to close socket connection");
			e.printStackTrace();
		}
		WorldServer.gameState.removeClient(this);
	}	
	
	
	
	
}

