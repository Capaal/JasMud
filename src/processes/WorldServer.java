package processes;

import interfaces.DatabaseInterface;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class WorldServer {
	
	public static GameState gameState;
	public static ExecutorService executor;
	public static ServerSocket s;
	
	public static void main(String[] args) {
		setGameState(new GameState());
		SkillExecutor skillExecutor = new SkillExecutor();
		skillExecutor.start();		
		CreateWorld.createWorld(); // CREATES EVERYTHING until loading and saveing work.
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

