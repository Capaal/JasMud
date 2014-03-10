package processes;

import interfaces.Item;
import interfaces.Mobile;
import interfaces.Quest;
import items.*;

import java.net.*; // Needed for Socket.
import java.util.*; // Needed for HashSet.
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.*; //Serializable, FileReader/Writer


public class WorldServer {
	// Contains transient sockets of each connected Player.
	public static Set<PlayerPrompt> activeClients = new HashSet<PlayerPrompt>();	
	// Contains all Location objects. Location's id to location object.
	public static Map<Integer, Location> locationCollection = new HashMap<Integer, Location>();
	// Mob list name + id
	public static Map<String, Mobile> mobList = new TreeMap<String, Mobile>();
	// List of all items name + id
	public static Map<String, StdItem> allItems = new TreeMap<String, StdItem>();
	// Collection of all skill books, Mobs then load a copy of each skill book.
	public static HashMap<Integer, SkillBook> AllSkillBooks = new HashMap<Integer, SkillBook>();
	
//	static Map<String, Command> skillCommands = new HashMap<String, Command>();	
	
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

	public static ExecutorService executor;
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

		
	
		// Where to put all of these, eh?	
	//	possibleItems.put("dagger", new Dagger(1));
	//	possibleItems.put("longsword", 1);
	//	possibleItems.put("dirk", 2);
		try {
			// Sets the port for others to connect to (23)
			// Host is personal IP at the moment, right now = 192.168.1.101...
			// That host is only immediate network, probably won't work via internet.
			ServerSocket s = new ServerSocket(23);			
			// This creates infinite loop, catches all incoming users.
			
			executor = Executors.newCachedThreadPool();
			while (true) {
				Socket incoming = s.accept();
				// Each socket controls incoming prompt info and out messages
				// for each person.

				PlayerPrompt newClient = new PlayerPrompt(incoming);
				activeClients.add(newClient);
				// newClient is effectively a person, programmed poorly.
				executor.execute(newClient);
				//	newClient.start();
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	 static void shutdownAndAwaitTermination(ExecutorService pool) {
		   pool.shutdown(); // Disable new tasks from being submitted
		   try {
		     // Wait a while for existing tasks to terminate
		     if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
		       pool.shutdownNow(); // Cancel currently executing tasks
		       // Wait a while for tasks to respond to being cancelled
		       if (!pool.awaitTermination(60, TimeUnit.SECONDS))
		           System.err.println("Pool did not terminate");
		     }
		   } catch (InterruptedException ie) {
		     // (Re-)Cancel if current thread also interrupted
		     pool.shutdownNow();
		     // Preserve interrupt status
		     Thread.currentThread().interrupt();
		   }
		 }
}

