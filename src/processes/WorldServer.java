package processes;

import interfaces.Mobile;
import items.*;
import java.io.IOException;
import java.net.*; // Needed for Socket.
import java.util.*; // Needed for HashSet.
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class WorldServer {
	// Contains transient sockets of each connected Player.
	public static Set<PlayerPrompt> activeClients = new HashSet<PlayerPrompt>();	
	
	// Contains all Location objects. Location's id to location object.
	public static Map<Integer, Location> locationCollection = new TreeMap<Integer, Location>();
	
	// Mob list name + id
	public static Map<String, Mobile> mobList = new TreeMap<String, Mobile>();
	
	// List of all items name + id
	public static Map<String, StdItem> allItems = new TreeMap<String, StdItem>();
	
	// Collection of all skill books, Mobs then load a copy of each skill book.
	public static HashMap<Integer, SkillBook> AllSkillBooks = new HashMap<Integer, SkillBook>();
		
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
	public static ServerSocket s;
	
	public static void main(String[] args) {
		CreateWorld.createWorld();
		ServerSocket s = null;
		try {
			// Sets the port for others to connect to (23)
			// Host is personal IP at the moment, right now = 192.168.1.101..
			s = new ServerSocket(23);			
						
			executor = Executors.newCachedThreadPool();
			while (true) {
				Socket incoming = s.accept();
				PlayerPrompt newClient = new PlayerPrompt(incoming);
				activeClients.add(newClient);
				executor.execute(newClient);
			}
		}
		catch (IOException e) {
			System.out.println(e);			
			shutdown();
		}
	}
	
	public static void shutdownAndAwaitTermination(ExecutorService pool) {
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
		shutdown();
	}
	
	public static void shutdown() {
		shutdownAndAwaitTermination(executor);
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

