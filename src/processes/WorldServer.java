package processes;

import interfaces.DatabaseInterface;

import java.io.IOException;
import java.net.*; // Needed for Socket.
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class WorldServer {
	
	public static GameState gameState;
	public static DatabaseInterface databaseInterface;
		
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
		setGameState(new GameState());
		setInterface(new SQLInterface());
		SkillExecutor skillExecutor = new SkillExecutor();
		skillExecutor.start();		
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
				gameState.addClient(newClient);
				executor.execute(newClient);
			}
		}
		catch (IOException e) {
			System.out.println(e);			
			shutdown();
		}
	}
	
	public static void setInterface(DatabaseInterface newInterface) {
		databaseInterface = newInterface;		
	}
	
	public static DatabaseInterface getInterface() {
		return databaseInterface;
	}

	public static void setGameState(GameState newGameState) {
		gameState = newGameState;		
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

