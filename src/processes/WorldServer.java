package processes;

import items.StackableItem;
import items.StdItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;


public class WorldServer {
	
	private static GameState gameState;
	public static ExecutorService executor;
	public static ServerSocket s;
	public static XStream xstream;
	
	public static final String GAMESTATEDEFAULTNAME = "GameState.xml";
	
	public static void main(String[] args) {		
		createXStream();
		if (!loadGameState(GAMESTATEDEFAULTNAME)) {
			setGameState(new GameState());
			CreateWorld.loadMap();
			CreateWorld.createWorldWithItems();
			System.out.println("Fresh Build.");
		}
		SkillExecutor skillExecutor = new SkillExecutor();
		skillExecutor.start();		
		ServerSocket s = null;
		try {
			// Sets the port for others to connect to (2587)
			// Host is personal IP at the moment, right now = 192.168.1.101..
			// External IP MIGHT BE : 50.132.32.174
			s = new ServerSocket(2587);	
			s.setPerformancePreferences(0, 1, 0);
			executor = Executors.newCachedThreadPool();
			while (true) {
				Socket incoming = s.accept();
				incoming.setTcpNoDelay(true);
		//		incoming.setSoTimeout(10000);
				incoming.setTrafficClass(56);
				PlayerPrompt newClient = new PlayerPrompt(incoming);
				gameState.addClient(newClient);
				executor.execute(newClient);
			}
		}
		catch (IOException e) {
			System.out.println("Attempt to connect a socket failed: " + e);			
			shutdown();
		}
	}
	
	public static void saveGameState(String gameStateName) {
		FileOutputStream fos = null;
		try {
			WorldServer.xstream.toXML(WorldServer.getGameState(), new FileWriter(new File("./GameState.xml")));
	//		WorldServer.xstream.toXML(WorldServer.getGameState(), new FileWriter(new File("./" + gameStateName)));
		} catch(Exception e) {
		    e.printStackTrace(); // this obviously needs to be refined.
		} finally {
		    if(fos!=null) {
		        try{ 
		            fos.close();
		        } catch (IOException e) {
		            e.printStackTrace(); // this obviously needs to be refined.
		        }
		    }
		}
	}

	// OMG can't load gamestate because there are no locations to place things, can't load locations because there is no gamestate
	private static boolean loadGameState(String gameStateName) {
		setGameState(new GameState());
		CreateWorld.loadMap();
		
		try{		    
			File xmlFile = new File("./GameState.xml");
	    //    File xmlFile = new File("./" + gameStateName);
	        if (!xmlFile.exists()) {
	        	System.out.println("Missing gamestate.");
	        	return false;
	    //    	throw new IllegalStateException("Critical Error, missing IDMAPS.");
	        }
	        GameState loadedGameState = ((GameState)(xstream.fromXML(xmlFile)));  
	        loadedGameState.locationCollection = WorldServer.getGameState().locationCollection;
	        setGameState(loadedGameState);   
	    } catch(XStreamException e){
	        System.err.println("Error in XML Read gamestate: " + e.getMessage());
	        return false;
	    }
		if (!gameState.loadSavedItems()) {return false;}
	//	setGameState(new GameState());
	//	if (!gameState.loadTemplates()){return false;}
	//	if (!gameState.loadTemplates()) {return false;}
	//	if (!gameState.loadIdMaps()) {return false;}
	//	if (!gameState.loadSavedItems()) {return false;}
		CreateWorld.createWorld();
		return true;
	}

	public static void setGameState(GameState newGameState) {
		gameState = newGameState;		
	}	
	
//	public static XStream getXstream() {
//		return xstream;
//	}

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
		shutdownAndAwaitTermination(gameState.getEffectExecutor());
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void createXStream() {
		xstream = new XStream();	
        xstream.alias("StdMob", StdMob.class);
        xstream.registerConverter(new LocationConverter(WorldServer.gameState));
		xstream.alias("Location", Location.class);
		xstream.alias("StdItem", StdItem.class);
		xstream.alias("Stackable", StackableItem.class);
		xstream.alias("GameState", GameState.class);
		xstream.processAnnotations(Location.class);
		xstream.processAnnotations(GameState.class);
		xstream.processAnnotations(Equipment.class);
		xstream.processAnnotations(StdItem.class);
		xstream.processAnnotations(StdMob.class);	
	}
	
	public static GameState getGameState() {
		return gameState;
	}

	
}