package processes;

import items.*;

import java.net.*; // Needed for Socket.
import java.util.*; // Needed for HashSet.
import java.io.*; //Serializable, FileReader/Writer

public class WorldServer {


	// Contains transient sockets of each connected Player.
	public static Set activeClients = new HashSet();
	
	// Contains all Location objects.
	public static HashMap<Integer, Location> locationCollection = new HashMap<Integer, Location>();
	// Mob list
	public static Map<String, Mobiles> mobList = new HashMap<String, Mobiles>();
	// List of all items
	static protected ArrayList<Item> allItems = new ArrayList<Item>();
	// Contains String of weapon names,and an integer that points to a creating case.
	public static HashMap<String, Item> possibleItems = new HashMap<String, Item>();
	
	// Contains all Allowable Commands and leads to their occuring.
	static protected Map<String, Integer> allowableCommands = new HashMap<String, Integer>();
	//ab generals skill
	static protected Map<String, String> allowableCommandsGeneralsDesc = new TreeMap<String, String>();
	//ab generals
	static protected ArrayList<String> allowableCommandsGeneralsShort = new ArrayList<String>();
	
	static Map<String, Command> skillCommands = new HashMap<String, Command>();
	
	
	
	//Mage skills as objects
//	static protected Map<String, AllSkills> mageAbilities = new HashMap <String, AllSkills>();
	//Skills usable by all as objects
//	static protected Map<String, AllSkills> allAbilities = new HashMap <String, AllSkills>();
	//General skills (one) as objects
//	static protected Map<String, AllSkills> survAbilities = new TreeMap <String, AllSkills>();
	
	static protected ArrayList<Quest> allQuests = new ArrayList<Quest>();
	//List of experience to gain player level
	static protected int[] Levels = new int[] {0, 2, 9, 20, 100, 2000};
	
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

		
	public static void main(String[] args) throws IOException {

		// Loads players, locations, mobs, and skills.		
//		mobList = loadMobs();
//		if (mobList.size() == 0) {
	//		CreateWorld.generateMobs();
//		}
//		allItems = loadItems();
//		CreateWorld.generateQuests();
//		if (allItems.size() == 0) {
//			Item startDagger = new Weapon(0, 15, "dagger", "A filler dagger, how'd you find it?");
//			allItems.add(startDagger);
//		}	
		//locationCollection = loadLocations();
		// If the above line in commented out, this generates a default start.
		if (locationCollection.size() == 0) {
			CreateWorld.createWorld();
//			CreateWorld.generateItems();
		}

		// The below line then succededs and re-initilizes all the commands as a HashMap.
		if (allowableCommands.size() == 0) {
			PossibleCommands.create();
		}
	
		// Where to put all of these, eh?		
		possibleItems.put("dagger", new Dagger(1));
	//	possibleItems.put("longsword", 1);
	//	possibleItems.put("dirk", 2);
		try {
			// Sets the port for others to connect to (23)
			// Host is personal IP at the moment, right now = 192.168.1.101...
			// That host is only immediate network, probably won't work via internet.
			ServerSocket s = new ServerSocket(23);			
			// This creates infinite loop, catches all incoming users.
			while (true) {
				Socket incoming = s.accept();
				// Each socket controls incoming prompt info and out messages
				// for each person.
				PlayerPrompt newClient = new PlayerPrompt(incoming);
				activeClients.add(newClient);
				// newClient is effectively a person, programmed poorly.
				newClient.start();
			}
		}
		catch (Exception e) {
		}
	}
	
	// Saves Player, Location, and commands. How often? when?
	public static void saveSystem() throws IOException {
		// Saves Player Objects.
		// Sets the save spot.
		// Saves Location Objects.
		String saveLocation = "locationCollection.ser";
		FileOutputStream fosL = new FileOutputStream(new File(saveLocation));
		try {
			ObjectOutputStream oosL = new ObjectOutputStream(fosL);
			oosL.writeObject(locationCollection);
			oosL.close();
		} finally {
			fosL.close();
		}
		// Save allowableCommands.
		String saveCommand = "allowableCommands.ser";
		FileOutputStream fosC = new FileOutputStream(new File(saveCommand));
		try {
			ObjectOutputStream oosC = new ObjectOutputStream(fosC);
			oosC.writeObject(allowableCommands);
			oosC.close();
		} finally {
			fosC.close();
		}
		// Save mobList.
		String saveMob = "mobList.ser";
		FileOutputStream fosM = new FileOutputStream(new File(saveMob));
		try {
			ObjectOutputStream oosM = new ObjectOutputStream(fosM);
			oosM.writeObject(mobList);
			oosM.close();
		} finally {
			fosM.close();
		}
		// Saves allItems.
		String saveItems = "allItems.ser";
		FileOutputStream fosI = new FileOutputStream(new File(saveItems));
		try {
			ObjectOutputStream oosI = new ObjectOutputStream(fosI);
			oosI.writeObject(allItems);
			oosI.close();
		} finally {
			fosI.close();
		}

	}
	
	// Loads Location Objects.
	public static Map<Integer, Location> loadLocations() throws FileNotFoundException {
		try {
 	 		ObjectInputStream oisL = new ObjectInputStream(new FileInputStream("locationCollection.ser"));
 			locationCollection = (HashMap<Integer, Location>)oisL.readObject();
 			oisL.close();
 		} catch(IOException ex)	{
 			ex.printStackTrace();
 		} catch(ClassNotFoundException ex) {
 			ex.printStackTrace();
		}
		return locationCollection;
	}	
	
	// Loads list of mobs.
	public static Map<String, Mobiles> loadMobs() throws FileNotFoundException {
		try {
 	 		ObjectInputStream oisL = new ObjectInputStream(new FileInputStream("mobList.ser"));
 			mobList = (Map<String, Mobiles>)oisL.readObject();
 			oisL.close();
 		} catch(IOException ex)	{
 			ex.printStackTrace();
 		} catch(ClassNotFoundException ex) {
 			ex.printStackTrace();
		}
		return mobList;
	}	
	
	// Loads allowable Commands.
	public static Map<String, Integer> loadCommands() throws FileNotFoundException {
		try {
			ObjectInputStream oisC = new ObjectInputStream(new FileInputStream("allowableCommands.ser"));
			allowableCommands = (HashMap<String, Integer>)oisC.readObject();
			oisC.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return allowableCommands;
	}
	
	// Loads all Items.
	public static ArrayList<Item> loadItems() throws FileNotFoundException {
		try {
 	 		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("allItems.ser"));
 			allItems = (ArrayList<Item>)ois.readObject();
 			ois.close();
 		} catch(IOException ex)	{
 			ex.printStackTrace();
 		} catch(ClassNotFoundException ex) {
 			ex.printStackTrace();
		}
		return allItems;
	}	
	
//	public static Command getSkillCommand(String key) {
//		if (key == null || !skillCommands.containsKey(key)) {
//			return null;
//		} else {
//			return skillCommands.get(key);
//		}
//	}
}

