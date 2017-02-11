package processes;

import interfaces.Holdable;
import interfaces.Mobile;
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


public class WorldServer {
	
	public static GameState gameState;
	public static ExecutorService executor;
	public static ServerSocket s;
	public static XStream xstream;
	//private static XStream xstream = new XStream();
	
	public static void main(String[] args) {
		setGameState(new GameState());
		createXStream();
	//	xstream.registerConverter(new LocationConverter(gameState));
		SkillExecutor skillExecutor = new SkillExecutor();
		skillExecutor.start();		
		CreateWorld.createWorld(); // CREATES EVERYTHING until loading and saveing work.
		loadSavedItems(); // DANGEROUS JUST FOR TEST
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
	
	public static void saveItem(Holdable item) {
		FileOutputStream fos = null;
		try {
			xstream.toXML(item, new FileWriter(new File("./Items/" + item.getName()+item.getId()+ ".xml")));
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
	/* Unused, load via hard-code, same with mobs (because we want both to defaults anyway on a full restart.)
	// Maybe don't do this, instead, locations created from hard-coded code, and insert items that have been saved.
	// TODO Currently saves WAYYYY too much
	public static void saveLocation(Location loc) {
		FileOutputStream fos = null;
		try {		
			xstream.toXML(loc, new FileWriter(new File(loc.getName()+loc.getId()+ ".xml")));
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
	}*/
	
	// PLAYERS ONLY (Mobs should just load from hardcoding defaults)
	public static void saveMobile(Mobile player) {	
		FileOutputStream fos = null;
		try {		
			xstream.toXML(player, new FileWriter(new File("./Players/" + player.getName() + player.getPassword() + ".xml")));
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
	
	public static void shutdown() {
		shutdownAndAwaitTermination(executor);
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadSavedItems() {		
		File[] roots;
		File fileDir = new File("./Items");
		roots = fileDir.listFiles();       
        for (File file : roots) {
        	if(file.exists() && (file.getName().endsWith(".xml")||file.getName().endsWith(".XML"))) {
	        	try{		    			       
			        Object item = xstream.fromXML(file);  
			        ((StdItem)item).getContainer().acceptItem((StdItem)item);
			    }catch(Exception e){
			        System.err.println("Error in XML Read: " + e.getMessage());
			    }
        	}
        }
	}
	
	private static void createXStream() {
		xstream = new XStream();	
        xstream.alias("StdMob", StdMob.class);
        xstream.registerConverter(new LocationConverter(WorldServer.gameState));
		xstream.alias("Location", Location.class);
		xstream.alias("StdItem", StdItem.class);
		xstream.alias("Stackable", StackableItem.class);
		xstream.processAnnotations(Location.class);
		xstream.processAnnotations(Equipment.class);
		xstream.processAnnotations(StdItem.class);
		xstream.processAnnotations(StdMob.class);	
	}
}

