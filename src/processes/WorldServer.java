package processes;

import interfaces.Holdable;
import interfaces.Mobile;
import items.ItemBuilder;
import items.StackableItem;
import items.StdItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.thoughtworks.xstream.XStream;


public class WorldServer {
	
	public static GameState gameState;
	public static ExecutorService executor;
	public static ServerSocket s;
	public static XStream xstream;
	
	public static void main(String[] args) {
		setGameState(new GameState());
		createXStream();
		SkillExecutor skillExecutor = new SkillExecutor();
		skillExecutor.start();
		loadTemplates();
		if (!loadIdMaps()) {
			CreateWorld.createWorldWithItems(); // CREATES EVERYTHING until loading and saving work.
		}		
		CreateWorld.createWorld();
		loadSavedItems(); // DANGEROUS JUST FOR TEST
		ServerSocket s = null;
		try {
			// Sets the port for others to connect to (2587)
			// Host is personal IP at the moment, right now = 192.168.1.101..
			// External IP MIGHT BE : 50.132.32.174
			s = new ServerSocket(2587);							
			executor = Executors.newCachedThreadPool();
			while (true) {
				Socket incoming = s.accept();
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
		if (item.getContainer() != null) {
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
			for (Holdable s : player.getInventory().values()) {
				s.delete();
			}
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
			        System.err.println("Error in XML Read item: " + file.getName() + e.getMessage());
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
	
	public static void saveIdMaps() {
		FileOutputStream fos = null;
		try {
			xstream.toXML(ItemBuilder.getIdMap(), new FileWriter(new File("./idmaps.xml")));
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
	
	private static boolean loadIdMaps() {
		try{		    	
	        File xmlFile = new File("./idmaps.xml");
	        if (!xmlFile.exists()) {
	        	System.out.println("Missing idmaps.");
	        	return false;
	    //    	throw new IllegalStateException("Critical Error, missing IDMAPS.");
	        }
	        Map<String, Integer> idMap = (Map<String,Integer>) WorldServer.xstream.fromXML(xmlFile);   
	        ItemBuilder.setIdMap(idMap);
	    } catch(Exception e){
	        System.err.println("Error in XML Read idMaps: " + e.getMessage());
	    }
		return true;
	}
	
	private static void loadTemplates() {
		try{		    	
	        File xmlFile = new File("./templates.xml");
	        if (!xmlFile.exists()) {
	        	System.out.println("Missing templates.");
	    //    	throw new IllegalStateException("Critical Error, missing templates.");
	        }
	        Map<String, ItemBuilder> itemTemplates = (Map<String, ItemBuilder>) WorldServer.xstream.fromXML(xmlFile);   
	        CreateWorld.setTemplates(itemTemplates);
	    } catch(Exception e){
	        System.err.println("Error in XML Read templates: " + e.getMessage());
	    }
	}

	public static void saveTemplates() {
		FileOutputStream fos = null;
		try {
			xstream.toXML(CreateWorld.viewItemTemplates(),  new FileWriter(new File("./templates.xml")));
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
}

